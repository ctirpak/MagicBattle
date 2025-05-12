package com.github.ctirpak.magicbattle;

import java.util.HashMap;
import java.util.Map;

import com.github.ctirpak.magicbattle.kititems.KitItem;
import com.github.ctirpak.magicbattle.kits.Kit;
import com.github.ctirpak.magicbattle.kits.WandKit;

public class KitManager {
	private KitManager() {};
	private static KitManager instance = null;
	/**
	 * A static instance of {@code KitManager} dedicated to managing
	 * kits, stored in the "kits.yml" file.
	 */
	
	/**
	 * Gets the singleton instance of {@code KitManager}. If no instance
	 * exists, a new one is created.
	 *
	 * @return The singleton instance of {@code KitManager}.
	 */
	public static KitManager getInstance() {
		if(instance == null) {
			instance = new KitManager();
		}
		return instance;
	}
	
	private static final Map<String, Kit> kits = new HashMap<>();
	
	public static void loadKits() {
		kits.clear();
		kits.put("wands", new WandKit("wands"));

	}
	/**
	 * Gets a kit by its name.
	 *
	 * @param name The name of the kit to retrieve.
	 * @return The {@code Kit} object associated with the given name, or null if not found.
	 */
	public Kit getKit(String name) {
		return kits.get(name);
	}
	
	public static String getKitNames() {
		StringBuilder sb = new StringBuilder();
		for (String name : kits.keySet()) {
			sb.append("/mb ").append(name).append(", ");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2); // Remove the trailing comma and space
		}
		return sb.toString();
	}
}
