/**
 * File: warlock.spell.GameState.java
 *
 * An abstract class representing the state the game currently is in.
 *
 * This class implements a singleton pattern to keep track of the active state as there can only be
 * one state active at a time, the class's static instance. Changing state is done by changing the
 * instance to a subclass of GameState.
 *
 * These states can currently be one of the following:
 *  - MenuState  : The first menu
 *  - LobbyState : The pre-game state menu where the playerslots are assigned and settings changed.
 *  - PlayState  : The state where the actual game occurs. Owns a game that handles the gameplay
 *                 aspects.
 *
 * This class provides the main "frame" of methods that cascades down the hierarchy. Many of the lower
 * "ranked" classes delegates these functions (not true delegation as they are not instances of,
 * GameState bar from other states, but methods with same name). These methods of the active state
 * are ran in the main file once per frame, and passes parameters always relevant to their purpose.
 * These methods are:
 *  - update(dt)      : A method for updating things, a double is sent with with the delta time from
 *                      last call to this method.
 *  - render(Graphic) : A method for drawing things on the screen. Passes a Graphic object to allow
 *                      drawing onto the screen.
 *  - handleInput(..) : A method for handling input. Passes an InputHandler for checking keyboard and
 *                      mouse related things.
 *
 */
package warlock.state;

import org.lwjgl.opengl.Display;
import warlock.camera.Camera;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;

public abstract class GameState {
   private static GameState instance;
   private static boolean exitRequested = false;
   private Camera camera;

   /**
    * @return the game's camera
    */
   public Camera getCamera() {
      return camera;
   }

   /**
    * @param camera new camera
    */
   public void setCamera(Camera camera) {
      this.camera = camera;
   }

   /**
    * Set the singleton instance.
    * @param g
    * @return the newly set instance
    */
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

   /**
    * Flag for exitting
    */
   public void exit() {
      exitRequested = true;
   }

   /**
    * @return whether exit has been requested
    */
   public boolean isExitRequested() {
      return exitRequested;
   }

   /**
    * @return the singleton instance
    */
   public static GameState getInstance() {
      return GameState.instance;
   }

   public abstract void handleInput(InputHandler input);

   public abstract void init();

   public abstract void update(double dt);

   public abstract void render(Graphic g);

   public abstract void destroy();
}
