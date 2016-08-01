package fr.MaGiikAl.OneInTheChamber.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventorySaver {

	public static Map<String, ItemStack[]> inventory = new HashMap<String, ItemStack[]>();
	public static Map<String, ItemStack[]> armour = new HashMap<String, ItemStack[]>();
	public static Map<String, String> gamemode = new HashMap<String, String>();
	public static Map<String, Integer> level = new HashMap<String, Integer>();
	public static Map<String, Float> experience = new HashMap<String, Float>();
	public static Map<String, Double> health = new HashMap<String, Double>();
	public static Map<String, Integer> foodlevel = new HashMap<String, Integer>();

	public static void savePlayerInventory(Player p){

		String pn = p.getName();

		ItemStack[] inv = p.getInventory().getContents();
		ItemStack[] am = p.getInventory().getArmorContents();
		String gm = p.getGameMode().toString();
		int lvl = p.getLevel();
		float exp = p.getExp();
		double hl = p.getHealth();
		int fl = p.getFoodLevel();

		inventory.put(pn, inv);
		armour.put(pn, am);
		gamemode.put(pn, gm);
		level.put(pn, lvl);
		experience.put(pn, exp);
		health.put(pn, hl);
		foodlevel.put(pn, fl);

	}

	public static void restorePlayerInventory(final Player p){

		String pn = p.getName();

		p.getInventory().clear();
		p.getInventory().setArmorContents(null);

		p.getInventory().setContents(inventory.get(pn));
		p.getInventory().setArmorContents(armour.get(pn));
		p.setGameMode(GameMode.valueOf(gamemode.get(pn)));
		p.setLevel(level.get(pn));
		p.setHealth(health.get(pn));
		p.setExp(experience.get(pn));
		p.setFoodLevel(foodlevel.get(pn));

		p.updateInventory();
		
	}

	public static void clearPlayerInventory(final Player p){
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setLevel(0);
		p.setHealth(p.getMaxHealth());
		p.setExp(0);
		p.setFoodLevel(20);
		p.setFireTicks(0);

		p.updateInventory();
	}
}
