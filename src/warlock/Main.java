/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import warlock.font.FontHandler;
import warlock.graphic.Graphic;
import warlock.graphic.OpenGL3Graphics;
import warlock.input.InputHandler;
import warlock.object.character.AttributeHandler;
import warlock.resource.ResourceManager;
import warlock.shop.ShopHandler;
import warlock.state.GameState;
import warlock.state.MenuState;
import warlock.time.Misc;

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

   static {
      try {
         LOGGER.addHandler(new FileHandler("errors.log", true));
      }
      catch (IOException ex) {
         LOGGER.log(Level.WARNING, ex.toString(), ex);
      }
   }

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
      lastUpdate = Misc.millitime();
      lastFPS = lastUpdate;
   }

   public void create() throws LWJGLException {
      //Display
      Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
      Display.setVSyncEnabled(true);

      Display.setFullscreen(false);
      Display.create();

      //Input
      Keyboard.create();
      Mouse.create();
      input = new InputHandler();

      //Graphic
      graphic = new OpenGL3Graphics(DISPLAY_WIDTH, DISPLAY_HEIGHT);

      //OpenGL
      init();
      resize();

      //Game
      GameState.setInstance(new MenuState());
      graphic.setCamera(GameState.getInstance().getCamera());
   }

   public void destroy() {
      //Methods already check if created before destroying.
      Mouse.destroy();
      Keyboard.destroy();
      Display.destroy();
   }

   public void init() {
      //Init handlers
      HandleLoader.register(ResourceManager.class);
      HandleLoader.register(AttributeHandler.class);
      HandleLoader.register(FontHandler.class);
      HandleLoader.register(ShopHandler.class);
      HandleLoader.initAll();
      graphic.init();

   }

   /**
    * Calculate the FPS and set it in the title bar
    */
   public void updateFPS() {
      if (Misc.millitime() - lastFPS > 1000) {
         Display.setTitle( HEADER_NAME + " [FPS: " + fps + "]" );
         fps = 0; //reset the FPS counter
         lastFPS += 1000; //add one second
      }
      fps++;
   }

   public void handleInput(double dt) {
      input.readKeyboard(); //Update status of keys for every keypress
      GameState.getInstance().handleInput(input);
   }

   public void render() {
      graphic.preRender();
      //Draw stuff!
      GameState.getInstance().render(graphic);
   }

   public void resize() {
      graphic.resize();
   }

   public void run() {
      while (!Display.isCloseRequested() && !GameState.getInstance().isExitRequested()) {
         if (Display.isVisible()) {
            update();
            render();
         }
         else {
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
         Display.sync(FPS);
      }
   }

   public void update() {
      updateFPS();
      long curTime = Misc.millitime();
      double dt = Misc.secondsBetween(lastUpdate, curTime);
      lastUpdate = curTime;
      GameState.getInstance().update(dt);
      handleInput(dt);
   }
}