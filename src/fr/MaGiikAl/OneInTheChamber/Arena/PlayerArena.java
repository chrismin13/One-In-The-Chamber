package fr.MaGiikAl.OneInTheChamber.Arena;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;
import fr.MaGiikAl.OneInTheChamber.Utils.InventorySaver;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilChatColor;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilSendMessage;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagDouble;
import net.minecraft.server.v1_9_R1.NBTTagInt;
import net.minecraft.server.v1_9_R1.NBTTagList;
import net.minecraft.server.v1_9_R1.NBTTagString;

public class PlayerArena {

	public static HashMap<String, PlayerArena> players = new HashMap<String, PlayerArena>();

	private Player player;
	private Arena arena;
	private int score = 0;
	private int lives;

	public PlayerArena(Player player, Arena arena){
		this.player = player;
		this.lives = arena.getLives();

		this.arena = arena;
		players.put(player.getName(), this);
		createConfig();
	}

	public void remove(){
		players.remove(this.player.getName());
	}

	public static PlayerArena getPlayerArenaByPlayer(Player player){
		if(players.containsKey(player.getName())){
			return players.get(player.getName());
		}
		return null;
	}

	public Arena getArena(){
		return this.arena;
	}

	public int getLives(){
		return this.lives;
	}

	public int getScore(){
		return this.score;
	}

	public Player getPlayer(){
		return this.player;
	}

	public String getName(){
		return this.player.getName();
	}

	public String getDisplayName(){
		return this.player.getDisplayName();
	}

	public Location getLocation(){
		return this.player.getLocation();
	}

	public boolean isDead(){
		return this.player.isDead();
	}

	public void setlevel(int xp){
		this.player.setLevel(xp);
	}

	public void setLives(int lives){
		this.lives = lives;
	}

	public void setScore(int score){
		this.score = score;
		this.arena.updateScores();
	}

	public void sendMessage(String message){
		UtilSendMessage.sendMessage(this.player, message);
	}

	public void reset(){
		ScoreboardManager scm = OneInTheChamber.instance.getServer().getScoreboardManager();
		Scoreboard board= scm.getNewScoreboard();

		this.player.setScoreboard(board);

		this.lives = this.arena.getLives();

		this.score = 0;
	}

	public void loadGameInventory(){
		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		String sword = UtilChatColor.colorizeString(Language.getString("Language.Stuff.Sword"));
		String bow = UtilChatColor.colorizeString(Language.getString("Language.Stuff.Bow"));
		String arrow = UtilChatColor.colorizeString(Language.getString("Language.Stuff.Arrow"));
		String redstone = UtilChatColor.colorizeString(Language.getString("Language.Stuff.Redstone"));

		if(arena.getType().equals(Type.LIVES)){
			ItemStack redstone2 = new ItemStack(Material.REDSTONE, this.lives);

			ItemStack sword2 = new ItemStack(Material.STONE_SWORD);
			ItemStack bow2 = new ItemStack(Material.BOW);
			ItemStack arrow2 = new ItemStack(Material.SPECTRAL_ARROW);

			ItemMeta imredstone = redstone2.getItemMeta();
			ItemMeta imsword = sword2.getItemMeta();
			ItemMeta imbow = bow2.getItemMeta();
			ItemMeta imarrow = arrow2.getItemMeta();

			imredstone.setDisplayName(redstone);
			imsword.setDisplayName(sword);
			imbow.setDisplayName(bow);
			imarrow.setDisplayName(arrow);

			redstone2.setItemMeta(imredstone);
			sword2.setItemMeta(imsword);
			bow2.setItemMeta(imbow);
			arrow2.setItemMeta(imarrow);

            net.minecraft.server.v1_9_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(sword2);
            NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
           
            NBTTagList modifiers = new NBTTagList();
           
            // -- attack damage
            NBTTagCompound damage = new NBTTagCompound();
            damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
            damage.set("Name", new NBTTagString("generic.attackDamage"));
            damage.set("Amount", new NBTTagInt(6));
            damage.set("Operation", new NBTTagInt(0));
            damage.set("UUIDLeast", new NBTTagInt(894654));
            damage.set("UUIDMost", new NBTTagInt(2872));
            damage.set("Slot", new NBTTagString("mainhand"));
           
            // -- attack speed
            NBTTagCompound speed = new NBTTagCompound();
            speed.set("AttributeName", new NBTTagString("generic.attackSpeed"));
            speed.set("Name", new NBTTagString("generic.attackSpeed"));
            speed.set("Amount", new NBTTagDouble(1024));
            speed.set("Operation", new NBTTagInt(0));
            speed.set("UUIDLeast", new NBTTagInt(894654));
            speed.set("UUIDMost", new NBTTagInt(2872));
            speed.set("Slot", new NBTTagString("mainhand"));


            // -- apply modifiers
            modifiers.add(damage);
            modifiers.add(speed);
            compound.set("AttributeModifiers", modifiers);
           
            nmsStack.setTag(compound);
			sword2 = CraftItemStack.asBukkitCopy(nmsStack);			


			this.player.getInventory().setItem(0, sword2);
			this.player.getInventory().setItem(1, bow2);
			this.player.getInventory().setItem(2, arrow2);
			this.player.getInventory().setItem(8, redstone2);
		}
		if(arena.getType().equals(Type.POINTS)){


			ItemStack sword2 = new ItemStack(Material.STONE_SWORD);
			ItemStack bow2 = new ItemStack(Material.BOW);
			ItemStack arrow2 = new ItemStack(Material.SPECTRAL_ARROW);

			ItemMeta imsword = sword2.getItemMeta();
			ItemMeta imbow = bow2.getItemMeta();
			ItemMeta imarrow = arrow2.getItemMeta();

			imsword.setDisplayName(sword);
			imbow.setDisplayName(bow);
			imarrow.setDisplayName(arrow);

			sword2.setItemMeta(imsword);
			bow2.setItemMeta(imbow);
			arrow2.setItemMeta(imarrow);
			
			            net.minecraft.server.v1_9_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(sword2);
            NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
           
            NBTTagList modifiers = new NBTTagList();
           
            // -- attack damage
            NBTTagCompound damage = new NBTTagCompound();
            damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
            damage.set("Name", new NBTTagString("generic.attackDamage"));
            damage.set("Amount", new NBTTagInt(6));
            damage.set("Operation", new NBTTagInt(0));
            damage.set("UUIDLeast", new NBTTagInt(894654));
            damage.set("UUIDMost", new NBTTagInt(2872));
            damage.set("Slot", new NBTTagString("mainhand"));
           
            // -- attack speed
            NBTTagCompound speed = new NBTTagCompound();
            speed.set("AttributeName", new NBTTagString("generic.attackSpeed"));
            speed.set("Name", new NBTTagString("generic.attackSpeed"));
            speed.set("Amount", new NBTTagDouble(1024));
            speed.set("Operation", new NBTTagInt(0));
            speed.set("UUIDLeast", new NBTTagInt(894654));
            speed.set("UUIDMost", new NBTTagInt(2872));
            speed.set("Slot", new NBTTagString("mainhand"));


            // -- apply modifiers
            modifiers.add(damage);
            modifiers.add(speed);
            compound.set("AttributeModifiers", modifiers);
           
            nmsStack.setTag(compound);
			sword2 = CraftItemStack.asBukkitCopy(nmsStack);			

			this.player.getInventory().setItem(0, sword2);
			this.player.getInventory().setItem(1, bow2);
			this.player.getInventory().setItem(2, arrow2);

		}
	}

	public void playSound(Sound sound, float volume){
		this.player.playSound(this.getLocation(), sound, 1, volume);
	}

	public void teleportToLobby(){
		this.player.teleport(ArenaManager.getArenaManager().getLobbyLocation());
	}

	@SuppressWarnings("deprecation")
	public void clear(Objective obj){
		if(obj != null){
			obj.getScore(this.player).setScore(0);
			this.arena.updateScores();
		}
		InventorySaver.clearPlayerInventory(this.player);
		this.player.setGameMode(GameMode.ADVENTURE);	

		ScoreboardManager scm = Bukkit.getScoreboardManager();
		Scoreboard board = scm.getNewScoreboard();
		this.player.setScoreboard(board);
	}

	public void restore(){
		InventorySaver.restorePlayerInventory(this.player);
	}

	public void save(){
		InventorySaver.savePlayerInventory(this.player);
	}

	public void addArrow(){

		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		String arrow = UtilChatColor.colorizeString(Language.getString("Language.Stuff.Arrow"));

		ItemStack arrows = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta imarrow = arrows.getItemMeta();
		imarrow.setDisplayName(arrow);
		arrows.setItemMeta(imarrow);

		this.player.getInventory().addItem(arrows);

	}

	public void createConfig(){
		File fichier_player = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Players" + File.separator + this.player.getName() + ".yml");
		FileConfiguration playerFile = YamlConfiguration.loadConfiguration(fichier_player);

		if(!fichier_player.exists()){
			playerFile.set("Played", 0);
			playerFile.set("Wins", 0);
			playerFile.set("Kills", 0);
			playerFile.set("Coins", 0);
		}
		try {
			playerFile.save(fichier_player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public void setScoreboard(Objective obj){
		obj.getScore(this.player).setScore(this.score);
		Scoreboard board = obj.getScoreboard();
		this.player.setScoreboard(board);
	}
	public void tell(String message){
		this.player.sendMessage(UtilChatColor.colorizeString(message));
	}
}
