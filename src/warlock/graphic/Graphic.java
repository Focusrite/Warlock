/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.graphic;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Focusrite
 */
public class Graphic {

   public void drawSquare(int x, int y, int w, int h) {
      // set the color of the quad (R,G,B,A)
      GL11.glColor3f(0.5f, 0.5f, 1.0f);

      // draw quad
      GL11.glBegin(GL11.GL_QUADS);
      GL11.glVertex2f(x, y);
      GL11.glVertex2f(x + w, y);
      GL11.glVertex2f(x + w, y + h);
      GL11.glVertex2f(x, y + h);
      GL11.glEnd();
   }
}
