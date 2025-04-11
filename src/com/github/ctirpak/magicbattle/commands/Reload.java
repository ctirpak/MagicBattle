package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * This class represents the command used to reload the MagicBattle arena configurations.
 * When an instance of this class is created or when the corresponding "reload"
 * or "r" command is executed by a player, it triggers the plugin to re-read
 * the arena settings from the configuration files. The
 * {@link #onCommand(Player, String[])} method is invoked upon execution. This
 * method directly calls the {@link ArenaManager#setupArenas()} method, which
 * handles the process of loading or reloading all defined arenas. After the
 * reload is complete, a confirmation message is sent to the player who executed
 * the command. This command is typically used by administrators to apply changes
 * made to the arena configurations without restarting the server.
 */
public class Reload extends MagicCommand {


	/**
	 * Handles the execution of the "reload" command.
	 * When a player executes this command, the method directly calls the
	 * {@link ArenaManager#setupArenas()} method. This action forces the
	 * {@link ArenaManager} to reload all arena configurations from the plugin's
	 * settings files. This is useful for applying changes made to arena settings
	 * without requiring a server restart. Upon successful completion of the
	 * reload, a positive confirmation message is sent to the player who initiated
	 * the command. This command does not require any arguments.
	 *
	 * @param p    The {@link Player} who executed the command.
	 * @param args An array of arguments passed with the command (not used by this command).
	 */
	@Override
	public void onCommand(Player p, String[] args) {
		// Reload the arena configurations.
		ArenaManager.getInstance().setupArenas();
		// Send a confirmation message to the player.
		MessageManager.getInstance().msg(p, MessageType.GOOD, "Reloaded arenas!");
	}

	/**
	 * Constructor for the {@code Reload} command.
	 * When a new instance of this class is created, it initializes the command
	 * with the description "Reload the arenas", an empty usage string (as this
	 * command does not require any arguments), and the alias "r". When the
	 * corresponding "reload" or "r" command is executed by a player, the
	 * {@link #onCommand(Player, String[])} method is invoked. This method then
	 * triggers the reloading of the arena configurations managed by the
	 * {@link ArenaManager}.
	 */
	public Reload() {
		// Call the superclass constructor with the command's description, usage, and alias.
		super("Reload the arenas", "", "r");
	}

}