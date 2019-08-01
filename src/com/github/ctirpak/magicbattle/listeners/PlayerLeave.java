package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.github.ctirpak.magicbattle.ArenaManager;

public class PlayerLeave implements Listener {
	@EventHandler
	public void onPlayerLoseHunger(FoodLevelChangeEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		if (ArenaManager.getInstance().getArena((Player) e.getEntity()) != null) e.setCancelled(true);;
	}
}
