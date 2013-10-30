/**
 * File: warlock.player.Player.java
 *
 * A player participating in the game. Contains info relevant to the player such as gold available,
 * what warlock he is controlling, etc. Also provides methods for actually handling these things,
 * for example actually control the warlock he owns.
 */
package warlock.player;

import org.lwjgl.input.Keyboard;
import warlock.camera.Camera;
import warlock.graphic.Color;
import warlock.input.InputHandler;
import warlock.level.Level;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;
import warlock.spell.SpellShortcut;

public class Player implements Comparable<Player> {
   public static final Color[] PRIMARY_COLORS = {
      Color.RED,
      Color.BLUE,
      Color.GREEN,
      Color.YELLOW,
      Color.PURPLE
   };
   public static final Color[] SECONDARY_COLORS = {
      Color.WINE_RED,
      Color.SKY_BLUE,
      Color.GREENISH,
      Color.ORANGE,
      Color.PINK
   };

   private int playerId;
   private int gold;
   private int score;
   private int killingblows;
   private String name;
   private double scrollSpeed = 300.0f;
   private Warlock warlock;
   private Color primaryColor;
   private Color secondaryColor;

   /**
    * Create a new playing player
    * @param playerId
    * @param primary
    * @param secondary
    */
   public Player(int playerId, Color primary, Color secondary) {
      this.warlock = new Warlock();
      this.playerId = playerId;
      this.primaryColor = primary;
      this.secondaryColor = secondary;
      this.name = "Player " + playerId;
   }

   /**
    * @return the name of the player
    */
   public String getName() {
      return name;
   }

   /**
    * @param name new name
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the primary color
    */
   public Color getPrimaryColor() {
      return primaryColor;
   }

   /**
    * @param primaryColor new primary color
    */
   public void setPrimaryColor(Color primaryColor) {
      this.primaryColor = primaryColor;
   }

   /**
    * @return secondary color
    */
   public Color getSecondaryColor() {
      return secondaryColor;
   }

   /**
    * @param secondaryColor new secondary color
    */
   public void setSecondaryColor(Color secondaryColor) {
      this.secondaryColor = secondaryColor;
   }

   /**
    * @return the amount of killing blows this player has done
    */
   public int getKillingblows() {
      return killingblows;
   }

   /**
    * @param killingblows new amount of killing blows
    */
   public void setKillingblows(int killingblows) {
      this.killingblows = killingblows;
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

   /**
    * @return the player id in the game/lobby
    */
   public int getPlayerId() {
      return playerId;
   }

   /**
    * @return the score
    */
   public int getScore() {
      return score;
   }

   /**
    * @param dScore amount to add to current score
    */
   public void modifyScore(int dScore) {
      this.score += dScore;
   }

   /**
    * @param score new score value
    */
   public void setScore(int score) {
      this.score = score;
   }

   /**
    * @return current gold
    */
   public int getGold() {
      return gold;
   }

   /**
    * @param gold new current gold
    */
   public void setGold(int gold) {
      this.gold = gold;
   }

   /**
    * @param dGold amount to add to gold
    */
   public void modifyGold(int dGold) {
      this.gold += dGold;
   }

   /**
    * @return this player's warlock
    */
   public Warlock getWarlock() {
      return warlock;
   }

   /**
    * @param warlock new warlock for this player
    */
   public void setWarlock(Warlock warlock) {
      this.warlock = warlock;
   }

   /**
    * Proxy
    * @return the level the warlock is currently on
    */
   public Level getLevel() {
      return getWarlock().getLevel();
   }

   /**
    * Proxy
    * @param x
    * @param y
    * @return the in-world coordinates of a (x,y) window coordinate
    */
   private Vector windowToLevelXY(int x, int y) {
      return getLevel().windowToLevelXY(x, y);
   }

   /**
    * Handle the scrolling of the camera for this player
    * @param input
    */
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

   /**
    * Handle the input, it takes care of scrolling the map and cast spells
    * @param input
    */
   public void handleInput(InputHandler input) {
      handleScrolling(input);

      if (input.isMouseDown(0)) {
         getWarlock().castSpell(SpellShortcut.MB, windowToLevelXY(input.getMouseX(), input.getMouseY()));
      }
      if (input.isMouseDown(1)) {
         getWarlock().setMoveTo(windowToLevelXY(input.getMouseX(), input.getMouseY()));
      }
      if (input.keyHeld(Keyboard.KEY_Q)) {
         getWarlock().castSpell(SpellShortcut.Q, windowToLevelXY(input.getMouseX(), input.getMouseY()));
      }
      if (input.keyHeld(Keyboard.KEY_E)) {
         getWarlock().castSpell(SpellShortcut.E, windowToLevelXY(input.getMouseX(), input.getMouseY()));
      }
      if (input.keyHeld(Keyboard.KEY_R)) {
         getWarlock().castSpell(SpellShortcut.R, windowToLevelXY(input.getMouseX(), input.getMouseY()));
      }
      if (input.keyHeld(Keyboard.KEY_SPACE)) {
         getWarlock().castSpell(SpellShortcut.SPC, windowToLevelXY(input.getMouseX(), input.getMouseY()));
      }
   }

   public void update(double dt) {
   }

   /**
    * Compares the score of two players
    * @param t
    * @return boolean true if this is bigger
    */
   @Override
   public int compareTo(Player t) {
      return t.getScore() - getScore();
   }

   /**
    * @return name of the player
    */
   @Override
   public String toString() {
      return name;
   }
}
