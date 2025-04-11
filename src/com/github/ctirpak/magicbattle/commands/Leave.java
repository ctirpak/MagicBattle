package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * This class represents the command used for a player to leave the MagicBattle
 * arena they are currently in. When an instance of this class is created or
 * when the corresponding "leave" or "l" command is executed by a player, it
 * attempts to remove the player from their current arena. The
 * {@link #onCommand(Player, String[])} method is invoked upon execution. This
 * method first checks if the executing player is currently in an arena. If not,
 * it informs the player that they are not in an arena and therefore cannot leave.
 * If the player is in an arena, it retrieves the {@link com.github.ctirpak.magicbattle.Arena}
 * object the player is associated with and calls the
 * {@link com.github.ctirpak.magicbattle.Arena#removePlayer(Player)} method to
 * remove the player from that arena.
 */
public class Leave extends MagicCommand {

	/**
	 * Handles the execution of the "leave" command.
	 * When a player executes this command, the method first checks if the player
	 * is currently associated with an arena using the {@link ArenaManager}. If the
	 * player is not in an arena, a message is sent to inform them of this. If the
	 * player is in an arena, the method retrieves the corresponding
	 * {@link com.github.ctirpak.magicbattle.Arena} object and calls its
	 * {@link com.github.ctirpak.magicbattle.Arena#removePlayer(Player)} method
	 * to remove the executing player from the arena. This command does not
	 * require any arguments.
	 *
	 * @param p    The {@link Player} who executed the command.
	 * @param args An array of arguments passed with the command (not used by this command).
	 */
	@Override
	public void onCommand(Player p, String[] args) {
		// Check if the player is currently in an arena.
		if (ArenaManager.getInstance().getArena(p) == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You are not already in an arena!");
			return;
		}
		// Remove the player from their current arena.
		ArenaManager.getInstance().getArena(p).removePlayer(p);
	}

	/**
	 * Constructor for the {@code Leave} command.
	 * When a new instance of this class is created, it initializes the command
	 * with the description "Leave an arena", an empty usage string (as this
	 * command does not require any arguments), and the alias "l". When the
	 * corresponding "leave" or "l" command is executed by a player, the
	 * {@link #onCommand(Player, String[])} method is invoked. This method then
	 * handles the logic of checking if the player is in an arena and, if so,
	 * removing them from it.
	 */
	public Leave() {
		// Call the superclass constructor with the command's description, usage, and alias.
		super("Leave an arena", "", "l");
	}
}