/**
 * File: warlock.graphic.Color.java
 *
 * A class with four values r, g, b, a from 0-255 that defines a color. Some prefab ones included.
 */
package warlock.graphic;

import warlock.ExtraMath;

public class Color {

   public static final Color BLACK = new Color(0, 0, 0);
   public static final Color WHITE = new Color(255, 255, 255);
   public static final Color LIGHT_GREY = new Color(200, 200, 200);
   public static final Color GREY = new Color(150, 150, 150);
   public static final Color DARK_GREY = new Color(100, 100, 100);
   public static final Color GOLD = new Color(255, 204, 0);
   public static final Color RED = new Color(255, 0, 0);
   public static final Color WINE_RED = new Color(150, 50, 50);
   public static final Color BLUE = new Color(0, 0, 255);
   public static final Color SKY_BLUE = new Color(0, 128, 255);
   public static final Color SLIME_GREEN = new Color(0, 255, 0);
   public static final Color GREEN = new Color(0, 170, 0);
   public static final Color GREENISH = new Color(50, 220, 114);
   public static final Color PURPLE = new Color(162, 0, 255);
   public static final Color PINK = new Color(255, 0, 247);
   public static final Color DARK_GREEN = new Color(0, 128, 0);
   public static final Color NONE = new Color(0, 0, 0, 0);
   public static final Color YELLOW = new Color(255, 255, 0);
   public static final Color ORANGE = new Color(255, 198, 64);
   public int r, g, b, a;

   /**
    * Create a new color.
    * @param r Red, 0-255
    * @param g Green, 0-255
    * @param b Blue, 0-255
    * @param a Alpha, 0-255
    */
   public Color(int r, int g, int b, int a) {
      this.r = r % 256;
      this.g = g % 256;
      this.b = b % 256;
      this.a = a % 256;
   }

   /**
    * Copy constructor
    * @param color
    */
   public Color(Color color) {
      this(color.r, color.g, color.b);
   }

   /**
    * Create a new color.
    * @param r Red, 0-255
    * @param g Green, 0-255
    * @param b Blue, 0-255
    */
   public Color(int r, int g, int b) {
      this(r, g, b, 255);
   }

   /**
    * Returns an array of length 4 with the components in this color as floats. Used for OpenGL rendering
    * @return array [r,g,b,a]
    */
   public float[] toFloatArray() {
      float[] t = {(float) r / 256, (float) g / 256, (float) b / 256, (float) a / 256};
      return t;
   }

   /**
    * A random color between to other!
    * @param c
    * @return a new random color
    */
   public Color randomColorBetween(Color c) {
      int r = Math.min(c.r, this.r) + ExtraMath.getRandom().nextInt(Math.max(Math.abs(this.r - c.r), 1));
      int g = Math.min(c.g, this.g) + ExtraMath.getRandom().nextInt(Math.max(Math.abs(this.g - c.g), 1));
      int b = Math.min(c.b, this.b) + ExtraMath.getRandom().nextInt(Math.max(Math.abs(this.b - c.b), 1));
      int a = 255;//Math.min(c.a, this.a) + ExtraMath.getRandom().nextInt(Math.abs(this.a - c.a));
      return new Color(r, g, b, a);
   }

   /**
    * Returns a textual representation of a color, in the same format used for text coloration.
    * Eg. |RRRGGGBBB
    * @return
    */
   @Override
   public String toString() {
      return "|" + String.format("%03d", r) + String.format("%03d", g) + String.format("%03d", b);
   }
}
