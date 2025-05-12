package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.kits.Wand;

/**
 * This class implements the {@link Listener} interface to handle player
 * interactions, specifically right-clicks, while they are in a MagicBattle arena
 * and holding a stick item that is identified as a MagicBattle wand. It prevents
 * wand usage before the arena has started and then delegates the wand's specific
 * action to the corresponding {@link Wand} enum value.
 */
public class PlayerInteract implements Listener {
	/**
	 * Handles the {@link PlayerInteractEvent}, which is triggered when a player
	 * interacts with the world (e.g., by clicking).
	 *
	 * <p>This method first checks if the interacting player is currently in a
	 * MagicBattle arena. If not, the event is ignored. It then checks if the
	 * action performed was a right-click (either on air or a block) and if the
	 * item held by the player in their main hand is a stick ({@link Material#STICK}).</p>
	 *
	 * <p>If these conditions are met, the method retrieves the {@link ItemMeta}
	 * of the stick and iterates through the {@link Wand} enum values. For each
	 * wand type, it compares the display name of the held stick with the full
	 * name of the wand. If a match is found, it checks if the arena the player
	 * is in has started ({@link ArenaState#STARTED}). If the arena has not started,
	 * it sends the player an error message and cancels the event. If the arena
	 * has started, the event is cancelled to prevent default stick behavior, and
	 * the {@link Wand#run(PlayerInteractEvent)} method of the matching wand enum
	 * value is called, executing the specific action associated with that wand.</p>
	 *
	 * @param e The {@link PlayerInteractEvent} triggered by a player's interaction.
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		// Ignore the event if the player is not in a MagicBattle arena.
		if (ArenaManager.getInstance().getArena(e.getPlayer()) == null) return;
		// Only process right-click interactions.
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK) && !(e.getAction() == Action.LEFT_CLICK_AIR) && !(e.getAction() == Action.LEFT_CLICK_BLOCK)) return;
//		// Only process interactions with a stick or arrow in the main hand.
//		Material m = e.getPlayer().getInventory().getItemInMainHand().getType();
//		if (!(m == Material.STICK || m == Material.ARROW)) return;


		ItemMeta stickMeta = e.getItem().getItemMeta();

		// Iterate through the defined MagicBattle wands.
		for (Wand w : Wand.values()) {
			// Check if the display name of the held stick matches a wand's full name.
			if (stickMeta.getDisplayName().equals(w.getFullName())) {
				// Check if the arena has started before allowing wand usage.
				if (ArenaManager.getInstance().getArena(e.getPlayer()).getState() != ArenaState.STARTED) {
					MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, "You can't use your wand until the battle starts.");
					return;
				}
				// Cancel the default stick interaction.
				e.setCancelled(true);
				// Run the specific action associated with the identified wand.
				w.run(e);
			}
		}
	}

	/**
	 * Constructor for the {@code PlayerInteract} listener.
	 * When an instance of this class is created and registered as an event
	 * listener, its {@link #onPlayerInteract(PlayerInteractEvent)} method will
	 * be automatically called by the Bukkit server whenever a player interacts
	 * with the world. The logic within this method then determines if the
	 * interaction involves a MagicBattle wand and, if so, executes the
	 * appropriate action based on the wand type and the current state of the
	 * player's arena.
	 */
	public PlayerInteract() {
		// No specific initialization needed for this listener.
	}
}