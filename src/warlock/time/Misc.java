/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.time;

/**
 *
 * @author Focusrite
 */
public class Misc {
   public static long millitime(){
      return System.nanoTime() / 1000000; //Because the currentTimeMillis is unreliable across OSes
   }

   public static long relativetime(int s) {
      return Misc.millitime() + (s * 1000);
   }

   public static double secondsBetween(long before, long after) {
      return ((after - before) / 1000.0);
   }
}
