/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.game;

import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import warlock.graphic.Graphic;
import warlock.hud.PlayHud;
import warlock.input.InputHandler;
import warlock.level.Level;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class PlayPhase extends GamePhase {
   private Level activeLevel;

   public PlayPhase(final Game owner) {
      super(owner);
      setHud(new PlayHud(getOwner().getPlayer()));
   }

   @Override
   public void init() {
      this.activeLevel = new Level(getOwner().getCamera());
      Iterator it = getOwner().getPlayersIterator();
      while(it.hasNext()) {
         Player p = (Player)it.next();
         this.activeLevel.addObjectRandomPosition(p.getWarlock(), p);
      }
      activeLevel.centerCameraOn(getOwner().getPlayer().getWarlock());
   }

   @Override
   public void render(Graphic g) {
      this.activeLevel.render(g);
      getHud().render(g);
   }

   @Override
   public void update(double dt) {
      this.activeLevel.update(dt);
      getHud().update(dt);
   }
}
