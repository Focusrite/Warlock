/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   public PlayPhase(final Game owner) {
      super(owner);
      if (getOwner().getPlayer() instanceof ObserverPlayer) {
         setHud(new ObserverHud(getOwner().getPlayer(), getOwner().getScoreTable(), getOwner().getFirstTo()));
      }
      else {
         setHud(new PlayHud(getOwner().getPlayer()));
      }
   }

   @Override
   public void init() {
      this.activeLevel = new Level(getOwner().getCamera());
      Iterator<Player> it = getOwner().getPlayersIterator();
      while (it.hasNext()) {
         Player p = it.next();
         p.getWarlock().cure(); //full health and no status effects
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

   @Override
   public void render(Graphic g) {
      this.activeLevel.render(g);
      getHud().render(g);
   }

   @Override
   public void update(double dt) {
      this.activeLevel.update(dt);
      getHud().update(dt);
      for (int i = 0; i < playersAlive.size(); i++) {
         playersAlive.get(i).update(dt);
      }
   }

   @Override
   public void handleInput(InputHandler input) {
      getOwner().getPlayer().handleInput(input);
   }

   @Override
   public void notifyDeath(Player p, Player killer) {
      if (killer != null) {
         killer.setKillingblows(killer.getKillingblows() + 1);
         killer.modifyGold(POINTS_FOR_KILL);
         getOwner().modifyScore(killer, POINTS_FOR_KILL);
         addMessage(Message.randomMessage(Message.KILLED_MESSAGES,
            p.getPrimaryColor().toString() + p + "|",
            killer.getPrimaryColor().toString() + killer + "|"));
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
               getOwner().setPhase(new ShopPhase(getOwner()));
            }
         }
      });
      t.setRepeats(false);
      t.start();
   }

   private void reward() {
      for (int i = 0; i < playersAlive.size(); i++) {
         playersAlive.get(i).modifyGold(POINTS_FOR_REMAINING);
         getOwner().modifyScore(playersAlive.get(i), POINTS_FOR_REMAINING);
      }
   }

   private void addMessage(String message) {
      if(getHud() instanceof PlayHud) {
         ((PlayHud)getHud()).addMessage(message);
      }
      else if(getHud() instanceof ObserverHud) {
         ((ObserverHud)getHud()).addMessage(message);
      }
   }
}
