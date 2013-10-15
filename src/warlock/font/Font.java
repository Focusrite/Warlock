/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   public Font(Texture font, Document xml) {
      this.texture = font;
      buildFromXML(xml, font);
   }

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

   public int getSize() {
      return size;
   }

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

   public void drawCentered(Graphic g, int x, int y, int z, String text, int size, Color color) {
      int length = getStringWidth(text);
      draw(g, x - (length / 2), y, z, text, size, color);
   }

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

   public Texture getTexture() {
      return texture;
   }

   public FontDefinition getDefinition(Character c) {
      try {
         return characters.get(c);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Character " + c + " doesn't exist in fontmap", e);
      }
   }
}
