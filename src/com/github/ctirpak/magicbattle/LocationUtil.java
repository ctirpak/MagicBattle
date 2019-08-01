package com.github.ctirpak.magicbattle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class LocationUtil {
	public static Location locationFromConfig(ConfigurationSection s, boolean pitchAndYaw) {
		Location loc = new Location(
				Bukkit.getServer().getWorld(s.getString("world")),
				s.getDouble("x"),
				s.getDouble("y"),
				s.getDouble("z")
				);
		if(pitchAndYaw) {
			loc.setPitch((float) s.getDouble("pitch"));
			loc.setYaw((float) s.getDouble("yaw"));
		}
		
		return loc;
	}
}
