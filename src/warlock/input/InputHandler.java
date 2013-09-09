/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.input;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class InputHandler {

   ArrayList<Keypress> keyBuffer = new ArrayList<Keypress>();

   public void readKeyboard() {
      while (Keyboard.next()) {
         keyBuffer.add(new Keypress(Keyboard.getEventKey(), Keyboard.getEventKeyState()));
      }
   }

   public boolean getKeyHeld() {
      return Keyboard.getEventKeyState();
   }


   public void clean() {
      keyBuffer.clear();
   }

   public double getMouseAngle() {
      return Math.atan2(Display.getWidth() - Mouse.getX(), Display.getHeight() - Mouse.getY());
   }

   public boolean isMouseDown(int mb) {
      return Mouse.isButtonDown(mb);
   }
}
