/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.game;

import java.util.ArrayList;
import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class GameOverPhase extends GamePhase {
   private static final int TEXT_OFFSETY = 50;

   private static final int SCORETABLE_OFFSETY = 500;
   private static final int SCORETABLE_LINEHEIGHT = 20;

   private ArrayList<Player> scoretable;

   public GameOverPhase(Game owner, ArrayList<Player> scoretable) {
      super(owner);
      this.scoretable = scoretable;
   }

   @Override
   public void render(Graphic g) {
      g.setScreenCoordinates(true);
      g.drawRectangle(g.getScreenWidth() / 2, g.getScreenHeight() / 2, ZLayers.BELOW_LEVEL,
         g.getScreenWidth(), g.getScreenHeight(), 0, Color.BLACK);
      g.drawText("Visitor", g.getScreenWidth() / 2, g.getScreenHeight() - TEXT_OFFSETY, ZLayers.GUI,
         scoretable.get(0).getName() + " won!", Font.SIZE_BIG, Color.WHITE, true); //The winner
      renderScoretable(g);
      g.setScreenCoordinates(false);
   }

   private void renderScoretable(Graphic g) {
      int x = g.getScreenWidth() / 2;
      int y = SCORETABLE_OFFSETY;
      int placement = 1;
      g.drawText(Font.STYLE_NORMAL, x, y, ZLayers.GUI, "Scoreboard", Font.SIZE_NORMAL, Color.WHITE, true);
      for(int i = 0; i < scoretable.size(); i++) {
         y -= SCORETABLE_LINEHEIGHT;
         Player p = scoretable.get(i);
         g.drawText(Font.STYLE_NORMAL, x, y, ZLayers.GUI,
            placement + ". " + p.getPrimaryColor() + p + "| (" + p.getScore() + " pts)",
            Font.SIZE_NORMAL, Color.WHITE, true);
         if(i + 1 < scoretable.size() && scoretable.get(i).compareTo(scoretable.get(i+1)) != 0) {
            placement++;
         }
      }
   }

   @Override
   public void update(double dt) {

   }

   @Override
   public void init() {

   }
}
