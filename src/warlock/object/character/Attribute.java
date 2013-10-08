/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

import java.util.Objects;

/**
 *
 * @author Focusrite
 */
public class Attribute {
   private AttributeType attrType;
   private double baseValue;  //Base
   private double value;      //Current

   public Attribute(AttributeType attrType, double value) {
      this.attrType = attrType;
      this.baseValue = value;
      this.value = value;
   }

   public AttributeType getAttributeType() {
      return attrType;
   }

   public String getAttributeTag() {
      return getAttributeType().getTag();
   }

   public double getBaseValue() {
      return baseValue;
   }

   public void setBaseValue(int baseValue) {
      this.baseValue = baseValue;
   }

   public double getValue() {
      return value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public void modValue(double dv){
      this.value = this.value + dv;
   }

   public Attribute(AttributeType attrType, double baseValue, double value) {
      this.attrType = attrType;
      this.baseValue = baseValue;
      this.value = value;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final Attribute other = (Attribute) obj;
      if (!Objects.equals(this.attrType, other.attrType)) {
         return false;
      }
      if (this.baseValue != other.baseValue) {
         return false;
      }
      if (this.value != other.value) {
         return false;
      }
      return true;
   }


}
