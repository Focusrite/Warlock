package warlock.resource;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Focusrite
 */
public class ResourceManager implements Handle {

   private static Map<String, Texture> textures = new HashMap<>();
   private static final String resourceLocation = "resources/";

   public static void init() {
      //Load textures
      loadTextureResource("images/missing.png", "PNG", "missing");//Missing texture
      //Icons
      loadTextureResource("images/spell-fireball.png", "PNG", "spell-fireball");
      loadTextureResource("images/spell-teleport.png", "PNG", "spell-teleport");
      loadTextureResource("images/spell-lightning.png", "PNG", "spell-lightning");
      loadTextureResource("images/spell-orb.png", "PNG", "spell-orb");
      loadTextureResource("images/spell-shield.png", "PNG", "spell-shield");

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
         System.out.println("No texture of name [" + name + "]");
         return getTexture("missing");
      }
   }

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
