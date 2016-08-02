package fr.MaGiikAl.OneInTheChamber.Main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.MaGiikAl.OneInTheChamber.Arena.Arena;
import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;
import fr.MaGiikAl.OneInTheChamber.Commands.MyCommandExecutor;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.BlockBreak;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.BlockPlace;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.DoubleJump;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.EntityDamage;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.EntityDamageByEntity;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.FoodLevelChange;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.InventoryClick;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerChat;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerCommandPreprocess;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerDeath;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerDropItem;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerMove;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerPickupItem;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerQuit;
import fr.MaGiikAl.OneInTheChamber.InGameEvents.PlayerRespawn;
import fr.MaGiikAl.OneInTheChamber.Sign.PlayerClick;
import fr.MaGiikAl.OneInTheChamber.Sign.SignBreak;
import fr.MaGiikAl.OneInTheChamber.Sign.SignChange;
import fr.MaGiikAl.OneInTheChamber.Sign.SignManager;
import fr.MaGiikAl.OneInTheChamber.Utils.Updater;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilSendMessage;
import fr.MaGiikAl.OneInTheChamber.Utils.Updater.UpdateResult;
import fr.MaGiikAl.OneInTheChamber.Utils.Updater.UpdateType;

public class OneInTheChamber extends JavaPlugin implements Listener{

	public static JavaPlugin instance;

	public void onEnable() {
		instance = this;

		System.out.println("[OneInTheChamber] Plugin by MaGiikAl (Visit my youtube channel !)");

		loadConfiguration();

		ArenaManager.loadArenas();
		SignManager.loadSigns();
		if(ArenaManager.getArenaManager().getArenas() != null){
			for(Arena arena : ArenaManager.getArenaManager().getArenas()){
				SignManager.updateSigns(arena);
			}
		}

		getCommand("oitc").setExecutor(new MyCommandExecutor());

		registerEvents();

		saveDefaultConfig();

		if(getConfig().getBoolean("auto-update") == true){
			new Updater(this, 77090, this.getFile(), UpdateType.DEFAULT, true);
		}
	}

	public void onDisable() {
		ArenaManager.getArenaManager().stopArenas();
		System.out.println("[OneInTheChamber] Plugin by MaGiikAl (Visit my youtube channel !)");

	}

	public void registerEvents(){
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(this, this);
		pm.registerEvents(new PlayerDeath(), this);		
		pm.registerEvents(new PlayerQuit(), this);		
		pm.registerEvents(new PlayerRespawn(), this);		
		pm.registerEvents(new EntityDamage(), this);		
		pm.registerEvents(new EntityDamageByEntity(), this);		
		pm.registerEvents(new PlayerPickupItem(), this);		
		pm.registerEvents(new PlayerDropItem(), this);		
		pm.registerEvents(new InventoryClick(), this);		
		pm.registerEvents(new BlockBreak(), this);		
		pm.registerEvents(new BlockPlace(), this);		
		pm.registerEvents(new FoodLevelChange(), this);		
		pm.registerEvents(new PlayerCommandPreprocess(), this);
		pm.registerEvents(new SignChange(), this);
		pm.registerEvents(new SignBreak(), this);
		pm.registerEvents(new PlayerClick(), this);
		pm.registerEvents(new PlayerChat(), this);
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new DoubleJump(), this);
	}

	public void loadConfiguration(){

		File fichier_language = new File(getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		Language.options().header("Supports unicode characters ! \n<3, [*], [**], [p], [v], [+], [++]");

		Language.addDefault("Language.Prefix", "&4&l[&6&lOneInTheChamber&4&l]");

		Language.addDefault("Language.Updater.Update_available", "&eUpdate available !! Type /oitc update to update the plugin.");
		Language.addDefault("Language.Updater.Succesfully_update", "&eYou succesfully update the plugin ! Reload the server.");
		Language.addDefault("Language.Updater.No_update_available", "&eThere is no update available ! You're good.");
		Language.addDefault("Language.Updater.Error", "&eCouldn't update the plugin !");

		Language.addDefault("Language.Scoreboard.Name", "&4&l>> &6&lLeaderboard &4&l<<");

		Language.addDefault("Language.Stuff.Sword", "&6&lKNIFE");
		Language.addDefault("Language.Stuff.Bow", "&3&lSNIPER");
		Language.addDefault("Language.Stuff.Arrow", "&3&lAN AMMO");
		Language.addDefault("Language.Stuff.Redstone", "&4&lLIVES");

		Language.addDefault("Language.Stats.Start", "&2&l>>>>>> &4&l[&6&l%player's Stats&4&l] &2&l<<<<<<");
		Language.addDefault("Language.Stats.Kills", "  &4>>>>>  &9Kills: &b%kills  &4<<<<<");
		Language.addDefault("Language.Stats.Wins", "  &4>>>>>  &9Wins: &b%wins  &4<<<<<");
		Language.addDefault("Language.Stats.Coins", "  &4>>>>>  &9Coins: &b%coins  &4<<<<<");
		Language.addDefault("Language.Stats.Played", "  &4>>>>>  &9Played: &b%played  &4<<<<<");
		Language.addDefault("Language.Stats.Not_found", "&cPlayer not found !");
		Language.addDefault("Language.Stats.Never_play", "&cYou've never played to OITC !");

		Language.addDefault("Language.Help.Menu", "&2&l>>>>>> &4&l[&6&lOne In The Chamber's Help&4&l] &2&l<<<<<<");
		Language.addDefault("Language.Help.Update", "  &4>>>>>  &9&l/oitc update &bto update the plugin !");
		Language.addDefault("Language.Help.Join", "  &4>>>>>  &9&l/oitc join <arena> &bto join an arena");
		Language.addDefault("Language.Help.Lobby", "  &4>>>>>  &9&l/oitc lobby &bto join the lobby");
		Language.addDefault("Language.Help.Leave", "  &4>>>>>  &9&l/oitc leave &bto leave an arena");
		Language.addDefault("Language.Help.Stats", "  &4>>>>>  &9&l/oitc stats &bto look your skills");
		Language.addDefault("Language.Help.Reload", "  &4>>>>>  &9&l/oitc reload &bto reload the plugin");
		Language.addDefault("Language.Help.Stats_player", "  &4>>>>>  &9&l/oitc stats <player> &bto look the player's skills");

		Language.addDefault("Language.Help.Setup.Lobby", "  &4>>>>>  &9&l/oitc setlobby &bto set the lobby");
		Language.addDefault("Language.Help.Setup.Create", "  &4>>>>>  &9&l/oitc create <arena> &bto create an arena");
		Language.addDefault("Language.Help.Setup.Delete", "  &4>>>>>  &9&l/oitc delete|remove <arena> &b to delete an arena");
		Language.addDefault("Language.Help.Setup.Max_players", "  &4>>>>>  &9&l/oitc setmaxplayers <number> <arena> &bto set the maxplayers");
		Language.addDefault("Language.Help.Setup.Min_players", "  &4>>>>>  &9&l/oitc setminplayers <number> <arena> &bto set the minplayers");
		Language.addDefault("Language.Help.Setup.Add_spawn", "  &4>>>>>  &9&l/oitc addspawn <arena> &bto add a spawn");
		Language.addDefault("Language.Help.Setup.Private_chat", "  &4>>>>>  &9&l/oitc setprivatechat <true|false> <arena> &bto set or not the private chat");
		Language.addDefault("Language.Help.Setup.Start", "  &4>>>>>  &9&l/oitc setstart <arena> &bto set the start location");
		Language.addDefault("Language.Help.Setup.Display_name", "  &4>>>>>  &9&l/oitc setdisplayname <arena> <display_name> &bto change the display name");
		Language.addDefault("Language.Help.Setup.Type", "  &4>>>>>  &9&l/oitc settype <type> <arena> &bto set the type of an arena");
		Language.addDefault("Language.Help.Setup.Lives", "  &4>>>>>  &9&l/oitc setlives <number> <arena> &bto set the lives of an arena");
		Language.addDefault("Language.Help.Setup.Points", "  &4>>>>>  &9&l/oitc setpoints <number> <arena> &bto set the points of an arena");
		Language.addDefault("Language.Help.Setup.Countdown", "  &4>>>>>  &9&l/oitc setcountdown <number> <arena> &bto set the countdown of an arena");
		Language.addDefault("Language.Help.Setup.Active", "  &4>>>>>  &9&l/oitc active <arena> &bto active an arena");
		Language.addDefault("Language.Help.Setup.Disactive", "  &4>>>>>  &9&l/oitc disactive <arena> &bto disactive an arena");
		Language.addDefault("Language.Help.Admin.Stop", "  &4>>>>>  &9&l/oitc stop <arena> &bto stop an arena");

		Language.addDefault("Language.Arena.Chat_format", "&2&l[&e&l%player&2&l] &a=> &b&l%message");
		Language.addDefault("Language.Arena.Full", "&3The arena is full ! You can't join...");
		Language.addDefault("Language.Arena.In_game", "&3The arena is in game ! You can't join...");
		Language.addDefault("Language.Arena.Disable", "&3The arena is disactivate ! You can't join..");
		Language.addDefault("Language.Arena.Not_in_game", "&3You're not in game ! You can't leave...");
		Language.addDefault("Language.Arena.Already_in_game", "&3You are already in game !");
		Language.addDefault("Language.Arena.Not_active", "&3This arena isn't active !");
		Language.addDefault("Language.Arena.Join", "&e&l%player &3joined the arena ! &6%number/%max");
		Language.addDefault("Language.Arena.StartsIn", "&3The game starts in &l%time &3seconds.");
		Language.addDefault("Language.Arena.Not_enough_players_to_launch", "&3There is not enough players to launch the game !");
		Language.addDefault("Language.Arena.Start", "&3The game started ! Good luck...");
		Language.addDefault("Language.Arena.Broadcast_leave", "&e&l%player &3left the arena !");
		Language.addDefault("Language.Arena.Leave", "&3You leave the arena !");
		Language.addDefault("Language.Arena.Win", "&3&lYou win the game !");
		Language.addDefault("Language.Arena.Broadcast_player_lost", "&e&l%player &3lost the game !");
		Language.addDefault("Language.Arena.Broadcast_player_win", "&e&l%player &3win the game !");
		Language.addDefault("Language.Arena.Player_lost", "&3You lost the game !");
		Language.addDefault("Language.Arena.End", "&3End of the game !");
		Language.addDefault("Language.Arena.Reload", "&3You reload the plugin !");
		Language.addDefault("Language.Arena.Server_reload", "&3Reload ! You leave the arena...");
		Language.addDefault("Language.Arena.Death_message", "&a&l%killer &3smashed &a&l%player &3!");

		Language.set("Language.Signs.Name", null);
		Language.addDefault("Language.Signs.Name_lives", "&4[&6&lOITC<3&4]");
		Language.addDefault("Language.Signs.Name_points", "&4[&6&lOITC[**]&4]");

		Language.set("Language.Signs.Arena", null);
		Language.addDefault("Language.Signs.Arena_lives", "&0&l%arena<3");
		Language.addDefault("Language.Signs.Arena_points", "&0&l%arena[**]");

		Language.addDefault("Language.Signs.Joinable", "&a[Joinable]");
		Language.addDefault("Language.Signs.InGame", "&8[InGame]");
		Language.addDefault("Language.Signs.Starting", "&6[Starting]");
		Language.addDefault("Language.Signs.Not_joinable", "&4[NotJoinable]");
		Language.addDefault("Language.Signs.Number_in_arena", "&8%number/%maxplayers");
		Language.addDefault("Language.Signs.Succesfully_set", "&3You succesfully set a join sign !");
		Language.addDefault("Language.Signs.Missing_arena", "&cYou must insert an arena name in the line 2 !");
		Language.addDefault("Language.Signs.Break", "&cYou've broken an OITC sign !");
		Language.addDefault("Language.Signs.Must_to_be_sneak", "&cYou must to be sneaking if you want to destroy an OITC sign !");

		Language.addDefault("Language.Error.Arena_does_not_exist", "&cThe arena &l%arena &cdoesn't exist !");
		Language.addDefault("Language.Error.Not_enough_spawns", "&cNot enough spawns defined to join !");
		Language.addDefault("Language.Error.No_start_location", "&cThe start location isn't set !");
		Language.addDefault("Language.Error.Not_ready", "&cThe arena isn't ready to play !");
		Language.addDefault("Language.Error.Not_permission", "&cYou don't have the permission !");
		Language.addDefault("Language.Error.Lobby_is_not_set", "&cThe lobby isn't set !");
		Language.addDefault("Language.Error.No_commands", "&3You can't use commands in a game ! ==> &l/oitc leave &3to leave the arena...");
		Language.addDefault("Language.Error.Wrong_command", "&cWrong command ! Type /oitc to see all the commands !");
		Language.addDefault("Language.Error.Not_enough_args", "&cNot enough arguments for this command !");
		Language.addDefault("Language.Error.Bad_args", "&cBad type of argument !");

		Language.addDefault("Language.Setup.Arena_succesfully_created", "&cThe arena &l%arena &chas been succesfully created !");
		Language.addDefault("Language.Setup.Arena_succesfully_deleted", "&cThe arena &l%arena &chas been succesfully deleted !");
		Language.addDefault("Language.Setup.Arena_already_exists", "&cThis arena &l%arena &calready exists !");
		Language.addDefault("Language.Setup.Lobby_succesfully_set", "&cThe lobby has been succesfully set !");
		Language.addDefault("Language.Setup.Display_name", "&cThe display name has been changed to &l%displayname &cfor the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Private_chat", "&cThe private chat has been set to &l%value &cfor the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Spawn_succesfully_added", "&cSpawn succesfully added for the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Lives", "&cThe number of lives has been set for the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Type", "&cThe type has been set for the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Points", "&cThe number of points has been set for the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Countdown", "&cThe countdown has been set for the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Start_set", "&cThe start of the arena &l%arena &chas been succesfully set !");
		Language.addDefault("Language.Setup.Max_players_succesfully_set", "&cMax players set for the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Max_players_too_small", "&cMax players smaller than min players !");
		Language.addDefault("Language.Setup.Min_players_succesfully_set", "&cMinimum players set for the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Min_players_too_big", "&cMin players bigger than max players !");
		Language.addDefault("Language.Setup.Active", "&cYou active the arena &l%arena &c!");
		Language.addDefault("Language.Setup.Disactive", "&cYou disactive the arena &l%arena &c!");

		Language.addDefault("Language.Force.Stop", "&cYou stopped the arene %arena !");
		Language.addDefault("Language.Force.Can_not_stop", "&cYou can't stop the arena %arena ! It isn't in game...");

		Language.options().copyDefaults(true);

		try {
			Language.save(fichier_language);
		} catch (IOException e) {
			e.printStackTrace();
		}


		File fichier_lobby = new File(getDataFolder() + File.separator + "Lobby.yml");
		FileConfiguration Lobby = YamlConfiguration.loadConfiguration(fichier_lobby);

		if(!fichier_lobby.exists()){

			Lobby.addDefault("Lobby.World", "");
			Lobby.addDefault("Lobby.X", "");
			Lobby.addDefault("Lobby.Y", "");
			Lobby.addDefault("Lobby.Z", "");
			Lobby.addDefault("Lobby.Yaw", "");
			Lobby.addDefault("Lobby.Pitch", "");

			Lobby.options().copyDefaults(true);

			try {
				Lobby.save(fichier_lobby);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File dossier_players = new File(getDataFolder() + File.separator + "Players");

		if(!dossier_players.exists()){

			dossier_players.mkdir();
		}

		File dossier_arenas = new File(getDataFolder() + File.separator + "Arenas");

		if(!dossier_arenas.exists()){

			dossier_arenas.mkdir();
		}

		File fichier_signs = new File(getDataFolder() + File.separator + "Signs.yml");
		YamlConfiguration signs = YamlConfiguration.loadConfiguration(fichier_signs);

		if(!fichier_signs.exists()){

			signs.set("Arenas", "");

			try {
				signs.save(fichier_signs);;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Updater update(UpdateType type, boolean announce){
		return new Updater(instance, 77090, this.getFile(), type, announce);
	}

	public static OneInTheChamber getInstance(){
		return (OneInTheChamber) instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){

		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		Player p = e.getPlayer();
		if(p.hasPermission("oitc.update")){
			Updater updater = update(UpdateType.NO_DOWNLOAD, false);
			if(updater.getResult() == UpdateResult.UPDATE_AVAILABLE){
				String updateAvailable = Language.getString("Language.Updater.Update_available");
				UtilSendMessage.sendMessage(p, updateAvailable);
			}else{
				return;
			}
		}
	}
	
}
