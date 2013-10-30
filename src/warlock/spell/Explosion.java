/**
 * File: warlock.spell.Explosion.java
 *
 * An AOE damage around the caster with heavy damage and heavy knockback, however leaves the caster
 * vurnable for a short period before casting and inflicts damage to the caster too.
 */
package warlock.spell;

import java.util.ArrayList;
import warlock.attribute.AttributeHandler;
import warlock.attribute.StatusEffectListener;
import warlock.attribute.StatusEffectType;
import warlock.object.particle.ParticleHandler;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;

public class Explosion extends Spell implements StatusEffectListener {

   private static final double cooldown[] = {10};
   private static final double damage[] = {20};
   private static final double selfDamage[] = {10};
   private static final double radius[] = {50};
   private static final double knockback[] = {400};
   private static final double onset[] = {1};

   /**
    * Create a new Explosion spell
    * @param owner
    */
   public Explosion(Warlock owner) {
      super(owner, "Explosion", SpellTarget.SELF, SpellShortcut.R, 1);
      setCooldown(cooldown[0]);
      setSpellIcon("spell-explosion");
      setMaxLevel(1);
      setType(SpellType.DEFENSIVE);

      setBaseDescription(
         "All warlocks know \n"
         + "from past experiences\n"
         + "how to explode. 1s setup.\n");
      setLevelDataNames(new String[]{"Damage", "Self Damage", "Radius", "Knockback", "Cooldown"});
      setLevelData(new double[][]{damage, selfDamage, radius, knockback, cooldown});
      rebuildDescription();
   }

   /**
    * Cast the spell.
    * @param castVector
    */
   @Override
   public void cast(Vector castVector) {
      getOwner().inflictStatusEffect(
         new StatusEffectType(-1, AttributeHandler.get("stun")), onset[getSpellLevel() - 1], null, this);
   }

   /**
    * @return a "copy" of the spell
    */
   @Override
   public Explosion clone() {
      Explosion spell = new Explosion(null);
      return spell;
   }

   /**
    * A StatusEffectListener event that fires when the warlock is no longer stunned and do the actual
    * spell.
    */
   @Override
   public void expired() {
      getOwner().inflictStatusEffect(new StatusEffectType(
         selfDamage[getSpellLevel() - 1], AttributeHandler.get("hp")),
         getOwner().getOwningPlayer()); //Self inflicted

      ArrayList<Warlock> inArea = getOwner().getLevel().getWarlocksInDistance(getOwner().getPosition(),
         getOwner(), radius[getSpellLevel() - 1]);
      for (int i = 0; i < inArea.size(); i++) {
         //Apply knockback
         Vector v = inArea.get(i).getPosition().subtract(getOwner().getPosition());
         v.setLength(knockback[getSpellLevel() - 1]);
         inArea.get(i).applyForce(v);
         //Apply damage
         inArea.get(i).inflictStatusEffect(new StatusEffectType(
            damage[getSpellLevel() - 1], AttributeHandler.get("hp")),
            getOwner().getOwningPlayer());
      }
      explosionFX();
   }

   /**
    * The fx cast when the spell is cast
    */
   private void explosionFX() {
      for(int i = 0; i < 36; i++) {
         ParticleHandler.spawn(2, getOwner().getPosition(), ParticleHandler.SIZE_LARGE,
            getOwner().getOwningPlayer().getPrimaryColor(),
            getOwner().getOwningPlayer().getSecondaryColor(),
            1, (Math.PI * i) / 18, 0.1, 70, 20, 0.5, 0.1);
      }
   }
}
