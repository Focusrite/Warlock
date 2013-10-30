/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.network;

import warlock.time.Time;

/**
 *
 * @author Focusrite
 */
public class SentPacket {
   public long timestamp;
   public int sequenceNumber;

   public SentPacket(int sequenceNumber) {
      this.timestamp = Time.millitime();
      this.sequenceNumber = sequenceNumber;
   }


}
