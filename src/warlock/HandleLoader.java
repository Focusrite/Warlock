/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import warlock.object.character.AttributeHandler;

/**
 *
 * @author Focusrite
 */
public class HandleLoader {
   //Since static methods in interfaces aren't available in java7

   private static ArrayList<Class> handles = new ArrayList<>();

   public static void register() {
      //Order is relevant
      register(AttributeHandler.class);
   }

   public static void initAll() {
      Iterator<Class> iter = handles.listIterator();
      while (iter.hasNext()) {
         Class c = iter.next();
         Method m = getMethod(c, "init", null);
         try {
            m.invoke(null);
         }
         catch (Exception e) {
            System.out.println(e.toString());
         }
      }
   }

   public static void register(Class c) {
      if (getMethod(c, "init", null) != null && Handle.class.isAssignableFrom(c)) {
         handles.add(c);
      }
   }

   private static Method getMethod(Class c, String name, Class<?>... parameterTypes) {
      try {
         return c.getMethod(name, parameterTypes);
      }
      catch (NoSuchMethodException e) {
         e.printStackTrace();
      }
      catch (SecurityException e) {
         e.printStackTrace();
      }
      return null;
   }
}
