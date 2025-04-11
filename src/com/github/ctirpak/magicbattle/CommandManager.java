package com.github.ctirpak.magicbattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.commands.Create;
import com.github.ctirpak.magicbattle.commands.Delete;
import com.github.ctirpak.magicbattle.commands.ForceStart;
import com.github.ctirpak.magicbattle.commands.ForceStop;
import com.github.ctirpak.magicbattle.commands.Join;
import com.github.ctirpak.magicbattle.commands.Leave;
import com.github.ctirpak.magicbattle.commands.MagicCommand;
import com.github.ctirpak.magicbattle.commands.Reload;
import com.github.ctirpak.magicbattle.commands.SetLocation;

/**
 * This class acts as the central command manager for the MagicBattle plugin.
 * It implements the {@link CommandExecutor} interface to handle all commands
 * prefixed with "magicbattle" (or "mb"). It registers and dispatches
 * execution to the appropriate subcommand handlers.
 */
public class CommandManager implements CommandExecutor {
	/**
	 * A list to store all the available MagicBattle commands.
	 */
	private ArrayList<MagicCommand> cmds = new ArrayList<MagicCommand>();

	/**
	 * Constructor for the CommandManager.
	 * Initializes and registers all the available MagicBattle commands.
	 */
	public CommandManager() {
		cmds.add(new Create());
		cmds.add(new Delete());
		cmds.add(new ForceStart());
		cmds.add(new ForceStop());
		cmds.add(new Join());
		cmds.add(new Leave());
		cmds.add(new Reload());
		cmds.add(new SetLocation());
	}

	/**
	 * Called when a command with the registered name is executed.
	 * This method handles the "magicbattle" (or "mb") base command and
	 * delegates the execution to the specific subcommand handler.
	 *
	 * @param sender        Source of the command
	 * @param cmd           Command which was executed
	 * @param commandLabel  Alias of the command which was used
	 * @param args          Passed command arguments
	 * @return true if a valid command, otherwise false
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Only players can execute MagicBattle commands.
		if (!(sender instanceof Player)) {
			MessageManager.getInstance().msg(sender, MessageType.BAD, "You must be a Player to use this command");
			return true;
		}
		Player p = (Player) sender;

		// Check if the command is the "magicbattle" command (or its alias).
		if (cmd.getName().equalsIgnoreCase("magicbattle")) {
			// If no arguments are provided, display a list of available commands.
			if (args.length == 0) {
				for (MagicCommand mc : cmds) {
					MessageManager.getInstance().msg(sender, MessageType.INFO, "/mb " + aliases(mc) + " " + mc.getUsage() + " - " + mc.getMessage());
				}
				return true;
			}

			// Get the subcommand based on the first argument.
			MagicCommand c = getCommand(args[0]);

			// If the subcommand doesn't exist, inform the player.
			if (c == null) {
				MessageManager.getInstance().msg(sender, MessageType.BAD, "That command doesn't exist!");
				return true;
			}

			// Remove the subcommand name from the arguments to pass to the subcommand handler.
			Vector<String> a = new Vector<String>(Arrays.asList(args));
			a.remove(0);
			String[] subArgs = a.toArray(new String[a.size()]);

			// Execute the subcommand.
			c.onCommand(p, subArgs);
			return true;
		}

		return true;
	}

	/**
	 * Formats the aliases of a given MagicCommand into a readable string.
	 *
	 * @param cmd The MagicCommand to get aliases from.
	 * @return A string containing all aliases separated by " | ".
	 */
	private String aliases(MagicCommand cmd) {
		String fin = "";

		for (String a : cmd.getAliases()) {
			fin += a + " | ";
		}
		// Remove the trailing " | " if there are aliases.
		if (!fin.isEmpty()) {
			return fin.substring(0, fin.lastIndexOf(" | "));
		}
		return fin; // Return an empty string if no aliases.
	}

	/**
	 * Retrieves a MagicCommand instance based on its name or alias.
	 *
	 * @param name The name or alias of the command to retrieve.
	 * @return The corresponding MagicCommand instance, or null if not found.
	 */
	private MagicCommand getCommand(String name) {
		for (MagicCommand cmd : cmds) {
			// Check if the provided name matches the command's simple class name (case-insensitive).
			if (cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
			// Check if the provided name matches any of the command's aliases (case-insensitive).
			for (String alias : cmd.getAliases()) {
				if (name.equalsIgnoreCase(alias)) return cmd;
			}
		}
		return null;
	}
}