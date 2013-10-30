/**
 * File: warlock.game.PlayPhase.java
 *
 * Instance of GamePhase. The phase in which the actual game is taking place, where warlocks battle
 * out for the ultimate prize of becoming the number #1 warlock on the block.
 *
 * This round ends when only one warlock is left alive, or there is a winner.
 */
package warlock.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;
import warlock.graphic.Graphic;
import warlock.hud.ObserverHud;
import warlock.hud.PlayHud;
import warlock.input.InputHandler;
import warlock.level.Level;
import warlock.message.Message;
import warlock.player.ObserverPlayer;
import warlock.player.Player;
import warlock.player.ai.AIPlayer;

/**
 *
 * @author Focusrite
 */
public class PlayPhase extends GamePhase {

   private static final double END_DELAY = PlayHud.MESSAGE_TIME; //Allow last message to finish view
   private static final int POINTS_FOR_REMAINING = 1;
   private static final int POINTS_FOR_KILL = 1;
   private static final int GOLD_PER_LEVEL = 5;
   private static final int POINTS_FOR_LAST_REMAINING = 5;
   private Level activeLevel;
   private ArrayList<Player> playersAlive = new ArrayList<>();

   /**
    * Initialize a new play phase and set up the appropriate hud depending on if the player is observing
    * or playing.
    * @param owner
    */
   public PlayPhase(final Game owner) {
      super(owner);
      if (getOwner().getPlayer() instanceof ObserverPlayer) {
         setHud(new ObserverHud(getOwner().getPlayer(), getOwner().getScoreTable(), getOwner().getFirstTo()));
      }
      else {
         setHud(new PlayHud(getOwner().getPlayer()));
      }
   }

   /**
    * Initialize the play phase.
    *
    * Sets up the level and adds the player's warlocks to the level.
    */
   @Override
   public void init() {
      this.activeLevel = new Level(getOwner().getCamera(), getOwner().getGroundSize());
      Iterator<Player> it = getOwner().getPlayersIterator();
      while (it.hasNext()) {
         Player p = it.next();
         p.getWarlock().cure(); //full health and no status effects for each new round
         this.activeLevel.addObjectRandomPosition(p.getWarlock(), p);
         playersAlive.add(p);
         if(p instanceof AIPlayer) {
            ((AIPlayer)p).shop();
         }
      }
      activeLevel.centerCameraOn(getOwner().getPlayer().getWarlock());
      if(getOwner().isObservermode()) {
         getOwner().getPlayer().getWarlock().setLevel(activeLevel);
      }
   }

   /**
    * Call hud's and level's render
    * @param g
    */
   @Override
   public void render(Graphic g) {
      this.activeLevel.render(g);
      getHud().render(g);
   }

   /**
    * Update the level, hud and players that are alive
    * @param dt
    */
   @Override
   public void update(double dt) {
      this.activeLevel.update(dt);
      getHud().update(dt);
      for (int i = 0; i < playersAlive.size(); i++) {
         playersAlive.get(i).update(dt);
      }
   }

   /**
    * Call the local player's handleInput
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      getOwner().getPlayer().handleInput(input);
   }

   /**
    * Award money and update score for kills as well as provide a cool edgy text.
    * @param p
    * @param killer
    */
   @Override
   public void notifyDeath(Player p, Player killer) {
      if (killer != null && p != killer) {
         killer.setKillingblows(killer.getKillingblows() + 1);
         killer.modifyGold(POINTS_FOR_KILL);
         getOwner().modifyScore(killer, POINTS_FOR_KILL);
         addMessage(Message.randomMessage(Message.KILLED_MESSAGES,
            p.getPrimaryColor().toString() + p + "|",
            killer.getPrimaryColor().toString() + killer + "|"));
      }
      else if(killer != null) {
         addMessage(Message.randomMessage(Message.DENIED, p.getPrimaryColor().toString() + p + "|"));
      }
      else {
         addMessage(Message.randomMessage(Message.BURNED_MESSAGES,
            p.getPrimaryColor().toString() + p + "|"));
      }
      if (playersAlive.size() <= 1) {
         return;
      }
      playersAlive.remove(p);
      if (playersAlive.size() > 1) {
         reward();
      }
      else {
         endRound();
      }
   }

   /**
    * A round has ended, exit the play phase!
    */
   private void endRound() {
      Player lastAlive = playersAlive.get(0);
      lastAlive.modifyGold(POINTS_FOR_LAST_REMAINING);
      getOwner().modifyScore(lastAlive, POINTS_FOR_REMAINING);
      addMessage(Message.randomMessage(Message.VICTORIOUS,
         lastAlive.getPrimaryColor().toString() + lastAlive + "|"));

      Iterator<Player> iter = getOwner().getPlayersIterator(); //Add gold to all
      while (iter.hasNext()) {
         iter.next().modifyGold(GOLD_PER_LEVEL);
      }

      Timer t = new Timer((int) (END_DELAY * 1000), new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            if (getOwner().isObservermode()) {
               getOwner().setPhase(new PlayPhase(getOwner()));
            }
            else {
               getOwner().setPhase(new ShopPhase(getOwner(), getOwner().getShopTime()));
            }
         }
      });
      t.setRepeats(false);
      t.start();
   }

   /**
    * Reward all players alive when another player's warlock gets iced.
    */
   private void reward() {
      for (int i = 0; i < playersAlive.size(); i++) {
         playersAlive.get(i).modifyGold(POINTS_FOR_REMAINING);
         getOwner().modifyScore(playersAlive.get(i), POINTS_FOR_REMAINING);
      }
   }

   /**
    * Add a message to be displayed to the hud.
    * @param message
    */
   private void addMessage(String message) {
      if(getHud() instanceof PlayHud) {
         ((PlayHud)getHud()).addMessage(message);
      }
      else if(getHud() instanceof ObserverHud) {
         ((ObserverHud)getHud()).addMessage(message);
      }
   }
}
