package com.github.ctirpak.magicbattle.listeners;

import java.util.HashMap;

import org.bukkit.block.Sign;
import org.bukkit.event.Listener;

import com.github.ctirpak.magicbattle.Arena;

public class SignManager implements Listener {
	/*
	 * Recognize signs ([MagicBattle] -> <sign>)
	 * Saving and Loading signs
	 * Setting signs
	 */
	
	private HashMap<Sign, Arena> signs = new HashMap<Sign, Arena>();
	
	public SignManager() {
		/*
		 * signs:
		 *   1:
		 *     location:
		 *       world:
		 *       x:
		 *       y:
		 *       z:
		 *     arenaNumber: 1
		 *     
		 */
	}

}
