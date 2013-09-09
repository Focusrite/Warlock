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
   private int baseValue;  //Base
   private int value;      //Current

   public Attribute(AttributeType attrType, int value) {
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

   public int getBaseValue() {
      return baseValue;
   }

   public void setBaseValue(int baseValue) {
      this.baseValue = baseValue;
   }

   public int getValue() {
      return value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public void modValue(int dv){
      this.value = this.value + dv;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 89 * hash + Objects.hashCode(this.attrType);
      hash = 89 * hash + this.baseValue;
      hash = 89 * hash + this.value;
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
