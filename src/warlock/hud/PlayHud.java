/**
 * File: warlock.hud.PlayHud.java
 *
 * The hud displayed during the PlayPhase if you are a warlock currently fighting. Displays your
 * hitpoints, score, spells with cooldown and similar.
 */
package warlock.hud;

import java.util.ArrayList;
import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.message.Message;
import warlock.object.warlock.Warlock;
import warlock.player.Player;
import warlock.resource.ResourceHandler;
import warlock.spell.Spell;
import warlock.spell.SpellShortcut;

/**
 *
 * @author Focusrite
 */
public class PlayHud extends Hud {

   //TODO make percentage instead
   private static final int BOTTOM_BACKGROUND_SIZE = 50;
   private static final int SPELL_ICON_SIZE = 40;
   private static final int SPELL_ICON_XOFFSET = 10;
   private static final int SPELL_ICON_YOFFSET = 20;
   private static final int HPBAR_WIDTH = 300;
   private static final int HPBAR_HEIGHT = 30;
   private static final int HPBAR_YOFFSET = 45;
   private static final int HPBAR_XOFFSET = 30;
   private static final int INFO_X = 100;
   private static final int GOLD_Y = 20;
   private static final int SCORE_Y = 35;
   private static final int KILLS_Y = 50;
   private static final int MESSAGE_OFFSETX = 10;
   private static final int MESSAGE_OFFSETY = 100;
   private static final int MESSAGE_LINEHEIGHT = 20;
   public static final int MESSAGE_TIME = 5;
   private ArrayList<Message> messages = new ArrayList<>();

   /**
    * Creates a new play hud
    *
    * @param player
    */
   public PlayHud(Player player) {
      super(player);
   }

   /**
    * Render the play hud with coldowns, hp and general info.
    * @param g
    */
   @Override
   public void render(Graphic g) {
      g.setScreenCoordinates(true); //Camera position is of no concern when drawing UI
      renderHudBackground(g);
      renderSpells(g);
      renderInfo(g);
      renderHP(g);
      renderMessages(g);
      g.setScreenCoordinates(false); //Reset
   }

   /**
    * Render the lower background of the hud
    * @param g
    */
   private void renderHudBackground(Graphic g) {
      int x = (g.getScreenWidth() / 2);
      //Main bottom background
      g.drawRectangle(x, BOTTOM_BACKGROUND_SIZE / 2, ZLayers.GUI_BACKGROUND, g.getScreenWidth(), BOTTOM_BACKGROUND_SIZE, 0,
         new Color(100, 50, 50));
      //Distingisher line - black
      g.drawRectangle(x, BOTTOM_BACKGROUND_SIZE, ZLayers.GUI_BACKGROUND, g.getScreenWidth(), 2, 0, new Color(100, 100, 100));
      //Distingisher line - white
      g.drawRectangle(x, BOTTOM_BACKGROUND_SIZE + 1, ZLayers.GUI_BACKGROUND, g.getScreenWidth(), 2, 0, new Color(0, 0, 0));
   }

   /**
    * Render any messages sent to the hud
    * @param g
    */
   private void renderMessages(Graphic g) {
      int y = MESSAGE_OFFSETY;
      for (int i = messages.size() - 1; i >= 0; i--) {
         messages.get(i).render(g, MESSAGE_OFFSETX, y, ZLayers.GUI, Color.WHITE);
         y+= MESSAGE_LINEHEIGHT;
      }
   }

   /**
    * Renders general info
    * @param g
    */
   private void renderInfo(Graphic g) {
      //Gold
      g.drawText(Font.STYLE_NORMAL, g.getScreenWidth() - INFO_X, GOLD_Y, ZLayers.GUI,
         "GOLD: " + getPlayer().getGold(), Font.SIZE_NORMAL, Color.BLACK);
      //Score
      g.drawText(Font.STYLE_NORMAL, g.getScreenWidth() - INFO_X, SCORE_Y, ZLayers.GUI,
         "SCORE: " + getPlayer().getScore(), Font.SIZE_NORMAL, Color.BLACK);
      //Score
      g.drawText(Font.STYLE_NORMAL, g.getScreenWidth() - INFO_X, KILLS_Y, ZLayers.GUI,
         "KILLS: " + getPlayer().getKillingblows(), Font.SIZE_NORMAL, Color.BLACK);
   }

   /**
    * Renders the hpbar
    * @param g
    */
   private void renderHP(Graphic g) {
      int x = g.getScreenWidth() / 2 + HPBAR_XOFFSET;
      Warlock w = getPlayer().getWarlock();
      w.drawHPbar(g, x, HPBAR_YOFFSET, ZLayers.GUI, HPBAR_WIDTH, HPBAR_HEIGHT);
      g.drawRectangle(x, HPBAR_YOFFSET, ZLayers.GUI, HPBAR_WIDTH, HPBAR_HEIGHT, 0, Color.BLACK);
      g.drawText(Font.STYLE_NORMAL, x - (HPBAR_WIDTH / 2), HPBAR_YOFFSET - (HPBAR_HEIGHT / 2) - (Font.SIZE_NORMAL / 2),
         ZLayers.GUI, w.attrVal("hp") + "/" + w.attrBaseVal("hp"),
         Font.SIZE_NORMAL, Color.BLACK);
   }

   /**
    * Renders spells and their cooldowns
    * @param g
    */
   private void renderSpells(Graphic g) {
      int x = SPELL_ICON_XOFFSET + (SPELL_ICON_SIZE / 2);
      int y = SPELL_ICON_YOFFSET + (SPELL_ICON_SIZE / 2);
      int textOffsetY = (SPELL_ICON_SIZE / 2) + 2;
      int textOffsetX = (SPELL_ICON_SIZE / 2);
      for (SpellShortcut shortcut : SpellShortcut.values()) {
         Spell spell = getPlayer().getWarlock().getSpell(shortcut);
         if (spell != null) {
            renderSpell(g, spell, x, y);
            g.drawUnfilledRectangle(x, y, ZLayers.GUI, SPELL_ICON_SIZE, SPELL_ICON_SIZE,
               1, 0, new Color(0, 0, 0));
         }
         else {
            //Placeholder
            g.drawBorderedRectangle(x, y, ZLayers.GUI, SPELL_ICON_SIZE, SPELL_ICON_SIZE,
               1, 0, new Color(200, 200, 200), new Color(0, 0, 0));
         }
         g.drawText(Font.STYLE_NORMAL, x - textOffsetX, y - textOffsetY, ZLayers.GUI, shortcut.toString(), Font.SIZE_NORMAL, Color.GREY);
         x += SPELL_ICON_XOFFSET + SPELL_ICON_SIZE;
      }
   }

   /**
    * Render an individual spell with cooldown if it's on it
    * @param g
    * @param spell
    * @param x
    * @param y
    */
   private void renderSpell(Graphic g, Spell spell, int x, int y) {
      //Spell icon
      g.drawTexture(ResourceHandler.getTexture(spell.getSpellIcon()), x, y, ZLayers.GUI, SPELL_ICON_SIZE, SPELL_ICON_SIZE, 0);
      //if on cooldown, draw cooldown meter
      if (spell.getCurrentCooldown() > 0) {
         double percentage = spell.getCurrentCooldown() / spell.getCooldown();
         int size = (int) (SPELL_ICON_SIZE * percentage);
         g.drawRectangle(x - (SPELL_ICON_SIZE / 2) + (size / 2), y,
            ZLayers.GUI_FOREGROUND, size, SPELL_ICON_SIZE, 0, new Color(0, 0, 0, 100));
      }
      //g.drawText(Font.STYLE_NORMAL, 200, 500, ZLayers.GUI, spell.getDescription(), 14, Color.BLACK);
   }

   /**
    * Update messages and remove those that has expired
    * @param dt
    */
   @Override
   public void update(double dt) {
      for (int i = messages.size() - 1; i >= 0; i--) {
         if(messages.get(i).getLifetime() <= 0) {
            messages.remove(i);
            continue;
         }
         messages.get(i).update(dt);
      }
   }

   /**
    * Append a message to the messages displayed on the hud
    * @param text
    */
   public void addMessage(String text) {
      messages.add(new Message(text, MESSAGE_TIME));
   }
}
