/**
 * File: warlock.Main.java
 *
 * The main class of Warlock, runs the game loop and setup of various handlers and similar. Also
 * creates the main window.
 */
package warlock;


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import warlock.attribute.AttributeHandler;
import warlock.font.FontHandler;
import warlock.graphic.Graphic;
import warlock.graphic.OpenGL3Graphic;
import warlock.input.InputHandler;
import warlock.resource.ResourceHandler;
import warlock.shop.ShopHandler;
import warlock.state.GameState;
import warlock.state.MenuState;
import warlock.time.Time;

public class Main {

   public static int DISPLAY_WIDTH = 800;
   public static int DISPLAY_HEIGHT = 600;
   public static int FPS = 60;
   public static String HEADER_NAME = "Warlock";
   private long lastUpdate;
   private long lastFPS;
   private int fps = 0;
   public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
   //Handlers and similar
   private Graphic graphic;
   private InputHandler input;

   static { //Create logger
      try {
         LOGGER.addHandler(new FileHandler("errors.log", true));
      }
      catch (IOException ex) {
         LOGGER.log(Level.WARNING, ex.toString(), ex);
      }
   }

   /**
    * The main of the project, takes care of pringing to log file.
    * @param args
    */
   public static void main(String[] args) {
      Main main = null;
      try {

         main = new Main();
         main.create();
         main.run();
      }
      catch (Exception ex) {
         LOGGER.log(Level.SEVERE, ex.toString(), ex);
      }
      finally {
         if (main != null) {
            main.destroy();
         }
      }
   }

   public Main() {
      //dostuff
      lastUpdate = Time.millitime();
      lastFPS = lastUpdate;
   }

   /**
    * Creates a display window for use and sets up graphic and input handlers.
    * @throws LWJGLException
    */
   public void create() throws LWJGLException {
      //Display
      Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
      Display.setVSyncEnabled(true);

      Display.setFullscreen(false);
      Display.create();

      //Handles
      init();
      resize();

      //Game
      GameState.setInstance(new MenuState());
      graphic.setCamera(GameState.getInstance().getCamera());
   }

   /**
    * Destructor cleaning up OpenGL things.
    */
   public void destroy() {
      //Methods already check if created before destroying.
      Mouse.destroy();
      Keyboard.destroy();
      Display.destroy();
   }

   /**
    * Register handles and initialize them.
    */
   public void init() throws LWJGLException {
      //Input
      Keyboard.create();
      Mouse.create();
      input = new InputHandler();
      //Graphic
      graphic = new OpenGL3Graphic(DISPLAY_WIDTH, DISPLAY_HEIGHT);
      graphic.init();

      //Init static handles
      HandleLoader.register(ResourceHandler.class);
      HandleLoader.register(AttributeHandler.class);
      HandleLoader.register(FontHandler.class);
      HandleLoader.register(ShopHandler.class);
      HandleLoader.initAll();
   }

   /**
    * Calculate the FPS and set it in the title bar
    */
   public void updateFPS() {
      if (Time.millitime() - lastFPS > 1000) {
         Display.setTitle( HEADER_NAME + " [FPS: " + fps + "]" );
         fps = 0; //reset the FPS counter
         lastFPS += 1000; //add one second
      }
      fps++;
   }

   /**
    * The handleInput method that's cascaded down the hierarchy to other classes.
    * @param dt
    */
   public void handleInput(double dt) {
      input.readKeyboard(); //Update status of keys for every keypress
      GameState.getInstance().handleInput(input);
   }

   /**
    * The render method that's cascaded down the hierarchy tree to other classes.
    */
   public void render() {
      graphic.preRender();
      //Draw stuff!
      GameState.getInstance().render(graphic);
   }

   /**
    * Resize of the window is done. Need updating to cascade down the hierarchy tree to allow for
    * different sizes of windows, which currently is not supported.
    */
   public void resize() {
      graphic.resize();
   }

   /**
    * The main run loop. Terminates when game state finishes or OS sends close request.
    */
   public void run() {
      while (!Display.isCloseRequested() && !GameState.getInstance().isExitRequested()) {
         if (Display.isVisible()) {
            update();
            render();
         }
         else { //Less intensive render when display is not in focus to preserve system resources
            if (Display.isDirty()) {
               render();
            }
            try {
               Thread.sleep(100);
            }
            catch (InterruptedException ex) {
            }
         }
         Display.update();
         Display.sync(FPS); //Aim to sync to FPS
      }
   }

   /**
    * The update method that's ran once every frame. Cascaded down to other classes for handling their
    * own update methods.
    */
   public void update() {
      updateFPS();
      long curTime = Time.millitime();
      double dt = Time.secondsBetween(lastUpdate, curTime);
      lastUpdate = curTime;
      GameState.getInstance().update(dt);
      handleInput(dt);
   }
}