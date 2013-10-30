/**
 * File: warlock.attribute.AttributeHandler.java
 *
 * Loads all attribute types that a warlock can have.
 */
package warlock.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import warlock.Handle;

public class AttributeHandler implements Handle {

   private static Map<String, AttributeType> typeMap = new HashMap<>(); //For get
   private static ArrayList<AttributeType> typeList = new ArrayList<>();//For iterator

   /**
    * Returns the attribute with the tag.
    *
    * @param tag tag of the attribute
    * @return AttributeType if found, else null
    */
   public static AttributeType get(String tag) {
      return typeMap.get(tag);
   }

   /**
    * Returns an iterator with all attributeTypes loaded.
    * @return ListIterator<AttributeType>
    */
   public static ListIterator<AttributeType> iterator() {
      return typeList.listIterator();
   }

   /**
    * Add a new attributeType to the handler.
    *
    * @param type AttributeType
    */
   public static void add(AttributeType type) {
      typeMap.put(type.getTag(), type);
      typeList.add(type);
   }

   /**
    * The initialization method ran by HandleLoader
    */
   public static void init() {
      add(new AttributeType("hp", "Hitpoints",
         "Your hero's health. When this value drops bellow zero your character gets knocked out.", 100));
      add(new AttributeType("ms", "Movementspeed",
         "Your hero's movementspeed. This is the top speed you can move at.", 100));
      add(new AttributeType("tr", "Turnrate",
         "Your hero's turnrate. Higher value makes you turn faster, helps for dodging projectiles.", 5));
      add(new AttributeType("shield", "Shield",
         "Shield, reflects incoming missiles", 0));
      add(new AttributeType("stun", "Stunned",
         "You are stunned", 0));
   }
}
