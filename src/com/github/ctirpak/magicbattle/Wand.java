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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.github.ctirpak.magicbattle.MessageManager.MessageType;

/**
 * An enumeration of different magic wands available in the MagicBattle plugin.
 * Each wand type has a name, a color associated with it, and a specific action
 * that is executed when a player right-clicks with the wand. The actions are
 * defined by anonymous implementations of the {@link WandRunnable} interface.
 *
 * <p>Each enum constant represents a unique wand with its own magical effect.
 * This enum also provides methods to get wand properties, create the ItemStack
 * representation of a wand, and retrieve a wand by its name.</p>
 */
public enum Wand {
	/**
	 * The Fire wand launches a fiery projectile (Fireball) that can set blocks on fire.
	 */
	FIRE("Fire", ChatColor.RED, new WandRunnable() {
		private final float MANA_COST = 15.0F;
		@Override
		public void run(PlayerInteractEvent e) {
			Arena a = ArenaManager.getInstance().getArena(e.getPlayer());
			if (a.useMana(e.getPlayer(), MANA_COST)) {
				Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
				fb.setIsIncendiary(true);
				fb.setYield(1F);				
			} else {
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, ChatColor.RED + "Not enough mana!");
			}
		}
	}),
	/**
	 * The Poison wand launches a non-incendiary Fireball and applies a poison
	 * effect to nearby players within a 10-block radius. It also sends messages
	 * to both the caster and the poisoned players.
	 */
	POISON("Poison", ChatColor.DARK_PURPLE, new WandRunnable() {
		private final float MANA_COST = 10.0F;
		@Override
		public void run(PlayerInteractEvent e) {
			Arena a = ArenaManager.getInstance().getArena(e.getPlayer());
			if (a.useMana(e.getPlayer(), MANA_COST)) {
				Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
				fb.setIsIncendiary(false);
				fb.setYield(0F);
				for (Entity en : e.getPlayer().getNearbyEntities(10, 10, 10)) {
					if (en instanceof Player) {
						((Player) en).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10, 1));
						MessageManager.getInstance().msg(e.getPlayer(), MessageType.INFO, ChatColor.DARK_PURPLE + "You have been poisoned by " + e.getPlayer().getName() + "!");
						MessageManager.getInstance().msg(e.getPlayer(), MessageType.INFO, ChatColor.DARK_PURPLE + "You have poisoned " + ((Player) en).getName() + "!");
					}
				}
			} else {
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, ChatColor.RED + "Not enough mana!");
			}
		}

	}),
	/**
	 * The Explode wand launches multiple (currently 3) TNT projectiles with randomized
	 * velocities and trajectories. This creates a spread of explosions. The velocity
	 * is based on the player's looking direction with added random variance in angle
	 * and speed. Debugging information about the launched TNT's velocity is logged.
	 */
	EXPLODE("Explode", ChatColor.DARK_RED, new WandRunnable() {
		private final float MANA_COST = 25.0F;
		private final Random random = new Random();
		private final double BASE_MULTIPLIER = 2.0;
		private final double MAX_ANGLE_VARIANCE = 15.0; // Maximum angle in degrees for horizontal/vertical shift
		private final double MAX_VELOCITY_VARIANCE = 0.5; // Maximum multiplier variance


		@Override
		public void run(PlayerInteractEvent e) {
			Arena a = ArenaManager.getInstance().getArena(e.getPlayer());
			if (a.useMana(e.getPlayer(), MANA_COST)) {
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
			} else {
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, ChatColor.RED + "Not enough mana!");
			}
		}

	});
	private String name;
	private ChatColor color;
	private WandRunnable run;

	private static final Logger logger = Logger.getLogger(MagicBattle.class.getName());

	/**
	 * Constructor for the {@code Wand} enum.
	 *
	 * @param name  The name of the wand (e.g., "Fire").
	 * @param color The {@link ChatColor} associated with the wand.
	 * @param run   The {@link WandRunnable} that defines the action of the wand.
	 */
	Wand(String name, ChatColor color, WandRunnable run) {
		this.name = name;
		this.color = color;
		this.run = run;
	}

	/**
	 * Gets the base name of the wand (without color).
	 *
	 * @return The name of the wand.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the {@link ChatColor} associated with the wand.
	 *
	 * @return The color of the wand.
	 */
	public ChatColor getColor() {
		return color;
	}

	/**
	 * Gets the full name of the wand, including its color code.
	 *
	 * @return The colored name of the wand.
	 */
	public String getFullName() {
		return color + name;
	}

	/**
	 * Executes the specific action of the wand when triggered by a
	 * {@link PlayerInteractEvent}.
	 *
	 * @param e The {@link PlayerInteractEvent} that triggered the wand usage.
	 */
	public void run(PlayerInteractEvent e) {
		run.run(e);
	}

	/**
	 * Creates an {@link ItemStack} representation of the wand. The ItemStack is a
	 * stick with a display name (including the wand's color) and lore indicating
	 * that it is a magic wand.
	 *
	 * @return The {@link ItemStack} representing the wand.
	 */
	public ItemStack createItemStack() {
		ItemStack i = new ItemStack(Material.STICK, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(getFullName());
		im.setLore(Arrays.asList("A Magic Wand!"));
		i.setItemMeta(im);

		return i;
	}

	/**
	 * Retrieves a {@code Wand} enum constant based on its name (case-insensitive).
	 *
	 * @param name The name of the wand to look for.
	 * @return The corresponding {@code Wand} enum constant, or {@code null} if
	 * no wand with the given name is found.
	 */
	public static Wand forName(String name) {
		for (Wand w : Wand.values()) {
			if (w.getName().equalsIgnoreCase(name)) {
				return w;
			}
		}
		return null;
	}
}

/**
 * An abstract class that defines the interface for the action that a {@link Wand}
 * performs when used by a player. Anonymous implementations of this class are
 * used within the {@code Wand} enum to define the specific behavior of each wand type.
 */
abstract class WandRunnable {
	/**
	 * Defines the action to be executed when a player interacts with a wand.
	 *
	 * @param e The {@link PlayerInteractEvent} associated with the wand usage.
	 */
	public abstract void run(PlayerInteractEvent e);
}