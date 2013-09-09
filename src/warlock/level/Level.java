/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.level;

import java.util.ArrayList;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.object.LevelObject;
import warlock.object.character.Hero;

/**
 *
 * @author Focusrite
 */
public class Level {

   private ArrayList<LevelObject> objects = new ArrayList<LevelObject>();
   private LevelObject player;

   public void handleInput(double dt, InputHandler input) {
      player.handleInput(dt, input);
   }

   public void init() {
      player = new Hero();
      addObject(player);
   }

   public void addObject(LevelObject o) {
      objects.add(o);
   }

   public void update(double dt) {
      for (LevelObject o : objects) {
         o.update(dt);
      }
   }

   public void render(Graphic g) {
      for (LevelObject o : objects) {
         o.render(g);
      }
   }
}
