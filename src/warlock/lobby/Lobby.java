/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.lobby;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.hud.interactables.Interactable;
import warlock.hud.interactables.InteractableInfo;
import warlock.hud.interactables.InteractableIntCounter;
import warlock.hud.interactables.InteractableListener;
import warlock.hud.interactables.InteractableListenerSlim;
import warlock.hud.interactables.InteractableTextField;
import warlock.hud.interactables.InteractableTextureButton;
import warlock.input.InputHandler;
import warlock.player.Player;
import warlock.player.ai.AIPlayer;
import warlock.state.GameState;
import warlock.state.PlayState;

/**
 *
 * @author Focusrite
 */
public class Lobby {

   private static final int MAX_PLAYERS = Player.PRIMARY_COLORS.length;
   private static final int MIN_FIRST_TO = 20;
   private static final int MAX_FIRST_TO = 200;
   private static final int FIRST_ROW_OFFSETX = 10;
   private static final int FIRST_TO_STEP = 5;
   private static final int FIRST_TO_OFFSETY = 20;
   private static final int FIRST_TO_WIDTH = 80;
   private static final int FIRST_TO_HEIGHT = FIRST_TO_WIDTH / 4;
   private static final int LOBBY_LINEHEIGHT = 20;
   private static final int LOBBY_PADDING = 5;
   private static final int LOBBY_OFFSETY = 50;
   private static final int LOBBY_WIDTH = 300;
   private static final int PLAY_OFFSETX = 10;
   private static final int PLAY_OFFSETY = FIRST_TO_OFFSETY;
   private static final int PLAY_WIDTH = 64;
   private static final int PLAY_HEIGHT = 25;
   private static final String EMPTY_SLOT = " Empty slot";

   private Player[] lobbyList = new Player[MAX_PLAYERS];
   private InteractableTextureButton[] removeButtons = new InteractableTextureButton[MAX_PLAYERS];
   private LobbyStatus status;
   private ArrayList<Interactable> interactables = new ArrayList<>();
   private Player selfPlayer;
   private int firstTo = 50;

   public Lobby() {
      status = LobbyStatus.HOST;
      init();
   }

   public Player[] getLobbyList() {
      return lobbyList;
   }

   public Player getSelfPlayer() {
      return selfPlayer;
   }

   public int getFirstTo() {
      return firstTo;
   }

   public void addInteractable(Interactable i) {
      interactables.add(i);
   }

   private void init() {
      //First to
      InteractableIntCounter counter = new InteractableIntCounter(firstTo, MIN_FIRST_TO, MAX_FIRST_TO, FIRST_TO_STEP,
         FIRST_ROW_OFFSETX, FIRST_TO_OFFSETY, FIRST_TO_WIDTH, FIRST_TO_HEIGHT);
      counter.addListener(new InteractableListener() {
         @Override
         public void clicked(InteractableInfo source) {
            firstTo = ((InteractableIntCounter) source.getSource()).getValue();
         }

         @Override
         public void mouseEntered(InteractableInfo source) {
         }

         @Override
         public void mouseExited(InteractableInfo source) {
         }
      });
      addInteractable(counter);
      addInteractable(new InteractableTextField(FIRST_ROW_OFFSETX + FIRST_TO_WIDTH * 2 - 5, FIRST_TO_OFFSETY,
         ZLayers.GUI, FIRST_TO_HEIGHT, "Win at score", Font.SIZE_NORMAL, Color.NONE, Color.WHITE, true));

      //Play
      final Lobby self = this;
      Interactable play = new InteractableTextureButton(Display.getWidth() - PLAY_OFFSETX - PLAY_WIDTH, PLAY_OFFSETY,
         PLAY_WIDTH, PLAY_HEIGHT, "ui-play", Color.LIGHT_GREY, Color.WHITE);
      play.addListener(new InteractableListener() {
         @Override
         public void clicked(InteractableInfo info) {
            GameState.setInstance(new PlayState(self));
         }

         @Override
         public void mouseEntered(InteractableInfo info) {
         }

         @Override
         public void mouseExited(InteractableInfo info) {
         }
      });
      addInteractable(play);
      initPlayerList();
   }

   private void initPlayerList() {
      int x = FIRST_ROW_OFFSETX;
      int y = Display.getHeight() - LOBBY_OFFSETY;
      for (int i = 0; i < lobbyList.length; i++) {
         final InteractableTextField field = new InteractableTextField(x, y, LOBBY_WIDTH, LOBBY_LINEHEIGHT,
            EMPTY_SLOT, Font.SIZE_NORMAL, Player.PRIMARY_COLORS[i], Color.BLACK, false);
         x += LOBBY_WIDTH + LOBBY_PADDING;
         final int j = i;

         //Join slot
         Interactable joinSlot = new InteractableTextureButton(x, y, LOBBY_LINEHEIGHT, LOBBY_LINEHEIGHT,
            "ui-join", Color.LIGHT_GREY, Color.WHITE);
         joinSlot.addListener(new InteractableListenerSlim() {
            @Override
            public void clicked(InteractableInfo source) {
               if(lobbyList[j] != null) {
                  return; //Cant join an already occupied slot
               }
               if (selfPlayer != null) { //Remove if player had previous position
                  removeButtons[selfPlayer.getPlayerId() - 1].notifyClicked(0, 0);
               }

               selfPlayer = new Player(j + 1, Player.PRIMARY_COLORS[j], Player.SECONDARY_COLORS[j]);
               lobbyList[j] = selfPlayer;
               field.setText(" " + lobbyList[j].toString());
            }
         });

         //Add computer
         x += LOBBY_LINEHEIGHT + LOBBY_PADDING;
         Interactable addComputer = new InteractableTextureButton(x, y, LOBBY_LINEHEIGHT, LOBBY_LINEHEIGHT,
            "ui-computer", Color.LIGHT_GREY, Color.WHITE);

         addComputer.addListener(new InteractableListenerSlim() {
            @Override
            public void clicked(InteractableInfo source) {
               if(lobbyList[j] != null) {
                  return; //Cant join an already occupied slot
               }
               lobbyList[j] = new AIPlayer(j + 1, Player.PRIMARY_COLORS[j], Player.SECONDARY_COLORS[j]);
               field.setText(" " + lobbyList[j].toString());
            }
         });

         //Remove player on position
         x += LOBBY_LINEHEIGHT + LOBBY_PADDING;
         InteractableTextureButton remove = new InteractableTextureButton(x, y, LOBBY_LINEHEIGHT, LOBBY_LINEHEIGHT,
            "ui-remove", Color.LIGHT_GREY, Color.WHITE);

         remove.addListener(new InteractableListenerSlim() {
            @Override
            public void clicked(InteractableInfo source) {
               if(lobbyList[j] == getSelfPlayer()) {
                  selfPlayer = null;
               }
               if(lobbyList[j] != null) {
                  lobbyList[j] = null;
                  field.setText(EMPTY_SLOT);
               }
            }
         });

         //Add
         addInteractable(field);
         addInteractable(joinSlot);
         addInteractable(addComputer);
         addInteractable(remove);
         removeButtons[i] = remove;
         y -= LOBBY_LINEHEIGHT + LOBBY_PADDING;
         x = FIRST_ROW_OFFSETX;
      }
   }

   public void render(Graphic g) {
      g.setScreenCoordinates(true);

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
