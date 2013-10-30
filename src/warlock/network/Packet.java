/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.network;

/**
 *
 * @author Focusrite
 */
public class Packet {
   public static final int PACKET_SIZE = 1024; //how many bites, yields network req. of 240 kbit/s
   private int ack;
   private int ackbit;
   private int sequenceNumber;

   public int getAck() {
      return ack;
   }

   public int getAckBit() {
      return ackbit;
   }

   public int getSequenceNumber() {
      return sequenceNumber;
   }

   public void setSequenceNumber(int sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
   }

   public void setAck(int ack) {
      this.ack = ack;
   }

   public void setAckbit(int ackbit) {
      this.ackbit = ackbit;
   }

   public boolean[] ackBitAsArray() {
      boolean[] bits = new boolean[Integer.SIZE];
      for (int i = Integer.SIZE - 1; i >= 0; i--) {
         bits[i] = (ackbit & (1 << i)) != 0;
      }
      return bits;
   }

   public byte[] buildPacket() {
      return new byte[PACKET_SIZE];
   }

   public static Packet parse(byte[] data) {
      return new Packet();
   }
}
