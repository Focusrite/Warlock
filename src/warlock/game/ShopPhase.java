/**
 * File: warlock.game.ShopPhase.java
 *
 * Instance of GamePhase. The phase in which warlocks stack up their spells and make themself ready
 * for a new round on the dangerous plateau. Doesn't actually do much other than call members
 * appropriate methods.
 */
package warlock.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import warlock.graphic.Graphic;
import warlock.hud.ShopHud;
import warlock.input.InputHandler;
import warlock.shop.Shop;
import warlock.state.InputEnabled;

public class ShopPhase extends GamePhase {
   private ShopHud hud;
   private int shopTime;

   /**
    * Create a new shop phase.
    *
    * @param owner
    * @param shopTime
    */
   public ShopPhase(final Game owner, int shopTime) {
      super(owner);
      this.shopTime = shopTime;
   }

   /**
    * Render hud
    * @param g
    */
   @Override
   public void render(Graphic g) {
      hud.render(g);
   }

   /**
    * Update hud
    * @param dt
    */
   @Override
   public void update(double dt) {
      hud.update(dt);
   }

   /**
    * Initialize the shopphase with a counter counting down until it has ended.
    */
   @Override
   public void init() {
      Timer t = new Timer((int)(shopTime*1000), new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            getOwner().setPhase(new PlayPhase(getOwner()));
            //add transition
         }
      });
      t.setRepeats(false);
      t.start();
      Shop shop = new Shop(getOwner().getPlayer());
      hud = new ShopHud(shop, getOwner().getScoreTable(), getOwner().getFirstTo(), shopTime);
      getOwner().getCamera().reset();
   }

   /**
    * Call huds handle input
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      hud.handleInput(input);
   }
}
