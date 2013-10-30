/*
 *
 * This class handles sending and recieving packets and aims to prevent throttling of network,
 * verifying what packets are recieved.
 */
package warlock.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import warlock.time.Time;

public class FlowController implements Runnable {
   //Settings

   private static final int PORT = 8439;
   private static final int PACKETS_PER_SECOND_GOOD = 30;
   private static final int PACKETS_PER_SECOND_BAD = 10;
   private static final double THRESHOLD_MIN = 1; //Seconds before state can change from bad -> good
   private static final double THRESHOLD_MAX = 60;
   private static final double RTT_THRESHOLD = 0.25d; //250 ms, when to consider changing to bad state
   private static final double DEFAULT_RESET_TIME = 10;
   private static final double STATE_TIMER_REDUCTION = 10; //Reduce/increse resetTimer by *2 once being
   private static final double MAX_RTT = 1; //Store sent packets for maximum this amout of seconds
   private static final double INFLUENCE_ON_RTT = 0.10; //How many % a packet influence rtt
   private static final int PACKET_SEQUENCE_MAX = 1024; //stable in state for this amount of time
   //Members
   private Queue<Packet> packets = new PriorityQueue<>();
   private final ArrayList<SentPacket> sentPackets = new ArrayList<>(1024); //8kb.. but time efficient
   private ControllerState state;
   private double timer;
   private double stateTimer;
   private double resetTimer;
   private double rtt; //round trip time, aka ping
   private int sequenceNumber;       //Local
   private int remoteSequenceNumber; //One remote (be it server or ONE client)
   private int ackBit;
   //Network components
   private DatagramSocket socket;
   private InetAddress address;

   public FlowController(InetAddress address) throws Exception {
      this.address = address;
      try {
         socket = new DatagramSocket();
      }
      catch (Exception e) {
         throw e;
      }
      state = ControllerState.GOOD;
      timer = 0;
      stateTimer = 0;
      sequenceNumber = 0;
      ackBit = 0;
      remoteSequenceNumber = 0;
      rtt = RTT_THRESHOLD * 0.9; //Just below rtt threshold
      resetTimer = DEFAULT_RESET_TIME;
   }

   public void update(double dt) {
      timer += dt;
      updateResetTimer(dt);
      sendIfAllowed();
      removeExpiredPackets();
   }

   private void sendNextPacket() {
      if (!packets.isEmpty()) {
         Packet p = packets.poll();
         p.setAck(remoteSequenceNumber);
         p.setAckbit(ackBit);
         sentPackets.set(p.getSequenceNumber(), new SentPacket(p.getSequenceNumber()));
         //Send
         byte[] data = p.buildPacket();
         DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, PORT);
         try {
            socket.send(sendPacket);
         }
         catch (Exception e) {
            System.out.println("Failed to send packet.");
         }
      }
   }

   private void updateResetTimer(double dt) {
      stateTimer += dt;
      if (stateTimer >= STATE_TIMER_REDUCTION) {
         if (state == ControllerState.GOOD && resetTimer > THRESHOLD_MIN) {
            resetTimer /= 2;
         }
         else if (state == ControllerState.BAD && resetTimer < THRESHOLD_MAX) {
            resetTimer *= 2;
         }
         stateTimer = 0;
      }
   }

   public void queuePacket(Packet p) {
      p.setSequenceNumber(sequenceNumber);
      sequenceNumber++;
      if (sequenceNumber == PACKET_SEQUENCE_MAX) {
         sequenceNumber = 0;
      }
      packets.add(p);
   }

   private void sendIfAllowed() {
      if ((state == ControllerState.GOOD && timer >= 1.0d / PACKETS_PER_SECOND_GOOD)
         || (state == ControllerState.BAD && timer >= 1.0d / PACKETS_PER_SECOND_BAD)) {
         timer = 0;
         sendNextPacket();
      }
   }

   public void recievedPacket(Packet p) {
      synchronized (sentPackets) {
         if (sentPackets.get(p.getAck()) == null) {
            return; //Disregard already recieved packets
         }
         if (p.getSequenceNumber() > remoteSequenceNumber) {
            int bitShift = p.getSequenceNumber() - remoteSequenceNumber;
            remoteSequenceNumber = p.getSequenceNumber();
            ackBit = ackBit << bitShift; //All bits to the right are zeroes, ie not recieved
            ackBit = setBit(ackBit, 0); //Set first bit (last remoteSequenceNumber)
         }
         else {
            ackBit = setBit(ackBit, remoteSequenceNumber - p.getSequenceNumber());
         }
         int counter = p.getAck();
         for (int i = p.getAck(); i < p.getAck() + 32; i++) {
            if (counter == 0) {
               counter = PACKET_SEQUENCE_MAX - 1;
            }
            counter--;
            SentPacket sp = sentPackets.get(counter);
            if (sp == null) {
               continue;
            }
            rtt += (Time.secondsBetween(sp.timestamp, Time.millitime()) - rtt)
               * INFLUENCE_ON_RTT; //Move rtt towards rtt of this packet scaled by INFLUENCE_ON_RTT
            sentPackets.remove(counter);
         }
      }
      switchState();
   }

   private void switchState() {
      if (rtt >= RTT_THRESHOLD && state == ControllerState.GOOD) {
         state = ControllerState.BAD; //Switch directly if rtt > threshold
         stateTimer = 0;
      }
      else if (rtt >= RTT_THRESHOLD && stateTimer >= resetTimer) {
         state = ControllerState.GOOD;
         stateTimer = 0;
      }
   }

   private void removeExpiredPackets() {
      synchronized (sentPackets) {
         for (int i = sentPackets.size() - 1; i >= 0; i--) {
            if (Time.secondsBetween(sentPackets.get(i).timestamp, Time.millitime()) >= MAX_RTT) {
               sentPackets.remove(i);
            }
         }
      }
   }

   private int setBit(int i, int position) {
      return i | (1 << position);
   }

   @Override
   public void run() {
      byte[] data = new byte[Packet.PACKET_SIZE];
      DatagramPacket recieved = new DatagramPacket(data, data.length);
      while (true) {
         try {
            socket.receive(recieved);
            Packet p = Packet.parse(recieved.getData());
            if(p != null) {
               recievedPacket(p);
            }
         }
         catch (Exception e) {
            System.out.println("Timeout occured.");
            return;
         }
      }
   }
}
