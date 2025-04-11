package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.ctirpak.magicbattle.ArenaManager;

/**
 * This class implements the {@link Listener} interface to prevent players
 * from breaking blocks within a MagicBattle arena. This is a common mechanism
 * in arena-based games to maintain the integrity of the playing field and
 * prevent unintended modifications to the environment during gameplay.
 */
public class BlockBreak implements Listener {

	/**
	 * Handles the {@link BlockBreakEvent}, which is triggered when a player attempts
	 * to break a block in the world.
	 *
	 * <p>This method checks if the player attempting to break the block is currently
	 * participating in a MagicBattle arena using the
	 * {@link ArenaManager#getArena(org.bukkit.entity.Player)} method. If the player
	 * is in an arena (meaning the method returns a non-null
	 * {@link com.github.ctirpak.magicbattle.Arena} object), the event is cancelled
	 * using {@code e.setCancelled(true)}. This effectively prevents the block
	 * from being broken by the player.</p>
	 *
	 * @param e The {@link BlockBreakEvent} triggered when a player attempts to break a block.
	 */
	@EventHandler
	public void onBLockBreak(BlockBreakEvent e) {
		// Check if the player breaking the block is in a MagicBattle arena.
		if (ArenaManager.getInstance().getArena(e.getPlayer()) == null) return;
		// If the player is in an arena, cancel the block break event.
		e.setCancelled(true);
	}

	/**
	 * Constructor for the {@code BlockBreak} listener.
	 * When an instance of this class is created and registered as an event
	 * listener, its {@link #onBLockBreak(BlockBreakEvent)} method will be
	 * automatically called by the Bukkit server whenever a player attempts to
	 * break a block. The logic within this method then determines whether to
	 * cancel the block breaking based on whether the player is currently in a
	 * MagicBattle arena.
	 */
	public BlockBreak() {
		// No specific initialization needed for this listener.
	}
}