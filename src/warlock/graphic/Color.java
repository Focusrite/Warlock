/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.graphic;

import warlock.ExtraMath;

/**
 *
 * @author Focusrite
 */
public class Color {

   public static final Color RED = new Color(255, 0, 0);
   public static final Color WINE_RED = new Color(150, 50, 50);
   public static final Color BLUE = new Color(0, 0, 255);
   public static final Color SKY_BLUE = new Color(0, 128, 255);
   int r, g, b, a;

   public Color(int r, int g, int b, int a) {
      this.r = r % 256;
      this.g = g % 256;
      this.b = b % 256;
      this.a = a % 256;
   }

   public Color(int r, int g, int b) {
      this(r, g, b, 255);
   }

   public float[] toArray() {
      float[] t = {(float) r / 256, (float) g / 256, (float) b / 256, (float) a / 256};
      return t;
   }

   public Color randomColorBetween(Color c) {
      int r = Math.min(c.r, this.r) + ExtraMath.getRandom().nextInt(Math.max(Math.abs(this.r - c.r), 1));
      int g = Math.min(c.g, this.g) + ExtraMath.getRandom().nextInt(Math.max(Math.abs(this.g - c.g), 1));
      int b = Math.min(c.b, this.b) + ExtraMath.getRandom().nextInt(Math.max(Math.abs(this.b - c.b), 1));
      int a = 255;//Math.min(c.a, this.a) + ExtraMath.getRandom().nextInt(Math.abs(this.a - c.a));
      return new Color(r, g, b, a);
   }

   public org.newdawn.slick.Color toSlick() {
      return new org.newdawn.slick.Color(r, g, b, a);
   }
}
