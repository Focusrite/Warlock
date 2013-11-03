/**
 * File: warlock.player.ai.AIMode.java
 *
 * An extended Player that has AI capabilities. It has a state member, aiMode, which notes what the AI
 * should actually do, and this class only handles shopping and the changing of states ("thinking").
 */

package warlock.player.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import warlock.ExtraMath;
import warlock.graphic.Color;
import warlock.level.GroundType;
import warlock.player.Player;
import warlock.shop.ShopHandler;
import warlock.shop.ShopItem;

/**
 *
 * @author Focusrite
 */
public class AIPlayer extends Player {

   private AIMode aiMode;
   private double modeTime;

   /**
    * Create a new AIPlayer.
    * @param playerId
    * @param primary
    * @param secondary
    */
   public AIPlayer(int playerId, Color primary, Color secondary) {
      super(playerId, primary, secondary);
      setName("Computer " + playerId);
      changeMode(new AIModeFleeing(this));
   }

   /**
    * Update the player, ie. execute AI actions.
    * @param dt
    */
   @Override
   public void update(double dt) {
      if(getLevel().getWarlocksLeft() <= 1) {
         return;
      }
      brain(dt);
      aiMode.updateCooldowns(dt);
      aiMode.execute(dt);
   }

   /**
    * @return the current ai mode
    */
   public AIMode getAIMode() {
      return aiMode;
   }

   /**
    * Do the thinking on what mode the ai should enter
    * @param dt
    */
   private void brain(double dt) {
      modeTime += dt;
      if (modeTime < aiMode.getMinModeTime()) {
         return;
      }

      if (getWarlock().getGroundType() == GroundType.LAVA) {
         changeMode(new AIModeFleeing(this));
      }
      else {
         changeMode(new AIModeCircleTarget(this, getWarlock().getClosestWarlock()));
      }
   }

   /**
    * Change the mode and reset mode timer
    * @param newMode
    */
   private void changeMode(AIMode newMode) {
      modeTime = 0;
      aiMode = newMode;
   }

   /**
    * Buy stuff! Buys the most pricey item they possibly can first and foremost
    */
   public void shop() {
      if(ExtraMath.getRandom().nextInt(3) == 0) {
         return; //Sometimes saves up for next round..
      }
      ArrayList<ShopItem> canPurchase = new ArrayList<>();
      Iterator<ShopItem> iter = ShopHandler.iterator();
      while(iter.hasNext()) {
         ShopItem item = iter.next();
         if(item.getGoldCost() <= getGold()) {
            canPurchase.add(item);
            Collections.sort(canPurchase, new Comparator<ShopItem>() {
               @Override
               public int compare(ShopItem t, ShopItem t1) {
                  if(t.getGoldCost() == t1.getGoldCost()) {
                     return (ExtraMath.getRandom().nextInt(1) == 0) ? -1 : 1;
                  }
                  else {
                     return t1.getGoldCost() - t.getGoldCost();
                  }
               }
            });
         }
      }
      while(canPurchase.size() > 0 && canPurchase.get(0).getGoldCost() <= getGold()) {
         canPurchase.get(0).purchase(this);
         canPurchase.remove(0);
      }
   }
}
