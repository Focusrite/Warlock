/**
 * File: warlock.player.ai.AIModeCircleTarget.java
 *
 * A mode where the AI atempts to circle a target but still be drawn towards the center.
 */
package warlock.player.ai;

import warlock.ExtraMath;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;

public class AIModeCircleTarget extends AIMode {

   private static final int RADIUS = 350;
   private Warlock target;
   private double wholeLength;
   private double reachedLength;
   private double radius;
   private short mod;

   /**
    * Create a new AIModeCircleTarget
    * @param player
    * @param target
    */
   public AIModeCircleTarget(AIPlayer player, Warlock target) {
      super(player);
      this.target = target;
      radius = RADIUS;
      wholeLength = radius * 2 * Math.PI;
      reachedLength = (target.getPosition().subtract(getWarlock().getPosition()).getAngle() / (2 * Math.PI)) * wholeLength;
      mod = (short)((ExtraMath.getRandom().nextInt(2) == 0) ? 1 : -1);

   }

   /**
    * Execute the actions
    * @param dt
    */
   @Override
   public void execute(double dt) {
      reachedLength = (reachedLength + getWarlock().attrVal("ms") * dt * mod) % wholeLength; // add ds * dt
      double radians = (reachedLength / wholeLength) * 2 * Math.PI;
      getWarlock().setMoveTo(target.getPosition().add(new Vector(radius, radians)).add(getVectorToCenter().scale(0.1 * dt)));
      useSpells();
   }
}
