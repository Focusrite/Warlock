/**
 * File: warlock.menu.Menu.java
 *
 * The glorious startup menu!
 */
package warlock.menu;

import java.util.ArrayList;
import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.hud.interactable.Interactable;
import warlock.hud.interactable.InteractableInfo;
import warlock.hud.interactable.InteractableListenerSlim;
import warlock.hud.interactable.InteractableTextureButton;
import warlock.input.InputHandler;
import warlock.resource.ResourceHandler;
import warlock.state.GameState;
import warlock.state.LobbyState;

/**
 *
 * @author Focusrite
 */
public class Menu {

   private static final int BUTTON_OFFSETX = 10;
   private static final int BUTTON_PLAY_OFFSETY = 60;
   private static final int BUTTON_EXIT_OFFSETY = 30;
   private static final int BUTTON_WIDTH = 50;
   private static final int BUTTON_HEIGHT = 24;
   private static final int LOGO_OFFSETX = 0;
   private static final int LOGO_OFFSETY = -50;
   private static final int LOGO_WIDTH = 512;
   private static final int LOGO_HEIGHT = 256;
   private ArrayList<Interactable> interactables = new ArrayList<>();
   private GameState owner;

   /**
    * Create a start menu with the menu buttons
    * @param owner
    */
   public Menu(GameState owner) {
      this.owner = owner;
      init();
   }

   /**
    * Add an interactable to the menu
    * @param i
    */
   public void addInteractable(Interactable i) {
      interactables.add(i);
   }

   /**
    * Initialize the menu with all the interactables they have
    */
   private void init() {
      Interactable play = new InteractableTextureButton(BUTTON_OFFSETX, BUTTON_PLAY_OFFSETY,
         BUTTON_WIDTH, BUTTON_HEIGHT, "ui-play", Color.LIGHT_GREY, Color.WHITE);
      play.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo info) {
            GameState.setInstance(new LobbyState());
         }
      });
      addInteractable(play);

      Interactable exit = new InteractableTextureButton(BUTTON_OFFSETX, BUTTON_EXIT_OFFSETY,
         BUTTON_WIDTH, BUTTON_HEIGHT, "ui-exit", Color.LIGHT_GREY, Color.WHITE);
      exit.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo info) {
            owner.exit();
         }
      });
      addInteractable(exit);
   }

   /**
    * Render the menu
    * @param g
    */
   public void render(Graphic g) {
      g.setScreenCoordinates(true);
      g.drawTexture(ResourceHandler.getTexture("ui-logo"),
         LOGO_OFFSETX + LOGO_WIDTH / 2, g.getScreenHeight() - LOGO_OFFSETY - LOGO_WIDTH / 2,
         ZLayers.GUI, LOGO_WIDTH, LOGO_HEIGHT, 0);
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).render(g);
      }
      g.setScreenCoordinates(false);
   }

   /**
    * Update all interactables
    * @param dt
    */
   public void update(double dt) {
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).update(dt);
      }
   }

   /**
    * Handle input for all interactables
    * @param input
    */
   public void handleInput(InputHandler input) {
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).handleInput(input);
      }
   }
}
