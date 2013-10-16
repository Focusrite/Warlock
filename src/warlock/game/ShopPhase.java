/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import warlock.graphic.Graphic;
import warlock.hud.ShopHud;
import warlock.input.InputHandler;
import warlock.shop.Shop;

/**
 *
 * @author Focusrite
 */
public class ShopPhase extends GamePhase {
   private Shop shop;
   private ShopHud hud;
   private int shopTime;

   public ShopPhase(final Game owner, int shopTime) {
      super(owner);
      this.shopTime = shopTime;
   }

   @Override
   public void render(Graphic g) {
      hud.render(g);
   }

   @Override
   public void update(double dt) {
      hud.update(dt);
   }

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
      shop = new Shop(getOwner().getPlayer());
      hud = new ShopHud(shop, getOwner().getScoreTable(), getOwner().getFirstTo(), shopTime);
      getOwner().getCamera().reset();
   }

   @Override
   public void handleInput(InputHandler input) {
      hud.handleInput(input);
   }
}
