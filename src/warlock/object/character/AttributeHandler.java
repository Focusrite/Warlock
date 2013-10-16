/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import warlock.Handle;

/**
 *
 * @author Focusrite
 */
public class AttributeHandler implements Handle {

   private static Map<String, AttributeType> typeMap = new HashMap<>();
   private static ArrayList<AttributeType> typeList = new ArrayList<>();

   public static AttributeType get(String s) {
      return typeMap.get(s);
   }

   public static ListIterator iterator() {
      return typeList.listIterator();
   }

   public static void add(AttributeType type) {
      typeMap.put(type.getTag(), type);
      typeList.add(type);
   }

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
