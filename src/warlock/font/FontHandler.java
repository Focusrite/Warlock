/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.font;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.Texture;
import org.w3c.dom.Document;
import warlock.Handle;
import warlock.resource.ResourceManager;
import warlock.graphic.Color;
import warlock.graphic.Graphic;

/**
 *
 * @author Focusrite
 */
public class FontHandler implements Handle {

   private static final boolean ANTIALIASING = false;
   private static Map<String, Font> fonts = new HashMap<>();

   public static void init() {
      load("Visitor", "fonts/visitor.png", "fonts/visitor.xml");
      load("Avantgarde", "fonts/avantgarde.png", "fonts/avantgarde.xml");
   }

   public static void load(String name, String textureResource, String xmlResource) {
      ResourceManager.loadTextureResource(textureResource, "PNG", "font-" + name);
      Texture tex = ResourceManager.getTexture("font-" + name);
      Document xml = ResourceManager.loadXMLResource(xmlResource);

      fonts.put(name, new Font(tex, xml));
   }

   public static void drawFont(Graphic g, String name, int x, int y, int z, String text, int size, Color color, boolean centered) {
      try {
         if (centered) {
            fonts.get(name).drawCentered(g, x, y, z, text, size, color);
         }
         else {
            fonts.get(name).draw(g, x, y, z, text, size, color);
         }
      }
      catch (Exception e) {
         throw new IllegalArgumentException("No font of name " + name, e);
      }
   }
}
