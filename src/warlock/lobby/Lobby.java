/**
 * File: warlock.lobby.Lobby.java
 *
 * The pre-game lobby which keeps track of the settings set up in it and what players have joined.
 * Also handles the actual rendering of the lobby too.
 */
package warlock.lobby;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.hud.interactable.Interactable;
import warlock.hud.interactable.InteractableInfo;
import warlock.hud.interactable.InteractableIntCounter;
import warlock.hud.interactable.InteractableListenerSlim;
import warlock.hud.interactable.InteractableTextField;
import warlock.hud.interactable.InteractableTextureButton;
import warlock.input.InputHandler;
import warlock.player.Player;
import warlock.player.ai.AIPlayer;
import warlock.state.GameState;
import warlock.state.InputEnabled;
import warlock.state.PlayState;
import warlock.state.Renderable;
import warlock.state.Updateable;

/**
 *
 * @author Focusrite
 */
public class Lobby implements InputEnabled, Updateable, Renderable {

   private static final int MAX_PLAYERS = Player.PRIMARY_COLORS.length;
   private static final int MIN_FIRST_TO = 20;
   private static final int MAX_FIRST_TO = 100;
   private static final int MIN_STARTING_GOLD = 0;
   private static final int MAX_STARTING_GOLD = 30;
   private static final int MIN_GROUND_SIZE = 200;
   private static final int MAX_GROUND_SIZE = 1000;
   private static final int MIN_SHOP_TIME = 5;
   private static final int MAX_SHOP_TIME = 30;
   private static final int FIRST_ROW_OFFSETX = 10;
   private static final int STEPSIZE_SMALL = 5;
   private static final int STEPSIZE_BIG = 100;
   private static final int SETTINGS_OFFSETY = 20;
   private static final int SETTING_WIDTH = 84;
   private static final int SETTING_HEIGHT = SETTING_WIDTH / 4;
   private static final int SETTING_ROW_HEIGHT = SETTING_HEIGHT + 5;
   private static final int LOBBY_LINEHEIGHT = 20;
   private static final int LOBBY_PADDING = 5;
   private static final int LOBBY_OFFSETY = 50;
   private static final int LOBBY_WIDTH = 300;
   private static final int PLAY_OFFSETX = 10;
   private static final int PLAY_OFFSETY = SETTINGS_OFFSETY;
   private static final int PLAY_WIDTH = 64;
   private static final int PLAY_HEIGHT = 25;
   private static final String EMPTY_SLOT = " Empty slot";
   private int currentPlayerId = 0;
   private Player[] lobbyList = new Player[MAX_PLAYERS];
   private ArrayList<Player> observers = new ArrayList<>();
   private InteractableTextureButton[] removeButtons = new InteractableTextureButton[MAX_PLAYERS];
   private LobbyStatus status;
   private ArrayList<Interactable> interactables = new ArrayList<>();
   private Player selfPlayer;
   private int firstTo = 50;
   private int startingGold = 10;
   private int groundSize = 700;
   private int shopTime = 15;

   /**
    * Create a new lobby
    */
   public Lobby() {
      status = LobbyStatus.HOST;
      init();
   }

   /**
    * Get the array of players that's on player slots
    *
    * @return
    */
   public Player[] getLobbyList() {
      return lobbyList;
   }

   /**
    * @return the local player
    */
   public Player getSelfPlayer() {
      return selfPlayer;
   }

   /**
    * @return the first to value set (at what score the winner is crowned)
    */
   public int getFirstTo() {
      return firstTo;
   }

   /**
    * @return the shop time per round set
    */
   public int getShopTime() {
      return shopTime;
   }

   /**
    * Add an interactable to the lobby
    *
    * @param i interactable
    */
   public void addInteractable(Interactable i) {
      interactables.add(i);
   }

   /**
    * Initialize the lobby with it's UI.
    */
   private void init() {
      initSettings();

      //Play
      final Lobby self = this;
      Interactable play = new InteractableTextureButton(Display.getWidth() - PLAY_OFFSETX - PLAY_WIDTH, PLAY_OFFSETY,
         PLAY_WIDTH, PLAY_HEIGHT, "ui-play", Color.LIGHT_GREY, Color.WHITE);
      play.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo info) {
            GameState.setInstance(new PlayState(self));
         }
      });
      addInteractable(play);
      initPlayerList();
   }

   /**
    * Initializes the settings part with int counters used to configure the game settings.
    */
   private void initSettings() {
      int x = FIRST_ROW_OFFSETX;
      int y = SETTINGS_OFFSETY;
      //First to -----------------------------------------------------------------------------------
      InteractableIntCounter winAtCounter = new InteractableIntCounter(firstTo, MIN_FIRST_TO, MAX_FIRST_TO,
         STEPSIZE_SMALL, x, y, SETTING_WIDTH, SETTING_HEIGHT);
      winAtCounter.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo source) {
            firstTo = ((InteractableIntCounter) source.getSource()).getValue();
         }
      });
      addInteractable(winAtCounter);
      addInteractable(new InteractableTextField(x + SETTING_WIDTH + 5, y, SETTING_WIDTH,
         SETTING_HEIGHT, "Score to win", Font.SIZE_NORMAL, Color.NONE, Color.WHITE, false));

      y += SETTING_ROW_HEIGHT;
      //Starting gold ------------------------------------------------------------------------------
      InteractableIntCounter startingGoldCounter = new InteractableIntCounter(startingGold,
         MIN_STARTING_GOLD, MAX_STARTING_GOLD, STEPSIZE_SMALL, x, y, SETTING_WIDTH, SETTING_HEIGHT);
      startingGoldCounter.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo source) {
            startingGold = ((InteractableIntCounter) source.getSource()).getValue();
         }
      });
      addInteractable(startingGoldCounter);
      addInteractable(new InteractableTextField(x + SETTING_WIDTH + 5, y, SETTING_WIDTH,
         SETTING_HEIGHT, "Starting gold", Font.SIZE_NORMAL, Color.NONE, Color.WHITE, false));

      y += SETTING_ROW_HEIGHT;
      //Level size ---------------------------------------------------------------------------------
      InteractableIntCounter sizeCounter = new InteractableIntCounter(groundSize,
         MIN_GROUND_SIZE, MAX_GROUND_SIZE, STEPSIZE_BIG, x, y, SETTING_WIDTH, SETTING_HEIGHT);
      sizeCounter.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo source) {
            groundSize = ((InteractableIntCounter) source.getSource()).getValue();
         }
      });
      addInteractable(sizeCounter);
      addInteractable(new InteractableTextField(x + SETTING_WIDTH + 5, y, SETTING_WIDTH,
         SETTING_HEIGHT, "Level size", Font.SIZE_NORMAL, Color.NONE, Color.WHITE, false));

      y += SETTING_ROW_HEIGHT;
      //Starting gold ------------------------------------------------------------------------------
      InteractableIntCounter shoptimeCounter = new InteractableIntCounter(shopTime,
         MIN_SHOP_TIME, MAX_SHOP_TIME, STEPSIZE_SMALL, x, y, SETTING_WIDTH, SETTING_HEIGHT);
      shoptimeCounter.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo source) {
            shopTime = ((InteractableIntCounter) source.getSource()).getValue();
         }
      });
      addInteractable(shoptimeCounter);
      addInteractable(new InteractableTextField(x + SETTING_WIDTH + 5, y, SETTING_WIDTH,
         SETTING_HEIGHT, "Shoptime (s)", Font.SIZE_NORMAL, Color.NONE, Color.WHITE, false));
   }

   /**
    * Initiializes the list of players and the buttons to join a slot, add computer and clear a
    * slot.
    */
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
               if (lobbyList[j] != null) {
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
               if (lobbyList[j] != null) {
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
               if (lobbyList[j] == getSelfPlayer()) {
                  selfPlayer = null;
               }
               if (lobbyList[j] != null) {
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

   /**
    * Render the Lobby
    *
    * @param g
    */
   @Override
   public void render(Graphic g) {
      g.setScreenCoordinates(true);

      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).render(g);
      }
      g.setScreenCoordinates(false);
   }

   /**
    * Update interactables
    *
    * @param dt
    */
   @Override
   public void update(double dt) {
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).update(dt);
      }
   }

   /**
    * Handle the input of all the interactables
    *
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).handleInput(input);
      }
   }

   /**
    * @return the starting gold set
    */
   public int getStartingGold() {
      return startingGold;
   }

   /**
    * @return the ground size set
    */
   public int getGroundSize() {
      return groundSize;
   }

   /**
    * Add a new player to the lobby as an observer (newly connected)
    *
    * @return the newly created Player
    */
   public Player addPlayerToLobby() {
      Player p = new Player(currentPlayerId, Color.NONE, Color.NONE);
      observers.add(p);
      return p;
   }
}
