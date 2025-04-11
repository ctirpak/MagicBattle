package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

/**
 * Abstract base class for all MagicBattle commands.
 * Subclasses of this class will implement specific command logic.
 */
public abstract class MagicCommand {

	/**
	 * A brief description of the command. This is used when displaying
	 * the list of available commands.
	 */
	private String message;

	/**
	 * The correct usage format for the command. This is displayed to the
	 * player when they use the command incorrectly.
	 */
	private String usage;

	/**
	 * An array of alternative names (aliases) that can be used to execute
	 * this command.
	 */
	private String[] aliases;

	/**
	 * Constructor for the MagicCommand class.
	 *
	 * @param message A brief description of the command.
	 * @param usage   The correct usage format for the command.
	 * @param aliases An array of alternative names for the command.
	 */
	public MagicCommand(String message, String usage, String... aliases) {
		this.message = message;
		this.usage = usage;
		this.aliases = aliases;
	}

	/**
	 * Abstract method that must be implemented by subclasses to define the
	 * specific action of the command when executed by a player.
	 *
	 * @param p    The player who executed the command.
	 * @param args An array of arguments provided with the command.
	 */
	public abstract void onCommand(Player p, String[] args);

	/**
	 * Gets the brief description of the command.
	 *
	 * @return The command's description.
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * Gets the correct usage format of the command.
	 *
	 * @return The command's usage format.
	 */
	public final String getUsage() {
		return usage;
	}

	/**
	 * Gets the array of aliases for the command.
	 *
	 * @return An array of alternative names for the command.
	 */
	public final String[] getAliases() {
		return aliases;
	}

}