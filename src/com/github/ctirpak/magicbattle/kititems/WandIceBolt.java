package com.github.ctirpak.magicbattle.kititems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandIceBolt extends KitItem {
    private final double DAMAGE_MULTIPLIER = 1.5;
    private final int FREEZE_DURATION = 40; // Ticks (2 seconds)
    
	public WandIceBolt() {
		super("Ice Bolt", ChatColor.AQUA, 7.0f, Material.STICK, "A freezing ice bolt spell.");
	}

	@Override
	public void run(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setFreezeTicks((int)(FREEZE_DURATION * DAMAGE_MULTIPLIER));

        // Apply freeze effect on hit (simplified - requires EntityDamageByEntityEvent listener)
        //  This part would go in a separate listener, not here.  We can't directly
        //  apply the freeze effect to a player from the projectile hitting.
        //  Instead, you'd store the shooter and target, and then in the
        //  EntityDamageByEntityEvent, check if the snowball was from this wand,
        //  and if so, apply the freeze.
	}

}
