package com.github.ctirpak.magicbattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.commands.Create;
import com.github.ctirpak.magicbattle.commands.Delete;
import com.github.ctirpak.magicbattle.commands.ForceStart;
import com.github.ctirpak.magicbattle.commands.ForceStop;
import com.github.ctirpak.magicbattle.commands.Join;
import com.github.ctirpak.magicbattle.commands.Leave;
import com.github.ctirpak.magicbattle.commands.MagicCommand;
import com.github.ctirpak.magicbattle.commands.Reload;
import com.github.ctirpak.magicbattle.commands.SetLocation;

public class CommandManager implements CommandExecutor {
	//private TreeSet<MagicCommand> cmds = new TreeSet<MagicCommand>();
	private ArrayList<MagicCommand> cmds = new ArrayList<MagicCommand>();
	
	public void setup() {
		cmds.add(new Create());
		cmds.add(new Delete());
		cmds.add(new ForceStart());
		cmds.add(new ForceStop());
		cmds.add(new Join());
		cmds.add(new Leave());
		cmds.add(new Reload());
		cmds.add(new SetLocation());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			MessageManager.getInstance().msg(sender, MessageType.BAD, "You must be a Player to use this command");
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("magicbattle")) {
			if(args.length == 0) {
				for(MagicCommand mc : cmds) {
					MessageManager.getInstance().msg(sender, MessageType.INFO, "/mb " + aliases(mc) + " " + mc.getUsage() + " - " + mc.getMessage());
				}
				return true;
			}

			MagicCommand c = getCommand(args[0]);
			
			if(c == null) {
				MessageManager.getInstance().msg(sender, MessageType.BAD, "That command doesn't exits!");
				return true;
			}
			
			//TreeSet<String> a = new TreeSet<String>(Arrays.asList(args));
			Vector<String> a = new Vector<String>(Arrays.asList(args));
			a.remove(0);
			args = a.toArray(new String[a.size()]);
			c.onCommand(p, args);
			return true;
		}
		
		return true;
	}
	
	private String aliases(MagicCommand cmd) {
		String fin = "";
		
		for(String a : cmd.getAliases()) {
			fin += a + " | ";
		}
		return fin.substring(0,fin.lastIndexOf(" | "));
	}
	
	private MagicCommand getCommand(String name) {
		for(MagicCommand cmd : cmds) {
			if(cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
		}
		return null;
	}
}

