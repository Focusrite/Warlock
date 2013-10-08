/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.font;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.TrueTypeFont;
import warlock.Handle;
import warlock.resource.ResourceManager;
import warlock.graphic.Color;

/**
 *
 * @author Focusrite
 */
public class FontHandler implements Handle {

   private static final boolean ANTIALIASING = false;
   private static Map<String, SlickFont> fonts = new HashMap<>();

   public static void init() {
      //load("Times New Roman", 16);
   }

   public static void load(String name, int size) {
      Font awtFont = new Font(name, Font.PLAIN, size);
      SlickFont f = new SlickFont(new TrueTypeFont(awtFont, ANTIALIASING), name);
      fonts.put(name, f);
   }

   public static void load(String name, String resource, int size) {
      Font awtFont = ResourceManager.loadFontResource(resource);
      awtFont = awtFont.deriveFont(24f); // set font size
      SlickFont f = new SlickFont(new TrueTypeFont(awtFont, ANTIALIASING), name);
      fonts.put(name, f);
   }

   public static void drawFont(String name, float x, float y, String text, Color color) {
      try {
         fonts.get(name).draw(x, y, text, color);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("No font of name " + name, e);
      }
   }
}
