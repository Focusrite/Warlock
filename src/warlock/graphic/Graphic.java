/**
 * File: warlock.graphic.Graphic.java
 *
 * An abstract graphic class that has methods for everything actually needed to draw the game. It's
 * extended in other classes that provides the actual code for doing the things needed.
 *
 * Why not just have one Graphic class? Good question, as of now it serves little purpose to have it
 * seperated as there's only one class extending this. This however allows for easier creation of an
 * alternative render engine other than OpenGL3 (with LWJGL) if needed in the future. Not all
 * computers support shaders which this game currently makes use of, extending another graphic engine
 * and do the displaying in say.. Java2D or OpenGL1 can solve this.
 *
 * It's currently not working fully to extend it like this, because a few parts elsewhere are written
 * to use only LWJGL (Main, for example - LWJGL used to create a display window), but fixing is
 * relatively straightforward. Some classes also feature a few relatively redundant features if it's
 * not in OpenGL though, such as camera knowing how to create a projection and view matrix, they are
 * fairly useless unless the engine of choice also need them.
 */
package warlock.graphic;

import org.newdawn.slick.opengl.Texture;
import warlock.camera.Camera;
import warlock.font.Font;

/**
 *
 * @author Focusrite
 */
public abstract class Graphic {
   private int screenWidth;
   private int screenHeight;
   private Camera camera;
   private boolean screenCoordinates = false;

   public Graphic(int screenWidth, int screenHeight) {
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
   }

   /**
    * @return the screen width
    */
   public int getScreenWidth() {
      return screenWidth;
   }

   /**
    * @param screenWidth new width
    */
   public void setScreenWidth(int screenWidth) {
      this.screenWidth = screenWidth;
   }

   /**
    * @return the screen height
    */
   public int getScreenHeight() {
      return screenHeight;
   }

   /**
    * @param screenHeight new height
    */
   public void setScreenHeight(int screenHeight) {
      this.screenHeight = screenHeight;
   }

   /**
    * @return the camera used for rendering
    */
   public Camera getCamera() {
      return camera;
   }

   /**
    * @param camera new camera to use when rendering
    */
   public void setCamera(Camera camera) {
      this.camera = camera;
   }

   /**
    * Set whether graphic coordinates are screen coordinates or in-world coordinates
    * @param screenCoordinates
    */
   public void setScreenCoordinates(boolean screenCoordinates) {
      this.screenCoordinates = screenCoordinates;
   }

   /**
    * @return whether screen coordinates or in-world coordinates are used
    */
   public boolean isScreenCoordinates() {
      return screenCoordinates;
   }

   public abstract void init();
   public abstract void resize();

   public abstract void preRender();

    public abstract void drawRectangle(int x, int y, int w, int h, double rot, Color color);
   public abstract void drawRectangle(int x, int y, int z, int w, int h, double rot, Color color);
   public abstract void drawUnfilledRectangle(int x, int y, int w, int h, int thickness, double rot, Color color);
   public abstract void drawUnfilledRectangle(int x, int y, int z, int w, int h, int thickness, double rot, Color color);
   public abstract void drawBorderedRectangle(int x, int y, int w, int h, int thickness, double rot, Color fill, Color border);
   public abstract void drawBorderedRectangle(int x, int y, int z, int w, int h, int thickness, double rot, Color fill, Color border);
   public abstract void drawCircle(int x, int y, int r, Color color);
   public abstract void drawCircle(int x, int y, int z, int r, Color color);
   public abstract void drawTriangle(int x, int y, int w, int h, double rot, Color color);
   public abstract void drawTriangle(int x, int y, int z, int w, int h, double rot, Color color);
   public abstract void drawText(String font, int x, int y, int z, String text, int size, Color color);
   public abstract void drawText(String font, int x, int y, int z, String text, int size, Color color, boolean centered);
   public abstract void drawCharacter(char c, Font f, int x, int y, int z, int size, Color color);
   public abstract void drawTexture(Texture texture, int x, int y, int z, int w, int h, double rot, Color tint);
   public abstract void drawTexture(Texture texture, int x, int y, int z, int w, int h, double rot);
   public abstract void drawTexture(Texture texture, int x, int y, int w, int h, double rot);
}
