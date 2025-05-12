package com.github.ctirpak.magicbattle.kititems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WandZoom extends KitItem {
    private final int SLOWNESS_DURATION = 60; // Ticks
    private final int SLOWNESS_AMPLIFIER = 255; // Level
    
	public WandZoom() {
		super("Zoom", ChatColor.DARK_PURPLE, 0.0f, Material.STICK, "A wand that allows you to zoom in.");
	}

	@Override
	public void run(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        e.setCancelled(true); // Cancel the default action

        // Apply slowness to the player
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, SLOWNESS_DURATION, SLOWNESS_AMPLIFIER));
        player.sendMessage(ChatColor.DARK_PURPLE + "Magnified view activated."); // Inform the player
	}

}
