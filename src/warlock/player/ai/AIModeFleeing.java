/**
 * File: warlock.player.ai.AIModeFleeing.java
 *
 * Causes the AI to panic and blindly head towards the center if he is in lava!
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

   /**
    * Create a new AIModeFleeing
    * @param player
    */
   public AIModeFleeing(AIPlayer player) {
      super(player);
      setMinModeTime(3);
      moveToCenter();
   }

   /**
    * Execute the actions
    * @param dt
    */
   @Override
   public void execute(double dt) {
      if (getWarlock().hasReachedDestination()) {
         moveToCenter();
      }
      useSpells();
   }

   /**
    * Move to a random point in the center
    */
   private void moveToCenter() {
      getWarlock().setMoveTo(new Vector(
            ExtraMath.randomDouble(-DESTINATION_AREA_SIZE, DESTINATION_AREA_SIZE),   //x
            ExtraMath.randomDouble(-DESTINATION_AREA_SIZE, DESTINATION_AREA_SIZE))); //y
   }
}
