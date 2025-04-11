package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.github.ctirpak.magicbattle.ArenaManager;

/**
 * This class implements the {@link Listener} interface to prevent players
 * who are currently participating in a MagicBattle arena from losing hunger.
 * This ensures a more consistent gameplay experience within the arena by
 * negating the effects of the server's natural hunger mechanics.
 */
public class PlayerLoseHunger implements Listener {

	/**
	 * Handles the {@link FoodLevelChangeEvent}, which is triggered when a player's
	 * food level changes (typically decreases due to activity).
	 *
	 * <p>This method first checks if the entity whose food level is changing is a
	 * {@link Player}. If it is, it then checks if that player is currently inside
	 * a MagicBattle arena using the {@link ArenaManager#getArena(Player)} method.
	 * If the player is indeed in an arena (meaning the method returns a non-null
	 * {@link com.github.ctirpak.magicbattle.Arena} object), the event is cancelled
	 * using {@code e.setCancelled(true)}. This prevents the player's food level
	 * from decreasing while they are participating in a MagicBattle.</p>
	 *
	 * @param e The {@link FoodLevelChangeEvent} triggered by a player's food level changing.
	 */
	@EventHandler
	public void onPlayerLoseHunger(FoodLevelChangeEvent e) {
		// Check if the entity is a player.
		if (!(e.getEntity() instanceof Player)) return;
		// Check if the player is currently in a MagicBattle arena.
		if (ArenaManager.getInstance().getArena(((Player) e.getEntity())) != null) {
			// If the player is in an arena, cancel the food level change (prevent hunger loss).
			e.setCancelled(true);
		}
	}

	/**
	 * Constructor for the {@code PlayerLoseHunger} listener.
	 * When an instance of this class is created and registered as an event
	 * listener, its {@link #onPlayerLoseHunger(FoodLevelChangeEvent)} method
	 * will be automatically called by the Bukkit server whenever a player's
	 * food level changes. The logic within this method then determines whether
	 * to cancel the hunger loss based on whether the player is currently in a
	 * MagicBattle arena.
	 */
	public PlayerLoseHunger() {
		// No specific initialization needed for this listener.
	}

}