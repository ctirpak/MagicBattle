package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.SettingsManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public class Delete extends MagicCommand {
	@Override
	public void onCommand(Player p, String[] args) {
		if(args.length == 0) {
			MessageManager.getInstance().msg(p,MessageType.BAD,"You must specify and arena number!");
			return;
		}
		
		int id = -1;
		
		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			MessageManager.getInstance().msg(p, MessageType.BAD, args[0] + " is not a valid number!");
			return;
		}
		
		Arena a = ArenaManager.getInstance().getArena(id);

		if(a == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "There is no arena with id " + args[0]);
		}
		if(a.getState() == ArenaState.STARTED) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "Arena " + args[0] + " is ingame!");
		}
		
		SettingsManager.getInstance().set(id + "", null);
		ArenaManager.getInstance().setupArenas();
	}
	public Delete() {
		super("Delete an arena", "<id>", "d");
	}
}