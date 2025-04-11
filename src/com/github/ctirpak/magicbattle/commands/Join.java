package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * This class represents the command used for a player to join a specific
 * MagicBattle arena. When an instance of this class is created or when the
 * corresponding "join" or "j" command is executed by a player, it attempts
 * to add the player to the target arena. The {@link #onCommand(Player, String[])}
 * method is invoked upon execution. This method first checks if the executing
 * player is already in an arena. If so, it informs the player that they cannot
 * join another arena. Next, it verifies if an arena ID is provided as an argument.
 * If not, it instructs the player on the correct usage. It then attempts to
 * parse the provided argument as an integer representing the arena ID. If the
 * parsing fails, it informs the player that the input is not a valid number.
 * It then retrieves the {@link Arena} object with the given ID. If no such arena
 * exists, the player is notified. Finally, it checks if the target arena is
 * currently disabled or already started; if so, the player cannot join, and they
 * are informed of the arena's status. If all checks pass, the player is added
 * to the specified arena using the {@link Arena#addPlayer(Player)} method.
 */
public class Join extends MagicCommand {

	/**
	 * Handles the execution of the "join" command.
	 * When a player executes this command, it expects a single integer argument
	 * representing the ID of the arena to join. The method first checks if the
	 * player is already in an arena. If not, it validates if an arena ID is provided
	 * and if it's a valid integer. It then retrieves the corresponding {@link Arena}
	 * object. If the arena exists and is not in a {@link ArenaState#DISABLED} or
	 * {@link ArenaState#STARTED} state, the executing player is added to that
	 * arena using the {@link Arena#addPlayer(Player)} method. If any of these
	 * checks fail, the player is informed of the reason they could not join the
	 * specified arena.
	 *
	 * @param p    The {@link Player} who executed the command.
	 * @param args An array of arguments passed with the command. Expected to contain
	 * a single integer representing the arena ID to join.
	 */
	@Override
	public void onCommand(Player p, String[] args) {
		// Check if the player is already in an arena.
		if (ArenaManager.getInstance().getArena(p) != null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You are already in an arena!");
			return;
		}
		// Check if an arena ID was provided.
		if (args.length == 0) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You must specify an arena number");
			return;
		}
		int id = -1;
		// Attempt to parse the first argument as an integer (the arena ID).
		try {
			id = Integer.parseInt(args[0]);
		} catch (Exception e) {
			MessageManager.getInstance().msg(p, MessageType.BAD, args[0] + " is not a number!");
			return;
		}
		// Retrieve the Arena object with the given ID.
		Arena a = ArenaManager.getInstance().getArena(id);
		// Check if an arena with the specified ID exists.
		if (a == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "That arena does not exit!");
			return;
		}
		// Check if the arena is disabled or already started.
		if (a.getState() == ArenaState.DISABLED || a.getState() == ArenaState.STARTED) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "That arena is " + a.getState().toString().toLowerCase() + "!");
			return;
		}
		// Add the player to the specified arena.
		a.addPlayer(p);
	}

	/**
	 * Constructor for the {@code Join} command.
	 * When a new instance of this class is created, it initializes the command
	 * with the description "Join an arena", the usage format "<id>" (indicating
	 * that it requires an arena ID as an argument), and the alias "j". When the
	 * corresponding "join" or "j" command is executed by a player, the
	 * {@link #onCommand(Player, String[])} method is invoked. This method then
	 * handles the logic of checking if the player is already in an arena, validating
	 * the provided arena ID, ensuring the arena exists and is in a joinable state,
	 * and finally adding the player to the specified arena.
	 */
	public Join() {
		// Call the superclass constructor with the command's description, usage, and alias.
		super("Join an arena", "<id>", "j");
	}
}