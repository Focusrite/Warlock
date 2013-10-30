/**
 * File: warlock.hud.Hud.java
 *
 * The hud (which in this case is more of an UI) that displays the shop for the user. The only hud
 * with clickable buttons.
 */
package warlock.hud;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.lwjgl.opengl.Display;
import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.hud.interactable.Interactable;
import warlock.hud.interactable.InteractableInfo;
import warlock.hud.interactable.InteractableListener;
import warlock.hud.interactable.InteractableListenerSlim;
import warlock.hud.interactable.InteractableTextureButton;
import warlock.input.InputHandler;
import warlock.player.Player;
import warlock.shop.Shop;
import warlock.shop.ShopColumn;
import warlock.shop.ShopItem;
import warlock.shop.ShopItemSpell;

/**
 *
 * @author Focusrite
 */
public class ShopHud extends Hud {

   private static final int TIMER_OFFSETX = 220;
   private static final int LOGO_OFFSETX = 10;
   private static final int TOPBAR_TEXT_Y = 10;
   private static final int TOPBAR_HEIGHT = 40;
   private static final int SHOP_OFFSETX = 20;
   private static final int SHOP_OFFSETY = 100;
   private static final int SHOP_ROW_PADDING = 10;
   private static final int SHOP_COLUMN_PADDING = 10;
   private static final int SHOP_ICON_SIZE = 40;
   private static final int SHOP_COLUMN_NAME_HEIGHT = 20;
   private static final int INFO_AREA_WIDTH = 300;
   private static final int INFO_AREA_HEIGHT = 300;
   private static final int INFO_AREA_OFFSETX = INFO_AREA_WIDTH / 2 + 20;
   private static final int INFO_AREA_OFFSETY = INFO_AREA_HEIGHT / 2 + TOPBAR_HEIGHT + 20;
   private static final int TEXTPADDING = 10;
   private static final int BUY_WIDTH = 64;
   private static final int BUY_HEIGHT = 25;
   private static final int BUY_OFFSETX = INFO_AREA_OFFSETX - INFO_AREA_WIDTH / 2 + BUY_WIDTH + 10;
   private static final int BUY_OFFSETY = BUY_HEIGHT - 15;
   private static final int SCORETABLE_WIDTH = INFO_AREA_WIDTH;
   private static final int SCORETABLE_HEIGHT = 200;
   private static final int SCORETABLE_OFFSETX = INFO_AREA_OFFSETX;
   private static final int SCORETABLE_OFFSETY = SCORETABLE_HEIGHT / 2 + 20;
   private static final int SCORETABLE_LINEHEIGHT = 20;
   private double timeLeft;
   private int firstTo;
   private Shop shop;
   private InteractableTextureButton buyButton;
   private ArrayList<Interactable> interactables = new ArrayList<>();
   private ArrayList<Player> scoretable;

   /**
    * Create a new shop hud
    *
    * @param shop the actual "shop" with the items available for sale
    * @param scoretable
    * @param firstTo
    * @param shopTime
    */
   public ShopHud(Shop shop, ArrayList<Player> scoretable, int firstTo, int shopTime) {
      super(shop.getShoppingPlayer());
      this.shop = shop;
      this.firstTo = firstTo;
      this.scoretable = scoretable;
      timeLeft = shopTime;
      init();
   }

   /**
    * Initialze the shophud and set up the interactable buttons on the hud.
    */
   private void init() {
      //Buy button
      InteractableTextureButton buy = new InteractableTextureButton(
         Display.getWidth() - BUY_OFFSETX,
         Display.getHeight() - (INFO_AREA_OFFSETY + (INFO_AREA_HEIGHT / 2) - BUY_OFFSETY),
         BUY_WIDTH, BUY_HEIGHT,
         "ui-buy", Color.LIGHT_GREY, Color.WHITE);
      buyButton = buy;
      interactables.add(buy);
      buy.addListener(new InteractableListenerSlim() {
         @Override
         public void clicked(InteractableInfo source) {
            shop.clicked(source);
            if (shop.getCurrentlySelected() != null) {
               buyButton.setDisabled(!shop.getCurrentlySelected().canPurchase(shop.getShoppingPlayer()));
            }
         }
      });
      initShopIcons();
   }

   /**
    * Initializes the items available for buy.
    */
   private void initShopIcons() {
      //Shop icons
      Iterator<ShopColumn> columnIter = shop.getColumnIterator();
      int x = SHOP_OFFSETX;
      int y = Display.getHeight() - SHOP_OFFSETY - SHOP_COLUMN_NAME_HEIGHT;
      while (columnIter.hasNext()) {
         Iterator<ShopItem> rowIter = columnIter.next().iterator();
         while (rowIter.hasNext()) {
            final ShopItemSpell item = (ShopItemSpell) rowIter.next();
            Interactable i = new InteractableTextureButton(x, y, SHOP_ICON_SIZE, SHOP_ICON_SIZE,
               item.getItem().getSpellIcon(), Color.LIGHT_GREY, Color.WHITE);
            i.addListener(new InteractableListener() {
               @Override
               public void clicked(InteractableInfo source) {
                  buyButton.setDisabled(!item.canPurchase(shop.getShoppingPlayer()));
                  item.clicked(source); //Buy
               }

               @Override
               public void mouseEntered(InteractableInfo source) {
                  item.mouseEntered(source); //Hover shows info
               }

               @Override
               public void mouseExited(InteractableInfo source) {
                  item.mouseExited(source); //Return to the clicked item
               }
            });
            interactables.add(i);
            y -= (SHOP_ICON_SIZE + SHOP_ROW_PADDING);
         }
         x += SHOP_ICON_SIZE + SHOP_COLUMN_PADDING;
         y = Display.getHeight() - SHOP_OFFSETY - SHOP_COLUMN_NAME_HEIGHT;
      }
   }

   /**
    * Render the hud
    *
    * @param g
    */
   @Override
   public void render(Graphic g) {
      renderBackground(g);
      renderTimer(g);
      renderInfo(g);
      renderScoretable(g);
      renderColumnNames(g);
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).render(g);
      }
   }

   /**
    * Handle input of all the interactables to see if they're interacted with.
    *
    * @param input
    */
   public void handleInput(InputHandler input) {
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).handleInput(input);
      }
   }

   /**
    * Render the info panel
    *
    * @param g
    */
   private void renderInfo(Graphic g) {
      int playerGold = shop.getShoppingPlayer().getGold();
      String gold = "Gold: " + Color.GOLD + playerGold;
      if (shop.getCurrentlySelected() != null) {
         int itemCost = shop.getCurrentlySelected().getGoldCost();
         g.drawText(Font.STYLE_NORMAL,
            g.getScreenWidth() - (INFO_AREA_OFFSETX + (INFO_AREA_WIDTH / 2) - TEXTPADDING),
            g.getScreenHeight() - (INFO_AREA_OFFSETY - (INFO_AREA_HEIGHT / 2) + TEXTPADDING),
            ZLayers.GUI, shop.getCurrentlySelected().getItemDescription(), Font.SIZE_NORMAL, Color.WHITE);
         gold = gold + "| " + ((playerGold < itemCost) ? Color.RED : Color.GREEN) + "-" + itemCost;
      }
      g.drawText(Font.STYLE_NORMAL,
         g.getScreenWidth() - (INFO_AREA_OFFSETX + (INFO_AREA_WIDTH / 2) - TEXTPADDING),
         g.getScreenHeight() - (INFO_AREA_OFFSETY + (INFO_AREA_HEIGHT / 2) - TEXTPADDING * 3),
         ZLayers.GUI, gold, Font.SIZE_NORMAL, Color.WHITE);
   }

   /**
    * Render the current scoretable
    *
    * @param g
    */
   private void renderScoretable(Graphic g) {
      int x = g.getScreenWidth() - (SCORETABLE_OFFSETX + SCORETABLE_WIDTH / 2 - TEXTPADDING);
      int y = SCORETABLE_OFFSETY + SCORETABLE_HEIGHT / 2 - TEXTPADDING;
      int placement = 1;
      g.drawText(Font.STYLE_NORMAL, x, y, ZLayers.GUI, "Scoreboard, first to " + firstTo,
         Font.SIZE_NORMAL, Color.WHITE);
      for (int i = 0; i < scoretable.size(); i++) {
         y -= SCORETABLE_LINEHEIGHT;
         Player p = scoretable.get(i);
         g.drawText(Font.STYLE_NORMAL, x, y, ZLayers.GUI,
            placement + ". " + p.getPrimaryColor() + p + "| (" + p.getScore() + " pts)",
            Font.SIZE_NORMAL, Color.WHITE);
         if (i + 1 < scoretable.size() && scoretable.get(i).compareTo(scoretable.get(i + 1)) != 0) {
            placement++;
         }
      }
   }

   /**
    * Render the names of the columns
    *
    * @param g
    */
   private void renderColumnNames(Graphic g) {
      int x = SHOP_OFFSETX;
      Iterator<ShopColumn> columnIter = shop.getColumnIterator();
      while (columnIter.hasNext()) {
         g.drawText(Font.STYLE_NORMAL, x, Display.getHeight() - SHOP_OFFSETY + SHOP_ICON_SIZE, ZLayers.GUI,
            columnIter.next().getName(), Font.SIZE_NORMAL, Color.WHITE);
         x += SHOP_ICON_SIZE + SHOP_COLUMN_PADDING;
      }
   }

   /**
    * Render the gametimer
    *
    * @param g
    */
   private void renderTimer(Graphic g) {
      g.drawText(Font.STYLE_NORMAL, g.getScreenWidth() - TIMER_OFFSETX, g.getScreenHeight() - TOPBAR_TEXT_Y, ZLayers.GUI,
         String.format(Locale.US, "Shoptime left: %1$.2f", Math.max(timeLeft, 0)), Font.SIZE_NORMAL, Color.WHITE);
   }

   /**
    * Render the backgrounds of the info and and scoretable, as well as the top logo-bar
    *
    * @param g
    */
   private void renderBackground(Graphic g) {
      //Header
      g.drawRectangle(g.getScreenWidth() / 2, g.getScreenHeight() - (TOPBAR_HEIGHT / 2), ZLayers.GUI_BACKGROUND,
         g.getScreenWidth(), TOPBAR_HEIGHT, 0, Color.BLACK);
      g.drawText(Font.STYLE_NORMAL, LOGO_OFFSETX, g.getScreenHeight() - TOPBAR_TEXT_Y, ZLayers.GUI,
         "Warlock lounge area", Font.SIZE_BIG, Color.WHITE);
      //Infoarea
      g.drawRectangle(g.getScreenWidth() - INFO_AREA_OFFSETX, g.getScreenHeight() - INFO_AREA_OFFSETY, ZLayers.GUI_BACKGROUND,
         INFO_AREA_WIDTH, INFO_AREA_HEIGHT, 0, Color.BLACK);
      //Scoreboard
      g.drawRectangle(g.getScreenWidth() - SCORETABLE_OFFSETX, SCORETABLE_OFFSETY, ZLayers.GUI_BACKGROUND,
         SCORETABLE_WIDTH, SCORETABLE_HEIGHT, 0, Color.BLACK);
   }

   /**
    * Update the interactables
    *
    * @param dt
    */
   @Override
   public void update(double dt) {
      timeLeft -= dt;
      for (int i = 0; i < interactables.size(); i++) {
         interactables.get(i).update(dt);
      }
   }
}
