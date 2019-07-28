package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public class Reload extends MagicCommand {


	@Override
	public void onCommand(Player p, String[] args) {
		ArenaManager.getInstance().setupArenas();
		MessageManager.getInstance().msg(p, MessageType.GOOD, "Reloaded arenas!");
	}
	public Reload() {
		super("Reload the arenas", "", "r");
	}

}
