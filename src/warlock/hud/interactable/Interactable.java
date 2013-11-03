/**
 * File: warlock.hud.interactable.Interactable.java
 *
 * An abstract class that is all the interactable components able to be placed, such as Buttons and
 * sliders. Sends messages to its listeners when it is manipulated.
 */
package warlock.hud.interactable;

import java.util.ArrayList;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.state.InputEnabled;
import warlock.state.Renderable;
import warlock.state.Updateable;

/**
 *
 * @author Focusrite
 */
public abstract class Interactable implements InputEnabled, Renderable, Updateable {

   private ArrayList<InteractableListenerSlim> listeners = new ArrayList<>();
   private boolean mouseInside = false;
   private boolean clicked = false;
   private boolean isDown = false;
   private int x, y, width, height;

   /**
    * Create a new interactable
    *
    * @param x
    * @param y
    * @param width
    * @param height
    */
   public Interactable(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   /**
    * @return the x of the interactable
    */
   public int getX() {
      return x;
   }

   /**
    * @return the y of the interactable
    */
   public int getY() {
      return y;
   }

   /**
    * @return the width of the interactable
    */
   public int getWidth() {
      return width;
   }

   /**
    * @return the height of the interactable
    */
   public int getHeight() {
      return height;
   }

   /**
    * Adds a listener to this interactable to notify when an event fires on this interactable
    * @param listener
    */
   public void addListener(InteractableListenerSlim listener) {
      listeners.add(listener);
   }

   /**
    * Notify all listeners this interactable was clicked.
    * @param x
    * @param y
    */
   public void notifyClicked(int x, int y) {
      for (int i = 0; i < listeners.size(); i++) {
         listeners.get(i).clicked(buildInfo(x, y));
      }
   }

   /**
    * Build an interactable info to pass along with the event fired.
    *
    * @param x
    * @param y
    * @return the generated InteractableInfo
    */
   private InteractableInfo buildInfo(int x, int y) {
      return new InteractableInfo(this, x, y);
   }

   /**
    * Build an interactable info to pass along with the event fired.
    *
    * @return the generated InteractableInfo
    */
   private InteractableInfo buildInfo() {
      return new InteractableInfo(this, 0, 0);
   }

   /**
    * Notify all listeners that the mouse entered this interactable
    */
   public void notifyMouseExited() {
      for (int i = 0; i < listeners.size(); i++) {
         if (listeners.get(i) instanceof InteractableListener) {
            ((InteractableListener)(listeners.get(i))).mouseExited(buildInfo());
         }
      }
   }

   /**
    * Notify all listeners that the mouse exited this interactable
    */
   public void notifyMouseEntered() {
      for (int i = 0; i < listeners.size(); i++) {
         if (listeners.get(i) instanceof InteractableListener) {
            ((InteractableListener)(listeners.get(i))).mouseEntered(buildInfo());
         }
      }
   }

   /**
    * Update the whether the mouse is inside this interactable and fire the notify mouseInside and
    * mouseExited, but don't double fire any of the events.
    *
    * @param within if it's currently within
    */
   void updateWithin(boolean within) {
      if (within != mouseInside && within) {
         notifyMouseEntered();
      }
      else if (within != mouseInside) {
         notifyMouseExited();
      }
      mouseInside = within;
   }

   /**
    * Checks if any event should be triggered.
    * @param input
    */
   @Override
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

   /**
    * Returns whether an x,y combination is inside this interactable.
    * @param mx
    * @param my
    * @return boolean if inside
    */
   private boolean isWithin(int mx, int my) {
      return (mx > x && mx < x + width && my > y && my < y + height);
   }

   /**
    * @return if the mouse was inside.
    */
   public boolean isMouseInside() {
      return mouseInside;
   }

   @Override
   public abstract void update(double dt);

   @Override
   public abstract void render(Graphic g);
}
