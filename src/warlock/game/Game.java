/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import warlock.camera.Camera;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.player.Player;

/**
 * -----> TODO: Add listener from player to listen when kills etc happen to display messages
 *
 * @author Focusrite
 */
public class Game {

   private ArrayList<Player> playerList = new ArrayList<>();
   private ArrayList<Player> scoreTable = new ArrayList<>();
   private Player player; //The local player
   private GamePhase phase;
   private Camera camera;

   public Player getPlayer() {
      return player;
   }

   public void setPlayer(Player self) {
      this.player = self;
   }

   public Camera getCamera() {
      return camera;
   }

   public Iterator<Player> getPlayersIterator() {
      return playerList.listIterator();
   }

   public Game(Camera camera) {
      this.camera = camera;
   }

   public void init() {
      setPhase(new PlayPhase(this)); //should start at shopphase
   }

   public GamePhase getPhase() {
      return phase;
   }

   public final void setPhase(GamePhase phase) {
      if (this.phase != null) {
         this.phase.terminate();
      }
      this.phase = phase;
      this.phase.init();
   }

   public void addPlayer(Player p) {
      if (this.playerList.contains(p)) {
         this.playerList.remove(p); //Same playerId exist, remove to replace with new player
      }
      this.playerList.add(p);
      this.scoreTable.add(p);
      Collections.sort(this.scoreTable);
   }

   public void modifyScore(Player p, int dScore) {
      p.modifyScore(dScore);
      Collections.sort(this.scoreTable);
   }

   public void update(double dt) {
      phase.update(dt);
   }

   public void render(Graphic g) {
      phase.render(g);
   }

   public void handleInput(InputHandler input) {
      this.player.handleInput(input);
      this.phase.handleInput(input);
   }
}
