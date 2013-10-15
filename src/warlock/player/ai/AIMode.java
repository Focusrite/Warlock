/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.player.ai;

import warlock.ExtraMath;
import warlock.level.GroundType;
import warlock.object.character.Warlock;
import warlock.object.projectile.Projectile;
import warlock.phys.Vector;
import warlock.spell.SpellShortcut;
import warlock.spell.SpellType;

/**
 *
 * @author Focusrite
 */
public abstract class AIMode {
   private static final double DEFAULT_MODE_TIME = 1;
   private static final double DEFENSIVE_MIN_DISTANCE = 45;

   private Warlock warlock;
   private AIPlayer player;
   private double minModeTime;
   private double[] spellDelay;

   public AIMode(AIPlayer player) {
      this.player = player;
      this.warlock = player.getWarlock();
      this.minModeTime = DEFAULT_MODE_TIME;
      this.spellDelay = new double[SpellShortcut.values().length];
   }

   public void updateCooldowns(double dt) {
      for(int i = 0; i < spellDelay.length; i++) {
         spellDelay[i] -= dt;
      }
   }

   public double getMinModeTime() {
      return minModeTime;
   }

   void setMinModeTime(double time) {
      minModeTime = time + ExtraMath.randomDouble(0, 1);
   }

   public AIPlayer getPlayer() {
      return player;
   }

   public Warlock getWarlock() {
      return warlock;
   }

   public abstract void execute(double dt);

   /**
    * Calulates the angle it takes for a spell projectile moving at speed s to hit A moving at V velocity
    * when B fires it. It's a standard square equation (ax^2 + bx + c = 0)
    *
    * Assumes constant V
    *
    * @param target
    * @param shortcut - must be a projectile spell
    * @return
    */
   private Vector targetToHit(Warlock target, SpellShortcut shortcut) {
      Vector distance = target.getPosition().subtract(getWarlock().getPosition());

      double projectileSpeed = getWarlock().getSpell(shortcut).getLevelData("Speed");

      double a = target.getMovementVector().getLength() - projectileSpeed * projectileSpeed;
      double b = 2 * target.getMovementVector().dotProduct(distance);
      double c = distance.getLength();

      double p = -b / (2 * a);
      double q = Math.sqrt((b * b) - 4 * a * c) / (2 * a);

      double t1 = p - q;
      double t2 = p + q;
      double t = (t1 > t2 && t2 > 0) ? t2 : t1;

      return target.getPosition().add(target.getMovementVector().scale(t));
   }

   void useSpells() {
      int i = 0;
      for(SpellShortcut shortcut : SpellShortcut.values()) {
         if(getWarlock().getSpell(shortcut) == null) {
            continue;
         }
         if(spellDelay[i] <= 0 && getWarlock().getSpell(shortcut).getType() == SpellType.PROJECTILE) {
            //Cast projectiles at enemies
            getWarlock().castSpell(shortcut, targetToHit(getWarlock().getClosestWarlock(), shortcut));
            spellDelay[i] = getWarlock().getSpell(shortcut).getCooldown() + ExtraMath.randomDouble(0, 0.5);
         }
         else if(spellDelay[i] <= 0 && getWarlock().getSpell(shortcut).getType() == SpellType.DEFENSIVE) {
            //Defensive spells, use when projectile near
            Projectile closest = getWarlock().getLevel().getClosestProjectile(getWarlock());
            if(closest == null || closest.getPosition().distance(getWarlock().getPosition()) > DEFENSIVE_MIN_DISTANCE) {
               continue;
            }
            getWarlock().castSpell(shortcut, null);
            spellDelay[i] = getWarlock().getSpell(shortcut).getCooldown() + ExtraMath.randomDouble(0, 0.2);
         }
         else if(spellDelay[i] <= 0 && getWarlock().getSpell(shortcut).getType() == SpellType.ESCAPE) {
            //Escape spells, use when on lava
            if(getWarlock().getGroundType() != GroundType.LAVA) {
               continue;
            }
            getWarlock().castSpell(shortcut, new Vector(0, 0));
            spellDelay[i] = getWarlock().getSpell(shortcut).getCooldown() + ExtraMath.randomDouble(0, 0.2);
         }
         i++;
      }
   }

   Vector getVectorToCenter() {
      return new Vector().subtract(getWarlock().getPosition());
   }
}
