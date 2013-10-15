/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.spell;

import warlock.object.character.Warlock;
import warlock.object.particle.ParticleHandler;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class Teleport extends Spell {

   private static final double cooldown[] = {20, 18, 15, 12, 10};
   private static final double range[] = {150, 200, 250, 300, 350};

   public Teleport(Warlock owner) {
      super(owner, "Teleport", SpellTarget.GROUND, SpellShortcut.SPC, 5);
      setCooldown(cooldown[0]);
      setSpellIcon("spell-teleport");
      setMaxLevel(5);
      setType(SpellType.ESCAPE);

      setBaseDescription(
           "By studying wormholes \n"
         + "Warlocks eventually\n"
         + "learned how to\n"
         + "teleport. \n"
         + "It's easy, really. \n");
      setLevelDataNames(new String[]{"Range", "Cooldown"});
      setLevelData(new double[][]{range, cooldown});
      rebuildDescription();
   }

   @Override
   public void levelUp() {
      super.levelUp();
      setCooldown(cooldown[getSpellLevel() - 1]);
   }

   @Override
   public void cast(Vector castVector) {
      teleportFX(getOwner().getPosition());
      if(castVector.distance(getOwner().getPosition()) > range[getSpellLevel() - 1]) {
         castVector.setLength(range[getSpellLevel() - 1]);
      }
      castVector = castVector.add(getOwner().getPosition());
      getOwner().setPosition(castVector);
      getOwner().setMoveTo(null);
      teleportFX(castVector);
   }

   @Override
   public Teleport clone() {
      Teleport tp = new Teleport(null);
      return tp;
   }

   private void teleportFX(Vector v) {
      for(int i = 0; i < 36; i++) {
         ParticleHandler.spawn(2, v, ParticleHandler.SIZE_SMALL,
            getOwner().getOwningPlayer().getPrimaryColor(),
            getOwner().getOwningPlayer().getSecondaryColor(),
            1, (Math.PI * i) / 18, 0.1, 70, 20, 0.3, 0.1);
      }

   }
}
