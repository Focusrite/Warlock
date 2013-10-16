/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.player.ai;

import warlock.ExtraMath;
import warlock.object.character.Warlock;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class AIModeCircleTarget extends AIMode {

   private static final int RADIUS = 350;
   private Warlock target;
   private double radians;
   private double wholeLength;
   private double reachedLength;
   private double radius;
   private short mod;

   public AIModeCircleTarget(AIPlayer player, Warlock target) {
      super(player);
      this.target = target;
      radius = RADIUS;
      wholeLength = radius * 2 * Math.PI;
      reachedLength = (target.getPosition().subtract(getWarlock().getPosition()).getAngle() / (2 * Math.PI)) * wholeLength;
      mod = (short)((ExtraMath.getRandom().nextInt(2) == 0) ? 1 : -1);

   }

   @Override
   public void execute(double dt) {
      reachedLength = (reachedLength + getWarlock().attrVal("ms") * dt * mod) % wholeLength; // add ds * dt
      radians = (reachedLength / wholeLength) * 2 * Math.PI; // Update radian to how far the unit got
      getWarlock().setMoveTo(target.getPosition().add(new Vector(radius, radians)).add(getVectorToCenter().scale(0.1 * dt)));
      useSpells();
   }
}
