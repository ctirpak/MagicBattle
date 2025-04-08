package com.github.ctirpak.magicbattle;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ctirpak.magicbattle.listeners.BlockBreak;
import com.github.ctirpak.magicbattle.listeners.PlayerDeath;
import com.github.ctirpak.magicbattle.listeners.PlayerInteract;
import com.github.ctirpak.magicbattle.listeners.PlayerLeave;
import com.github.ctirpak.magicbattle.listeners.PlayerLoseHunger;
import com.github.ctirpak.magicbattle.listeners.SignManager;



public class MagicBattle extends JavaPlugin {
	private static final Logger logger = Logger.getLogger(MagicBattle.class.getName());

	private PluginDescriptionFile pdfFile = getDescription();

	@Override
	public void onEnable() {
		logger.info("Checking for redstone lamps...");
//		World w = Bukkit.getServer().getWorld("world");
//		for (Chunk chunk : w.getLoadedChunks()) {
//			for (Entity entity : chunk.getEntities()) {
//				if (entity == Material.HAY_BLOCK) {
//					String s = "Redstone lamp (x,z): " +entity.getLocation().toString();
//					logger.info(s);
//				}
//			}
//		}
//		logger.info("Checking for redstone lamps...done.");
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		pm.registerEvents(new SignManager(), this);
		ArenaManager.getInstance().setupArenas();
		
		getCommand("magicbattle").setExecutor(new CommandManager());

		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new PlayerDeath(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerLeave(), this);
		pm.registerEvents(new PlayerLoseHunger(), this);

		logger.info(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been enabled");
	}
	@Override
	public void onDisable() {
		logger.info(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been disabled");
	}

	public static Plugin getPlugin() {
		return Bukkit.getServer().getPluginManager().getPlugin("MagicBattle");
	}
}
