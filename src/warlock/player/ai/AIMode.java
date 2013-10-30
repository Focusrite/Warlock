/**
 * File: warlock.player.ai.AIMode.java
 *
 * An abstract class representing what "mode" the AI currently is in. Extended to provide the execute
 * for mode, ie what to do in each respective mode. AIPlayer uses an instance of this class as a
 * state object.
 *
 * Also contains logic of when to use various spells.
 */
package warlock.player.ai;

import warlock.ExtraMath;
import warlock.level.GroundType;
import warlock.object.projectile.Projectile;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;
import warlock.spell.SpellShortcut;
import warlock.spell.SpellType;

public abstract class AIMode {

   private static final double DEFAULT_MODE_TIME = 1;
   private static final double DEFENSIVE_MIN_DISTANCE = 45;
   private static final int AOE_MIN_IN_RADIUS = 1;
   private Warlock warlock;
   private AIPlayer player;
   private double minModeTime;
   private double[] spellDelay; //To add some variation, don't always shoot "on cooldown"

   /**
    * Creates a new ai mode.
    * @param player
    */
   public AIMode(AIPlayer player) {
      this.player = player;
      this.warlock = player.getWarlock();
      this.minModeTime = DEFAULT_MODE_TIME;
      this.spellDelay = new double[SpellShortcut.values().length];
   }

   /**
    * Update the cooldowns the ai may cast spells at
    * @param dt
    */
   public void updateCooldowns(double dt) {
      for (int i = 0; i < spellDelay.length; i++) {
         spellDelay[i] -= dt;
      }
   }

   /**
    * @return the minimum amount of secodns the ai must be in this mode
    */
   public double getMinModeTime() {
      return minModeTime;
   }

   /**
    * @param time new min time this mode must be in, up to a second random time is added
    */
   void setMinModeTime(double time) {
      minModeTime = time + ExtraMath.randomDouble(0, 1);
   }

   /**
    * @return the AI player this mode is used for
    */
   public AIPlayer getPlayer() {
      return player;
   }

   /**
    * @return the warlock this AI controlls
    */
   public Warlock getWarlock() {
      return warlock;
   }

   public abstract void execute(double dt);

   /**
    * Calulates the angle it takes for a spell projectile moving at speed s to hit A moving at V
    * velocity when B fires it. It's a standard square equation (ax^2 + bx + c = 0)
    *
    * Assumes constant V
    *
    * @param target
    * @param shortcut - must be a projectile spell
    * @return vector needed to fire at
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

   /**
    * Use spells off cooldown that's appropriate to use
    */
   void useSpells() {
      int i = 0;
      for (SpellShortcut shortcut : SpellShortcut.values()) {
         if (getWarlock().getSpell(shortcut) == null) {
            continue;
         }
         if (spellDelay[i] <= 0 && getWarlock().getSpell(shortcut).getType() == SpellType.PROJECTILE) {
            //Cast projectiles at enemies
            getWarlock().castSpell(shortcut, targetToHit(getWarlock().getClosestWarlock(), shortcut));
            spellDelay[i] = getWarlock().getSpell(shortcut).getCooldown() + ExtraMath.randomDouble(0, 0.5);
         }
         else if (spellDelay[i] <= 0 && getWarlock().getSpell(shortcut).getType() == SpellType.DEFENSIVE) {
            //Defensive spells, use when projectile near
            Projectile closest = getWarlock().getLevel().getClosestProjectile(getWarlock());
            if (closest == null || closest.getPosition().distance(getWarlock().getPosition()) > DEFENSIVE_MIN_DISTANCE) {
               continue;
            }
            getWarlock().castSpell(shortcut, null);
            spellDelay[i] = getWarlock().getSpell(shortcut).getCooldown() + ExtraMath.randomDouble(0, 0.2);
         }
         else if (spellDelay[i] <= 0 && getWarlock().getSpell(shortcut).getType() == SpellType.ESCAPE) {
            if (getWarlock().getGroundType() != GroundType.LAVA) {
               continue;//Escape spells, use when on lava
            }
            getWarlock().castSpell(shortcut, new Vector(0, 0));
            spellDelay[i] = getWarlock().getSpell(shortcut).getCooldown() + ExtraMath.randomDouble(0, 0.2);
         }
         else if (spellDelay[i] <= 0 && getWarlock().getSpell(shortcut).getType() == SpellType.AOE) {
            Warlock w = getWarlock().getClosestWarlock();
            if (w == null || w.getPosition().distance(getWarlock().getPosition()) >=
               getWarlock().getSpell(shortcut).getLevelData("Range") +
               getWarlock().getSpell(shortcut).getLevelData("Radius") / 2) {
               continue; //Only use if in range (Radius contributes to range)
            }
            if(getWarlock().getLevel().getWarlocksInDistance(w.getPosition(), getWarlock(),
               getWarlock().getSpell(shortcut).getLevelData("Radius")).size() < AOE_MIN_IN_RADIUS) {
               continue; //If less than AOE_MIN_IN_RADIUS are in radius, don't cast
            }
            getWarlock().castSpell(shortcut, w.getPosition());
            spellDelay[i] = getWarlock().getSpell(shortcut).getCooldown() + ExtraMath.randomDouble(0, 1);
         }
         i++;
      }
   }

   /**
    * @return A vector pointing towards the center of the map from the position of the warlock
    */
   Vector getVectorToCenter() {
      return new Vector().subtract(getWarlock().getPosition());
   }
}
