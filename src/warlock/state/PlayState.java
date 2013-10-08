/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.state;

import warlock.camera.Camera;
import warlock.game.Game;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class PlayState extends GameState {
   private Game game;

   @Override
   public void init() {
      this.game = new Game(getCamera());
      //Later read from lobby info and add players/bots as set up there
      Player self = new Player(1, Color.RED, Color.WINE_RED);
      this.game.addPlayer(self);
      this.game.setPlayer(self);
      this.game.addPlayer(new Player(2, Color.BLUE, Color.SKY_BLUE));
      this.game.init();
   }

   @Override
   public void update(double dt) {
      this.game.update(dt);
   }

   @Override
   public void render(Graphic g) {
      this.game.render(g);
   }

   @Override
   public void destroy() {

   }

   @Override
   public void handleInput(InputHandler input) {
      this.game.handleInput(input);
   }

}
