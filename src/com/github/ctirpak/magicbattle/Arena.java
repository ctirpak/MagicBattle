package com.github.ctirpak.magicbattle;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public class Arena {
	
	public enum ArenaState {DISABLED,WAITING,COUNTING_DOWN,STARTED;}
	
	private int id;
	private int taskId;
	private int numPlayers;
	private int currentPlayers = 0;
	protected ArenaState state = ArenaState.DISABLED;
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
			MessageManager.getInstance().msg(p, MessageType.BAD, "There are too many players");
			return;
		}
		data.add(new PlayerData(p));
		p.getInventory().clear();
		//add wand
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
	
	public void start() {
		this.state = ArenaState.COUNTING_DOWN;
		final Countdown c = new Countdown(30, "Game starting in %t seconds!", this, 30, 20, 10, 5, 4, 3, 2, 1);
		this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SettingsManager.getInstance().getPlugin(), new Runnable() {
			public void run() {
				if(!c.isDone()) {
					c.run();
				} else
					Bukkit.getServer().getScheduler().cancelTask(taskId);
			}
		}, 0, 20);
	}
	
	public void stop() {
		for(PlayerData pd : data) {
			pd.restorePlayer();
		}
	}

	public boolean containsPlayer(Player p) {
		return getPlayerData(p) != null;
	}
	
	protected void sendMessage(MessageType type, String... messages) {
		for(PlayerData d : data) {
			MessageManager.getInstance().msg(d.getPlayer(), type, messages);
		}
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
	
	protected Player getPlayer() {
		return Bukkit.getServer().getPlayer(playerName);
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

class Countdown implements Runnable {
	private boolean isDone = false;
	private int timer;
	private Arena a;
	private String msg;
	private ArrayList<Integer> countingNums; 
	
	public Countdown(int start, String msg, Arena a, int... countingNums) {
		this.timer = start;
		this.msg = msg;
		this.a = a;
		this.countingNums = new ArrayList<Integer>();
		for(int i : countingNums) {
			this.countingNums.add(i);
		}
	}

	@Override
	public void run() {
		if(timer == 0) {
			a.sendMessage(MessageType.GOOD, "The game has begun!");
			a.state = ArenaState.STARTED;
			isDone = true;
			return;
		}
		if(countingNums.contains(timer)) {
			a.sendMessage(MessageType.INFO, msg.replaceAll("%t", timer + ""));
		}
		timer--;
	}
	
	public boolean isDone() {
		return isDone;
	}
}