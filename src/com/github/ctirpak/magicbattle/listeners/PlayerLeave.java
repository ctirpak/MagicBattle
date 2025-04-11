package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * This class implements the {@link Listener} interface to handle the event
 * when a player leaves the server (quits). It is designed to ensure that if
 * a player was participating in a MagicBattle arena when they quit, they are
 * properly removed from that arena to maintain the integrity of the game.
 */
public class PlayerLeave implements Listener {

	/**
	 * Handles the {@link PlayerQuitEvent}, which is triggered when a player
	 * disconnects from the server.
	 *
	 * <p>This method checks if the player who quit was currently in a
	 * MagicBattle arena using the {@link ArenaManager#getArena(Player)} method.
	 * If the player was in an arena (meaning the method returns a non-null
	 * {@link com.github.ctirpak.magicbattle.Arena} object), the player is
	 * removed from that arena using the
	 * {@link com.github.ctirpak.magicbattle.Arena#removePlayer(Player)} method.
	 * A message indicating that the player has left the arena is also sent to
	 * the remaining players in that arena.</p>
	 *
	 * @param e The {@link PlayerQuitEvent} triggered when a player leaves the server.
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		// Check if the leaving player was in a MagicBattle arena.
		if (ArenaManager.getInstance().getArena(p) != null) {
			// Remove the player from the arena.
			ArenaManager.getInstance().getArena(p).removePlayer(p);
			// Send a message to the remaining players in the arena.
			MessageManager.getInstance().broadcast(MessageType.INFO, p.getName() + " has left the arena.");
		}
	}

	/**
	 * Constructor for the {@code PlayerLeave} listener.
	 * When an instance of this class is created and registered as an event
	 * listener, its {@link #onPlayerQuit(PlayerQuitEvent)} method will be
	 * automatically called by the Bukkit server whenever a player leaves the
	 * server. The logic within this method then handles the removal of the
	 * player from their MagicBattle arena, if they were participating in one.
	 */
	public PlayerLeave() {
		// No specific initialization needed for this listener.
	}
}