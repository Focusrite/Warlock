/**
 * File: warlock.font.Font.java
 *
 * A single font. Also handles the setup of the font from an XML document exported using the tool
 * fontbuilder which generates a bitmap and an xml file from a normal font file.
 */
package warlock.font;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.Texture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import warlock.graphic.Color;
import warlock.graphic.Graphic;

/**
 *
 * @author Focusrite
 */
public class Font {
   public static String STYLE_NORMAL = "Visitor";
   public static int SIZE_NORMAL = 14;
   public static int SIZE_BIG = 16;
   public static int SIZE_HUGE = 20;
   private Map<Character, FontDefinition> characters = new HashMap<>();
   private Texture texture;
   private int margin = 1;
   private int size;

   /**
    * Create a new font from texture and xml document
    *
    * @param font
    * @param xml
    */
   public Font(Texture font, Document xml) {
      this.texture = font;
      buildFromXML(xml, font);
   }

   /**
    * Setup the character definations based on a xml document.
    * @param doc
    * @param tex
    */
   private void buildFromXML(Document doc, Texture tex) {
      NodeList charList = doc.getElementsByTagName("Char");
      size = Integer.parseInt(doc.getDocumentElement().getAttribute("size"));
      for (int i = 0; i < charList.getLength(); i++) {
         Element node = (Element) charList.item(i);
         String[] rect = node.getAttribute("rect").split(" "); //rect is always "x y w h"
         int x = Integer.parseInt(rect[0]);
         int y = Integer.parseInt(rect[1]);
         int w = Integer.parseInt(rect[2]);
         int h = Integer.parseInt(rect[3]);

         String[] offsets = node.getAttribute("offset").split(" "); //offset "x y"
         int offset = Integer.parseInt(offsets[1]);
         char c = node.getAttribute("code").charAt(0);
         characters.put(new Character(c), new FontDefinition(tex, x, y, w, h, offset));
      }
   }

   /**
    * The default font size
    * @return
    */
   public int getSize() {
      return size;
   }

   /**
    * Draw a font with some nifty additions such as supporting text coloration parsing and breaklines.
    * Inline coloration uses the syntax |RRRGGGBBB text | where the text in between the both "|" are
    * colored with the color RRRGGGBBB where "RRR", "GGG", "BBB" is an integer in range 0-255.
    * (they're wrapped back around if over 255)
    *
    * @param g the graphic handler
    * @param x of top-left coordinate
    * @param y of top-left coordinate
    * @param z
    * @param text
    * @param size
    * @param color
    */
   public void draw(Graphic g, int x, int y, int z, String text, int size, Color color) {
      int tX = x;
      int tY = y;
      boolean differentColor = false;
      Color inlineColor = new Color(color);
      char[] arr = text.toCharArray();
      for (int i = 0; i < arr.length; i++) {
         if (arr[i] == '\n') {
            tY -= size + 1;
            tX = x;
            continue;
         }
         else if (arr[i] == '|') {
            //Char for colorcode: default color |RRRGGGBBBdifferent color| default color
            //TODO: make it crash safe if at end of string..
            if (!differentColor) {
               differentColor = true;
               i++;
               inlineColor = new Color(
                  Integer.parseInt(String.copyValueOf(arr, i, 3)), //r
                  Integer.parseInt(String.copyValueOf(arr, i + 3, 3)), //g
                  Integer.parseInt(String.copyValueOf(arr, i + 6, 3)));//b
               i += 8;//one added on start of next
            }
            else {
               differentColor = false;
               inlineColor = new Color(color);
            }
            continue;
         }
         g.drawCharacter(arr[i], this, tX, tY, z, size, inlineColor);
         FontDefinition f = getDefinition(arr[i]); //It's either this or make drawCharacter return int characterWidth;
         tX += f.getWidth() + (int) (margin * ((float) size / this.size));
      }
   }

   /**
    * Same drawing as drawFont but calulates the width of the string before to instead of using x,y
    * as top left corner have x centered in the middle.
    *
    * @param g
    * @param x
    * @param y
    * @param z
    * @param text
    * @param size
    * @param color
    */
   public void drawCentered(Graphic g, int x, int y, int z, String text, int size, Color color) {
      int length = getStringWidth(text);
      draw(g, x - (length / 2), y, z, text, size, color);
   }

   /**
    * Calculates the width of a string rendered with this font.
    * @param text
    * @return width
    */
   private int getStringWidth(String text) {
      int result = 0;
      int rowResult = 0;
      boolean differentColor = false;

      char[] arr = text.toCharArray();
      for (int i = 0; i < arr.length; i++) {
         if (arr[i] == '\n') {
            rowResult = 0;
            continue;
         }
         else if (arr[i] == '|') {
            if (!differentColor) {
               differentColor = true;
               i += 9; //one added on start of next
            }
            else {
               differentColor = false;
            }
            continue;
         }
         rowResult += getDefinition(arr[i]).getWidth();
         result = Math.max(rowResult, result);
      }
      return result;
   }

   /**
    * @return the texture used for rendering this font
    */
   public Texture getTexture() {
      return texture;
   }

   /**
    * Return the definition of a certain character in the fontmap
    * @param c character
    * @return FontDefiniton the character has
    */
   public FontDefinition getDefinition(Character c) {
      try {
         return characters.get(c);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Character " + c + " doesn't exist in fontmap", e);
      }
   }
}
