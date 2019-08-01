package com.github.ctirpak.magicbattle;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ArenaManager {
	private static ArenaManager instance = null;
	private ArenaManager() {}
	public static ArenaManager getInstance() {
		if(instance == null) {
			instance = new ArenaManager();
		}
		return instance;
	}
	
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	
	public void setupArenas() {
		for(String key : SettingsManager.getArenas().<ConfigurationSection>get("arenas").getKeys(true)) {
			arenas.add(new Arena(Integer.parseInt(key)));
			/*
			 * arenas:
			 *   1:
			 *     spawn:
			 *       x:
			 *       y:
			 *       z:
			 *       pitch:
			 *       yaw:
			 *     numPlayers:
			 *   2:
			 *     spawn:
			 */
		}
	}
	
	public ArrayList<Arena> getArenas() {
		return arenas;
	}
	public Arena getArena(int id) {
		for(Arena a : arenas) {
			if(a.getID() == id) return a;
		}
		return null;
	}
	public Arena getArena(Player p) {
		for(Arena a : arenas) {
			if(a.containsPlayer(p)) return a;
		}
		return null;
	}
	public void start() {
		// TODO need to complete
		
	}
	public void stop() {
		// TODO need to complete
		
	}
}
