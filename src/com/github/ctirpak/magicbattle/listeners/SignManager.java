package com.github.ctirpak.magicbattle.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.LocationUtil;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.SettingsManager;

public class SignManager implements Listener {
	/*
	 * Recognize signs ([MagicBattle] -> <sign>)
	 * Saving and Loading signs
	 * Setting signs
	 */
	
	private static HashMap<Sign, Integer> signs = new HashMap<Sign, Integer>();
	
	public SignManager() {
		/*
		 * signs:
		 *   1:
		 *     location:
		 *       world:
		 *       x:
		 *       y:
		 *       z:
		 *     arenaNumber:
		 *       1:
		 *     
		 */
		
		if(!SettingsManager.getLobbySigns().contains("signs")) {
			SettingsManager.getLobbySigns().createConfigurationSection("signs");
		}
		
		
		//loads signs
		for(String str : SettingsManager.getLobbySigns().<ConfigurationSection>get("signs").getKeys(false)) {
			System.out.println("lobby sign: " + str);
			ConfigurationSection section = SettingsManager.getLobbySigns().get("signs." + str);
			System.out.println("lobby sign: " + str + " section: " + section);
			
			Location loc = LocationUtil.locationFromConfig(section.getConfigurationSection("location"), false);
			System.out.println("config section: " + section.getConfigurationSection("location"));
			System.out.println("location: " + loc);
			Sign s = (Sign) loc.getBlock().getState();
			
			System.out.println("Sign: " + s.getLine(0).toString() + "; " + s.getLine(1).toString() + "; " + s.getLine(2).toString() + "; " + s.getLine(3).toString() + "; ");
			signs.put(s, section.getInt("arenaNumber"));
			System.out.println("Arena Number: " + section.getInt("arenaNumber"));
		}
		System.out.println("Sign count: " + signs.size());
	}
	
	public static ArrayList<Sign> getSigns(Arena a) {
		ArrayList<Sign> s = new ArrayList<Sign>();
		System.out.println("getSigns:");
		for(Sign sign : signs.keySet()) {
			System.out.println("Sign: " + sign.toString());
			System.out.println("Arena ID: " + a.getID());
			System.out.println("Sign: " + sign);
			System.out.println("Sign: " + signs.get(sign));
			System.out.println("Arena ID Calc: " + ArenaManager.getInstance().getArena(signs.get(sign)));
//			if(ArenaManager.getInstance().getArena(signs.get(sign)) == a) {
			if(signs.get(sign) == a.getID()) {
				s.add(sign);
			}
		}
		return s;
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("[MagicBattle]")) {
			int id;
			try {
				id = Integer.parseInt(e.getLine(1));
			} catch (Exception ex) {
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, "That is not a valid arena number!");
				return;
			}
			
			Arena a = ArenaManager.getInstance().getArena(id);
			if(a == null) {
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, "That is not a valid arena!");
				return;
			}
			
			ConfigurationSection section = SettingsManager.getLobbySigns().createConfigurationSection("signs." + SettingsManager.getLobbySigns().<ConfigurationSection>get("signs").getKeys(true).size() + 1);
			
			ConfigurationSection location = section.createSection("location");
			location.set("world", e.getBlock().getLocation().getWorld().getName());
			location.set("x", e.getBlock().getLocation().getX());
			location.set("y", e.getBlock().getLocation().getY());
			location.set("z", e.getBlock().getLocation().getZ());
			
			section.set("arenaNumber", id);
			
			signs.put((Sign) e.getBlock().getState(), a.getID());

			e.setLine(2, a.getCurrentPlayers() + " Players");
			e.setLine(3, a.getState().toString());
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if(e.getClickedBlock() == null || e.getClickedBlock().getState() == null) return;
		
		if(e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if(s.getLine(0).equalsIgnoreCase("[MagicBattle]")) {
				ArenaManager.getInstance().getArena(Integer.parseInt(s.getLine(1))).addPlayer(e.getPlayer());
			}
		}
	}

}
