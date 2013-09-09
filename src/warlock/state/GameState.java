/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.state;

import warlock.graphic.Graphic;
import warlock.input.InputHandler;

/**
 * Abstract GameState class implementing a singelton pattern.
 *
 * @author Focusrite
 */
public abstract class GameState {

   private static GameState instance;

   public static GameState setInstance(GameState g) {
      if (instance != null) {
         getInstance().destroy();
      }
      GameState.instance = g;
      g.init();
      return g;
   }

   public static GameState getInstance() {
      return GameState.instance;
   }

   public abstract void handleInput(double dt, InputHandler input);

   public abstract void init();

   public abstract void update(double dt);

   public abstract void render(Graphic g);

   public abstract void destroy();
}
