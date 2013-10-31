/**
 * File: warlock.state.LobbyState.java
 *
 * The state which takes care of the lobby part of the game.
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

   /**
    * Create a new LobbyState.
    */
   public LobbyState() {
   }

   /**
    * Handle the lobby's input
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      lobby.handleInput(input);
   }

   /**
    * Initialize and create a new lobby
    */
   @Override
   public void init() {
      lobby = new Lobby();
      getCamera().reset();
   }

   /**
    * Update the lobby
    * @param dt
    */
   @Override
   public void update(double dt) {
      lobby.update(dt);
   }

   /**
    * Render the lobby
    * @param g
    */
   @Override
   public void render(Graphic g) {
      lobby.render(g);
   }

   @Override
   public void destroy() { }

}
