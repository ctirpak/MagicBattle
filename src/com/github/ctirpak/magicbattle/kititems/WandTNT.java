package com.github.ctirpak.magicbattle.kititems;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class WandTNT extends KitItem {
	private final Random random = new Random();
	private final double BASE_MULTIPLIER = 2.0;
	private final double MAX_ANGLE_VARIANCE = 15.0; // Maximum angle in degrees for horizontal/vertical shift
	private final double MAX_VELOCITY_VARIANCE = 0.5; // Maximum multiplier variance
	
	public WandTNT() {
		super("Explode", ChatColor.DARK_RED, 25f, Material.TNT, "A wand that causes explosions.");
	}

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

}
