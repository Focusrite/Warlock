/**
 * File: warlock.game.Game.java
 *
 * The class handling the actual gameplay aspects, such as keeping track of the score, variable
 * gameplay values (Starting gold etc) loaded from the lobby. The actual gameplay, however, is not
 * handled here.
 *
 * Since the game is divided into different gameplay "phases" (Shop phase, Play phase) the game
 * has a member "phase" that runs the current phase related gameplay aspects, ie. this class utilizes
 * a state pattern to keep track of what gameplay wise is really happening.
 */
package warlock.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import warlock.camera.Camera;
import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.lobby.Lobby;
import warlock.object.warlock.DeathListener;
import warlock.player.ObserverPlayer;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class Game implements DeathListener {

   private static final double TRANSITION_TIME = 2;
   private double transition = 0;
   private boolean inTransition = true;
   private static final int TRANSITION_BARS = 16;
   private ArrayList<Player> playerList = new ArrayList<>();
   private ArrayList<Player> scoretable = new ArrayList<>();
   private Player player; //The local player
   private GamePhase phase;
   private GamePhase temporaryPhase;
   private Camera camera;
   private boolean lastPhaseChange = false;
   private int firstTo;
   private int startingGold;
   private int groundSize;
   private int shopTime;

   /**
    * @return the own local player
    */
   public Player getPlayer() {
      return player;
   }

   /**
    * @param self new local player
    */
   public void setPlayer(Player self) {
      this.player = self;
   }

   /**
    * @return the camera
    */
   public Camera getCamera() {
      return camera;
   }

   /**
    * Sets a flag indicationg that the game is over and the game phase no longer can be changed.
    */
   public void noFurtherPhaseChanges() {
      lastPhaseChange = true;
   }

   /**
    * @return an iterator of all the playing players in the game
    */
   public Iterator<Player> getPlayersIterator() {
      return playerList.listIterator();
   }

   /**
    * @return the sorted ArrayList with the scoreboard. Leading player at position 0.
    */
   public ArrayList<Player> getScoreTable() {
      return scoretable;
   }

   /**
    * Initialize a new game and read info from lobby.
    * @param camera
    * @param lobby
    */
   public Game(Camera camera, Lobby lobby) {
      this.camera = camera;
      readFromLobby(lobby);
   }

   /**
    * Read the settings that was set in the lobby and apply to the game.
    * @param lobby
    */
   private void readFromLobby(Lobby lobby) {
      startingGold = lobby.getStartingGold();
      groundSize = lobby.getGroundSize();
      shopTime = lobby.getShopTime();
      if (lobby.getSelfPlayer() == null) {
         setPlayer(new ObserverPlayer());
      }
      else {
         setPlayer(lobby.getSelfPlayer());
      }
      for (int i = 0; i < lobby.getLobbyList().length; i++) {
         if (lobby.getLobbyList()[i] != null) {
            addPlayer(lobby.getLobbyList()[i]);
            lobby.getLobbyList()[i].setGold(startingGold);
         }
      }
      setFirstTo(lobby.getFirstTo());
   }

   /**
    * @return the score set that the game terminates at
    */
   public int getFirstTo() {
      return firstTo;
   }

   /**
    * @param firstTo new FirstTo value
    */
   public void setFirstTo(int firstTo) {
      this.firstTo = firstTo;
   }

   /**
    * Initialize a new game and set correct game phase.
    */
   public void init() {
      if(isObservermode()) {
         setPhase(new PlayPhase(this));
      }
      else {
         setPhase(new ShopPhase(this, getShopTime()));
      }
   }

   /**
    * @return the current phase
    */
   public GamePhase getPhase() {
      return phase;
   }

   /**
    * Sets the current phase, the change is not instantaneous, instead a fadeout animation is played
    * to fade to the new one.
    *
    * @param phase new phase
    */
   public final void setPhase(GamePhase phase) {
      if(lastPhaseChange) {
         return;
      }
      temporaryPhase = phase;
      if (this.phase == null) {
         changedPhase();
      }
      else {
         transition = TRANSITION_TIME;
         inTransition = true;
      }
   }

   /**
    * @return true if the phasechange reached a point where it can be changed, ie transition finished
    * playing.
    */
   private boolean changedPhase() {
      if (temporaryPhase == null || transition > 0) {
         return false;
      }
      if (this.phase != null) {
         transition = TRANSITION_TIME;
         inTransition = !inTransition;
         this.phase.terminate();
      }
      this.phase = temporaryPhase;
      this.phase.init();
      this.temporaryPhase = null;
      return true;
   }

   /**
    * Add a player to the game as a player
    * @param p
    */
   public void addPlayer(Player p) {
      if (this.playerList.contains(p)) {
         this.playerList.remove(p); //Same playerId exist, remove to replace with new player
      }
      this.playerList.add(p);
      this.scoretable.add(p);
      p.getWarlock().addDeathListener(this); //Register KO listener
      Collections.sort(this.scoretable);
   }

   /**
    * Modify a players score.
    *
    * @param p
    * @param dScore
    */
   public void modifyScore(Player p, int dScore) {
      p.modifyScore(dScore);
      Collections.sort(this.scoretable);
   }

   /**
    * Run phase's update function and check for phase change.
    * @param dt
    */
   public void update(double dt) {
      if (changedPhase()) {
         return;
      }
      if (transition > 0) {
         transition -= dt;
      }
      phase.update(dt);
   }

   /**
    * Call phase's render and if transitioning, draw transition effect
    * @param g
    */
   public void render(Graphic g) {
      if (transition > 0) {
         drawTransition(g);
      }
      phase.render(g);
   }

   /**
    * The transition effect.
    * @param g
    */
   private void drawTransition(Graphic g) {
      g.setScreenCoordinates(true);
      double transitionLeft = (inTransition) ? 1 - (transition / TRANSITION_TIME) : (transition / TRANSITION_TIME);
      int x = (g.getScreenWidth() / TRANSITION_BARS) / 2;
      double height = (transitionLeft * TRANSITION_BARS);
      while (height > 0) {
         int h = (int) (g.getScreenHeight() * Math.min(1, height));
         g.drawRectangle(x, h / 2, ZLayers.OVERLAY,
            g.getScreenWidth() / TRANSITION_BARS, h,
            0, Color.BLACK);
         x += (g.getScreenWidth() / TRANSITION_BARS);
         height -= 1;
      }
      g.setScreenCoordinates(false);
   }

   /**
    * Call phase's handleInput
    * @param input
    */
   public void handleInput(InputHandler input) {
      this.phase.handleInput(input);
   }

   /**
    * Ran when a player death is registered to update scoreboeard and award points.
    * @param p
    * @param killer
    */
   @Override
   public void notifyDeath(Player p, Player killer) {
      this.phase.notifyDeath(p, killer);
      Collections.sort(this.scoretable);
      if(scoretable.get(0).getScore() >= firstTo && scoretable.size() > 1 &&
         scoretable.get(0).getScore() != scoretable.get(1).getScore()) {
         setPhase(new GameOverPhase(this, scoretable));
         noFurtherPhaseChanges();
      }
   }

   /**
    * @return true if local player is observing
    */
   boolean isObservermode() {
      return getPlayer() instanceof ObserverPlayer;
   }

   /**
    * @return the size of the level set in lobby
    */
   public int getGroundSize() {
      return groundSize;
   }

   /**
    * @return the shop time set in lobby
    */
   public int getShopTime() {
      return shopTime;
   }

}
