package com.github.ctirpak.magicbattle.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

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
	
	private HashMap<Sign, Arena> signs = new HashMap<Sign, Arena>();
	
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
		
		for(String str : SettingsManager.getLobbySigns().<ConfigurationSection>get("signs").getKeys(true)) {
			ConfigurationSection section = SettingsManager.getLobbySigns().get("signs." + str);
			
			Location loc = LocationUtil.locationFromConfig(section.getConfigurationSection("location"), false);
			Sign s = (Sign) loc.getBlock().getState();
			Arena a = ArenaManager.getInstance().getArena(section.getInt("arenaNumber"));
			
			signs.put(s, a);
		}
	}
	
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
			
			
		}
	}

}
