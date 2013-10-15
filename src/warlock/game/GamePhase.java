/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.game;

import warlock.graphic.Graphic;
import warlock.hud.Hud;
import warlock.input.InputHandler;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public abstract class GamePhase {
   private Game owner;
   private Hud hud;

   public Hud getHud() {
      return hud;
   }

   public void setHud(Hud hud) {
      this.hud = hud;
   }

   public GamePhase(final Game owner) {
      this.owner = owner;
   }

   public Game getOwner() {
      return owner;
   }

   public void setOwner(Game owner) {
      this.owner = owner;
   }

   public abstract void render(Graphic g);
   public abstract void update(double dt);
   public abstract void init();
   public void notifyDeath(Player p, Player killer) {}
   public void handleInput(InputHandler input) {}
   //overidable terminate action for cleanup
   public void terminate() { }

}
