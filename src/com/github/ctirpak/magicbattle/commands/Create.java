package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.SettingsManager;

/**
 * This class represents the command used to create a new MagicBattle arena.
 * When an instance of this class is created or when the corresponding
 * "create" or "c" command is executed by a player, it triggers the creation
 * of a new arena configuration. Specifically, the {@link #onCommand(Player, String[])}
 * method is invoked, which determines a unique ID for the new arena, creates
 * a new configuration section in the plugin's arena settings file, sets a
 * default number of players for it (currently 10), informs the player of the
 * successful creation, and then prompts the {@link ArenaManager} to reload
 * its arena configurations to include the newly created arena.
 */
public class Create extends MagicCommand {

	/**
	 * Handles the execution of the "create" command.
	 * When a player executes this command, a new arena configuration section
	 * is created in the plugin's arena settings file. This new arena is
	 * assigned a unique ID based on the current number of existing arenas.
	 * A default number of players (currently 10) is set for the new arena.
	 * Finally, a success message is sent to the player, and the {@link ArenaManager}
	 * is instructed to reload and recognize the newly created arena.
	 *
	 * @param p    The {@link Player} who executed the command.
	 * @param args An array of arguments passed with the command (not used by this command).
	 */
	@Override
	public void onCommand(Player p, String[] args) {
		// Determine the ID for the new arena by counting the existing arenas and adding 1.
		int id = ArenaManager.getInstance().getArenas().size() + 1;

		// Create a new configuration section for the arena with the generated ID.
		SettingsManager.getArenas().createConfigurationSection("arenas." + id);
		// Set the default number of players for the newly created arena.
		SettingsManager.getArenas().set("arenas." + id + ".numPlayers", 10);
		// Send a success message to the player who executed the command.
		MessageManager.getInstance().msg(p, MessageType.GOOD, "Created Arena " + id + "!");
		// Instruct the ArenaManager to reload its arena configurations, including the newly created one.
		ArenaManager.getInstance().setupArenas();
	}

	/**
	 * Constructor for the {@code Create} command.
	 * When a new instance of this class is created, it initializes the command
	 * with the description "Create an arena", an empty usage string (as this
	 * command does not require any arguments), and the alias "c". When the
	 * corresponding "create" or "c" command is executed by a player, the
	 * {@link #onCommand(Player, String[])} method is called. This method
	 * proceeds to create a new arena configuration in the plugin's settings,
	 * assigns it a unique ID, sets a default player count, notifies the player
	 * of the creation, and triggers the {@link ArenaManager} to reload the
	 * arena configurations.
	 */
	public Create() {
		// Call the superclass constructor with the command's description, usage, and alias.
		super("Create an arena", "", "c");
	}
}