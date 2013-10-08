/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.font;

import org.newdawn.slick.TrueTypeFont;
import warlock.graphic.Color;

/**
 *
 * @author Focusrite
 */
public class SlickFont {
   private TrueTypeFont font;
   private String name;

   public SlickFont(TrueTypeFont font, String name) {
      this.font = font;
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void draw(float x, float y, String text, Color color) {
      font.drawString(x, y, name, color.toSlick());
   }
}
