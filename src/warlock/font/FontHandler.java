/**
 * File: warlock.font.FontHandler.java
 *
 * A handler that loads all the fonts used in the project.
 */
package warlock.font;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.Texture;
import org.w3c.dom.Document;
import warlock.Handle;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.resource.ResourceHandler;

/**
 *
 * @author Focusrite
 */
public class FontHandler implements Handle {
   private static Map<String, Font> fonts = new HashMap<>();

   /**
    * Initialize all fonts that's used in the project. Invoked by HandleLoader
    */
   public static void init() {
      load("Visitor", "fonts/visitor.png", "fonts/visitor.xml");
   }

   /**
    * Load a new font into the fonts-map and sets up the font.
    * @param name
    * @param textureResource
    * @param xmlResource
    */
   public static void load(String name, String textureResource, String xmlResource) {
      ResourceHandler.loadTextureResource(textureResource, "PNG", "font-" + name);
      Texture tex = ResourceHandler.getTexture("font-" + name);
      Document xml = ResourceHandler.loadXMLResource(xmlResource);

      fonts.put(name, new Font(tex, xml));
   }

   /**
    * Draw a font form the fontmap
    * @param g
    * @param name
    * @param x
    * @param y
    * @param z
    * @param text
    * @param size
    * @param color
    * @param centered
    */
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
