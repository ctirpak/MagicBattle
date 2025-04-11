package com.github.ctirpak.magicbattle.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.ctirpak.magicbattle.Arena;
import com.github.ctirpak.magicbattle.ArenaManager;
import com.github.ctirpak.magicbattle.LocationUtil;
import com.github.ctirpak.magicbattle.MagicBattle;
import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.SettingsManager;

/**
 * This class implements the {@link Listener} interface to handle events related
 * to MagicBattle lobby signs. It manages the creation, loading, and interaction
 * with these signs, allowing players to join arenas by right-clicking them.
 *
 * <p>The class maintains a mapping of {@link Sign} objects to the arena IDs they
 * represent. It loads existing signs from the plugin's configuration on startup
 * and handles the creation of new signs when a player places a sign with the
 * "[MagicBattle]" tag. It also updates the sign's text to reflect the arena's
 * current player count and state. Finally, it listens for player interactions
 * with these signs, allowing players to join the associated arena by right-clicking.</p>
 */
public class SignManager implements Listener {
	/*
	 * Recognize signs ([MagicBattle] -> <sign>)
	 * Saving and Loading signs
	 * Setting signs
	 */

	private static final Logger logger = Logger.getLogger(MagicBattle.class.getName());

	private static HashMap<Sign, Integer> signs = new HashMap<Sign, Integer>();

	/**
	 * Constructor for the {@code SignManager} class.
	 *
	 * <p>This constructor initializes the sign management system. It first checks
	 * if the "signs" configuration section exists in the lobby signs configuration.
	 * If not, it creates it. Then, it proceeds to load any existing MagicBattle
	 * lobby signs from the configuration. For each sign found, it retrieves its
	 * location and the associated arena number. It then attempts to get the
	 * {@link Sign} object at that location and stores it in the {@code signs}
	 * HashMap, mapping the {@link Sign} to its corresponding arena ID.</p>
	 *
	 * <p>The constructor also logs information about the loaded signs and their
	 * associated arena numbers for debugging and informational purposes.</p>
	 */
	public SignManager() {
		/*
		 * signs:
		 * 1:
		 * location:
		 * world:
		 * x:
		 * y:
		 * z:
		 * arenaNumber: 1
		 */

		if (!SettingsManager.getLobbySigns().contains("signs")) {
			SettingsManager.getLobbySigns().createConfigurationSection("signs");
		}

		//loads signs
		for (String str : SettingsManager.getLobbySigns().<ConfigurationSection>get("signs").getKeys(false)) {
			logger.info("lobby sign: " + str);
			ConfigurationSection section = SettingsManager.getLobbySigns().get("signs." + str);
			logger.info("lobby sign: " + str + " section: " + section);

			Location loc = LocationUtil.locationFromConfig(section.getConfigurationSection("location"), false);
			logger.info("config section: " + section.getConfigurationSection("location"));
			logger.info("location: " + loc);
			Sign s = (Sign) loc.getBlock().getState();

			logger.info("Sign: " + s.getSide(Side.FRONT).getLine(0).toString() + "; " + s.getSide(Side.FRONT).getLine(1).toString() + "; " + s.getSide(Side.FRONT).getLine(2).toString() + "; " + s.getSide(Side.FRONT).getLine(3).toString() + "; ");
			signs.put(s, section.getInt("arenaNumber"));
			logger.info("Arena Number: " + section.getInt("arenaNumber"));
		}
		logger.info("Sign count: " + signs.size());
	}

	/**
	 * Retrieves a list of {@link Sign} objects associated with a specific {@link Arena}.
	 *
	 * <p>This method iterates through the {@code signs} HashMap and checks if the
	 * arena ID associated with each sign matches the ID of the provided {@link Arena}.
	 * If a match is found, the {@link Sign} object is added to the list that is
	 * returned.</p>
	 *
	 * @param a The {@link Arena} for which to retrieve the associated signs.
	 * @return An {@link ArrayList} of {@link Sign} objects linked to the specified arena.
	 */
	public static ArrayList<Sign> getSigns(Arena a) {
		ArrayList<Sign> s = new ArrayList<Sign>();
		logger.info("getSigns:");
		for (Sign sign : signs.keySet()) {
			logger.info("Sign: " + sign.toString());
			logger.info("Arena ID: " + a.getID());
			logger.info("Sign: " + sign);
			logger.info("Sign: " + signs.get(sign));
			logger.info("Arena ID Calc: " + ArenaManager.getInstance().getArena(signs.get(sign)));
			//			if(ArenaManager.getInstance().getArena(signs.get(sign)) == a) {
			if (signs.get(sign) == a.getID()) {
				s.add(sign);
			}
		}
		return s;
	}

	/**
	 * Handles the {@link SignChangeEvent}, which is triggered when a player places
	 * or modifies a sign.
	 *
	 * <p>This method checks if the first line of the placed sign is "[MagicBattle]".
	 * If it is, it attempts to parse the second line as an integer, which should
	 * represent the ID of the arena the sign is for. If the second line is not a
	 * valid number, the player is informed. It then checks if an arena with the
	 * parsed ID exists. If not, the player is notified.</p>
	 *
	 * <p>If a valid arena ID is provided, a new configuration section for this
	 * lobby sign is created in the lobby signs configuration, storing the sign's
	 * location and the associated arena ID. The {@link Sign} object and its
	 * corresponding arena ID are also added to the {@code signs} HashMap. Finally,
	 * the third and fourth lines of the sign are updated to display the current
	 * number of players in the arena and the arena's current state, respectively.</p>
	 *
	 * @param e The {@link SignChangeEvent} triggered by a player placing or modifying a sign.
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[MagicBattle]")) {
			int id;
			try {
				id = Integer.parseInt(e.getLine(1));
			} catch (Exception ex) {
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, "That is not a valid arena number!");
				return;
			}

			Arena a = ArenaManager.getInstance().getArena(id);
			if (a == null) {
				MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, "That is not a valid arena!");
				return;
			}

			ConfigurationSection section = SettingsManager.getLobbySigns().createConfigurationSection("signs." + SettingsManager.getLobbySigns().<ConfigurationSection>get("signs").getKeys(true).size() + 1);

			ConfigurationSection location = section.createSection("location");
			location.set("world", e.getBlock().getLocation().getWorld().getName());
			location.set("x", e.getBlock().getLocation().getX());
			location.set("y", e.getBlock().getLocation().getY());
			location.set("z", e.getBlock().getLocation().getZ());

			section.set("arenaNumber", id);

			signs.put((Sign) e.getBlock().getState(), a.getID());

			e.setLine(2, a.getCurrentPlayers() + " Players");
			e.setLine(3, a.getState().toString());
		}
	}

	/**
	 * Handles the {@link PlayerInteractEvent}, which is triggered when a player
	 * interacts with a block.
	 *
	 * <p>This method checks if the player right-clicked a block and if that block's
	 * state is a {@link Sign}. If it is a {@link Sign}, it further checks if the
	 * first line of the sign's text (on the front side) is "[MagicBattle]". If it
	 * is, it attempts to parse the second line as an integer, which should be the
	 * ID of the arena associated with the sign. It then retrieves the {@link Arena}
	 * object with that ID and adds the interacting player to that arena using the
	 * {@link Arena#addPlayer(Player)} method.</p>
	 *
	 * @param e The {@link PlayerInteractEvent} triggered by a player interacting with a block.
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if (e.getClickedBlock() == null || e.getClickedBlock().getState() == null) return;

		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getSide(Side.FRONT).getLine(0).equalsIgnoreCase("[MagicBattle]")) {
				try {
					int arenaId = Integer.parseInt(s.getSide(Side.FRONT).getLine(1));
					ArenaManager.getInstance().getArena(arenaId).addPlayer(e.getPlayer());
				} catch (NumberFormatException ex) {
					// Log an error if the arena ID on the sign is not a valid number.
					logger.warning("Invalid arena ID on MagicBattle sign at " + s.getLocation());
					MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, "This sign is incorrectly configured!");
				} catch (NullPointerException ex) {
					// Log an error if the arena with the given ID does not exist.
					logger.warning("No MagicBattle arena found with ID " + s.getSide(Side.FRONT).getLine(1) + " at " + s.getLocation());
					MessageManager.getInstance().msg(e.getPlayer(), MessageType.BAD, "This arena does not exist!");
				}
			}
		}
	}

}