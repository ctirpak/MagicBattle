package com.github.ctirpak.magicbattle;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public enum Wand {
	FIRE("Fire", ChatColor.RED, new WandRunnable() {
		@Override
		public void run(PlayerInteractEvent e) {
			Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
			fb.setIsIncendiary(false);
			fb.setYield(0F);
		}
	}),
	POISON("Poison", ChatColor.DARK_PURPLE, new WandRunnable() {
		@Override
		public void run(PlayerInteractEvent e) {
			Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
			fb.setIsIncendiary(false);
			fb.setYield(0F);
			for(Entity en : e.getPlayer().getNearbyEntities(10,10,10)) {
				if(en instanceof Player) {
					((Player) en).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10, 1));
					MessageManager.getInstance().msg(e.getPlayer(), MessageType.INFO, ChatColor.DARK_PURPLE + "You have been poisoned by " + e.getPlayer().getName() + "!");
					MessageManager.getInstance().msg(e.getPlayer(), MessageType.INFO, ChatColor.DARK_PURPLE + "You have poisoned " + ((Player) en).getName() + "!");
				}
			}
		}
		
	}),
	EXPLODE("Explode", ChatColor.DARK_RED, new WandRunnable() {
		@Override
		public void run(PlayerInteractEvent e) {
			
			Vector v = e.getPlayer().getEyeLocation().getDirection().multiply(2);
			for(int x = 0; x < 3; x++) {
				
				TNTPrimed tnt = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(),TNTPrimed.class);
				v = e.getPlayer().getEyeLocation().getDirection().multiply(2 + x);
				// Apply the vector to the primed tnt
				tnt.setVelocity(v);
			}
		}
		
	});
	private String name;
	private ChatColor color;
	private WandRunnable run;
	Wand(String name, ChatColor color, WandRunnable run) {
		this.name = name;
		this.color = color;
		this.run = run;
	}
	public String getName() {
		return name;
	}
	public ChatColor getColor() {
		return color;
	}
	public String getFullName() {
		return color + name;
	}
	public void run(PlayerInteractEvent e) {
		run.run(e);
	}
	public ItemStack createItemStack() {
		ItemStack i = new ItemStack(Material.STICK, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(getFullName());
		im.setLore(Arrays.asList("A Magic Wand!"));
		i.setItemMeta(im);
		
		return i;
	}
	public static Wand forName(String name) {
		for(Wand w : Wand.values()) {
			if(w.getName().equalsIgnoreCase(name)) {
				return w;
			}
		}
		return null;
	}
}

abstract class WandRunnable {
	public abstract void run(PlayerInteractEvent e);
}
