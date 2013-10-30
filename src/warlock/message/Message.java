/**
 * File: warlock.message.Message.java
 *
 * An individual message sent to the player. Contains the string to be displayed and the lifetime of
 * the message (how long it's displayed)
 */
package warlock.message;

import java.util.Locale;
import warlock.ExtraMath;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;

/**
 *
 * @author Focusrite
 */
public class Message {
   private String message;
   private static final String FONT = Font.STYLE_NORMAL;
   private static final double FADEOUT_START = 1;
   private double lifetime;

   //Premade messages
   public static final String[] KILLED_MESSAGES = {
     "%s lost a duel to %s!",
     "%s is no more due to %s!",
     "%s got owned by %s!",
     "%s's party was crashed by %s!"
   };
   public static final String[] BURNED_MESSAGES = {
     "%s got BBQed!",
     "%s tried firewalking, and failed!",
     "%s got burned!",
     "%s is now ash!"
   };
   public static final String[] VICTORIOUS = {
     "%s won the round!",
     "All hail %s, winner of the round!"
   };
   public static final String[] DENIED = {
     "%s denied himself!",
     "%s is playing Pudge, denied!",
     "%s is no fun, denied himself!"
   };

   /**
    * Create a new message
    * @param message
    * @param lifetime
    */
   public Message(String message, double lifetime) {
      this.message = message;
      this.lifetime = lifetime;
   }

   /**
    * Render the message on the screen. Fadeout sadly doesn't work currently due to alpha blending,
    * graphic would need to implement a way to draw blended textures prior to solid one first.
    * @param g
    * @param x
    * @param y
    * @param z
    * @param color
    */
   public void render(Graphic g, int x, int y, int z, Color color) {
      Color c = new Color(color);
      c.a = Math.min(255, (int)(255 * lifetime / FADEOUT_START));
      g.drawText(FONT, x, y, z, message, Font.SIZE_NORMAL, c);
   }

   /**
    * Update the lifetime of the message
    * @param dt
    */
   public void update(double dt) {
      lifetime -= dt;
   }

   /**
    * @return the message string
    */
   public String getMessage() {
      return message;
   }

   /**
    * @return the lifetime left
    */
   public double getLifetime() {
      return lifetime;
   }

   /**
    * Takes a string array and returns a random string in it processed with String.format
    * @param sourceArray
    * @param strings
    * @return the processed random string
    */
   public static String randomMessage(String[] sourceArray, String... strings) {
      int i = ExtraMath.getRandom().nextInt(sourceArray.length);
      return String.format(Locale.US, sourceArray[i], (Object[]) strings);
   }
}
