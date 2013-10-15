/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.state;

import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.lobby.Lobby;

/**
 *
 * @author Focusrite
 */
public class LobbyState extends GameState {
   private Lobby lobby;

   public LobbyState() {
   }

   @Override
   public void handleInput(InputHandler input) {
      lobby.handleInput(input);
   }

   @Override
   public void init() {
      lobby = new Lobby();
      getCamera().reset();
   }

   @Override
   public void update(double dt) {
      lobby.update(dt);
   }

   @Override
   public void render(Graphic g) {
      lobby.render(g);
   }

   @Override
   public void destroy() { }

}
