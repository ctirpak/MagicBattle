package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * This class represents the command used to forcefully start a MagicBattle arena.
 * When an instance of this class is created or when the corresponding
 * "forcestart", "fstart", or "start" command is executed by a player, it attempts
 * to bypass any waiting periods or player count requirements and immediately
 * initiate the specified arena. The {@link #onCommand(Player, String[])} method
 * is invoked upon execution. This method first checks if an arena ID is provided
 * as an argument. If not, it informs the player of the correct usage. It then
 * attempts to parse the provided argument as an integer representing the arena ID.
 * If the parsing fails or if no arena with the given ID exists, the player is
 * notified. It also checks if the target arena is already started; if so, it
 * informs the player that the arena is already in progress. If all checks pass,
 * the {@link Arena#start()} method of the specified arena is called, and a
 * confirmation message is sent to the player.
 */
public class ForceStart extends MagicCommand {
	/**
	 * Handles the execution of the "forcestart", "fstart", or "start" command.
	 * When a player executes this command, it expects a single integer argument
	 * representing the ID of the arena to be forcefully started. The method first
	 * validates if an argument is provided and if it's a valid integer. It then
	 * checks if an arena with the given ID exists. If the arena exists, it further
	 * verifies if the arena is already in a started state. If the arena is not
	 * already running, the {@link Arena#start()} method is called to initiate
	 * the arena, and a confirmation message is sent to the player.
	 *
	 * @param p    The {@link Player} who executed the command.
	 * @param args An array of arguments passed with the command. Expected to contain
	 * a single integer representing the arena ID to forcefully start.
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
		// Check if the arena is already in a started state.
		if (a.getState() == ArenaState.STARTED) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "Arena " + id + " is ingame!");
			return;
		}

		// Forcefully start the arena.
		a.start();
		// Send a success message to the player.
		MessageManager.getInstance().msg(p, MessageType.GOOD, "Force started Arena " + id + "!");
	}

	/**
	 * Constructor for the {@code ForceStart} command.
	 * When a new instance of this class is created, it initializes the command
	 * with the description "Force start an arena", the usage format "<id>" (indicating
	 * that it requires an arena ID as an argument), and the aliases "fstart" and
	 * "start". When the corresponding "forcestart", "fstart", or "start" command
	 * is executed by a player, the {@link #onCommand(Player, String[])} method
	 * is invoked. This method then handles the logic of validating the provided
	 * arena ID, checking if the arena is already running, and if not, proceeding
	 * to forcefully start the arena and inform the player.
	 */
	public ForceStart() {
		// Call the superclass constructor with the command's description, usage, and aliases.
		super("Force start an arena", "<id>", "fstart", "start");
	}
}