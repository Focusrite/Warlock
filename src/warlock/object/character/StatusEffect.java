/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

import java.util.ArrayList;
import java.util.Objects;
import warlock.player.Player;
import warlock.time.Misc;

/**
 *
 * @author Focusrite
 */
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

   public StatusEffect(Attribute affectedAttr, StatusEffectType type) {
      this.affectedAttr = affectedAttr;
      this.type = type;
      this.magnitude = type.getMagnitude();
      this.timestamp = Misc.millitime();
   }

   public static StatusEffect newInstance(Attribute affectedAttr, StatusEffectType type) {
      return new StatusEffect(affectedAttr, type);
   }

   public Attribute getAttr() {
      return affectedAttr;
   }

   public void forceExpiration() {
      manualExpired = true;
   }

   public double getMagnitude() {
      return magnitude;
   }

   public StatusEffect setMagnitude(int magnitude) {
      this.magnitude = magnitude;
      return this;
   }

   public Player getInflicter() {
      return inflicter;
   }

   public StatusEffect setInflicter(Player inflicter) {
      this.inflicter = inflicter;
      return this;
   }

   public boolean isTemporary() {
      return temporary;
   }

   public StatusEffect setTemporary(boolean temporary) {
      this.temporary = temporary;
      return this;
   }

   public long getExprStamp() {
      return exprStamp;
   }

   public StatusEffect setExprStamp(long exprStamp) {
      this.exprStamp = exprStamp;
      return this;
   }

   public long getTimeLeft() {
      return (this.isTemporary()) ? this.exprStamp - Misc.millitime() : 1;
   }

   public boolean hasExpired() {
      return getTimeLeft() < 0 || manualExpired;
   }

   public void onset() {
      this.getAttr().modValue(-this.getMagnitude());
   }

   public void expire() {
      if (this.hasExpired()) {
         this.getAttr().modValue(this.getMagnitude());
         notifyExpired();
      }
   }

   public void addListener(StatusEffectListener listener) {
      listeners.add(listener);
   }

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
