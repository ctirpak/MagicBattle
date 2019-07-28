package com.github.ctirpak.magicbattle;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicBattle extends JavaPlugin {
	private PluginDescriptionFile pdfFile = getDescription();

	@Override
	public void onEnable() {
		SettingsManager.getInstance().setup(this);
		ArenaManager.getInstance().setupArenas();
		
		CommandManager cm = new CommandManager();
		cm.setup();
		getCommand("magicbattle").setExecutor(cm);
		
		System.out.println(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been enabled");
	}
	
	@Override
	public void onDisable() {
		
		System.out.println(pdfFile.getName() + " [version " + pdfFile.getVersion() + "] has been disabled");
	}

}
