/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.graphic.VertexDefination;

/**
 *
 * @author Focusrite
 */
public class FontDefinition {
   private VertexDefination def;
   private int width;
   private int height;
   private int offset;

   public int getWidth() {
      return width;
   }

   public int getHeight() {
      return height;
   }

   public int getOffset() {
      return offset;
   }

   public VertexDefination getDefinition() {
      return def;
   }

   public FontDefinition(Texture tex, int x, int y, int w, int h, int offset) {
      this.offset = offset;
      float textureWidth = tex.getImageWidth();
      float textureHeight = tex.getImageHeight();
      width = w;
      height = h;
      //Rectangle
      float[] vertices = {
         -1.0f, 1.0f, 0.0f,
         1.0f, 1.0f, 0.0f,
         -1.0f, -1.0f, 0.0f,
         -1.0f, -1.0f, 0.0f,
         1.0f, 1.0f, 0.0f,
         1.0f, -1.0f, 0.0f,};
      float[] uv = {
         x / textureWidth, y / textureHeight,
         (x + w) / textureWidth, y / textureHeight,
         x / textureWidth, (y + h) / textureHeight,
         x / textureWidth, (y + h) / textureHeight,
         (x + w) / textureWidth, y / textureHeight,
         (x + w) / textureWidth, (y + h) / textureHeight
      };
      def = new VertexDefination(vertices, uv, GL11.GL_TRIANGLES);
   }
}
