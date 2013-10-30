/**
 * File: warlock.player.ObserverPlayer.java
 *
 * On observing player not actually in the game. Overrides some of the methods to remove the "Warlock"
 * aspect of the player.
 */
package warlock.player;

import warlock.graphic.Color;
import warlock.input.InputHandler;

/**
 *
 * @author Focusrite
 */
public class ObserverPlayer extends Player {

   /**
    * Create a new observer
    */
   public ObserverPlayer() {
      super(0, Color.NONE, Color.NONE);
      setName("Observer");
   }

   /**
    * Handle input, only needs to be able to controll scrolling
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      handleScrolling(input);
   }



}
