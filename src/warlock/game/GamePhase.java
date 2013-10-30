/**
 * File: warlock.game.GamePhase.java
 *
 * An abstract class "Game" utilizes to switch the state the current game is in. The phase the game
 * currently is in will change mutliple times during the course up until the end of the game is
 * reached.
 *
 * Read more on the individual phase classes to learn more about them.
 */
package warlock.game;

import warlock.graphic.Graphic;
import warlock.hud.Hud;
import warlock.input.InputHandler;
import warlock.player.Player;

public abstract class GamePhase {
   private Game owner;
   private Hud hud;

   /**
    * @return the hud this phase has (aka GUI, UI)
    */
   public Hud getHud() {
      return hud;
   }

   /**
    * Set the phase's hud.
    * @param hud
    */
   public void setHud(Hud hud) {
      this.hud = hud;
   }

   /**
    * Abstract class
    * @param owner
    */
   public GamePhase(final Game owner) {
      this.owner = owner;
   }

   /**
    * @return Game owning this phase.
    */
   public Game getOwner() {
      return owner;
   }

   /**
    * Set the game that owns this phase
    * @param owner
    */
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
