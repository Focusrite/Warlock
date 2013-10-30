/**
 * File: warlock.input.InputHandler.java
 *
 * A handler reading keypresses and mouse interactions and stores them alternatively provides a proxy
 * for all handleInput(..) calls to read.
 */
package warlock.input;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import warlock.Main;

/**
 *
 * @author Focusrite
 */
public class InputHandler {

   Map<Integer, Boolean> keys = new HashMap<>();

   /**
    * Read in what keys are held down
    */
   public void readKeyboard() {
      while (Keyboard.next()) {
         int key = Keyboard.getEventKey();
         keys.put(new Integer(key), Keyboard.getEventKeyState());
      }
   }

   /**
    * Check whether a key is down
    * @param key
    * @return boolean
    */
   public boolean keyHeld(int key) {
      return (keys.containsKey(new Integer(key)) && keys.get(key).booleanValue());
   }

   /**
    * Clear the keypresses
    */
   public void clean() {
      keys.clear();
   }

   /**
    * @return the angle in radians from center of window to mouse position
    */
   public double getMouseAngle() {
      return Math.atan2((double) (Mouse.getX() - (Main.DISPLAY_WIDTH / 2)), (double) (Mouse.getY() - (Main.DISPLAY_HEIGHT / 2)));
   }

   /**
    * @return x of mouse
    */
   public int getMouseX() {
      return Mouse.getX();
   }

   /**
    * @return y of mouse
    */
   public int getMouseY() {
      return Mouse.getY();
   }

   /**
    * @param mb which key
    * @return boolean if down
    */
   public boolean isMouseDown(int mb) {
      return Mouse.isButtonDown(mb);
   }

   /**
    * @return The width of the window
    */
   public int windowWidth() {
      return Main.DISPLAY_WIDTH;
   }

   /**
    * @return the height of the window
    */
   public int windowHeight() {
      return Main.DISPLAY_HEIGHT;
   }
}
