/**
 * File: warlock.shop.Shop.java
 *
 * An extended shop item for spells.
 */
package warlock.shop;

import warlock.object.warlock.Warlock;
import warlock.player.Player;
import warlock.spell.Spell;

/**
 *
 * @author Focusrite
 */
public class ShopItemSpell extends ShopItem {
   private Spell item;

   /**
    * Create a new shop item for buying spells
    * @param item
    * @param goldCost
    */
   public ShopItemSpell(Spell item, int goldCost) {
      super(goldCost);
      this.item = item;
   }

   /**
    * @return the spell this item upgrades
    */
   public Spell getItem() {
      return item;
   }

   /**
    * Make the purchase if possible and return if it succeded or not
    * @param purchaser
    * @return boolean, if succeded
    */
   @Override
   public boolean purchase(Player purchaser) {
      if(!super.purchase(purchaser)) {
         return false;
      }
      Warlock w = purchaser.getWarlock();
      if(w.canLearn(item)) {
         w.addSpell(item.getShortcut(), item.newInstance());
      }
      else if (w.knowsSpell(item) && w.getSpell(item.getShortcut()).getSpellLevel() < item.getMaxLevel()) {
         w.getSpell(item.getShortcut()).levelUp();
         item.levelUp();
      }
      else{
         return false;
      }
      makePurchase(purchaser);
      return true;
   }

   /**
    * @param p
    * @return true if player can purchase this spell, false else
    */
   @Override
   public boolean canPurchase(Player p) {
      Warlock w = p.getWarlock();
      return super.canPurchase(p) && (w.canLearn(item) || (w.knowsSpell(item) && w.getSpell(item.getShortcut()).getSpellLevel() < item.getMaxLevel()));
   }

   /**
    * @return the spell description
    */
   @Override
   public String getItemDescription() {
      return item.getDescription();
   }


}
