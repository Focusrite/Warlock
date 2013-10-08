/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.resource;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Focusrite
 */
public class ResourceManager {

   private static Map<String, Texture> textures = new HashMap<>();
   private static final String resourceLocation = "resources/";

   private static InputStream getInputStream(String resource) {
      try {
         return ResourceManager.class.getClassLoader().getResourceAsStream(resourceLocation + resource);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Couldn't retrieve resource [" + resource + "]", e);
      }
   }

   public static String loadTextResource(String resource) {
      StringBuilder result = new StringBuilder();
      String line = null;
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(resource), "UTF-8"));
         while ((line = reader.readLine()) != null) {
            result.append(line);
            result.append('\n');
         }
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Unable to load text from file [" + resource + "]", e);
      }

      return result.toString();
   }

   public static Font loadFontResource(String resource) {
      InputStream inputStream = getInputStream(resource);
      try {
         return Font.createFont(Font.TRUETYPE_FONT, inputStream);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Unable to load font from file [" + resource + "]", e);
      }
   }

   public static void loadTextureResource(String resource, String type, String storeName) {
      Texture textureResource;
      try {
         textureResource = TextureLoader.getTexture(type, getInputStream(resource));
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Unable to load texture from file [" + resource + ", of type " + type + "]", e);
      }
      textures.put(storeName, textureResource);
   }

   public static Texture getTexture(String name) {
      try {

         return textures.get(name);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("No texture of name [" + name + "]", e);
      }
   }
}
