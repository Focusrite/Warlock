/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.state;

import org.lwjgl.opengl.Display;
import warlock.camera.Camera;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;

/**
 * Abstract GameState class implementing a singelton pattern.
 *
 * @author Focusrite
 */
public abstract class GameState {
   private static GameState instance;
   private static boolean exitRequested = false;
   private Camera camera;

   public Camera getCamera() {
      return camera;
   }

   public void setCamera(Camera camera) {
      this.camera = camera;
   }

   public static GameState setInstance(GameState g) {
      Camera cam;
      if (instance != null) {
         getInstance().destroy();
         cam = getInstance().getCamera();
      }
      else {
         cam = new Camera(0,0, Display.getWidth(), Display.getHeight());
      }
      GameState.instance = g;
      g.setCamera(cam);
      g.init();
      return g;
   }

   public void exit() {
      exitRequested = true;
   }

   public boolean isExitRequested() {
      return exitRequested;
   }

   public static GameState getInstance() {
      return GameState.instance;
   }

   public abstract void handleInput(InputHandler input);

   public abstract void init();

   public abstract void update(double dt);

   public abstract void render(Graphic g);

   public abstract void destroy();
}
