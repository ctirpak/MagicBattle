package com.github.ctirpak.magicbattle;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
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

/**
 * The main class for the MagicBattle Bukkit plugin. This class extends {@link JavaPlugin}
 * and serves as the entry point for the plugin's lifecycle, handling enabling and
 * disabling logic. It initializes essential plugin components, registers event
 * listeners, and sets up command executors.
 */
public class MagicBattle extends JavaPlugin {
	private static final Logger logger = Logger.getLogger(MagicBattle.class.getName());

	private PluginDescriptionFile pdfFile = getDescription();

	/**
	 * Called when the plugin is enabled. This method performs the initial setup
	 * of the plugin, including loading arena configurations, registering commands,
	 * and registering event listeners.
	 */
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
		logger.info("Checking for redstone lamps...done.");

		PluginManager pm = Bukkit.getServer().getPluginManager();

		// Load arena configurations from the plugin's settings.
		ArenaManager.getInstance().setupArenas();

		// Set the executor for the main "magicbattle" command.
		getCommand("magicbattle").setExecutor(new CommandManager());

		// Register all the event listeners used by the plugin.
		pm.registerEvents(new SignManager(), this);
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new PlayerDeath(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerLeave(), this);
		pm.registerEvents(new PlayerLoseHunger(), this);

		logger.info(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been enabled");
	}

	/**
	 * Called when the plugin is disabled. This method performs any necessary
	 * cleanup tasks before the plugin is unloaded from the server. Currently,
	 * it only logs a message indicating that the plugin has been disabled.
	 */
	@Override
	public void onDisable() {
		logger.info(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been disabled");
	}

	/**
	 * Provides a static method to get the instance of the MagicBattle plugin.
	 * This is useful for other classes within the plugin that need to access
	 * the main plugin instance (e.g., for scheduling tasks).
	 *
	 * @return The instance of the MagicBattle plugin.
	 */
	public static Plugin getPlugin() {
		return Bukkit.getServer().getPluginManager().getPlugin("MagicBattle");
	}
}