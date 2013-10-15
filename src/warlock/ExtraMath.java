/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock;

import java.util.Random;

/**
 *
 * @author Focusrite
 */
public class ExtraMath {

   private static Random rand = new Random();

   public static Random getRandom() {
      return rand;
   }

   public static double randomDouble(double low, double high) {
      return low + getRandom().nextDouble() * (high - low);
   }
}
