/**
 * File: warlock.shop.ShopHandler.java
 *
 * Sets up all items available for purchase and the price they cost.
 */

package warlock.shop;

import java.util.ArrayList;
import java.util.ListIterator;
import warlock.Handle;
import warlock.spell.Explosion;
import warlock.spell.Fireball;
import warlock.spell.Lightning;
import warlock.spell.Seekerorb;
import warlock.spell.Shield;
import warlock.spell.Teleport;


public class ShopHandler implements Handle {
   private static ArrayList<ShopItem> shopItems = new ArrayList<>();

   /**
    * Register all items available for purchase
    */
   public static void init() {
      addItem(new ShopItemSpell(new Fireball(null), 10));
      addItem(new ShopItemSpell(new Teleport(null), 12));
      addItem(new ShopItemSpell(new Lightning(null), 10));
      addItem(new ShopItemSpell(new Seekerorb(null), 15));
      addItem(new ShopItemSpell(new Shield(null), 12));
      addItem(new ShopItemSpell(new Explosion(null), 1));
   }

   /**
    * Add an item to the list of items available for purchase
    * @param item
    */
   public static void addItem(ShopItem item) {
      shopItems.add(item);
   }

   /**
    * @return an iterator of all the items available for purchase
    */
   public static ListIterator<ShopItem> iterator() {
      return shopItems.listIterator();
   }
}
