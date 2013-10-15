/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.menu;

import java.util.ArrayList;
import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.hud.interactables.Interactable;
import warlock.hud.interactables.InteractableInfo;
import warlock.hud.interactables.InteractableListener;
import warlock.hud.interactables.InteractableTextureButton;
import warlock.input.InputHandler;
import warlock.resource.ResourceManager;
import warlock.state.GameState;
import warlock.state.LobbyState;
import warlock.state.PlayState;

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

   public Menu(GameState owner) {
      this.owner = owner;
      init();
   }

   public void addInteractable(Interactable i) {
      interactables.add(i);
   }

   private void init() {
      Interactable play = new InteractableTextureButton(BUTTON_OFFSETX, BUTTON_PLAY_OFFSETY,
         BUTTON_WIDTH, BUTTON_HEIGHT, "ui-play", Color.LIGHT_GREY, Color.WHITE);
      play.addListener(new InteractableListener() {
         @Override
         public void clicked(InteractableInfo info) {
            GameState.setInstance(new LobbyState());
         }

         @Override
         public void mouseEntered(InteractableInfo info) {
         }

         @Override
         public void mouseExited(InteractableInfo info) {
         }
      });
      addInteractable(play);

      Interactable exit = new InteractableTextureButton(BUTTON_OFFSETX, BUTTON_EXIT_OFFSETY,
         BUTTON_WIDTH, BUTTON_HEIGHT, "ui-exit", Color.LIGHT_GREY, Color.WHITE);
      exit.addListener(new InteractableListener() {
         @Override
         public void clicked(InteractableInfo info) {
            owner.exit();
         }

         @Override
         public void mouseEntered(InteractableInfo info) {
         }

         @Override
         public void mouseExited(InteractableInfo info) {
         }
      });
      addInteractable(exit);
   }

   public void render(Graphic g) {
      g.setScreenCoordinates(true);
      g.drawTexture(ResourceManager.getTexture("ui-logo"),
         LOGO_OFFSETX + LOGO_WIDTH / 2, g.getScreenHeight() - LOGO_OFFSETY - LOGO_WIDTH / 2,
         ZLayers.GUI, LOGO_WIDTH, LOGO_HEIGHT, 0);
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).render(g);
      }
      g.setScreenCoordinates(false);
   }

   public void update(double dt) {
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).update(dt);
      }
   }

   public void handleInput(InputHandler input) {
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).handleInput(input);
      }
   }
}
