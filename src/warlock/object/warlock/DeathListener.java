/**
 * File: warlock.object.warlock.DeathListener.java
 *
 * A listener fired when a Warlock dies with the last damage inflicted and what player's character
 * died.
 */
package warlock.object.warlock;

import warlock.player.Player;

public interface DeathListener {
   public void notifyDeath(Player p, Player killer);
}
