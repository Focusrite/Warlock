/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import warlock.graphic.Graphic;

/**
 *
 * @author Focusrite
 */
public class ShopPhase extends GamePhase {
   private static final int SHOPTIME = 20*1000; //20 seconds in milliseconds
   private Timer shoptime;

   public ShopPhase(final Game owner) {
      super(owner);
      this.shoptime = new Timer(SHOPTIME, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            owner.setPhase(new PlayPhase(getOwner()));
            //add transition
         }
      });
      this.shoptime.setRepeats(false);
      this.shoptime.start();
   }

   @Override
   public void render(Graphic g) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void update(double dt) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void init() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
