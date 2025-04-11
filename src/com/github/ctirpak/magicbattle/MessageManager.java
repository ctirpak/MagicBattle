package com.github.ctirpak.magicbattle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class manages the sending of formatted messages within the MagicBattle
 * plugin. It provides methods to send messages to specific command senders
 * (players or the console) and to broadcast messages to all online players.
 * All messages are prefixed with a consistent plugin identifier and colored
 * according to the specified {@link MessageType}.
 *
 * <p>This class follows the Singleton design pattern, ensuring that only one
 * instance of {@code MessageManager} exists throughout the plugin's lifecycle.
 * This centralizes message formatting and sending.</p>
 */
public class MessageManager {
	private static MessageManager instance = null;

	/**
	 * Private constructor to enforce the Singleton pattern.
	 */
	private MessageManager() {
	}

	/**
	 * Gets the singleton instance of {@code MessageManager}. If no instance
	 * exists, a new one is created.
	 *
	 * @return The singleton instance of {@code MessageManager}.
	 */
	public static MessageManager getInstance() {
		if (instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}

	/**
	 * An enumeration defining different types of messages with associated colors.
	 * This allows for consistent coloring of informational, positive, and negative messages.
	 */
	public enum MessageType {
		/**
		 * Represents informational messages, colored gray.
		 */
		INFO(ChatColor.GRAY),
		/**
		 * Represents positive or successful messages, colored gold.
		 */
		GOOD(ChatColor.GOLD),
		/**
		 * Represents negative or error messages, colored red.
		 */
		BAD(ChatColor.RED);

		private ChatColor color;

		/**
		 * Constructor for the {@code MessageType} enum.
		 *
		 * @param color The {@link ChatColor} associated with this message type.
		 */
		MessageType(ChatColor color) {
			this.color = color;
		}

		/**
		 * Gets the {@link ChatColor} associated with this message type.
		 *
		 * @return The color of the message type.
		 */
		public ChatColor getColor() {
			return color;
		}
	}

	/**
	 * The standard prefix used for all MagicBattle plugin messages.
	 */
	private String prefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "MagicBattle" + ChatColor.GRAY + "] " + ChatColor.RESET;

	/**
	 * Sends one or more messages to a specific command sender (player or console)
	 * with the specified message type and formatting.
	 *
	 * @param sender   The {@link CommandSender} to send the message(s) to.
	 * @param type     The {@link MessageType} to determine the color of the message(s).
	 * @param messages An array of strings representing the messages to send. Each
	 * message will be sent on a new line.
	 */
	public void msg(CommandSender sender, MessageType type, String... messages) {
		for (String msg : messages) {
			sender.sendMessage(prefix + type.getColor() + msg);
		}
	}

	/**
	 * Broadcasts one or more messages to all currently online players on the server
	 * with the specified message type and formatting.
	 *
	 * @param type     The {@link MessageType} to determine the color of the message(s).
	 * @param messages An array of strings representing the messages to broadcast. Each
	 * message will be sent on a new line to each online player.
	 */
	public void broadcast(MessageType type, String... messages) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			for (String msg : messages) {
				p.sendMessage(prefix + type.getColor() + msg);
			}
		}
	}
}