/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import warlock.object.character.DeathListener;
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

   public Player getPlayer() {
      return player;
   }

   public void setPlayer(Player self) {
      this.player = self;
   }

   public Camera getCamera() {
      return camera;
   }

   public void noFurtherPhaseChanges() {
      lastPhaseChange = true;
   }

   public Iterator<Player> getPlayersIterator() {
      return playerList.listIterator();
   }

   public ArrayList<Player> getScoreTable() {
      return scoretable;
   }

   public Game(Camera camera, Lobby lobby) {
      this.camera = camera;
      readFromLobby(lobby);
   }

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

   public int getFirstTo() {
      return firstTo;
   }

   public void setFirstTo(int firstTo) {
      this.firstTo = firstTo;
   }

   public void init() {
      if(isObservermode()) {
         setPhase(new PlayPhase(this));
      }
      else {
         setPhase(new ShopPhase(this, getShopTime()));
      }
   }

   public GamePhase getPhase() {
      return phase;
   }

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

   public void addPlayer(Player p) {
      if (this.playerList.contains(p)) {
         this.playerList.remove(p); //Same playerId exist, remove to replace with new player
      }
      this.playerList.add(p);
      this.scoretable.add(p);
      p.getWarlock().addDeathListener(this); //Register KO listener
      Collections.sort(this.scoretable);
   }

   public void modifyScore(Player p, int dScore) {
      p.modifyScore(dScore);
      Collections.sort(this.scoretable);
   }

   public void update(double dt) {
      if (changedPhase()) {
         return;
      }
      if (transition > 0) {
         transition -= dt;
      }
      phase.update(dt);
   }

   public void render(Graphic g) {
      if (transition > 0) {
         drawTransition(g);
      }
      phase.render(g);
   }

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

   public void handleInput(InputHandler input) {
      this.phase.handleInput(input);
   }

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

   boolean isObservermode() {
      return getPlayer() instanceof ObserverPlayer;
   }

   public int getGroundSize() {
      return groundSize;
   }

   public int getShopTime() {
      return shopTime;
   }

}
