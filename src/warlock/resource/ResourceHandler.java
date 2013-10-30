/**
 * File: warlock.resource.ResourceHandler.java
 *
 * A handle which handles reading of files and parsing them to useful things. Also tends to the actual
 * storage of textures and loading every texture from initialization.
 *
 * The actual texture loading uses a utility library called "Slick-Util", because writing a file parser
 * for a image type seemed overkill for this project.
 *
 */

package warlock.resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.w3c.dom.Document;
import warlock.Handle;

public class ResourceHandler implements Handle {

   private static Map<String, Texture> textures = new HashMap<>();
   private static final String resourceLocation = "resources/";

   /**
    * Load all resources needed for the game.
    */
   public static void init() {
      //Load textures
      loadTextureResource("images/missing.png", "PNG", "missing");//Missing texture
      //Icons
      loadTextureResource("images/spell-fireball.png", "PNG", "spell-fireball");
      loadTextureResource("images/spell-teleport.png", "PNG", "spell-teleport");
      loadTextureResource("images/spell-lightning.png", "PNG", "spell-lightning");
      loadTextureResource("images/spell-orb.png", "PNG", "spell-orb");
      loadTextureResource("images/spell-shield.png", "PNG", "spell-shield");
      loadTextureResource("images/spell-explosion.png", "PNG", "spell-explosion");

      //FX
      loadTextureResource("images/fx-shield.png", "PNG", "fx-shield");

      //UI
      loadTextureResource("images/ui-hpbar.png", "PNG", "ui-hpbar");
      loadTextureResource("images/ui-buy.png", "PNG", "ui-buy");
      loadTextureResource("images/ui-play.png", "PNG", "ui-play");
      loadTextureResource("images/ui-exit.png", "PNG", "ui-exit");
      loadTextureResource("images/ui-logo.png", "PNG", "ui-logo");
      loadTextureResource("images/ui-decrease.png", "PNG", "ui-decrease");
      loadTextureResource("images/ui-increase.png", "PNG", "ui-increase");
      loadTextureResource("images/ui-join.png", "PNG", "ui-join");
      loadTextureResource("images/ui-computer.png", "PNG", "ui-computer");
      loadTextureResource("images/ui-remove.png", "PNG", "ui-remove");
      loadTextureResource("images/ui-quit.png", "PNG", "ui-quit");
   }

   /**
    * Gets an input stream for a resource.
    * @param resource
    * @return the input stream
    */
   private static InputStream getInputStream(String resource) {
      try {
         return ResourceHandler.class.getClassLoader().getResourceAsStream(resourceLocation + resource);
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Couldn't retrieve resource [" + resource + "]", e);
      }
   }

   /**
    * Loads a resource as text
    * @param resource
    * @return the text as a string
    */
   public static String loadTextResource(String resource) {
      StringBuilder result = new StringBuilder();
      String line;
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

   /**
    * Load a texture resource and store it for retrieval later.
    * @param resource path to the resource
    * @param type PNG, TGA, etc - but only currently tested with PNG
    * @param storeName what the texture is stored as
    */
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

   /**
    * @param name
    * @return the texture or the "missing" placeholder texture if invalid texture
    */
   public static Texture getTexture(String name) {
      try {
         return textures.get(name);
      }
      catch (Exception e) {
         System.out.println("No texture of name [" + name + "]");
         return getTexture("missing");
      }
   }

   /**
    * Load a resorce as a xml document
    * @param resource path
    * @return xml Document
    */
   public static Document loadXMLResource(String resource) {
      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(getInputStream(resource));
         doc.normalize();
         return doc;
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Couldn't parse file [" + resource + "]", e);
      }
   }
}
