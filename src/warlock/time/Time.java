/**
 * File: warlock.time.GameState.java
 *
 * Various time related static methods for handling accurate time and time comparision.
 */
package warlock.time;

/**
 *
 * @author Focusrite
 */
public class Time {
   /**
    * @return the current unix timestamp, precise due to using nanoTime instead of milliTime
    */
   public static long millitime(){
      return System.nanoTime() / 1000000; //Because the currentTimeMillis is unreliable across OSes
   }

   /**
    * Return the relative timestamp at dt seconds into the future
    * @param dt
    * @return timestamp
    */
   public static long relativetime(double dt) {
      return Time.millitime() + (int)(dt * 1000);
   }

   /**
    * Return the seconds between two timestamps
    * @param before
    * @param after
    * @return time
    */
   public static double secondsBetween(long before, long after) {
      return ((after - before) / 1000.0);
   }
}
