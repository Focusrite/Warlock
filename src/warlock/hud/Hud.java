/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud;

import warlock.graphic.Graphic;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public abstract class Hud {
   private Player player;

   public Hud(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return player;
   }

   public void setPlayer(Player player) {
      this.player = player;
   }

   public abstract void render(Graphic g);
   public abstract void update(double dt);
}
