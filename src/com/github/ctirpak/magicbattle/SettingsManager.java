package com.github.ctirpak.magicbattle;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsManager {
	private static SettingsManager arenas = new SettingsManager("arenas");
	private static SettingsManager lobbySigns = new SettingsManager("lobbysigns");
	
	private File file;
	private FileConfiguration config;
	
	private SettingsManager(String fileName) {
		if(!MagicBattle.getPlugin().getDataFolder().exists()) MagicBattle.getPlugin().getDataFolder().mkdir();
		
		this.file = new File(MagicBattle.getPlugin().getDataFolder(), fileName + ".yml");
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static SettingsManager getArenas() {
		return arenas;
	}
	public static SettingsManager getLobbySigns() {
		return lobbySigns;
	}

	public void set(String path, Object value) {
		config.set(path,value);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ConfigurationSection createConfigurationSection(String path) {
		ConfigurationSection cs = config.createSection(path);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cs;
	}
	@SuppressWarnings("unchecked")
	public <T> T get(String path) {
		return (T) config.get(path);
	}

}
