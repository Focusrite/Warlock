/**
 * File: warlock.object.warlock.Attribute.java
 *
 * A warlock, the unit a player controlls, that fights on a level. These have a lot of methods and
 * members relevant to any of it's purpose, such as what spells it knows, attributes and much more.
 *
 * The control of the warlock is however handled by the player.
 */
package warlock.object.warlock;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import warlock.ExtraMath;
import warlock.attribute.Attribute;
import warlock.attribute.AttributeHandler;
import warlock.attribute.AttributeType;
import warlock.attribute.StatusEffect;
import warlock.attribute.StatusEffectListener;
import warlock.attribute.StatusEffectType;
import warlock.constant.MathConstants;
import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.level.GroundType;
import warlock.object.LevelObject;
import warlock.object.particle.ParticleHandler;
import warlock.phys.Vector;
import warlock.player.Player;
import warlock.resource.ResourceHandler;
import warlock.spell.Explosion;
import warlock.spell.Fireball;
import warlock.spell.Spell;
import warlock.spell.SpellShortcut;
import warlock.time.Time;

public class Warlock extends LevelObject {

   private static final double LAVA_DPS = 10;
   private static final double LAVA_DAMAGE_DELAY = 1;
   private static final double COLLIDE_FORCE_SCALE = 1.0;
   private static final int WARLOCK_SIZE = 20;
   private static final int HPBAR_OFFSET = 25;
   private static final int HPBAR_WIDTH = 35;
   private static final int HPBAR_HEIGHT = 5;
   private static double FRICTION = 200;
   private double lavaCounter = 0.0f;
   private Map<String, Attribute> attributes = new HashMap<>();
   private Map<SpellShortcut, Spell> spells = new EnumMap<>(SpellShortcut.class);
   private ArrayList<StatusEffect> statusEffects = new ArrayList<>();
   private ArrayList<DeathListener> listeners = new ArrayList<>();
   private Vector momentum; //The dMovement done per sec
   private Vector moveTo;
   private double facingAngle;
   private boolean dead;

   /**
    * Create a new fresh warlock!
    */
   public Warlock() {
      momentum = new Vector();
      dead = false;
      facingAngle = 0;
      addSpell(SpellShortcut.MB, new Fireball(this));
      addSpell(SpellShortcut.R, new Explosion(this));
      init();
   }

   /**
    * Initializes all the attributes set up by the attributehandler to be available on this warlock.
    */
   private final void initAttr() {
      Iterator<AttributeType> iter = AttributeHandler.iterator();
      while (iter.hasNext()) {
         AttributeType attr = iter.next();
         this.attributes.put(attr.getTag(), new Attribute(attr, attr.getDefaultValue()));
      }
   }

   /**
    * Init.. just runs initAttr at the moment
    */
   public final void init() {
      this.initAttr();
   }

   /**
    * Cure the warlock, resetting all attributes to their default base value
    */
   public void cure() {
      dead = false;
      statusEffects = new ArrayList<>();
      this.momentum = new Vector();
      this.moveTo = null;
      for (int i = 0; i < this.attributes.size(); i++) {
         Attribute attr = (Attribute) this.attributes.values().toArray()[i];
         attr.setValue((int) attr.getBaseValue());
      }
   }

   /**
    * Add a death listener to notify when the warlock gets KO'd
    * @param listener
    */
   public void addDeathListener(DeathListener listener) {
      listeners.add(listener);
   }

   /**
    * Notify all listener this warlock just passed away/got torched
    * @param killer
    */
   public void notifyDeath(Player killer) {
      for (int i = 0; i < listeners.size(); i++) {
         listeners.get(i).notifyDeath(this.getOwningPlayer(), killer);
      }
   }

   /**
    * Remove all death listeners from this warlock
    */
   public void clearDeathListener() {
      listeners.clear();
   }

   /**
    * Returns an attribute on the warlock if tag exists
    *
    * @param tag
    * @return Attribute if exist, or null if not available
    */
   public Attribute attr(String tag) {
      return this.attributes.get(tag);
   }

   /**
    * Returns the current value of an attribute
    * @param tag of the attribute
    * @return current value if tag is valid, else null
    */
   public double attrVal(String tag) {
      Attribute attr = attr(tag);
      return (attr == null) ? null : attr.getValue();
   }

   /**
    * Return the base value of an attribute
    * @param tag
    * @return base value if tag is valid, else null
    */
   public double attrBaseVal(String tag) {
      Attribute attr = attr(tag);
      return (attr == null) ? null : attr.getBaseValue();
   }

   /**
    * Set the position of the warlock in (x,y) coordinates
    * @param x
    * @param y
    */
   public void setPosition(int x, int y) {
      super.getPosition().setX(x);
      super.getPosition().setY(y);
   }

   /**
    * @return momentum force of the warlock
    */
   public Vector getMomentum() {
      return momentum;
   }

   /**
    * @param momentum new momentum
    */
   public void setMomentum(Vector momentum) {
      this.momentum = momentum;
   }

   /**
    * @return the vector the warlock is moving towards
    */
   public Vector getMoveTo() {
      return moveTo;
   }

   /**
    * Sets the position the warlock moves towards, he must first turn to face that angle before moving
    * @param moveTo position
    */
   public void setMoveTo(Vector moveTo) {
      this.moveTo = moveTo;
   }

   /**
    * @return the angle the warlock is facing
    */
   public double getFacingAngle() {
      return facingAngle;
   }

   /**
    * Inflict a status effect on the warlock. These can both be positive and negative.
    * @param type the type of status effect, ie what attribute it targets
    * @param secondsLasting
    * @param inflicter the inflicting player
    * @param listener a expire listener that fires when the status effect expires
    */
   public void inflictStatusEffect(StatusEffectType type, double secondsLasting, Player inflicter,
      StatusEffectListener listener) {

      StatusEffect effect = StatusEffect.newInstance(this.attr(type.getAffectedTag()), type);
      effect.setInflicter(inflicter).setTemporary((secondsLasting > 0)).
         setExprStamp((secondsLasting > 0) ? Time.relativetime(secondsLasting) : 0);

      effect.onset(); //"start" status effect
      if(listener != null) {
         effect.addListener(listener);
      }
      statusEffects.add(effect);
   }
   public void inflictStatusEffect(StatusEffectType type, double secondsLasting, Player inflicter) {
      inflictStatusEffect(type, secondsLasting, inflicter, null);
   }

   public void inflictStatusEffect(StatusEffectType type, Player inflicter) {
      inflictStatusEffect(type, 0, inflicter);
   }

   public void inflictStatusEffect(StatusEffectType type, double secondsLasting) {
      inflictStatusEffect(type, secondsLasting, null);
   }

   /**
    * Add momentum to this warlock
    * @param v force to add
    */
   public void applyForce(Vector v) {
      momentum = momentum.add(v);
   }

   /**
    * Update the status effects this warlock has inflicted on him, and cure any expired ones
    * @param dt
    */
   private void updateStatusEffects(double dt) {
      for (int i = statusEffects.size() - 1; i >= 0; i--) {
         StatusEffect e = statusEffects.get(i);
         if (e.isTemporary() && e.hasExpired()) {
            e.expire();
            statusEffects.remove(e);
         }
      }
   }

   /**
    * Turn towards the angle
    * @param angle
    * @param dAngle
    */
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

   /**
    * Move towards moveTo if warlock is able to.
    * @param dt
    */
   public void steer(double dt) {
      if (this.moveTo == null || isStunned()) {
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

   /**
    * Do movement updates, both applied force and player controlled movement
    * @param dt
    */
   private void updateMovement(double dt) {
      doForce(dt);//Apply force movement
      steer(dt); //Handle player movement
   }

   /**
    * Move the warlock if it has any momentum force.
    * @param dt
    */
   private void doForce(double dt) {
      if (getMomentum().getLength() > 0) {
         setPosition(getMomentum().scale(dt).add(getPosition()));
         if (getMomentum().getLength() < 3) {
            setMomentum(new Vector(0, 0));
         }
         else {
            setMomentum(getMomentum().add(new Vector(Warlock.FRICTION * dt, getMomentum().getAngle() - Math.PI)));
         }
      }
   }

   /**
    * Updates spell cooldowns
    * @param dt
    */
   private void updateSpells(double dt) {
      for (SpellShortcut ss : SpellShortcut.values()) {
         Spell spell = spells.get(ss);
         if (spell != null) {
            spell.update(dt);
         }
      }
   }

   /**
    * Checks the groundtype and damages the warlock if on lava
    * @param dt
    */
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

   /**
    * Check whether the warlcok is dead or not, and kills it if it is, with a nifty particle explosion
    * to boot.
    */
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

   /**
    * The last inflicted status effect of the attribute type "tag"
    * @param tag attribute tag
    * @return the last inflicted status effect if any inflicted, else null
    */
   public StatusEffect getLastStatusEffect(String tag) {
      for (int i = statusEffects.size() - 1; i >= 0; i--) {
         if (statusEffects.get(i).getAttr().getAttributeTag().equals(tag)) {
            return statusEffects.get(i);
         }
      }
      return null;
   }

   /**
    * Update the warlock
    * @param dt
    */
   @Override
   public void update(double dt) {
      this.updateStatusEffects(dt);
      this.updateMovement(dt);
      this.updateSpells(dt);
      this.updateGround(dt);
      checkDeath();
   }

   /**
    * Cause the warlock to cast a spell
    * @param shorcut
    * @param castingPoint
    */
   public void castSpell(SpellShortcut shorcut, Vector castingPoint) {
      if(isStunned()) {
         return;
      }
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

   /**
    * @param shortcut
    * @return spell on shortcut if possible, else null
    */
   public Spell getSpell(SpellShortcut shortcut) {
      return this.spells.get(shortcut);
   }

   /**
    * Add spell to shortcut
    * @param shortcut
    * @param spell to add
    */
   public final void addSpell(SpellShortcut shortcut, Spell spell) {
      spell.setOwner(this);
      this.spells.put(shortcut, spell);
   }

   /**
    * Checks whether the warlock knows how to cast a spell or not
    * @param spell
    * @return true if able, else false
    */
   public boolean knowsSpell(Spell spell) {
      boolean sameClass = spell.getClass().equals(getSpell(spell.getShortcut()).getClass());
      return this.spells.containsKey(spell.getShortcut()) && sameClass;
   }

   /**
    * Checks whether a warlock can learn a nes spell, or if he already knows one that occupies that
    * shortcut slot. Returns false if warlocks knows the same spell.
    * @param spell
    * @return true if able to learn, else false
    */
   public boolean canLearn(Spell spell) {
      return !this.spells.containsKey(spell.getShortcut());
   }

   /**
    * @param angle new facing angle
    */
   private void setFacingAngle(double angle) {
      angle = (angle) % (2 * Math.PI);
      angle = (angle >= 0) ? angle : (2 * Math.PI) + angle;
      this.facingAngle = angle;
   }

   /**
    * @param dAngle angle to add to facing angle
    */
   private void modifyFacingAngle(double dAngle) {
      setFacingAngle(this.facingAngle + dAngle);
   }

   /**
    * Render the warlock
    * @param g
    */
   @Override
   public void render(Graphic g) {
      //draw hero
      g.drawRectangle((int) getPosition().getX(), (int) getPosition().getY(), ZLayers.ON_GROUND,
         WARLOCK_SIZE, WARLOCK_SIZE, getFacingAngle(), getOwningPlayer().getPrimaryColor());
      drawHPbar(g, (int) getPosition().getX(), (int) getPosition().getY() + HPBAR_OFFSET, ZLayers.ABOVE_LEVEL, HPBAR_WIDTH, HPBAR_HEIGHT);
      if(attrVal("shield") > 0) {
         g.drawTexture(ResourceHandler.getTexture("fx-shield"),
            (int) getPosition().getX(), (int) getPosition().getY(), ZLayers.OBJECT,
            WARLOCK_SIZE + 20, WARLOCK_SIZE + 20, 0, getOwningPlayer().getPrimaryColor());
      }
   }

   /**
    * Draw the hp-bar for a warlock
    * @param g
    * @param x
    * @param y
    * @param z
    * @param w width
    * @param h height
    */
   public void drawHPbar(Graphic g, int x, int y, int z, int w, int h) {
      double hpLeft = Math.max(attrVal("hp") / attrBaseVal("hp"), 0);
      g.drawUnfilledRectangle(x, y, z, w, h, 1, 0, new Color(0, 0, 0));
      g.drawTexture(ResourceHandler.getTexture("ui-hpbar"), x - (int) ((1 - hpLeft) * (w / 2)), y, z, (int) (w * hpLeft), h, 0, getHealthColor());
   }

   /**
    * Get the color the healthbar should have based on damage taken (green full health, red damaged).
    * @return color
    */
   public Color getHealthColor() {
      double hpLeft = Math.max(attrVal("hp") / attrBaseVal("hp"), 0);
      return new Color((int) (255 * (1 - hpLeft)), (int) (255 * hpLeft), 0);
   }

   /**
    * Handle collision with other objects
    * @param collidingObject
    */
   @Override
   public void handleCollision(LevelObject collidingObject) {
      if (collidingObject instanceof Warlock) {
         Warlock collider = (Warlock) collidingObject;
         if (collider.getPosition().equals(getPosition())) {
            modifyPosition(Vector.create(5, 0));
         }
         Vector difference = getPosition().subtract(collider.getPosition());
         applyForce(difference.scale(COLLIDE_FORCE_SCALE));
         difference.rotate(Math.PI);
         collider.applyForce(difference.scale(COLLIDE_FORCE_SCALE));
      }
   }

   /**
    * The particle effect played when the warlock is hit by an projectile
    * @param angle
    */
   public void hitFX(double angle) {
      ParticleHandler.spawn(30, getPosition(), ParticleHandler.SIZE_SMALL, //Positional and size
         getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
         1, //No slowdown
         angle, Math.PI / 20, //Angle
         200, 50, //Particle speed
         0.5, 0.3); //Lifetime
   }

   /**
    * @return true if warlock no longer needs to move, else false
    */
   public boolean hasReachedDestination() {
      return moveTo == null;
   }

   /**
    * @return the combined movement vector with movement and momentum
    */
   public Vector getMovementVector() {
      if (getMoveTo() != null) {
         Vector t = new Vector(getMoveTo());
         t.setLength(attrVal("ms"));
         return getMomentum().add(t);
      }
      return getMomentum();
   }

   /**
    * @return the closest warlock on the level to this
    */
   public Warlock getClosestWarlock() {
      return getLevel().getClosestWarlock(this);
   }

   /**
    * @return true if warlock is stunned, else false
    */
   private boolean isStunned() {
      return attrVal("stun") != 0;
   }
}
