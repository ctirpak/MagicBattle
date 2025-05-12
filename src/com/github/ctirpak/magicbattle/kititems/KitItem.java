package com.github.ctirpak.magicbattle.kititems;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.ctirpak.magicbattle.MagicBattle;

public abstract class KitItem {
	private final String name;
	private final ChatColor color;
	private final float manaCost;
	private final Material material;
	private final ItemStack item;
	private final List<String> lore;
	
	protected static final Logger logger = Logger.getLogger(MagicBattle.class.getName());
	
	public KitItem(String name, ChatColor color, float manaCost, Material material, String lore) {
		this.name = name;
		this.color = color;
		this.manaCost = manaCost;
		this.material = material;
		this.lore = Arrays.asList(lore);
		
		ItemStack i = new ItemStack(this.material, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(getFullName());
		im.setLore(this.lore);
		i.setItemMeta(im);
		
		this.item = i;
	}
	
	public String getName() {
		return name;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public String getFullName() {
		return color + name;
	}
	
	public float getManaCost() {
		return manaCost;
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
	public abstract void run(PlayerInteractEvent e);
	
	
}
