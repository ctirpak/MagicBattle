package com.github.ctirpak.magicbattle;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {
	private static MessageManager instance = null;
	private MessageManager() {}
	public static MessageManager getInstance() {
		if(instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}
	
	public enum MessageType {
		INFO(ChatColor.GRAY),
		GOOD(ChatColor.GOLD),
		BAD(ChatColor.RED);
		private ChatColor color;
		MessageType(ChatColor color) {
			this.color = color;
		}
		public ChatColor getColor() {
			return color;
		}
	}
	
	private String prefix = ChatColor.GRAY + "[" + ChatColor.GOLD +"MagicBattle" + ChatColor.GRAY + "] " + ChatColor.RESET;
	
	public void msg(CommandSender sender, MessageType type, String... messages) {
		for(String msg : messages) {
			sender.sendMessage(prefix + type.getColor() + msg);
		}
	}
}
