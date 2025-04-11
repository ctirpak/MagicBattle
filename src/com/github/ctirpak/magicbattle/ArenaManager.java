package com.github.ctirpak.magicbattle;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This class manages all the MagicBattle arenas. It is responsible for loading
 * arenas from the configuration, storing them, and providing methods to access
 * individual arenas by their ID or by the player currently in them. This class
 * follows the Singleton design pattern to ensure that only one instance manages
 * all the arenas.
 */
public class ArenaManager {
	private ArenaManager() {}
	private static ArenaManager instance = null;
	/**
	 * Gets the singleton instance of {@code ArenaManager}. If no instance
	 * exists, a new one is created.
	 *
	 * @return The singleton instance of {@code ArenaManager}.
	 */
	public static ArenaManager getInstance() {
		if(instance == null) {
			instance = new ArenaManager();
		}
		return instance;
	}

	private ArrayList<Arena> arenas = new ArrayList<Arena>();

	/**
	 * Loads or reloads all the MagicBattle arenas from the plugin's configuration.
	 * It first checks if the "arenas" section exists in the arena settings. If not,
	 * it creates it. Then, it clears the current list of loaded arenas and iterates
	 * through each key in the "arenas" configuration section. For each key (which
	 * represents an arena ID), it creates a new {@link Arena} object with that ID
	 * and adds it to the {@code arenas} list.
	 *
	 * <p>The configuration structure for arenas is expected to be as follows:</p>
	 * <pre>
	 * arenas:
	 * 1:
	 * spawn:
	 * world: world_name
	 * x: 0.0
	 * y: 0.0
	 * z: 0.0
	 * pitch: 0.0
	 * yaw: 0.0
	 * numPlayers: 10
	 * 2:
	 * spawn:
	 * ...
	 * </pre>
	 */
	public void setupArenas() {
		if(SettingsManager.getArenas().<ConfigurationSection>get("arenas") == null) {
			SettingsManager.getArenas().createConfigurationSection("arenas");
		}
		arenas.clear();
		for(String key : SettingsManager.getArenas().<ConfigurationSection>get("arenas").getKeys(false)) {
			arenas.add(new Arena(Integer.parseInt(key)));
			/*
			 * arenas:
			 * 1:
			 * spawn:
			 * x:
			 * y:
			 * z:
			 * pitch:
			 * yaw:
			 * numPlayers:
			 * 2:
			 * spawn:
			 */
		}
	}

	/**
	 * Gets the list of all currently loaded MagicBattle arenas.
	 *
	 * @return An {@link ArrayList} containing all the loaded {@link Arena} objects.
	 */
	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	/**
	 * Retrieves a specific {@link Arena} by its unique ID.
	 *
	 * @param id The integer ID of the arena to retrieve.
	 * @return The {@link Arena} object with the matching ID, or {@code null} if
	 * no arena with that ID is currently loaded.
	 */
	public Arena getArena(int id) {
		for(Arena a : arenas) {
			if(a.getID() == id) return a;
		}
		return null;
	}

	/**
	 * Retrieves the {@link Arena} that a specific player is currently in.
	 *
	 * @param p The {@link Player} to check for their current arena.
	 * @return The {@link Arena} object that the player is currently a part of,
	 * or {@code null} if the player is not in any loaded arena.
	 */
	public Arena getArena(Player p) {
		for(Arena a : arenas) {
			if(a.containsPlayer(p)) return a;
		}
		return null;
	}
}