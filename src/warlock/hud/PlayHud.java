/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud;

import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.player.Player;
import warlock.spell.Spell;
import warlock.spell.SpellShortcut;

/**
 *
 * @author Focusrite
 */
public class PlayHud extends Hud {

   private static final int BOTTOM_BACKGROUND_SIZE = 50;
   private static final int SPELL_ICON_SIZE = 40;
   private static final int SPELL_ICON_XOFFSET = 10;
   private static final int SPELL_ICON_YOFFSET = 15;

   public PlayHud(Player player) {
      super(player);
   }

   @Override
   public void render(Graphic g) {
      g.setScreenCoordinates(true); //Camera position is of no concern when drawing UI
      //g.drawText("Times New Roman", 0, 0, "HEJ", Color.RED);
      renderHudBackground(g);
      renderSpells(g);
      g.setScreenCoordinates(false); //Reset
   }

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

   private void renderSpells(Graphic g) {
      int x = SPELL_ICON_XOFFSET + (SPELL_ICON_SIZE / 2);
      int y = SPELL_ICON_YOFFSET + (SPELL_ICON_SIZE / 2);
      for (SpellShortcut shortcut : SpellShortcut.values()) {
         Spell spell = getPlayer().getWarlock().getSpell(shortcut);
         if (spell != null) {
            renderSpell(g, spell, x, y);
         }
         else {
            //draw placeholder
         }
         x += SPELL_ICON_XOFFSET + SPELL_ICON_SIZE;
      }
   }

   private void renderSpell(Graphic g, Spell spell, int x, int y) {
      g.drawBorderedRectangle(x, y, ZLayers.GUI, SPELL_ICON_SIZE, SPELL_ICON_SIZE,
         1, 0, new Color(200, 200, 200), new Color(0, 0, 0));
      //if on cooldown, draw cooldown meter
      if (spell.getCurrentCooldown() > 0) {
         double percentage = spell.getCurrentCooldown() / spell.getCooldown();
         int size = (int) (SPELL_ICON_SIZE * percentage);
         g.drawRectangle(x - (SPELL_ICON_SIZE / 2) + (size / 2), y,
            ZLayers.GUI_FOREGROUND, size, SPELL_ICON_SIZE, 0, new Color(0, 0, 0, 100));
      }
   }

   @Override
   public void update(double dt) {
   }
}
