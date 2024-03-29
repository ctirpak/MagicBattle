package com.github.ctirpak.magicbattle;

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

public class MagicBattle extends JavaPlugin {
	private PluginDescriptionFile pdfFile = getDescription();

	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		pm.registerEvents(new SignManager(), this);
		ArenaManager.getInstance().setupArenas();
		
		getCommand("magicbattle").setExecutor(new CommandManager());

		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new PlayerDeath(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerLeave(), this);
		pm.registerEvents(new PlayerLoseHunger(), this);
		
		System.out.println(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been enabled");
	}
	
	@Override
	public void onDisable() {
		
		System.out.println(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been disabled");
	}

	public static Plugin getPlugin() {
		return Bukkit.getServer().getPluginManager().getPlugin("MagicBattle");
	}

}
