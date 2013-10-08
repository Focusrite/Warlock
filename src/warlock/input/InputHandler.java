/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   Map<Integer, Boolean> keyBuffer = new HashMap<>();

   public void readKeyboard() {
      while (Keyboard.next()) {
         int key = Keyboard.getEventKey();
         keyBuffer.put(new Integer(key), Keyboard.getEventKeyState());
      }
   }

   public boolean getKeyHeld() {
      return Keyboard.getEventKeyState();
   }

   public boolean keyHeld(int key) {
      return (keyBuffer.containsKey(new Integer(key)) && keyBuffer.get(key).booleanValue());
   }

   public void clean() {
      keyBuffer.clear();
   }

   public double getMouseAngle() {
      return Math.atan2((double) (Mouse.getX() - (Main.DISPLAY_WIDTH / 2)), (double) (Mouse.getY() - (Main.DISPLAY_HEIGHT / 2)));
   }

   public int getMouseX() {
      return Mouse.getX();
   }

   public int getMouseY() {
      return Mouse.getY();
   }

   public boolean isMouseDown(int mb) {
      return Mouse.isButtonDown(mb);
   }

   public int windowWidth() {
      return Main.DISPLAY_WIDTH;
   }

   public int windowHeight() {
      return Main.DISPLAY_HEIGHT;
   }
}
