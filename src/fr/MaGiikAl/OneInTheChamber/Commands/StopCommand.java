package fr.MaGiikAl.OneInTheChamber.Commands;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.MaGiikAl.OneInTheChamber.Arena.Arena;
import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;
import fr.MaGiikAl.OneInTheChamber.Arena.Status;
import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilSendMessage;

public class StopCommand implements BasicCommand{
	
	public boolean onCommand(Player player, String[] args) {
		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);
		
		if(player.hasPermission(getPermission())){
			if(args.length < 1){
				String notEnoughArgs = Language.getString("Language.Error.Not_enough_args");
				UtilSendMessage.sendMessage(player, notEnoughArgs);
				return true;
			}if(args.length > 0){
				String arenaName = args[0];
				if(ArenaManager.getArenaManager().getArenaByName(arenaName) != null){
					Arena arena = ArenaManager.getArenaManager().getArenaByName(arenaName);
					if(arena.getStatus() != Status.JOINABLE || !arena.getPlayers().isEmpty()){
						String end = Language.getString("Language.Arena.End");
						String succes = Language.getString("Language.Force.Stop").replaceAll("%arena", arenaName);
						arena.stop(end, Status.JOINABLE);
						UtilSendMessage.sendMessage(player, succes);
						return true;
					}else{
						String cantStop = Language.getString("Language.Force.Can_not_stop").replaceAll("%arena", arenaName);
						UtilSendMessage.sendMessage(player, cantStop);
						return true;
					}
				}else{
					String doesntExist = Language.getString("Language.Error.Arena_does_not_exist").replaceAll("%arena", arenaName);
					UtilSendMessage.sendMessage(player, doesntExist);
					return true;
				}
			}
		}else{
			String notPerm = Language.getString("Language.Error.Not_permission");
			UtilSendMessage.sendMessage(player, notPerm);
			return true;
		}
		return true;
	}

	public String help(Player player) {

		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

		String help = Language.getString("Language.Help.Admin.Stop");

		if(player.hasPermission(getPermission())){

			return help;
		}
		return "";
	}

	public String getPermission() {
		return "oitc.stop";
	}

}
