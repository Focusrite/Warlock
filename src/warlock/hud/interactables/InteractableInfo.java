/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud.interactables;

/**
 *
 * @author Focusrite
 */
public class InteractableInfo {
   private int x, y;
   private Interactable source;

   public InteractableInfo(Interactable source, int x, int y) {
      this.x = x;
      this.y = y;
      this.source = source;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public Interactable getSource() {
      return source;
   }
}
