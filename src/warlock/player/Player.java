/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.player;

import org.lwjgl.input.Keyboard;
import warlock.camera.Camera;
import warlock.graphic.Color;
import warlock.input.InputHandler;
import warlock.level.Level;
import warlock.object.character.Warlock;
import warlock.phys.Vector;
import warlock.spell.SpellShortcut;

/**
 *
 * @author Focusrite
 */
public class Player implements Comparable<Player> {

   private int playerId;
   private int gold = 10;
   private int score = 0;
   private double scrollSpeed = 300.0f;
   private Warlock warlock;
   private Color primaryColor;
   private Color secondaryColor;

   public Player(int playerId, Color primary, Color secondary) {
      this.warlock = new Warlock();
      this.playerId = playerId;
      this.primaryColor = primary;
      this.secondaryColor = secondary;
   }

   public Color getPrimaryColor() {
      return primaryColor;
   }

   public void setPrimaryColor(Color primaryColor) {
      this.primaryColor = primaryColor;
   }

   public Color getSecondaryColor() {
      return secondaryColor;
   }

   public void setSecondaryColor(Color secondaryColor) {
      this.secondaryColor = secondaryColor;
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 83 * hash + this.playerId;
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final Player other = (Player) obj;
      if (this.playerId != other.playerId) {
         return false;
      }
      return true;
   }

   public int getPlayerId() {
      return playerId;
   }

   public int getScore() {
      return score;
   }

   public void modifyScore(int dScore) {
      this.score += dScore;
   }

   public void setScore(int score) {
      this.score = score;
   }

   public int getGold() {
      return gold;
   }

   public void setGold(int gold) {
      this.gold = gold;
   }

   public void modifyGold(int dGold) {
      this.gold += dGold;
   }

   public Warlock getWarlock() {
      return warlock;
   }

   public void setWarlock(Warlock warlock) {
      this.warlock = warlock;
   }

   //Proxy
   public Level getLevel() {
      return getWarlock().getLevel();
   }

   //Proxy
   private Vector windowToLevelXY(int x, int y) {
      return getLevel().windowToLevelXY(x, y);
   }

   public void handleScrolling(InputHandler input) {
      int mouseX = input.getMouseX();
      if (mouseX < Camera.SCROLL_ZONE_SIZE && mouseX > 0 || input.keyHeld(Keyboard.KEY_A)) {
         getLevel().setScrollX(-scrollSpeed);
      }
      else if (input.windowWidth() - input.getMouseX() < Camera.SCROLL_ZONE_SIZE && mouseX > 0
         || input.keyHeld(Keyboard.KEY_D)) {
         getLevel().setScrollX(scrollSpeed);
      }
      else {
         getLevel().setScrollX(0);
      }

      int mouseY = input.getMouseY();
      if (mouseY < Camera.SCROLL_ZONE_SIZE && mouseY > 0 || input.keyHeld(Keyboard.KEY_S)) {
         getLevel().setScrollY(-scrollSpeed);
      }
      else if (input.windowHeight() - input.getMouseY() < Camera.SCROLL_ZONE_SIZE && mouseY > 0
         || input.keyHeld(Keyboard.KEY_W)) {
         getLevel().setScrollY(scrollSpeed);
      }
      else {
         getLevel().setScrollY(0);
      }
   }

   public void handleInput(InputHandler input) {
      handleScrolling(input);

      if (input.isMouseDown(0)) {
         Vector direction = windowToLevelXY(input.getMouseX(), input.getMouseY()).
            subtract(getWarlock().getPosition()); //the directional vector from position to mouseclick
         getWarlock().castSpell(SpellShortcut.MB, direction.getAngle());
      }
      if (input.isMouseDown(1)) {
         getWarlock().setMoveTo(windowToLevelXY(input.getMouseX(), input.getMouseY()));
      }
   }

   public void update(double dt) {
   }

   @Override
   public int compareTo(Player t) {
      return getScore() - t.getScore();
   }
}
