/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.shop;

import warlock.object.character.Warlock;
import warlock.player.Player;
import warlock.spell.Spell;
import warlock.spell.SpellShortcut;

/**
 *
 * @author Focusrite
 */
public class ShopItemSpell extends ShopItem {
   private Spell item;

   public ShopItemSpell(Spell item, int goldCost) {
      super(goldCost);
      this.item = item;
   }

   public Spell getItem() {
      return item;
   }

   @Override
   public boolean purchase(Player purchaser) {
      if(!super.purchase(purchaser)) {
         return false;
      }
      Warlock w = purchaser.getWarlock();
      if(w.canLearn(item)) {
         w.addSpell(item.getShortcut(), item.clone());
      }
      else if (w.knowsSpell(item) && w.getSpell(item.getShortcut()).getSpellLevel() < item.getMaxLevel()) {
         w.getSpell(SpellShortcut.MB).levelUp();
         item.levelUp();
      }
      else{
         return false;
      }
      makePurchase(purchaser);
      return true;
   }

   @Override
   public String getItemDescription() {
      return item.getDescription();
   }


}
