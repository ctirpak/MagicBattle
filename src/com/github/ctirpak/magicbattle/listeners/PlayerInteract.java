package com.github.ctirpak.magicbattle.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.Wand;

public class PlayerInteract implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (ArenaManager.getInstance().getArena(e.getPlayer()) == null) return;
		if(!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if(!(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK)) return;
		
		ItemMeta stickMeta = e.getItem().getItemMeta();
		
		for(Wand w : Wand.values()) {
			if(stickMeta.getDisplayName().equals(w.getFullName())) {
				e.setCancelled(true);
				w.run(e);
			}
		}
	}
}
