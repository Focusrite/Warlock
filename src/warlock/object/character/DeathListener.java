/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public interface DeathListener {
   public void notifyDeath(Player p, Player killer);
}
