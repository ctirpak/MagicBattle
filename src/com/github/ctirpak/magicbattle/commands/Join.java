package com.github.ctirpak.magicbattle.commands;

import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MagicCommand;
import com.github.ctirpak.magicbattle.Arena.ArenaState;

public class Join extends MagicCommand {

	@Override
	public void onCommand(Player p, String[] args) {
		if(ArenaManager.getInstance().getArena(p) != null) {
			//in arena
			return;
		}
		if(args.length == 0) {
			//not enough args
			return;
		}
		int id = -1;
		try {
			 id = Integer.parseInt(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Arena a = ArenaManager.getInstance().getArena(id);
		if(a == null) {
			//arena is null
			return;
		}
		if(a.getState() == ArenaState.DISABLED || a.getState() == ArenaState.STARTED) {
			//can't join
			return;
		}
		a.addPlayer(p);
	}
	
	public Join() {
		super("Join an arena", "<id>", "j");
	}
}
