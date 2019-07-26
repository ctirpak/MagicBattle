package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public class Leave extends MagicCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(ArenaManager.getInstance().getArena(p) == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You are not already in an arena!");
			return;
		}
		ArenaManager.getInstance().getArena(p).removePlayer(p);
	}
	
	public Leave() {
		super("Leave an arena", "", "l");
	}
}
