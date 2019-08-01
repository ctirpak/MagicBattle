package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.github.ctirpak.magicbattle.ArenaManager;

public class PlayerDeath implements Listener {
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (ArenaManager.getInstance().getArena(e.getEntity()) == null) return;
		ArenaManager.getInstance().getArena(e.getEntity()).removePlayer(e.getEntity());
	}
}
