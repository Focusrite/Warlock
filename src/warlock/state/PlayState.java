/**
 * File: warlock.state.PlayState.java
 *
 * The state taking care of the actual gameplay part of the game.
 */
package warlock.state;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import warlock.game.Game;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.hud.interactable.InteractableInfo;
import warlock.hud.interactable.InteractableListenerSlim;
import warlock.hud.interactable.InteractableTextureButton;
import warlock.input.InputHandler;
import warlock.lobby.Lobby;

public class PlayState extends GameState {

   private static final int QUITBUTTON_WIDTH = 50;
   private static final int QUITBUTTON_HEIGHT = 20;
   private InteractableTextureButton quitButton;
   private Game game;
   private Lobby lobby;
   private boolean menu = false;
   private boolean quit = false;

   /**
    * Create a new PlayState
    * @param lobby
    */
   public PlayState(Lobby lobby) {
      this.lobby = lobby;
   }

   /**
    * Initialize the play state and the "menu" overlay that's displayed when holding ESC.
    */
   @Override
   public void init() {
      this.game = new Game(getCamera(), lobby);
      quitButton = new InteractableTextureButton(1, Display.getHeight() - QUITBUTTON_HEIGHT - 1,
         QUITBUTTON_WIDTH, QUITBUTTON_HEIGHT, "ui-quit",
         Color.LIGHT_GREY, Color.WHITE);
      quitButton.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo source) {
            quit = true; //Exiting mid update loop can cause issues when in the middle of a loop
         }
      });
      this.game.init();
   }

   /**
    * Update the game
    * @param dt
    */
   @Override
   public void update(double dt) {
      if (quit) {
         setInstance(new MenuState());
         return;
      }
      this.game.update(dt);
   }

   /**
    * Render the game
    * @param g
    */
   @Override
   public void render(Graphic g) {
      if (menu) {
         g.setScreenCoordinates(true);
         quitButton.render(g);
         g.setScreenCoordinates(false);
      }
      this.game.render(g);
   }

    /**
    * Handle input of game
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      menu = input.keyHeld(Keyboard.KEY_ESCAPE);
      this.game.handleInput(input);
      quitButton.handleInput(input);
   }
}
