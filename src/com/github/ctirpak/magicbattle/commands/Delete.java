package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.SettingsManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * This class represents the command used to delete an existing MagicBattle arena.
 * When an instance of this class is created or when the corresponding
 * "delete" or "d" command is executed by a player, it attempts to remove
 * the specified arena from the plugin's configuration. The {@link #onCommand(Player, String[])}
 * method is invoked upon execution. This method first checks if an arena ID
 * is provided as an argument. If not, it informs the player of the correct usage.
 * It then attempts to parse the provided argument as an integer representing
 * the arena ID. If the parsing fails or if no arena with the given ID exists,
 * the player is notified. Additionally, it checks if the target arena is currently
 * in a started state; if so, the deletion is prevented, and the player is informed.
 * If all checks pass, the arena's configuration section is removed from the
 * settings file, and the {@link ArenaManager} is instructed to reload its
 * arena configurations, effectively deleting the arena.
 */
public class Delete extends MagicCommand {
	/**
	 * Handles the execution of the "delete" command.
	 * When a player executes this command, it expects a single integer argument
	 * representing the ID of the arena to be deleted. The method first validates
	 * if an argument is provided and if it's a valid integer. It then checks
	 * if an arena with the given ID exists. If the arena exists, it further
	 * verifies if the arena is currently in a started state. Deletion is only
	 * allowed if the arena is not running. If all validations pass, the
	 * configuration section for the specified arena ID is removed from the
	 * {@link SettingsManager}'s arena configuration, and the {@link ArenaManager}
	 * is instructed to reload its configurations to reflect the deletion.
	 *
	 * @param p    The {@link Player} who executed the command.
	 * @param args An array of arguments passed with the command. Expected to contain
	 * a single integer representing the arena ID to delete.
	 */
	@Override
	public void onCommand(Player p, String[] args) {
		// Check if the player provided an arena ID.
		if (args.length == 0) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You must specify and arena number!");
			return;
		}

		int id = -1;

		// Attempt to parse the first argument as an integer (the arena ID).
		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			MessageManager.getInstance().msg(p, MessageType.BAD, args[0] + " is not a valid number!");
			return;
		}

		// Retrieve the Arena object with the given ID.
		Arena a = ArenaManager.getInstance().getArena(id);

		// Check if an arena with the specified ID exists.
		if (a == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "There is no arena with id " + args[0]);
			return;
		}
		// Check if the arena is currently in a started state.
		if (a.getState() == ArenaState.STARTED) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "Arena " + id + " is ingame!");
			return;
		}

		// Remove the configuration section for the specified arena ID.
		SettingsManager.getArenas().set("arenas." + id, null);
		// Instruct the ArenaManager to reload its arena configurations to apply the deletion.
		ArenaManager.getInstance().setupArenas();
	}

	/**
	 * Constructor for the {@code Delete} command.
	 * When a new instance of this class is created, it initializes the command
	 * with the description "Delete an arena", the usage format "<id>" (indicating
	 * that it requires an arena ID as an argument), and the alias "d". When the
	 * corresponding "delete" or "d" command is executed by a player, the
	 * {@link #onCommand(Player, String[])} method is invoked. This method then
	 * handles the logic of validating the provided arena ID, checking if the
	 * arena is running, and if not, proceeding to remove its configuration
	 * and updating the active arena list.
	 */
	public Delete() {
		// Call the superclass constructor with the command's description, usage, and alias.
		super("Delete an arena", "<id>", "d");
	}
}