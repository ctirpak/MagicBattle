package com.github.ctirpak.magicbattle;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This class manages the loading, saving, and accessing of configuration
 * files for the MagicBattle plugin. It provides static instances for managing
 * arena settings and lobby sign settings, each stored in separate YAML files.
 *
 * <p>Each instance of {@code SettingsManager} is associated with a specific
 * file name and handles the creation of the file if it doesn't exist, loading
 * its contents, and providing methods to set, create, get, and check for the
 * existence of configuration paths. All modifications are automatically saved
 * to the associated file.</p>
 */
public class SettingsManager {
	/**
	 * A static instance of {@code SettingsManager} dedicated to managing
	 * arena-related settings, stored in the "arenas.yml" file.
	 */
	private static SettingsManager arenas = new SettingsManager("arenas");
	/**
	 * A static instance of {@code SettingsManager} dedicated to managing
	 * lobby sign settings, stored in the "lobbysigns.yml" file.
	 */
	private static SettingsManager lobbySigns = new SettingsManager("lobbysigns");

	private File file;
	private FileConfiguration config;

	/**
	 * Private constructor for the {@code SettingsManager} class.
	 *
	 * @param fileName The name of the YAML file to manage (without the .yml extension).
	 * This file will be located in the plugin's data folder.
	 * If the data folder does not exist, it will be created.
	 * If the specified file does not exist, it will be created.
	 * The configuration from the file is then loaded into a
	 * {@link FileConfiguration} object for manipulation.
	 */
	private SettingsManager(String fileName) {
		// Create the plugin's data folder if it doesn't exist.
		if (!MagicBattle.getPlugin().getDataFolder().exists()) MagicBattle.getPlugin().getDataFolder().mkdir();

		// Create the File object for the specified YAML file in the data folder.
		this.file = new File(MagicBattle.getPlugin().getDataFolder(), fileName + ".yml");

		// Create the file if it doesn't exist.
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Load the configuration from the file.
		this.config = YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Gets the static instance of {@code SettingsManager} for managing arena settings.
	 *
	 * @return The {@code SettingsManager} instance for "arenas.yml".
	 */
	public static SettingsManager getArenas() {
		return arenas;
	}

	/**
	 * Gets the static instance of {@code SettingsManager} for managing lobby sign settings.
	 *
	 * @return The {@code SettingsManager} instance for "lobbysigns.yml".
	 */
	public static SettingsManager getLobbySigns() {
		return lobbySigns;
	}

	/**
	 * Sets a value at the specified path in the configuration and saves the changes to the file.
	 *
	 * @param path  The path to set the value at (e.g., "arenas.1.name").
	 * @param value The value to set at the path.
	 */
	public void set(String path, Object value) {
		config.set(path, value);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new {@link ConfigurationSection} at the specified path and saves the changes to the file.
	 * If a section already exists at the path, it will be returned.
	 *
	 * @param path The path where the new configuration section should be created (e.g., "arenas.2").
	 * @return The newly created or existing {@link ConfigurationSection}.
	 */
	public ConfigurationSection createConfigurationSection(String path) {
		ConfigurationSection cs = config.createSection(path);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cs;
	}

	/**
	 * Gets the value at the specified path from the configuration.
	 * The caller is responsible for casting the returned object to the appropriate type.
	 *
	 * @param path The path to retrieve the value from (e.g., "arenas.1.numPlayers").
	 * @param <T>  The expected type of the value.
	 * @return The value at the specified path, or null if the path does not exist.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String path) {
		return (T) config.get(path);
	}

	/**
	 * Checks if the specified path exists in the configuration.
	 *
	 * @param path The path to check for existence (e.g., "arenas.1").
	 * @return {@code true} if the path exists, {@code false} otherwise.
	 */
	public boolean contains(String path) {
		return config.contains(path);
	}

}