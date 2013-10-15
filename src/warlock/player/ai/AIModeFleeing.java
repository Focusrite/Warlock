/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.player.ai;

import warlock.ExtraMath;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class AIModeFleeing extends AIMode {

   private static final int DESTINATION_AREA_SIZE = 40;

   public AIModeFleeing(AIPlayer player) {
      super(player);
      setMinModeTime(3);
      moveToCenter();
   }

   @Override
   public void execute(double dt) {
      if (getWarlock().hasReachedDestination()) {
         moveToCenter();
      }
      useSpells();
   }

   private void moveToCenter() {
      getWarlock().setMoveTo(new Vector(
            ExtraMath.randomDouble(-DESTINATION_AREA_SIZE, DESTINATION_AREA_SIZE),   //x
            ExtraMath.randomDouble(-DESTINATION_AREA_SIZE, DESTINATION_AREA_SIZE))); //y
   }
}
