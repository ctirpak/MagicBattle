package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.Arena.ArenaState;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;

public class Join extends MagicCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(ArenaManager.getInstance().getArena(p) != null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You are already in an arena!");
			return;
		}
		if(args.length == 0) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You must specify an arena number");
			return;
		}
		int id = -1;
		try {
			 id = Integer.parseInt(args[0]);
		} catch (Exception e) {
			MessageManager.getInstance().msg(p, MessageType.BAD, args[0] + " is not a number!");
		}
		Arena a = ArenaManager.getInstance().getArena(id);
		if(a == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "That arena does not exit!");
			return;
		}
		if(a.getState() == ArenaState.DISABLED || a.getState() == ArenaState.STARTED) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "That arena is " + a.getState().toString().toLowerCase() + "!");
			return;
		}
		a.addPlayer(p);
	}
	
	public Join() {
		super("Join an arena", "<id>", "j");
	}
}
