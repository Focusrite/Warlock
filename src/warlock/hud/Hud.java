/**
 * File: warlock.hud.Hud.java
 *
 * An abstract class extended by various Huds (Heads up displays - essentially UI). It's various
 * subclasses are the one doing the work.
 *
 * A big todo with all hud-related things are to provide options for various other screen resolutions.
 * But, as this currently can't change - it's not an issue for now.
 */
package warlock.hud;

import warlock.graphic.Graphic;
import warlock.player.Player;
import warlock.state.Renderable;
import warlock.state.Updateable;

/**
 *
 * @author Focusrite
 */
public abstract class Hud implements Updateable, Renderable {
   private Player player;

   /**
    * Initialize a hud, take the player it concerns
    * @param player
    */
   public Hud(Player player) {
      this.player = player;
   }

   /**
    * @return the player the hud is for
    */
   public Player getPlayer() {
      return player;
   }

   /**
    * @param player new player hud is for
    */
   public void setPlayer(Player player) {
      this.player = player;
   }

   @Override
   public abstract void render(Graphic g);
   @Override
   public abstract void update(double dt);
}
