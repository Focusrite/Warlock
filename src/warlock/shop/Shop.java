/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.shop;

import java.util.ArrayList;
import java.util.Iterator;
import warlock.hud.interactables.InteractableInfo;
import warlock.hud.interactables.InteractableListener;
import warlock.player.Player;
import warlock.spell.SpellShortcut;

/**
 *
 * @author Focusrite
 */
public class Shop implements InteractableListener {

   private ShopItem currentlySelected;
   private ShopItem clickedItem;
   private ArrayList<ShopColumn> shopColumns = new ArrayList<>();
   private ArrayList<String> columnNames = new ArrayList<>();
   private Player shoppingPlayer;

   public Shop(Player shoppingPlayer) {
      this.shoppingPlayer = shoppingPlayer;
      for (SpellShortcut shortcut : SpellShortcut.values()) {
         addColumn(shortcut.toString());
      }
      Iterator<ShopItem> iter = ShopHandler.iterator();
      while (iter.hasNext()) {
         ShopItem item = iter.next();
         addToColumn(item);
         item.setShopOwner(this);
      }
   }

   private void addColumn(String type) {
      shopColumns.add(new ShopColumn(type));
      columnNames.add(type);
   }

   private void addToColumn(ShopItem item) {
      if (item instanceof ShopItemSpell) {
         ShopItemSpell i = (ShopItemSpell) item;
         shopColumns.get(columnNames.indexOf(i.getItem().getShortcutAsString())).addItem(item);
      }
   }

   public ShopItem getCurrentlySelected() {
      return currentlySelected;
   }

   public void setCurrentlySelected(ShopItem currentlySelected) {
      this.currentlySelected = currentlySelected;
   }

   public Player getShoppingPlayer() {
      return shoppingPlayer;
   }

   public void hoversItem(ShopItem item) {
      currentlySelected = item;
   }

   public void clicksItem(ShopItem item) {
      currentlySelected = item;
      clickedItem = item;
   }

   public void stopsHovering() {
      currentlySelected = clickedItem;
   }

   public Iterator<ShopColumn> getColumnIterator() {
      return shopColumns.listIterator();
   }

   @Override
   public void clicked(InteractableInfo info) {
      if (currentlySelected != null) {
         currentlySelected.purchase(shoppingPlayer);
      }
   }

   @Override
   public void mouseEntered(InteractableInfo info) {
   }

   @Override
   public void mouseExited(InteractableInfo info) {
   }
}
