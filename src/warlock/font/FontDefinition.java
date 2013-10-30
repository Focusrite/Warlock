/**
 * File: warlock.font.FontDefinition.java
 *
 * A single character from a font. It has it's own vertex definition with the UV values of the
 * texture that exact character is using.
 */
package warlock.font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
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

   /**
    * @return width of the definition
    */
   public int getWidth() {
      return width;
   }

   /**
    * @return height of the definition
    */
   public int getHeight() {
      return height;
   }

   /**
    * @return Offset of the definition in y.
    */
   public int getOffset() {
      return offset;
   }

   /**
    * @return the vertexDefinition this font character has
    */
   public VertexDefination getDefinition() {
      return def;
   }

   /**
    * Create a new font definition.
    * 
    * @param tex
    * @param x
    * @param y
    * @param w
    * @param h
    * @param offset
    */
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
