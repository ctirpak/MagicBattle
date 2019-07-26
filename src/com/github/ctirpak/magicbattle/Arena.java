package com.github.ctirpak.magicbattle;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Arena {
	
	public enum ArenaState {DISABLED,WAITING,COUNTING_DOWN,STARTED;}
	
	private int id;
	private int numPlayers;
	private int currentPlayers = 0;
	private ArenaState state = ArenaState.DISABLED;
	private ArrayList<PlayerData> data;
	private Location spawnPoint;
	
	protected Arena(int id) {
		this.id = id;
		this.data = new ArrayList<PlayerData>();
		this.numPlayers = SettingsManager.getInstance().get("arenas." + id + ".numPlayers");
		
		ConfigurationSection s = SettingsManager.getInstance().get("arenas." + id + ".spawn");
		this.spawnPoint = new Location(
				Bukkit.getServer().getWorld(s.getString("world")),
				s.getDouble("x"),
				s.getDouble("y"),
				s.getDouble("z"),
				(float) s.getDouble("pitch"),
				(float) s.getDouble("yaw"));
		this.state = ArenaState.WAITING;
	}
	
	public int getID() {
		return id;
	}

	public ArenaState getState() {
		return state;
	}

	public void addPlayer(Player p) {
		if(currentPlayers >= numPlayers) {
			//send message
			return;
		}
		data.add(new PlayerData(p));
		p.teleport(spawnPoint);
		currentPlayers++;
	}
	public void removePlayer(Player p) {
		PlayerData d = getPlayerData(p);
		d.restorePlayer();
		data.remove(d);
		currentPlayers --;
	}
	private PlayerData getPlayerData(Player p) {
		for(PlayerData d : data) {
			if(d.isForPlayer(p)) return d;
		}
		return null;
	}

	public boolean containsPlayer(Player p) {
		return getPlayerData(p) != null;
	}
}

class PlayerData {
	private String playerName;
	private ItemStack[] contents;
	private ItemStack[] armorContents;
	private Location location;
	
	protected PlayerData(Player p) {
		this.playerName = p.getName();
		this.contents = p.getInventory().getContents();
		this.armorContents = p.getInventory().getArmorContents();
		this.location = p.getLocation();
	}
	
	protected String getPlayerName() {
		return playerName;
	}
	protected void restorePlayer() {
		Player p = Bukkit.getServer().getPlayer(playerName);
		
		p.getInventory().setContents(contents);
		p.getInventory().setArmorContents(armorContents);
		p.teleport(location);
	}
	protected boolean isForPlayer(Player p) {
		return playerName.equalsIgnoreCase(p.getName());
	}
}
