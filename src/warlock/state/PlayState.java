/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.state;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import warlock.game.Game;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.hud.interactables.InteractableInfo;
import warlock.hud.interactables.InteractableListenerSlim;
import warlock.hud.interactables.InteractableTextureButton;
import warlock.input.InputHandler;
import warlock.lobby.Lobby;

/**
 *
 * @author Focusrite
 */
public class PlayState extends GameState {

   private static final int QUITBUTTON_WIDTH = 50;
   private static final int QUITBUTTON_HEIGHT = 20;
   private InteractableTextureButton quitButton;
   private Game game;
   private Lobby lobby;
   private boolean menu = false;
   private boolean quit = false;

   public PlayState(Lobby lobby) {
      this.lobby = lobby;
   }

   @Override
   public void init() {
      this.game = new Game(getCamera(), lobby);
      quitButton = new InteractableTextureButton(1, Display.getHeight() - QUITBUTTON_HEIGHT - 1,
         QUITBUTTON_WIDTH, QUITBUTTON_HEIGHT, "ui-quit",
         Color.LIGHT_GREY, Color.WHITE);
      quitButton.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo source) {
            quit = true;
         }
      });
      this.game.init();
   }

   @Override
   public void update(double dt) {
      if (quit) {
         setInstance(new MenuState());
         return;
      }
      this.game.update(dt);
   }

   @Override
   public void render(Graphic g) {
      if (menu) {
         g.setScreenCoordinates(true);
         quitButton.render(g);
         g.setScreenCoordinates(false);
      }
      this.game.render(g);
   }

   @Override
   public void destroy() {
   }

   @Override
   public void handleInput(InputHandler input) {
      menu = input.keyHeld(Keyboard.KEY_ESCAPE);
      this.game.handleInput(input);
      quitButton.handleInput(input);
   }
}
