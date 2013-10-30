/**
 * File: warlock.attribute.StatusEffect.java
 *
 * An effect that modifies an attribute, either for a particular time or indefinitely. Sends an
 * expired notice to any StatusEffectListener that's registered.
 *
 */
package warlock.attribute;

import java.util.ArrayList;
import java.util.Objects;
import warlock.player.Player;
import warlock.time.Time;

public class StatusEffect {
   private ArrayList<StatusEffectListener> listeners = new ArrayList<>();
   private Attribute affectedAttr;
   private StatusEffectType type;
   private double magnitude;
   private Player inflicter;
   private long timestamp;
   //Time
   private boolean temporary;
   private long exprStamp;
   private boolean manualExpired;

   /**
    * Create a new status effect.
    *
    * @param affectedAttr
    * @param type
    */
   public StatusEffect(Attribute affectedAttr, StatusEffectType type) {
      this.affectedAttr = affectedAttr;
      this.type = type;
      this.magnitude = type.getMagnitude();
      this.timestamp = Time.millitime();
   }

   /**
    * Same as constructor but allows for chaining of method calls
    *
    * @param affectedAttr
    * @param type
    * @return created StatusEffect
    */
   public static StatusEffect newInstance(Attribute affectedAttr, StatusEffectType type) {
      return new StatusEffect(affectedAttr, type);
   }

   /**
    * Get the attribute this status effect is inflicted on
    * @return Attribute
    */
   public Attribute getAttr() {
      return affectedAttr;
   }

   /**
    * Force a expiration of the status effect
    */
   public void forceExpiration() {
      manualExpired = true;
   }

   /**
    * @return double, the magnitude of this status effect (ie how much it modifies the attribute value)
    */
   public double getMagnitude() {
      return magnitude;
   }

   /**
    * Sets new magnitude. Returns self for metohd call chaining. Notice that magnitude is negative
    * as default, ie a positive number x removes x from attribute. For buffing use a negative value.
    * @param magnitude new magnitude
    * @return this
    */
   public StatusEffect setMagnitude(int magnitude) {
      this.magnitude = magnitude;
      return this;
   }

   /**
    * @return Player, the player that inflicted the status effect
    */
   public Player getInflicter() {
      return inflicter;
   }

   /**
    * Sets inflicter, returns self for method call chaining.
    * @param inflicter
    * @return this
    */
   public StatusEffect setInflicter(Player inflicter) {
      this.inflicter = inflicter;
      return this;
   }

   /**
    * @return boolean whether this status effect will expire with time
    */
   public boolean isTemporary() {
      return temporary;
   }

   /**
    * Sets if this status effect is temporary or not, returns self for chaining.
    * @param temporary
    * @return this
    */
   public StatusEffect setTemporary(boolean temporary) {
      this.temporary = temporary;
      return this;
   }

   /**
    * @return long, the unix-timestamp when this method expires
    */
   public long getExprStamp() {
      return exprStamp;
   }

   /**
    * Sets expiration timestamp, returns self for method call chaining
    * @param exprStamp
    * @return this
    */
   public StatusEffect setExprStamp(long exprStamp) {
      this.exprStamp = exprStamp;
      return this;
   }

   /**
    * Returns the time left on this status effect. Always 1 the status effect never expires
    * @return integer milliseconds
    */
   public long getTimeLeft() {
      return (this.isTemporary()) ? this.exprStamp - Time.millitime() : 1;
   }

   /**
    * @return boolean whether the status effect has expired or not
    */
   public boolean hasExpired() {
      return getTimeLeft() < 0 || manualExpired;
   }

   /**
    * Modify the attribute with the magnitude of the status effect. This removes magnitude from value,
    * buffs should be negative to give a increase in attribute.
    */
   public void onset() {
      this.getAttr().modValue(-this.getMagnitude());
   }

   /**
    * Expires this status effect and returns magnitude to attribute (setting it back to what it was
    * before). Only ran if it status effect has expired.
    */
   public void expire() {
      if (this.hasExpired()) {
         this.getAttr().modValue(this.getMagnitude());
         notifyExpired();
      }
   }

   /**
    * Add a statusEffectListener that listens to when status effect ends.
    * @param listener
    */
   public void addListener(StatusEffectListener listener) {
      listeners.add(listener);
   }

   /**
    * Notify all listeners this status effect has expired.
    */
   private void notifyExpired() {
      for(int i = 0; i < listeners.size(); i++) {
         listeners.get(i).expired();
      }
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 97 * hash + Objects.hashCode(this.affectedAttr);
      hash = 97 * hash + Objects.hashCode(this.type);
      hash = 97 * hash + (int) (Double.doubleToLongBits(this.magnitude) ^ (Double.doubleToLongBits(this.magnitude) >>> 32));
      hash = 97 * hash + Objects.hashCode(this.inflicter);
      hash = 97 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
      hash = 97 * hash + (this.temporary ? 1 : 0);
      hash = 97 * hash + (int) (this.exprStamp ^ (this.exprStamp >>> 32));
      return hash;
   }


   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final StatusEffect other = (StatusEffect) obj;
      if (!Objects.equals(this.affectedAttr, other.affectedAttr)) {
         return false;
      }
      if (!Objects.equals(this.type, other.type)) {
         return false;
      }
      if (this.magnitude != other.magnitude) {
         return false;
      }
      if (!Objects.equals(this.inflicter, other.inflicter)) {
         return false;
      }
      if (this.timestamp != other.timestamp) {
         return false;
      }
      if (this.temporary != other.temporary) {
         return false;
      }
      if (this.exprStamp != other.exprStamp) {
         return false;
      }
      return true;
   }

}
