/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.graphic;

/**
 *
 * @author Focusrite
 */
public class Vertex {
   private float[] pos;
   private float[] colour;

   public Vertex(float x, float y, float z, Color colour) {
      this.pos = new float[]{x, y, z};
      this.colour = new float[]{colour.r/(float)255, colour.g/(float)255, colour.b/(float)255};
   }

   public float[] getPosArray() {
      return pos;
   }

   public float[] getColourArray() {
      return colour;
   }
}
