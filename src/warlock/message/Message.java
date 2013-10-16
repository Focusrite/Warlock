/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   public Message(String message, double lifetime) {
      this.message = message;
      this.lifetime = lifetime;
   }

   public void render(Graphic g, int x, int y, int z, Color color) {
      Color c = new Color(color);
      c.a = Math.min(255, (int)(255 * lifetime / FADEOUT_START));
      g.drawText(FONT, x, y, z, message, Font.SIZE_NORMAL, c);
   }

   public void update(double dt) {
      lifetime -= dt;
   }

   public String getMessage() {
      return message;
   }

   public double getLifetime() {
      return lifetime;
   }

   public static String randomMessage(String[] sourceArray, String... strings) {
      int i = ExtraMath.getRandom().nextInt(sourceArray.length);
      return String.format(Locale.US, sourceArray[i], strings);
   }
}
