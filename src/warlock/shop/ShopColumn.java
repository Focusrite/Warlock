/**
 * File: warlock.shop.ShopColumn.java
 *
 * A column with ShopItems in the shop.
 */
package warlock.shop;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author Focusrite
 */
public class ShopColumn {
   private String name;
   private ArrayList<ShopItem> items = new ArrayList<>();

   /**
    * Create a new shop column with items
    * @param name
    */
   public ShopColumn(String name) {
      this.name = name;
   }

   /**
    * @return the title of the column
    */
   public String getName() {
      return name;
   }

   /**
    * Add an item to this column at the bottom
    * @param item
    */
   public void addItem(ShopItem item) {
      items.add(item);
   }

   /**
    * @return an iterator for all the shop items in this column
    */
   public ListIterator<ShopItem> iterator() {
      return items.listIterator();
   }
}
