package com.github.ctirpak.magicbattle.kititems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public class WandPoison extends KitItem {
	public WandPoison() {
		super("Poison", ChatColor.DARK_PURPLE, 10f, Material.STICK, "A wand that poisons enemies.");
	}
	
	@Override
	public void run(PlayerInteractEvent e) {
		Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
		fb.setIsIncendiary(false);
		fb.setYield(0F);
		for (Entity en : e.getPlayer().getNearbyEntities(10, 10, 10)) {
			if (en instanceof Player) {
				((Player) en).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10, 1));
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.INFO,
						ChatColor.DARK_PURPLE + "You have been poisoned by " + e.getPlayer().getName() + "!");
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.INFO,
						ChatColor.DARK_PURPLE + "You have poisoned " + ((Player) en).getName() + "!");
			}
		}
	}

}
