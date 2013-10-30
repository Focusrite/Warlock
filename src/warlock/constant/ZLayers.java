/**
 * File: warlock.constant.ZLayers.java
 *
 * A class with static constants of various Z layers, and convienient labels for them.
 *
 * Since the game uses a orthogonal projection matrix the actual z value will be removed and all
 * objects will look equally as far from the camera. The z value only affects what things are drawn
 * on top of other
 */
package warlock.constant;

public class ZLayers {
   public static final int BELOW_LEVEL = 0;
   public static final int LEVEL = 1;
   public static final int ON_GROUND = 2;
   public static final int OBJECT = 3;
   public static final int ABOVE_LEVEL = 4;
   public static final int GUI_BACKGROUND = 5;
   public static final int GUI = 6;
   public static final int GUI_FOREGROUND = 7;
   public static final int OVERLAY = 8;
}
