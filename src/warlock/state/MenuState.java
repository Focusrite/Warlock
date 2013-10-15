/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   public MenuState() {
   }

   @Override
   public void handleInput(InputHandler input) {
      menu.handleInput(input);
   }

   @Override
   public void init() {
      menu = new Menu(this);
      getCamera().reset();
   }

   @Override
   public void update(double dt) {
      menu.update(dt);
   }

   @Override
   public void render(Graphic g) {
      menu.render(g);
   }

   @Override
   public void destroy() { }

}
