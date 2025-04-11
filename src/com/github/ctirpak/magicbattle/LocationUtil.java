package com.github.ctirpak.magicbattle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * This utility class provides a static method for creating a {@link Location}
 * object from a given {@link ConfigurationSection}. This is commonly used to
 * load location data stored in the plugin's configuration files.
 */
public class LocationUtil {

	/**
	 * Creates a {@link Location} object from the data stored in a
	 * {@link ConfigurationSection}. The section is expected to contain keys for
	 * "world", "x", "y", and "z". Optionally, if the {@code pitchAndYaw} flag
	 * is true, the section is also expected to contain "pitch" and "yaw" keys
	 * to set the orientation of the location.
	 *
	 * <p>If any of the required keys ("world", "x", "y", "z") are missing or
	 * if the world specified by the "world" key cannot be found, or if a
	 * {@link ClassCastException} occurs during the retrieval of the double
	 * values, this method will return {@code null}.</p>
	 *
	 * @param s             The {@link ConfigurationSection} containing the location data.
	 * @param pitchAndYaw   A boolean indicating whether to also load and set the
	 * pitch and yaw from the configuration section (if present).
	 * @return A new {@link Location} object if the data is valid and complete,
	 * otherwise {@code null}.
	 */
	public static Location locationFromConfig(ConfigurationSection s, boolean pitchAndYaw) {
		try {
			// Create a new Location object using the world name and coordinates from the config.
			Location loc = new Location(
					Bukkit.getServer().getWorld(s.getString("world")),
					s.getDouble("x"),
					s.getDouble("y"),
					s.getDouble("z")
			);
			// If pitchAndYaw is true, attempt to load and set the pitch and yaw.
			if (pitchAndYaw) {
				loc.setPitch((float) s.getDouble("pitch"));
				loc.setYaw((float) s.getDouble("yaw"));
			}

			return loc; // Return the created Location object.

		} catch (Exception e) {
			// Catch any exceptions that might occur (e.g., missing keys, invalid world, class cast).
			return null; // Return null if there was an error creating the Location.
		}
	}
}