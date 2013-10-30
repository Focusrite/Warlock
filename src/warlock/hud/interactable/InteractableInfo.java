/**
 * File: warlock.hud.interactable.InteractableInfo.java
 *
 * A class that wraps an interactable for sending along to the listeners.
 */
package warlock.hud.interactable;

public class InteractableInfo {
   private int x, y;
   private Interactable source;

   /**
    * Create a new InteractableInfo, passed to listeners
    *
    * @param source
    * @param x
    * @param y
    */
   public InteractableInfo(Interactable source, int x, int y) {
      this.x = x;
      this.y = y;
      this.source = source;
   }

   /**
    * @return the x of the mouse where it was clicked
    */
   public int getX() {
      return x;
   }

   /**
    * @return the y of the mouse where it was clicked
    */
   public int getY() {
      return y;
   }

   /**
    * @return interactable that triggered the event
    */
   public Interactable getSource() {
      return source;
   }
}
