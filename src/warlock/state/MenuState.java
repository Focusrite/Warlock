/**
 * File: warlock.state.MenuState.java
 *
 * The state which takes care of the menu part of the game
 */
package warlock.state;

import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.menu.Menu;

/**
 *
 * @author Focusrite
 */
public class MenuState extends GameState {
   private Menu menu;

   /**
    * Create a new MenuState
    */
   public MenuState() {
   }

   /**
    * Handle the input of the menu
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      menu.handleInput(input);
   }

   /**
    * Initialize a new menu
    */
   @Override
   public void init() {
      menu = new Menu();
      getCamera().reset();
   }

   /**
    * Update the menu
    * @param dt
    */
   @Override
   public void update(double dt) {
      menu.update(dt);
   }

   /**
    * Render the menu
    * @param g
    */
   @Override
   public void render(Graphic g) {
      menu.render(g);
   }

}
