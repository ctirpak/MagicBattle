package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * This class implements the {@link Listener} interface to handle the event
 * when a player dies within the game. It is specifically designed to manage
 * player deaths that occur within a MagicBattle arena, ensuring that the
 * player is properly removed from the arena upon death.
 */
public class PlayerDeath implements Listener {

	/**
	 * Handles the {@link PlayerDeathEvent}, which is triggered when a player dies.
	 *
	 * <p>This method first checks if the player who died was currently in a
	 * MagicBattle arena using the {@link ArenaManager#getArena(Player)} method.
	 * If the player was in an arena (meaning the method returns a non-null
	 * {@link com.github.ctirpak.magicbattle.Arena} object), the player is removed
	 * from that arena using the
	 * {@link com.github.ctirpak.magicbattle.Arena#removePlayer(Player)} method.
	 * This ensures that the arena's player count and state are updated correctly
	 * when a player is eliminated.</p>
	 *
	 * @param e The {@link PlayerDeathEvent} triggered when a player dies.
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		// Check if the dead player was in a MagicBattle arena.
		if (ArenaManager.getInstance().getArena(p) == null) return;
		// Remove the player from the arena upon death.
		ArenaManager.getInstance().getArena(p).removePlayer(p);
		// Optionally, you could add a message here to broadcast the death within the arena.
		// MessageManager.getInstance().broadcast(ArenaManager.getInstance().getArena(p), MessageType.INFO, p.getName() + " has been eliminated!");
	}

	/**
	 * Constructor for the {@code PlayerDeath} listener.
	 * When an instance of this class is created and registered as an event
	 * listener, its {@link #onPlayerDeath(PlayerDeathEvent)} method will be
	 * automatically called by the Bukkit server whenever a player dies. The
	 * logic within this method then handles the removal of the dead player
	 * from their MagicBattle arena, if they were participating in one.
	 */
	public PlayerDeath() {
		// No specific initialization needed for this listener.
	}
}