package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.ctirpak.magicbattle.ArenaManager;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBLockBreak(BlockBreakEvent e) {
		if (ArenaManager.getInstance().getArena(e.getPlayer()) == null) return;
		e.setCancelled(true);
	}
}
