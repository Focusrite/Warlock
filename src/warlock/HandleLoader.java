/**
 * File: warlock.HandleLoader.java
 *
 * Automatically loads handles that has been registered to this loader. All handles must implement
 * the interface "Handle" to use this functionality.
 *
 * Implementation of this was inspiered by PHP's autoloader capacity, a nifty way of taking care of
 * static Handles.
 */
package warlock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class HandleLoader {
   private static ArrayList<Class> handles = new ArrayList<>();

   /**
    * Initializes all handles that are registered to this loader. Running it mutliple times will
    * re-initialize handles.
    */
   public static void initAll() {
      Iterator<Class> iter = handles.listIterator();
      while (iter.hasNext()) {
         Class c = iter.next();
         Method m = getMethod(c, "init", null);
         try {
            m.invoke(null);
         }
         catch (Exception e) {
            System.out.println(e.getCause().toString());
            e.getCause().printStackTrace();
         }
      }
   }

   /**
    * Register a class to this loader to be initialized with the initAll method.
    *
    * @param c Class
    */
   public static void register(Class c) {
      if (getMethod(c, "init", null) != null && Handle.class.isAssignableFrom(c)) {
         handles.add(c);
      }
   }

   /**
    * Private helper method for retrieving a invokable Method.
    *
    * @param c
    * @param name
    * @param parameterTypes
    * @return Method if found, else null
    */
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
