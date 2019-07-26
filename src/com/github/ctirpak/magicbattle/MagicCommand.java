package com.github.ctirpak.magicbattle;

import org.bukkit.entity.Player;

public abstract class MagicCommand {

	private String message;
	private String usage;
	private String[] aliases;
	
	public MagicCommand(String message, String usage, String... aliases) {
		this.message = message;
		this.usage = usage;
		this.aliases = aliases;
	}
	
	public abstract void onCommand(Player p, String[] args);

	public final String getMessage() {
		return message;
	}

	public final String getUsage() {
		return usage;
	}

	public final String[] getAliases() {
		return aliases;
	}

}
