package com.github.ctirpak.magicbattle.commands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.SettingsManager;

/**
 * This class represents the command used to set the spawn location for a specific
 * MagicBattle arena. When an instance of this class is created or when the
 * corresponding "setlocation", "s", "sloc", "location", or "loc" command is
 * executed by a player, it attempts to save the player's current location as the
 * spawn point for the specified arena in the plugin's configuration. The
 * {@link #onCommand(Player, String[])} method is invoked upon execution. This
 * method first checks if an arena ID is provided as an argument. If not, it
 * informs the player of the correct usage. It then attempts to parse the provided
 * argument as an integer representing the arena ID. If the parsing fails or if
 * no arena with the given ID exists in the configuration, the player is notified.
 * If a valid arena ID is provided, a new configuration section for the arena's
 * spawn location is created (or accessed if it already exists). The current
 * world, X, Y, and Z coordinates of the executing player's location are then
 * saved within this spawn configuration. Finally, the {@link ArenaManager} is
 * instructed to reload its arena configurations to reflect the newly set spawn
 * location, and a confirmation message is sent to the player.
 */
public class SetLocation extends MagicCommand {
	/**
	 * Handles the execution of the "setlocation", "s", "sloc", "location", or "loc" command.
	 * When a player executes this command, it expects a single integer argument
	 * representing the ID of the arena for which the spawn location should be set.
	 * The method first validates if an arena ID is provided and if it's a valid
	 * integer. It then checks if an arena configuration exists for the given ID.
	 * If it does, a new configuration section named "spawn" is created within the
	 * arena's configuration (or retrieved if it exists). The current world name
	 * and the X, Y, and Z coordinates of the executing player's location are then
	 * stored in this "spawn" section. After saving the location, the
	 * {@link ArenaManager} is instructed to reload its arena configurations to
	 * ensure the new spawn location is loaded. Finally, a success message indicating
	 * the arena ID for which the spawn was set is sent to the player.
	 *
	 * @param p    The {@link Player} who executed the command. The player's current
	 * location will be used as the spawn point.
	 * @param args An array of arguments passed with the command. Expected to contain
	 * a single integer representing the arena ID.
	 */
	@Override
	public void onCommand(Player p, String[] args) {
		// Check if an arena ID was provided.
		if (args.length == 0) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You did not specify an arena id!");
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

		// Check if an arena configuration exists for the given ID.
		if (SettingsManager.getArenas().<ConfigurationSection>get("arenas." + id) == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "There is no arena with id " + args[0]);
			return;
		}

		// Create (or get) the configuration section for the arena's spawn.
		ConfigurationSection s = SettingsManager.getArenas().createConfigurationSection("arenas." + id + ".spawn");

		// Set the world, X, Y, and Z coordinates of the player's current location.
		s.set("world", p.getWorld().getName());
		s.set("x", p.getLocation().getX());
		s.set("y", p.getLocation().getY());
		s.set("z", p.getLocation().getZ());

		// Save the spawn configuration.
		SettingsManager.getArenas().set("arenas." + id + ".spawn", s);
		// Reload the arena configurations to apply the new spawn location.
		ArenaManager.getInstance().setupArenas();
		// Send a success message to the player.
		MessageManager.getInstance().msg(p, MessageType.GOOD, "Set spawn for arena " + args[0]);


	}

	/**
	 * Constructor for the {@code SetLocation} command.
	 * When a new instance of this class is created, it initializes the command
	 * with the description "Set the spawn location", the usage format "<id>"
	 * (indicating that it requires an arena ID as an argument), and the aliases
	 * "s", "sloc", "location", and "loc". When the corresponding command is
	 * executed by a player, the {@link #onCommand(Player, String[])} method is
	 * invoked. This method then handles the logic of validating the provided
	 * arena ID, accessing or creating the arena's spawn configuration, saving
	 * the player's current location, and triggering an arena configuration reload.
	 */
	public SetLocation() {
		// Call the superclass constructor with the command's description, usage, and aliases.
		super("Set the spawn location", "<id>", "s", "sloc", "location", "loc");
	}


}