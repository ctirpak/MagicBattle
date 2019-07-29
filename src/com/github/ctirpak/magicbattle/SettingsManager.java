package com.github.ctirpak.magicbattle;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SettingsManager {
	private static SettingsManager instance = null;
	private SettingsManager() {}
	public static SettingsManager getInstance() {
		if(instance == null) {
			instance = new SettingsManager();
		}
		return instance;
	}
	
	private Plugin p;
	private File arenaFile;
	private FileConfiguration arenaConfig;
	
	public void setup(Plugin p) {
		this.p = p;
		if(!p.getDataFolder().exists()) p.getDataFolder().mkdir();
		arenaFile = new File(p.getDataFolder(), "arenas.yml");
		if(!arenaFile.exists()) {
			try {
				arenaFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
	}
	
	public void set(String path, Object value) {
		arenaConfig.set(path,value);
		try {
			arenaConfig.save(arenaFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public <T> T get(String path) {
		return (T) arenaConfig.get(path);
	}
	public ConfigurationSection createConfigurationSection(String path) {
		ConfigurationSection cs = arenaConfig.createSection(path);
		try {
			arenaConfig.save(arenaFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cs;
	}
	public Plugin getPlugin() {
		return p;
	}
}
