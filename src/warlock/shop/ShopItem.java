/**
 * File: warlock.shop.ShopItem.java
 *
 * An individual item in the shop available for purchase. These are represented in shop hud with a
 * button. Abstract since items have to feature something to actually purchase too.
 */
package warlock.shop;

import warlock.hud.interactable.InteractableInfo;
import warlock.hud.interactable.InteractableListener;
import warlock.player.Player;

public abstract class ShopItem implements InteractableListener {
   private int goldCost;
   private Shop shopOwner;

   /**
    * @return the shop owning this item
    */
   public Shop getShopOwner() {
      return shopOwner;
   }

   /**
    * Set the owner of this item
    * @param shopOwner
    */
   public void setShopOwner(Shop shopOwner) {
      this.shopOwner = shopOwner;
   }

   /**
    * Create a new Shop item
    * @param goldCost
    */
   public ShopItem(int goldCost) {
      this.goldCost = goldCost;
   }

   /**
    * @return the cost to purchase this item
    */
   public int getGoldCost() {
      return goldCost;
   }

   /**
    * Make the purchase and return if it succeded or not. Overwritten to do the actual purchase
    * @param purchaser
    * @return boolean whether it was purchased or not
    */
   public boolean purchase(Player purchaser) {
      return purchaser.getGold() >= goldCost;
   }

   /**
    * Commit the purchase and widthraw the gold.
    * @param p
    */
   void makePurchase(Player p) {
      p.modifyGold(-goldCost);
   }

   /**
    * @param p
    * @return true if player p can purchase item.
    */
   public boolean canPurchase(Player p) {
      return p.getGold() >= goldCost;
   }

   /**
    * Item clicked, register with shopOwner that it was clicked
    * @param info
    */
   @Override
   public void clicked(InteractableInfo info) {
      shopOwner.clicksItem(this);
   }

   /**
    * Item hovered, register with shopOwner that mouse is hovering
    * @param info
    */
   @Override
   public void mouseEntered(InteractableInfo info) {
      shopOwner.hoversItem(this);
   }

   /**
    * Item no longer hovered, register that with shopOwner.
    * @param info
    */
   @Override
   public void mouseExited(InteractableInfo info) {
      shopOwner.stopsHovering();
   }

   public abstract String getItemDescription();
}
