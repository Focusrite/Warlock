/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.player;

import warlock.graphic.Color;
import warlock.input.InputHandler;

/**
 *
 * @author Focusrite
 */
public class ObserverPlayer extends Player {

   public ObserverPlayer() {
      super(0, Color.NONE, Color.NONE);
      setName("Observer");
   }

   @Override
   public void handleInput(InputHandler input) {
      handleScrolling(input);
   }



}
