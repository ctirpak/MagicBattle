package com.github.ctirpak.magicbattle.kititems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class WandArrow extends KitItem {
	private final double DAMAGE_MULTIPLIER = 2.0;
	private final int FIRE_TICKS = 60; // 3 seconds
	private final double SPEED_MULTIPLIER = 3;
	private final double ACCURACY_SPREAD = 0.0; // higher value means more spread
    private final boolean GRAVITY = false;
    
    public WandArrow() {
		super("Arrow",ChatColor.GOLD, 10f, Material.BOW, "A powerful arrow wand.");
	}

	@Override
	public void run(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		SpectralArrow arrow = player.launchProjectile(SpectralArrow.class);
		arrow.setFireTicks(FIRE_TICKS);
		arrow.setDamage(arrow.getDamage() * DAMAGE_MULTIPLIER);
		
		Vector direction = player.getEyeLocation().getDirection();
		Vector velocity = direction.multiply(SPEED_MULTIPLIER);
		arrow.setGravity(GRAVITY);
		
		
//		// Add some randomness for inaccuracy
//		Random random = new Random();
//		double spreadX = (random.nextDouble() - 0.5) * ACCURACY_SPREAD;
//		double spreadY = (random.nextDouble() - 0.5) * ACCURACY_SPREAD;
//		double spreadZ = (random.nextDouble() - 0.5) * ACCURACY_SPREAD;
//
//		velocity.add(new Vector(spreadX, spreadY, spreadZ));
		arrow.setVelocity(velocity);
	}

}
