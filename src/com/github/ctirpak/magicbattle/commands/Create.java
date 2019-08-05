package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.SettingsManager;

public class Create extends MagicCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		int id = ArenaManager.getInstance().getArenas().size() + 1;
		
		SettingsManager.getArenas().createConfigurationSection("arenas." + id);
		SettingsManager.getArenas().set("arenas." + id + ".numPlayers", 10);
		MessageManager.getInstance().msg(p, MessageType.GOOD, "Created Arena " + id + "!");
		ArenaManager.getInstance().setupArenas();
		
		
	}
	
	public Create() {
		super("Create an arena", "", "c");
		
	}


}
