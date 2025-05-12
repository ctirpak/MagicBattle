package com.github.ctirpak.magicbattle.kititems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandFire extends KitItem {
	public WandFire() {
		super("Fire", ChatColor.RED, 15f, Material.FIRE_CHARGE, "A powerful fire wand.");
	}

	@Override
	public void run(PlayerInteractEvent e) {
		Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
		fb.setIsIncendiary(true);
		fb.setYield(1F);
	}

}
