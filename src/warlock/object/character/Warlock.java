/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import warlock.ExtraMath;
import warlock.constant.MathConstants;
import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.level.GroundType;
import warlock.object.LevelObject;
import warlock.object.particle.ParticleHandler;
import warlock.phys.Force;
import warlock.phys.Vector;
import warlock.player.Player;
import warlock.spell.Fireball;
import warlock.spell.Spell;
import warlock.spell.SpellShortcut;
import warlock.time.Misc;

/**
 * TODO: MOVE STUFF TO PLAYER, SO A PLAYER OWNS A WARLOCK
 *
 * @author Focusrite
 */
public class Warlock extends LevelObject {

   private static final double LAVA_DPS = 10;
   private static final double LAVA_DAMAGE_DELAY = 1;
   private double lavaCounter = 0.0f;
   private static final int HPBAR_OFFSET = 25;
   private static final int HPBAR_WIDTH = 35;
   private static final int HPBAR_HEIGHT = 5;
   private Map<String, Attribute> attributes = new HashMap<>();
   private Map<SpellShortcut, Spell> spells = new EnumMap<>(SpellShortcut.class);
   private ArrayList<StatusEffect> statusEffects = new ArrayList<>();
   private Force momentum; //The dMovement done per sec
   private Vector moveTo;
   private double facingAngle = 0;
   private static double FRICTION = 0.98;

   public Warlock() {
      momentum = new Force();
      this.spells.put(SpellShortcut.MB, new Fireball(this));
      init();
   }

   private final void initAttr() {
      Iterator<AttributeType> iter = AttributeHandler.iterator();
      while (iter.hasNext()) {
         AttributeType attr = iter.next();
         this.attributes.put(attr.getTag(), new Attribute(attr, attr.getDefaultValue()));
      }
   }

   public final void init() {
      this.initAttr();
   }

   /**
    * I miss __call magic functions, so much neater :C
    *
    * @param name
    * @return Attribute if exist, or null if not available
    */
   public Attribute attr(String name) {
      return this.attributes.get(name);
   }

   public double attrVal(String name) {
      Attribute attr = attr(name);
      return (attr == null) ? null : attr.getValue();
   }

   public double attrBaseVal(String name) {
      Attribute attr = attr(name);
      return (attr == null) ? null : attr.getBaseValue();
   }

   public void setPosition(int x, int y) {
      super.getPosition().setX(x);
      super.getPosition().setY(y);
   }

   public Force getMomentum() {
      return momentum;
   }

   public void setMomentum(Force momentum) {
      this.momentum = momentum;
   }

   public Vector getMoveTo() {
      return moveTo;
   }

   public void setMoveTo(Vector moveTo) {
      this.moveTo = moveTo;
   }

   public double getFacingAngle() {
      return facingAngle;
   }

   public void inflictStatusEffect(StatusEffectType type, double secondsLasting, Player inflicter) {
      StatusEffect effect = StatusEffect.newInstance(this.attr(type.getAffectedTag()), type);
      effect.setInflicter(inflicter).setTemporary((secondsLasting > 0)).
         setExprStamp((secondsLasting > 0) ? Misc.relativetime(secondsLasting) : 0);

      effect.onset(); //"start" status effect
      statusEffects.add(effect);
   }

   public void inflictStatusEffect(StatusEffectType type, Player inflicter) {
      inflictStatusEffect(type, 0, inflicter);
   }

   public void inflictStatusEffect(StatusEffectType type, int secondsLasting) {
      inflictStatusEffect(type, secondsLasting, null);
   }

   public void applyForce(Vector v) {
      momentum = momentum.add(v);
   }

   private void updateStatusEffects(double dt) {
      for (StatusEffect e : statusEffects) {
         if (e.isTemporary() && e.hasExpired()) {
            e.expire();
            statusEffects.remove(e);
         }
      }
   }

   private void updateMovement(double dt) {
      doForce(dt);//Apply force movement
      steer(dt); //Handle player movement
   }

   private void doForce(double dt) {
      if (getMomentum().getLength() > 0) {
         setPosition(getMomentum().scale(dt).add(getPosition()));
         if (getMomentum().getLength() < 3) {
            setMomentum(new Force(0, 0));
         }
         else {
            setMomentum(getMomentum().scale(Warlock.FRICTION));
         }
      }
   }

   private void updateSpells(double dt) {
      for (SpellShortcut ss : SpellShortcut.values()) {
         Spell spell = spells.get(ss);
         if (spell != null) {
            spell.update(dt);
         }
      }
   }

   private void updateGround(double dt) {
      if (getGroundType() == GroundType.LAVA) {
         if (lavaCounter < 0) {
            inflictStatusEffect(new StatusEffectType(LAVA_DPS, AttributeHandler.get("hp")), 0);
            ParticleHandler.spawn(20, getPosition(), ParticleHandler.SIZE_NORMAL, //Positional and size
               getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
               1, //No slowdown
               ExtraMath.getRandom().nextDouble() * 2 * Math.PI, Math.PI / 4, //Angle
               50, 10, //Particle speed
               1, 0.5); //Lifetime
            lavaCounter = LAVA_DAMAGE_DELAY;
         }
         else {
            lavaCounter -= dt;
         }
      }
      else {
         lavaCounter = LAVA_DAMAGE_DELAY;
      }
   }

   private void checkDeath() {
      if (attr("hp").getValue() <= 0) {
         kill();
         ParticleHandler.spawn(40, getPosition(), ParticleHandler.SIZE_NORMAL, //Positional and size
               getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
               1, //No slowdown
               0, Math.PI, //Angle
               50, 10, //Particle speed
               1, 0.5); //Lifetime
      }
   }

   @Override
   public void update(double dt) {
      this.updateStatusEffects(dt);
      this.updateMovement(dt);
      this.updateSpells(dt);
      this.updateGround(dt);
      checkDeath();
   }

   public void castSpell(SpellShortcut shorcut, double angle) {
      Spell spell = this.spells.get(SpellShortcut.MB);
      if (spell != null && spell.canCast()) {
         spell.preCast();
         spell.cast(angle);
      }
   }

   public Spell getSpell(SpellShortcut shortcut) {
      return this.spells.get(shortcut);
   }

   private void setFacingAngle(double angle) {
      angle = (angle) % (2 * Math.PI);
      angle = (angle >= 0) ? angle : (2 * Math.PI) + angle;
      this.facingAngle = angle;
   }

   private void modifyFacingAngle(double dAngle) {
      setFacingAngle(this.facingAngle + dAngle);
   }

   private void turnTowards(double angle, double dAngle) {
      double dir = Math.abs(angle - this.facingAngle);
      if (dir <= Math.PI) {
         modifyFacingAngle((angle > this.facingAngle) ? dAngle : -dAngle);
      }
      else if (angle < this.facingAngle) {
         modifyFacingAngle((angle + (2 * Math.PI) > this.facingAngle) ? dAngle : -dAngle);
      }
      else {
         modifyFacingAngle((angle > this.facingAngle + (2 * Math.PI)) ? dAngle : -dAngle);
      }
   }

   public void steer(double dt) {
      if (this.moveTo == null) {
         return;
      }
      Vector directionVector = this.moveTo.subtract(getPosition());
      //results in the vector from position to moveTo

      double movementSpeed = dt * attrVal("ms");
      double turnRate = dt * Math.PI * attrVal("tr");
      double difference = Math.abs(this.facingAngle - directionVector.getAngle());
      double crossover = Math.PI / 10;
      if (difference > crossover) {
         turnTowards(directionVector.getAngle(), turnRate);
         return; //Dont move unless turned correct way
      }
      else if (difference <= crossover && difference > MathConstants.EPSILON) {
         this.facingAngle = directionVector.getAngle();
      }
      modifyPosition(new Vector(movementSpeed, directionVector.getAngle()));
      if (directionVector.getLength() < movementSpeed) { //need better check
         this.moveTo = null;
      }
   }

   @Override
   public void render(Graphic g) {
      //draw hero
      g.drawRectangle((int) getPosition().getX(), (int) getPosition().getY(), ZLayers.OBJECT,
         20, 20, getFacingAngle(), getOwningPlayer().getPrimaryColor());
      drawHPbar(g);
   }

   private void drawHPbar(Graphic g) {
      int x = (int) getPosition().getX();
      int y = (int) getPosition().getY() + HPBAR_OFFSET;
      double hpLeft = Math.max(attrVal("hp") / attrBaseVal("hp"), 0);
      g.drawRectangle(x - (int) ((1 - hpLeft) * (HPBAR_WIDTH / 2)), y, ZLayers.ABOVE_LEVEL, (int) (HPBAR_WIDTH * hpLeft), HPBAR_HEIGHT, 0,
         new Color((int) (255 * (1 - hpLeft)), (int) (255 * hpLeft), 0));

      g.drawUnfilledRectangle(x, y, ZLayers.ABOVE_LEVEL, HPBAR_WIDTH, HPBAR_HEIGHT, 1, 0, new Color(0, 0, 0));
   }

   @Override
   public void handleCollision(LevelObject collidingObject) {
   }

   public void hitFX(double angle) {
      ParticleHandler.spawn(30, getPosition(), ParticleHandler.SIZE_SMALL, //Positional and size
               getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
               1, //No slowdown
               angle, Math.PI / 20, //Angle
               200, 50, //Particle speed
               0.5, 0.3); //Lifetime
   }
}
