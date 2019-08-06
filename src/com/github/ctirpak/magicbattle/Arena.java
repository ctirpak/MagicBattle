package com.github.ctirpak.magicbattle;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.listeners.SignManager;

public class Arena {

	public enum ArenaState {
		DISABLED, WAITING, COUNTING_DOWN, STARTED;
	}

	private int id;
	private int taskId;
	private int numPlayers;
	private int currentPlayers = 0;
	protected ArenaState state = ArenaState.DISABLED;
	private ArrayList<PlayerData> data;
	private ArrayList<Sign> signs;
	private Location spawnPoint;

	protected Arena(int id) {
		this.id = id;
		this.data = new ArrayList<PlayerData>();
		this.numPlayers = SettingsManager.getArenas().get("arenas." + id + ".numPlayers");

		if (SettingsManager.getArenas().contains("arenas." + id + ".spawn")) {
			ConfigurationSection s = SettingsManager.getArenas().get("arenas." + id + ".spawn");
			this.spawnPoint = LocationUtil.locationFromConfig(s, true);
		}
		this.state = ArenaState.WAITING;
		this.signs = SignManager.getSigns(this);
	}

	public int getID() {
		return id;
	}

	public ArenaState getState() {
		return state;
	}

	public int getCurrentPlayers() {
		return currentPlayers;
	}

	public void addPlayer(Player p) {
		if (currentPlayers >= numPlayers) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "There are too many players");
			return;
		}
		if (spawnPoint == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "Spawn point not set!");
			return;
		}
		data.add(new PlayerData(p));
		p.getInventory().clear();
		p.getInventory().addItem(Wand.values()[new Random().nextInt(Wand.values().length)].createItemStack());
		p.teleport(spawnPoint);
		currentPlayers++;

		for (Sign s : signs) {
			s.setLine(2, currentPlayers + "");
			s.update();
		}
	}

	public void removePlayer(Player p) {
		PlayerData d = getPlayerData(p);
		d.restorePlayer();
		data.remove(d);
		currentPlayers--;

		for (Sign s : signs) {
			s.setLine(2, currentPlayers + "");
			s.update();
		}
		if (currentPlayers == 1) {
			stop(data.get(0).getPlayer());
		} else if (currentPlayers == 0) {
			stop();
		}
	}

	private PlayerData getPlayerData(Player p) {
		for (PlayerData d : data) {
			if (d.isForPlayer(p))
				return d;
		}
		return null;
	}

	public void start() {
		this.state = ArenaState.COUNTING_DOWN;
		for (Sign s : signs) {
			s.setLine(3, getState().toString());
			s.update();
		}
		final Countdown c = new Countdown(30, "Game starting in %t seconds!", this, 30, 20, 10, 5, 4, 3, 2, 1);
		this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MagicBattle.getPlugin(),
				new Runnable() {
					public void run() {
						if (!c.isDone()) {
							c.run();
							state = ArenaState.STARTED;
							for (Sign s : signs) {
								s.setLine(3, getState().toString());
								s.update();
							}
						} else
							Bukkit.getServer().getScheduler().cancelTask(taskId);
					}
				}, 0, 20);
	}

	public void stop(Player winner) {
		if (winner != null)
			MessageManager.getInstance().broadcast(MessageType.GOOD, winner.getName() + " has won arena " + id + "!");

		for (PlayerData pd : data)
			pd.restorePlayer();

		this.state = ArenaState.WAITING;
	}

	public void stop() {
		for (PlayerData pd : data) {
			pd.restorePlayer();
		}

		state = ArenaState.WAITING;
		for (Sign s : signs) {
			s.setLine(3, getState().toString());
			s.update();
		}
	}

	public boolean containsPlayer(Player p) {
		return getPlayerData(p) != null;
	}

	protected void sendMessage(MessageType type, String... messages) {
		for (PlayerData d : data) {
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
		for (int i : countingNums) {
			this.countingNums.add(i);
		}
	}

	@Override
	public void run() {
		if (timer == 0) {
			a.sendMessage(MessageType.GOOD, "The game has begun!");
			a.state = ArenaState.STARTED;
			isDone = true;
			return;
		}
		if (countingNums.contains(timer)) {
			a.sendMessage(MessageType.INFO, msg.replaceAll("%t", timer + ""));
		}
		timer--;
	}

	public boolean isDone() {
		return isDone;
	}
}