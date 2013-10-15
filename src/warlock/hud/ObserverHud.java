/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud;

import java.util.ArrayList;
import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.message.Message;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class ObserverHud extends Hud {

   private static final int SCORETABLE_WIDTH = 250;
   private static final int SCORETABLE_HEIGHT = 200;
   private static final int SCORETABLE_OFFSETX = SCORETABLE_WIDTH / 2 + 10;
   private static final int SCORETABLE_OFFSETY = SCORETABLE_HEIGHT / 2 + 5;
   private static final int SCORETABLE_LINEHEIGHT = 20;
   private static final int TEXTPADDING = 10;
   private static final int MESSAGE_OFFSETX = 10;
   private static final int MESSAGE_OFFSETY = 25;
   private static final int MESSAGE_LINEHEIGHT = 20;
   public static final int MESSAGE_TIME = 5;
   private int firstTo;
   private ArrayList<Player> scoretable;
   private ArrayList<Message> messages = new ArrayList<>();

   public ObserverHud(Player player, ArrayList<Player> scoretable, int firstTo) {
      super(player);
      this.scoretable = scoretable;
      this.firstTo = firstTo;
   }

   @Override
   public void render(Graphic g) {
      g.setScreenCoordinates(true); //Camera position is of no concern when drawing UI

      renderScoretable(g);
      renderMessages(g);
      g.setScreenCoordinates(false);
   }

   private void renderScoretable(Graphic g) {
      int x = g.getScreenWidth() - (SCORETABLE_OFFSETX + SCORETABLE_WIDTH / 2 - TEXTPADDING);
      int y = g.getScreenHeight() - (SCORETABLE_OFFSETY - SCORETABLE_HEIGHT / 2 + TEXTPADDING);
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

   private void renderMessages(Graphic g) {
      int y = MESSAGE_OFFSETY;
      for (int i = messages.size() - 1; i >= 0; i--) {
         messages.get(i).render(g, MESSAGE_OFFSETX, y, ZLayers.GUI, Color.WHITE);
         y += MESSAGE_LINEHEIGHT;
      }
   }

   @Override
   public void update(double dt) {
      for (int i = messages.size() - 1; i >= 0; i--) {
         if (messages.get(i).getLifetime() <= 0) {
            messages.remove(i);
            continue;
         }
         messages.get(i).update(dt);
      }
   }

   public void addMessage(String text) {
      messages.add(new Message(text, MESSAGE_TIME));
   }
}
