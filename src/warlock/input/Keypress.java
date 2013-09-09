/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.input;

/**
 *
 * @author Focusrite
 */
public class Keypress {
   private int key;
   private boolean down; //if false, just realased

   public Keypress(int key, boolean down) {
      this.key = key;
      this.down = down;
   }

   public int getKey() {
      return key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public boolean isDown() {
      return down;
   }

   public void setDown(boolean down) {
      this.down = down;
   }

}
