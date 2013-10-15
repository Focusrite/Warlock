/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud.interactables;

import java.util.ArrayList;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;

/**
 *
 * @author Focusrite
 */
public abstract class Interactable {

   private ArrayList<InteractableListenerSlim> listeners = new ArrayList<>();
   private boolean mouseInside = false;
   private boolean clicked = false;
   private boolean isDown = false;
   private int x, y, width, height;

   public Interactable(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public int getWidth() {
      return width;
   }

   public int getHeight() {
      return height;
   }

   public void addListener(InteractableListenerSlim listener) {
      listeners.add(listener);
   }

   public void notifyClicked(int x, int y) {
      for (int i = 0; i < listeners.size(); i++) {
         listeners.get(i).clicked(buildInfo(x, y));
      }
   }

   private InteractableInfo buildInfo(int x, int y) {
      return new InteractableInfo(this, x, y);
   }

   private InteractableInfo buildInfo() {
      return new InteractableInfo(this, 0, 0);
   }

   public void notifyMouseExited() {
      for (int i = 0; i < listeners.size(); i++) {
         if (listeners.get(i) instanceof InteractableListener) {
            ((InteractableListener)(listeners.get(i))).mouseExited(buildInfo());
         }
      }
   }

   public void notifyMouseEntered() {
      for (int i = 0; i < listeners.size(); i++) {
         if (listeners.get(i) instanceof InteractableListener) {
            ((InteractableListener)(listeners.get(i))).mouseEntered(buildInfo());
         }
      }
   }

   void updateWithin(boolean within) {
      if (within != mouseInside && within) {
         notifyMouseEntered();
      }
      else if (within != mouseInside && !within) {
         notifyMouseExited();
      }
      mouseInside = within;
   }

   public void handleInput(InputHandler input) {
      updateWithin(isWithin(input.getMouseX(), input.getMouseY()));

      if (!clicked && input.isMouseDown(0) && mouseInside) { //Clicked
         clicked = true;
         isDown = true;
      }
      else if (isDown && !input.isMouseDown(0) && mouseInside) { //Released, register event
         clicked = false;
         isDown = false;
         notifyClicked(input.getMouseX(), input.getMouseY());
      }
   }

   private boolean isWithin(int mx, int my) {
      return (mx > x && mx < x + width && my > y && my < y + height);
   }

   public boolean isMouseInside() {
      return mouseInside;
   }

   public abstract void update(double dt);

   public abstract void render(Graphic g);
}
