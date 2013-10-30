/**
 * File: warlock.shop.Shop.java
 *
 * The class representing the shop with it's layout and items. It does not however handle rendering,
 * ShopHud does that. Listens to ShopHuds buy button click for handling the purchase of the currently
 * selected item.
 */
package warlock.shop;

import java.util.ArrayList;
import java.util.Iterator;
import warlock.hud.interactable.InteractableInfo;
import warlock.hud.interactable.InteractableListenerSlim;
import warlock.player.Player;
import warlock.spell.SpellShortcut;

public class Shop implements InteractableListenerSlim {

   private ShopItem currentlySelected;
   private ShopItem clickedItem;
   private ArrayList<ShopColumn> shopColumns = new ArrayList<>();
   private ArrayList<String> columnNames = new ArrayList<>();
   private Player shoppingPlayer;

   /**
    * Create a new shop.
    * @param shoppingPlayer
    */
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

   /**
    * Add a column with items to the shop
    * @param type
    */
   private void addColumn(String type) {
      shopColumns.add(new ShopColumn(type));
      columnNames.add(type);
   }

   /**
    * Add a shop item to a column
    * @param item
    */
   private void addToColumn(ShopItem item) {
      if (item instanceof ShopItemSpell) {
         ShopItemSpell i = (ShopItemSpell) item;
         shopColumns.get(columnNames.indexOf(i.getItem().getShortcutAsString())).addItem(item);
      }
   }

   /**
    * @return the item currently selected
    */
   public ShopItem getCurrentlySelected() {
      return currentlySelected;
   }

   /**
    * @param currentlySelected new item selected
    */
   public void setCurrentlySelected(ShopItem currentlySelected) {
      this.currentlySelected = currentlySelected;
   }

   /**
    * @return get the player whos shop this is
    */
   public Player getShoppingPlayer() {
      return shoppingPlayer;
   }

   /**
    * Fired when an item is hoovered, to temporary override the selected item
    * @param item the item in question
    */
   public void hoversItem(ShopItem item) {
      currentlySelected = item;
   }

   /**
    * Fired when an item is clicked, making it the selected item
    * @param item the item in question
    */
   public void clicksItem(ShopItem item) {
      currentlySelected = item;
      clickedItem = item;
   }

   /**
    * Fired when an item is no longer hovered, returns the selected item to the item that was clicked
    */
   public void stopsHovering() {
      currentlySelected = clickedItem;
   }

   /**
    * @return a iterator for all the columns
    */
   public Iterator<ShopColumn> getColumnIterator() {
      return shopColumns.listIterator();
   }

   /**
    * Fired when the buy button is clicked and attempts to purchase the item in question.
    * @param info
    */
   @Override
   public void clicked(InteractableInfo info) {
      if (currentlySelected != null) {
         currentlySelected.purchase(shoppingPlayer);
      }
   }

}
