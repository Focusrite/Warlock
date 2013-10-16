/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.shop;

import warlock.hud.interactables.InteractableInfo;
import warlock.hud.interactables.InteractableListener;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public abstract class ShopItem implements InteractableListener {
   private int goldCost;
   private Shop shopOwner;

   public Shop getShopOwner() {
      return shopOwner;
   }

   public void setShopOwner(Shop shopOwner) {
      this.shopOwner = shopOwner;
   }

   public ShopItem(int goldCost) {
      this.goldCost = goldCost;
   }

   public int getGoldCost() {
      return goldCost;
   }

   public boolean purchase(Player purchaser) {
      return purchaser.getGold() >= goldCost;
   }

   void makePurchase(Player p) {
      p.modifyGold(-goldCost);
   }

   public boolean canPurchase(Player p) {
      return p.getGold() >= goldCost;
   }

   @Override
   public void clicked(InteractableInfo info) {
      shopOwner.clicksItem(this);
   }

   @Override
   public void mouseEntered(InteractableInfo info) {
      shopOwner.hoversItem(this);
   }

   @Override
   public void mouseExited(InteractableInfo info) {
      shopOwner.stopsHovering();
   }

   public abstract String getItemDescription();
}
