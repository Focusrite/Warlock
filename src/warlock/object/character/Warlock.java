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
import warlock.resource.ResourceManager;
import warlock.spell.Fireball;
import warlock.spell.Spell;
import warlock.spell.SpellShortcut;
import warlock.time.Misc;

/**
 *       ////////////// TODO: TWEAK MOVEMENT //////////////
 *
 * @author Focusrite
 */
public class Warlock extends LevelObject {

   private static final double LAVA_DPS = 10;
   private static final double LAVA_DAMAGE_DELAY = 1;
   private static final double COLLIDE_FORCE_SCALE = 1.0;
   private static final int WARLOCK_SIZE = 20;
   private static final int HPBAR_OFFSET = 25;
   private static final int HPBAR_WIDTH = 35;
   private static final int HPBAR_HEIGHT = 5;
   private static double FRICTION = 0.98;
   private double lavaCounter = 0.0f;
   private Map<String, Attribute> attributes = new HashMap<>();
   private Map<SpellShortcut, Spell> spells = new EnumMap<>(SpellShortcut.class);
   private ArrayList<StatusEffect> statusEffects = new ArrayList<>();
   private ArrayList<DeathListener> listeners = new ArrayList<>();
   private Force momentum; //The dMovement done per sec
   private Vector moveTo;
   private double facingAngle;
   private boolean dead;

   public Warlock() {
      momentum = new Force();
      dead = false;
      facingAngle = 0;
      addSpell(SpellShortcut.MB, new Fireball(this));
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

   public void cure() {
      dead = false;
      this.momentum = new Force();
      this.moveTo = null;
      for (int i = 0; i < this.attributes.size(); i++) {
         Attribute attr = (Attribute) this.attributes.values().toArray()[i];
         attr.setValue((int) attr.getBaseValue());
      }
   }

   public void addDeathListener(DeathListener listener) {
      listeners.add(listener);
   }

   public void notifyDeath(Player killer) {
      for (int i = 0; i < listeners.size(); i++) {
         listeners.get(i).notifyDeath(this.getOwningPlayer(), killer);
      }
   }

   public void clearDeathListener() {
      listeners.clear();
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

   public void inflictStatusEffect(StatusEffectType type, double secondsLasting) {
      inflictStatusEffect(type, secondsLasting, null);
   }

   public void applyForce(Vector v) {
      momentum = momentum.add(v);
   }

   private void updateStatusEffects(double dt) {
      for (int i = statusEffects.size() - 1; i >= 0; i--) {
         StatusEffect e = statusEffects.get(i);
         if (e.isTemporary() && e.hasExpired()) {
            e.expire();
            statusEffects.remove(e);
         }
      }
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
      Vector modify = new Vector(movementSpeed, directionVector.getAngle());
      modifyPosition(modify);
      if (directionVector.getLength() < movementSpeed) { //need better check
         this.moveTo = null;
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
         dead = true;
         StatusEffect lastDamageInstance = getLastStatusEffect("hp");
         notifyDeath(lastDamageInstance.getInflicter());
         ParticleHandler.spawn(40, getPosition(), ParticleHandler.SIZE_NORMAL, //Positional and size
            getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
            1, //No slowdown
            0, Math.PI, //Angle
            50, 10, //Particle speed
            1, 0.5); //Lifetime
      }
   }

   public StatusEffect getLastStatusEffect(String type) {
      for (int i = statusEffects.size() - 1; i >= 0; i--) {
         if (statusEffects.get(i).getAttr().getAttributeTag().equals(type)) {
            return statusEffects.get(i);
         }
      }
      return null;
   }

   @Override
   public void update(double dt) {
      this.updateStatusEffects(dt);
      this.updateMovement(dt);
      this.updateSpells(dt);
      this.updateGround(dt);
      checkDeath();
   }

   public void castSpell(SpellShortcut shorcut, Vector castingPoint) {
      Spell spell = this.spells.get(shorcut);
      if (spell != null && spell.canCast()) {
         spell.preCast();
         if(castingPoint != null) {
            spell.cast(castingPoint.subtract(getPosition())); //Vector from position to casting point
         }
         else {
            spell.cast(null); //Vector from position to casting point
         }
      }
   }

   public Spell getSpell(SpellShortcut shortcut) {
      return this.spells.get(shortcut);
   }

   public final void addSpell(SpellShortcut shortcut, Spell spell) {
      spell.setOwner(this);
      this.spells.put(shortcut, spell);
   }

   public boolean knowsSpell(Spell spell) {
      boolean sameClass = spell.getClass().equals(getSpell(spell.getShortcut()).getClass());
      return this.spells.containsKey(spell.getShortcut()) && sameClass;
   }

   public boolean canLearn(Spell spell) {
      return !this.spells.containsKey(spell.getShortcut());
   }

   private void setFacingAngle(double angle) {
      angle = (angle) % (2 * Math.PI);
      angle = (angle >= 0) ? angle : (2 * Math.PI) + angle;
      this.facingAngle = angle;
   }

   private void modifyFacingAngle(double dAngle) {
      setFacingAngle(this.facingAngle + dAngle);
   }

   @Override
   public void render(Graphic g) {
      //draw hero
      g.drawRectangle((int) getPosition().getX(), (int) getPosition().getY(), ZLayers.OBJECT,
         WARLOCK_SIZE, WARLOCK_SIZE, getFacingAngle(), getOwningPlayer().getPrimaryColor());
      drawHPbar(g, (int) getPosition().getX(), (int) getPosition().getY() + HPBAR_OFFSET, ZLayers.ABOVE_LEVEL, HPBAR_WIDTH, HPBAR_HEIGHT);
      if(attrVal("shield") > 0) {
         g.drawTexture(ResourceManager.getTexture("fx-shield"),
            (int) getPosition().getX(), (int) getPosition().getY(), ZLayers.OBJECT,
            WARLOCK_SIZE + 20, WARLOCK_SIZE + 20, 0, getOwningPlayer().getPrimaryColor());
      }
   }

   public void drawHPbar(Graphic g, int x, int y, int z, int w, int h) {
      double hpLeft = Math.max(attrVal("hp") / attrBaseVal("hp"), 0);
      g.drawUnfilledRectangle(x, y, z, w, h, 1, 0, new Color(0, 0, 0));
      g.drawTexture(ResourceManager.getTexture("ui-hpbar"), x - (int) ((1 - hpLeft) * (w / 2)), y, z, (int) (w * hpLeft), h, 0, getHealthColor());
   }

   public Color getHealthColor() {
      double hpLeft = Math.max(attrVal("hp") / attrBaseVal("hp"), 0);
      return new Color((int) (255 * (1 - hpLeft)), (int) (255 * hpLeft), 0);
   }

   @Override
   public void handleCollision(LevelObject collidingObject) {
      if (collidingObject instanceof Warlock) {
         Warlock collider = (Warlock) collidingObject;
         if (collider.getPosition().equals(getPosition())) {
            modifyPosition(Vector.create(5, 0));
         }
         Vector difference = getPosition().subtract(collider.getPosition()); //TODO: take force into consideration
         applyForce(difference.scale(COLLIDE_FORCE_SCALE));
         difference.rotate(Math.PI);
         collider.applyForce(difference.scale(COLLIDE_FORCE_SCALE));
      }
   }

   public void hitFX(double angle) {
      ParticleHandler.spawn(30, getPosition(), ParticleHandler.SIZE_SMALL, //Positional and size
         getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
         1, //No slowdown
         angle, Math.PI / 20, //Angle
         200, 50, //Particle speed
         0.5, 0.3); //Lifetime
   }

   public boolean hasReachedDestination() {
      return moveTo == null;
   }

   public Vector getMovementVector() {
      if (getMoveTo() != null) {
         Vector t = new Vector(getMoveTo());
         t.setLength(attrVal("ms"));
         return getMomentum().add(t);
      }
      return getMomentum();
   }

   public Warlock getClosestWarlock() {
      return getLevel().getClosestWarlock(this);
   }
}
