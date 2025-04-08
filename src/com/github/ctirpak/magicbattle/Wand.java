package com.github.ctirpak.magicbattle;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public enum Wand {
	FIRE("Fire", ChatColor.RED, new WandRunnable() {
		@Override
		public void run(PlayerInteractEvent e) {
			Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
			fb.setIsIncendiary(true);
			fb.setYield(1F);
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
		private final Random random = new Random();
		private final double BASE_MULTIPLIER = 2.0;
		private final double MAX_ANGLE_VARIANCE = 15.0; // Maximum angle in degrees for horizontal/vertical shift
		private final double MAX_VELOCITY_VARIANCE = 0.5; // Maximum multiplier variance


		@Override
		public void run(PlayerInteractEvent e) {
			for (int x = 0; x < 3; x++) {
				TNTPrimed tnt = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), TNTPrimed.class);
				tnt.setFuseTicks(80);

				Vector baseDirection = e.getPlayer().getEyeLocation().getDirection();

				// Apply random angle variance
				double horizontalAngle = Math.toRadians((random.nextDouble() * 2 - 1) * MAX_ANGLE_VARIANCE);
				double verticalAngle = Math.toRadians((random.nextDouble() * 2 - 1) * MAX_ANGLE_VARIANCE);

				// Rotate the base direction vector
				Vector rotatedDirection = baseDirection.clone();
				rotatedDirection.rotateAroundY(horizontalAngle);

				// To rotate around the horizontal axis (pitch), we need a temporary axis
				Vector right = rotatedDirection.clone().crossProduct(new Vector(0, 1, 0)).normalize();
				rotatedDirection.rotateAroundAxis(right, verticalAngle);

				// Apply random velocity variance
				double velocityMultiplier = BASE_MULTIPLIER + (random.nextDouble() * 2 - 1) * MAX_VELOCITY_VARIANCE;
				if (velocityMultiplier <= 0) {
					velocityMultiplier = 0.1; // Ensure velocity isn't zero or negative
				}

				// Set the final velocity
				Vector finalVelocity = rotatedDirection.multiply(velocityMultiplier);
                tnt.setVelocity(finalVelocity);
				
                // Use the logger for debugging output
                logger.info("Launched TNT #" + (x + 1) + " with velocity: " + finalVelocity.toString());

			}
		}

	});
	private String name;
	private ChatColor color;
	private WandRunnable run;
	
	private static final Logger logger = Logger.getLogger(MagicBattle.class.getName());
	


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
