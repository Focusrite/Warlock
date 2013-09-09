/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.state;

import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.level.Level;

/**
 *
 * @author Focusrite
 */
public class PlayState extends GameState {
   private Level activeLevel;

   @Override
   public void init() {
      activeLevel = new Level();
      activeLevel.init();
   }

   @Override
   public void update(double dt) {
      activeLevel.update(dt);
   }

   @Override
   public void render(Graphic g) {
      activeLevel.render(g);
   }

   @Override
   public void destroy() {

   }

   @Override
   public void handleInput(double dt, InputHandler input) {
      activeLevel.handleInput(dt, input);
   }

}
