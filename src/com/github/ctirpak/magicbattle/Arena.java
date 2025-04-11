package com.github.ctirpak.magicbattle;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.listeners.SignManager;

/**
 * Represents a single MagicBattle arena. This class manages the state of the
 * arena, the players currently in it, the spawn point, and any associated lobby
 * signs. It handles player joining, leaving, game starting, game stopping, and
 * keeps track of the current number of players.
 */
public class Arena {

	private static final Logger logger = Logger.getLogger(MagicBattle.class.getName());

	/**
	 * An enumeration representing the possible states of an arena.
	 */
	public enum ArenaState {
		/**
		 * The arena is disabled and cannot be joined.
		 */
		DISABLED,
		/**
		 * The arena is waiting for players to join.
		 */
		WAITING,
		/**
		 * The arena is in the countdown phase before the game starts.
		 */
		COUNTING_DOWN,
		/**
		 * The arena has started and the game is in progress.
		 */
		STARTED;
	}

	private int id;
	private int numPlayers;
	private int currentPlayers = 0;
	private ArrayList<PlayerData> data;
	private ArrayList<Sign> signs;
	private Location spawnPoint;
	protected ArenaState state = ArenaState.DISABLED;

	/**
	 * Constructor for the {@code Arena} class.
	 *
	 * @param id The unique identifier for this arena.
	 */
	protected Arena(int id) {

		this.id = id;
		this.data = new ArrayList<PlayerData>();
		// Load the maximum number of players for this arena from the configuration.
		this.numPlayers = SettingsManager.getArenas().get("arenas." + id + ".numPlayers");

		// Load the spawn point for this arena from the configuration, if it exists.
		if (SettingsManager.getArenas().contains("arenas." + id + ".spawn")) {
			ConfigurationSection s = SettingsManager.getArenas().get("arenas." + id + ".spawn");
			this.spawnPoint = LocationUtil.locationFromConfig(s, true);
		}
		// Set the initial state of the arena to WAITING.
		this.state = ArenaState.WAITING;
		// Get all the lobby signs associated with this arena.
		this.signs = SignManager.getSigns(this);
	}

	/**
	 * Gets the unique ID of this arena.
	 *
	 * @return The arena's ID.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gets the current state of this arena.
	 *
	 * @return The arena's current {@link ArenaState}.
	 */
	public ArenaState getState() {
		return state;
	}

	/**
	 * Gets the number of players currently in this arena.
	 *
	 * @return The current player count.
	 */
	public int getCurrentPlayers() {
		return currentPlayers;
	}

	/**
	 * Adds a player to this arena.
	 *
	 * @param p The {@link Player} to add.
	 */
	public void addPlayer(Player p) {
		logger.info("addPlayer");
		// Check if the arena is full.
		if (currentPlayers >= numPlayers) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "There are too many players");
			return;
		}
		// Check if the spawn point is set.
		if (spawnPoint == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "Spawn point not set!");
			return;
		}

		// Check if the player is already in this arena.
		if (containsPlayer(p)) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You are already in this arena!");
			return;
		}
		// Create PlayerData for the joining player and add them to the list.
		data.add(new PlayerData(p));

		// Clear the player's inventory and give them wands.
		p.getInventory().clear();
		for (Wand w : Wand.values()) {
			p.getInventory().addItem(w.createItemStack());
		}
		// Teleport the player to the arena's spawn point.
		p.teleport(spawnPoint);
		currentPlayers++;
		logger.info("Player count: " + currentPlayers);
		logger.info("Sign count: " + signs.size());
		// Update all associated lobby signs with the new player count.
		for (Sign s : signs) {
			s.getSide(Side.FRONT).setLine(2, currentPlayers + " Players");
			s.update(true);
		}
	}

	/**
	 * Removes a player from this arena.
	 *
	 * @param p The {@link Player} to remove.
	 */
	public void removePlayer(Player p) {
		// Get the PlayerData for the player and restore their pre-arena state.
		PlayerData d = getPlayerData(p);
		d.restorePlayer();
		data.remove(d);
		currentPlayers--;

		// Update all associated lobby signs with the new player count.
		for (Sign s : signs) {
			s.getSide(Side.FRONT).setLine(2, currentPlayers + " Players");
			s.update(true);
			logger.info(s.getSide(Side.FRONT).getLine(2));
		}
		// Handle arena stopping conditions based on the remaining player count.
		if (currentPlayers == 1) {
			stop(data.get(0).getPlayer());
		} else if (currentPlayers == 0) {
			stop();
		}
	}

	/**
	 * Gets the {@link PlayerData} associated with a given player in this arena.
	 *
	 * @param p The {@link Player} to find the data for.
	 * @return The {@link PlayerData} object for the player, or {@code null} if
	 * the player is not in this arena.
	 */
	private PlayerData getPlayerData(Player p) {
		for (PlayerData d : data) {
			if (d.isForPlayer(p))
				return d;
		}
		return null;
	}

	/**
	 * Starts the game in this arena. This initiates a countdown before the game
	 * actually begins.
	 */
	public void start() {
		this.state = ArenaState.COUNTING_DOWN;
		// Update all associated lobby signs with the new arena state.
		for (Sign s : signs) {
			s.getSide(Side.FRONT).setLine(3, getState().toString());
			s.update(true);
			logger.info(s.getSide(Side.FRONT).getLine(3));
		}
		// Start the countdown task.
		new Countdown(
				30,
				"Game starting in %t seconds!",
				this,
				30,
				20,
				10,
				5,
				4,
				3,
				2,
				1).runTaskTimer(MagicBattle.getPlugin(), 0, 20);
	}

	/**
	 * Stops the game in this arena, declaring a winner if one is provided.
	 * All players are restored to their pre-arena state.
	 *
	 * @param winner The {@link Player} who won the arena, or {@code null} if
	 * there is no winner (e.g., the arena was force-stopped).
	 */
	public void stop(Player winner) {
		if (winner != null)
			MessageManager.getInstance().broadcast(MessageType.GOOD, winner.getName() + " has won arena " + id + "!");

		// Restore all players in the arena to their pre-game state.
		for (PlayerData pd : data)
			pd.restorePlayer();

		this.state = ArenaState.WAITING;
		// Update all associated lobby signs to the WAITING state and reset player count.
		for (Sign s : signs) {
			s.getSide(Side.FRONT).setLine(3, getState().toString());
			s.getSide(Side.FRONT).setLine(2, "0 Players");
			s.update(true);
			logger.info(s.getSide(Side.FRONT).getLine(3));
		}
		MessageManager.getInstance().broadcast(MessageType.GOOD, "Arena " + id + " has been stopped!");
	}

	/**
	 * Stops the game in this arena without declaring a specific winner.
	 * All players are restored to their pre-arena state.
	 */
	public void stop() {
		// Restore all players in the arena to their pre-game state.
		for (PlayerData pd : data) {
			pd.restorePlayer();
		}

		state = ArenaState.WAITING;
		// Update all associated lobby signs to the WAITING state and reset player count.
		for (Sign s : signs) {
			s.getSide(Side.FRONT).setLine(3, getState().toString());
			s.getSide(Side.FRONT).setLine(2, "0 Players");
			s.update(true);
			logger.info(s.getSide(Side.FRONT).getLine(3));
		}
		MessageManager.getInstance().broadcast(MessageType.GOOD, "Arena " + id + " has been stopped!");
	}

	/**
	 * Checks if a specific player is currently in this arena.
	 *
	 * @param p The {@link Player} to check for.
	 * @return {@code true} if the player is in this arena, {@code false} otherwise.
	 */
	public boolean containsPlayer(Player p) {
		return getPlayerData(p) != null;
	}

	/**
	 * Sends a formatted message to all players currently in this arena.
	 *
	 * @param type     The {@link MessageType} for the message.
	 * @param messages The messages to send.
	 */
	protected void sendMessage(MessageType type, String... messages) {
		for (PlayerData d : data) {
			MessageManager.getInstance().msg(d.getPlayer(), type, messages);
		}
	}

	/**
	 * A nested class that handles the countdown sequence before a game starts.
	 */
	private class Countdown extends BukkitRunnable {
		private int timer;
		private String msg;
		private Arena a;
		private ArrayList<Integer> countingNums;

		/**
		 * Constructor for the {@code Countdown} task.
		 *
		 * @param start        The initial value of the countdown timer (in seconds).
		 * @param msg          The message to send during the countdown, where "%t"
		 * will be replaced with the current timer value.
		 * @param a            The {@link Arena} for which this countdown is running.
		 * @param countingNums An array of integer values representing the specific
		 * times at which the countdown message should be sent.
		 */
		public Countdown(int start, String msg, Arena a, int... countingNums) {
			this.timer = start;
			this.msg = msg;
			this.a = a;
			this.countingNums = new ArrayList<Integer>();
			for (int i : countingNums) {
				this.countingNums.add(i);
			}
		}

		/**
		 * Called every tick by the Bukkit scheduler. Handles the countdown logic.
		 */
		public void run() {
			// When the timer reaches 0, start the game.
			if (timer == 0) {
				for (PlayerData pd : data) {
					pd.getPlayer().teleport(spawnPoint);
				}
				a.sendMessage(MessageType.GOOD, "The game has begun!");
				a.state = ArenaState.STARTED;
				// Update all associated lobby signs to the STARTED state.
				for (Sign s : signs) {
					s.getSide(Side.FRONT).setLine(3, getState().toString());
					s.update(true);
					logger.info(s.getSide(Side.FRONT).getLine(3));
				}
				cancel(); // Stop the countdown task.
			}
			// Send the countdown message at specific time intervals.
			if (countingNums.contains(timer)) {
				a.sendMessage(MessageType.INFO, msg.replaceAll("%t", timer + ""));
				// Update the lobby signs to show the remaining start time.
				for (Sign s : signs) {
					s.getSide(Side.FRONT).setLine(3, "Starting in " + timer);
					s.update(true);
				}

			}
			timer--; // Decrement the timer.
		}
	}

}

/**
 * Represents the pre-arena state of a player, used to restore their inventory,
 * armor, and location when they leave an arena.
 */
class PlayerData {
	private String playerName;
	private ItemStack[] contents;
	private ItemStack[] armorContents;
	private Location location;

	/**
	 * Constructor for the {@code PlayerData} class.
	 *
	 * @param p The {@link Player} to store the data for.
	 */
	protected PlayerData(Player p) {
		this.playerName = p.getName();
		this.contents = p.getInventory().getContents();
		this.armorContents = p.getInventory().getArmorContents();
		this.location = p.getLocation();
	}

	/**
	 * Gets the {@link Player} object associated with this data.
	 *
	 * @return The {@link Player} object.
	 */
	protected Player getPlayer() {
		return Bukkit.getServer().getPlayer(playerName);
	}

	/**
	 * Gets the name of the player associated with this data.
	 *
	 * @return The player's name.
	 */
	protected String getPlayerName() {
		return playerName;
	}

	/**
	 * Restores the player's inventory, armor, and location to what they were
	 * before joining the arena.
	 */
	protected void restorePlayer() {
		Player p = Bukkit.getServer().getPlayer(playerName);

		p.getInventory().setContents(contents);
		p.getInventory().setArmorContents(armorContents);
		p.teleport(location);
	}

	/**
	 * Checks if this {@code PlayerData} is for a specific player.
	 *
	 * @param p The {@link Player} to check against.
	 * @return {@code true} if this data is for the given player, {@code false} otherwise.
	 */
	protected boolean isForPlayer(Player p) {
		return playerName.equalsIgnoreCase(p.getName());
	}
}