/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   public ShopColumn(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void addItem(ShopItem item) {
      items.add(item);
   }

   public ListIterator iterator() {
      return items.listIterator();
   }
}
