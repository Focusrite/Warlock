/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.state.GameState;
import warlock.state.PlayState;
import warlock.time.Misc;


public class Main {
   public static int DISPLAY_WIDTH = 800;
   public static int DISPLAY_HEIGHT = 600;
   public static String HEADER_NAME = "Warlock";

   private long lastUpdate;

   public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

   //Handlers and similar
   private Graphic graphic;
   private InputHandler input;

   static {
      try {
         LOGGER.addHandler(new FileHandler("errors.log", true));
      } catch (IOException ex) {
         LOGGER.log(Level.WARNING, ex.toString(), ex);
      }
   }

   public static void main(String[] args) {
      Main main = null;
      try {

         main = new Main();
         main.create();
         main.run();
      } catch (Exception ex) {
         LOGGER.log(Level.SEVERE, ex.toString(), ex);
      } finally {
         if (main != null) {
            main.destroy();
         }
      }
   }

   public Main() {
      //dostuff
      lastUpdate = Misc.millitime();
   }

   public void create() throws LWJGLException {
      //Display
      Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));

      Display.setFullscreen(false);
      Display.setTitle(HEADER_NAME);
      Display.create();

      //Input
      Keyboard.create();
      Mouse.create();
      input = new InputHandler();

      //OpenGL
      initGL();
      resizeGL();

      //Graphic
      graphic = new Graphic();

      //Game
      GameState.setInstance(new PlayState());
   }

   public void destroy() {
      //Methods already check if created before destroying.
      Mouse.destroy();
      Keyboard.destroy();
      Display.destroy();
   }

   public void initGL() {
      //2D Initialization
      glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
      glDisable(GL_DEPTH_TEST);
      glDisable(GL_LIGHTING);
   }

   public void handleInput(double dt) {
      input.readKeyboard();
      GameState.getInstance().handleInput(dt, input);
      input.clean();
   }

   public void render() {
      glClear(GL_COLOR_BUFFER_BIT);
      glLoadIdentity();

      //Draw stuff!
      GameState.getInstance().render(graphic);
   }

   public void resizeGL() {
      //2D Scene
      glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);

      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      gluOrtho2D(0.0f, DISPLAY_WIDTH, 0.0f, DISPLAY_HEIGHT);
      glPushMatrix();

      glMatrixMode(GL_MODELVIEW);
      glLoadIdentity();
      glPushMatrix();
   }

   public void run() {
      while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
         if (Display.isVisible()) {
            update();
            render();
         } else {
            if (Display.isDirty()) {
               render();
            }
            try {
               Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
         }
         Display.update();
         Display.sync(60);
      }
   }

   public void update() {
      double dt = Misc.secondsBetween(lastUpdate, Misc.millitime());
      handleInput(dt);
      GameState.getInstance().update(dt);
   }
}