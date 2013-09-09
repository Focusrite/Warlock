/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object;

import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public abstract class LevelObject {
   private Vector position = new Vector();

   public Vector getPosition() {
      return position;
   }

   public void handleInput(double dt, InputHandler input) { }

   public void setPosition(Vector position) {
      this.position = position;
   }

   public abstract void update(double dt);

   public abstract void render(Graphic g);
}
