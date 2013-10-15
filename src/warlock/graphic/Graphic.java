/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   public int getScreenWidth() {
      return screenWidth;
   }

   public void setScreenWidth(int screenWidth) {
      this.screenWidth = screenWidth;
   }

   public int getScreenHeight() {
      return screenHeight;
   }

   public void setScreenHeight(int screenHeight) {
      this.screenHeight = screenHeight;
   }

   public Camera getCamera() {
      return camera;
   }

   public void setCamera(Camera camera) {
      this.camera = camera;
   }

   public void setScreenCoordinates(boolean screenCoordinates) {
      this.screenCoordinates = screenCoordinates;
   }

   public boolean isScreenCoordinates() {
      return screenCoordinates;
   }

   public abstract void init();
   public abstract void resize();

   public abstract void preRender();
   public abstract void postRender();

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
