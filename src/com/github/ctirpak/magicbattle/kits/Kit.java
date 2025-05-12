package com.github.ctirpak.magicbattle.kits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.ctirpak.magicbattle.MessageManager;
import com.github.ctirpak.magicbattle.MessageManager.MessageType;
import com.github.ctirpak.magicbattle.kititems.KitItem;

public abstract class Kit {
	private final String name;
	private final List<KitItem> items;
	
    public Kit(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<KitItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    protected void addItem(KitItem item) {
        items.add(item);
    }

    public void applyToPlayer(Player player) {
        for (KitItem item : items) {
            player.getInventory().addItem(item.getItemStack());
        }
        MessageManager.getInstance().msg(player, MessageType.INFO, "You have received the " + name + " kit!");
    }
	
    public void handleInteraction(PlayerInteractEvent e) {
    	ItemMeta im = e.getItem().getItemMeta();
		for (KitItem item : items) {
			if (im.getDisplayName().equals(item.getFullName())) {
				item.run(e);
				break;
			}
		}
	}

}
