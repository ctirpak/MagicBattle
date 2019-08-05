package com.github.ctirpak.magicbattle.commands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.SettingsManager;

public class SetLocation extends MagicCommand {
	@Override
	public void onCommand(Player p, String[] args) {
		if (args.length == 0) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "You did not specify an arena id!");
			return;
		}

		int id = -1;

		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			MessageManager.getInstance().msg(p, MessageType.BAD, args[0] + " is not a valid number!");
			return;
		}
		
		if(SettingsManager.getArenas().<ConfigurationSection>get(id + "") == null) {
			MessageManager.getInstance().msg(p, MessageType.BAD, "There is no arena with id " + args[0]);
		}
		
		ConfigurationSection s = SettingsManager.getArenas().createConfigurationSection("arenas." + id + ".spawn");
		
		s.set("world",p.getWorld().getName());
		s.set("x",p.getLocation().getX());
		s.set("y",p.getLocation().getY());
		s.set("z",p.getLocation().getZ());
		
		SettingsManager.getArenas().set("arenas." + id + ".spawn", s);
		ArenaManager.getInstance().setupArenas();
		MessageManager.getInstance().msg(p, MessageType.GOOD, "Set spawn for arena " + args[0]);
		

	}
	public SetLocation() {
		super("Set the spawn location", "<id>", "s", "sloc", "location", "loc");
	}


}
