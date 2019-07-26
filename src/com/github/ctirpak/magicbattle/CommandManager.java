package com.github.ctirpak.magicbattle;

import java.util.Arrays;
import java.util.TreeSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.commands.MagicCommand;

public class CommandManager implements CommandExecutor {
	private TreeSet<MagicCommand> cmds = new TreeSet<MagicCommand>();
	
	public void setup() {
		//add commands
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			//send message
			MessageManager.getInstance().msg(sender, MessageType.BAD, "You must be a Player to use this command");
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("magicbattle")) {
			if(args.length == 0) {
				//send command usages
				return true;
			}

			MagicCommand c = getCommand(args[0]);
			
			if(c == null) {
				MessageManager.getInstance().msg(sender, MessageType.BAD, "That command doesn't exits!");
				return true;
			}
			
			TreeSet<String> a = new TreeSet<String>(Arrays.asList(args));
			a.remove(0);
			args = a.toArray(new String[a.size()]);
			c.onCommand(p, args);
			return true;
		}
		
		return true;
	}
	
	private MagicCommand getCommand(String name) {
		for(MagicCommand cmd : cmds) {
			if(cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
		}
		return null;
	}
}

