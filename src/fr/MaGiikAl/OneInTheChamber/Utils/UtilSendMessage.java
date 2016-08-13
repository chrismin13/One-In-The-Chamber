package fr.MaGiikAl.OneInTheChamber.Utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;

public class UtilSendMessage {

	public static void sendMessage(Player player, String message){
		
		File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
		FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);
		
		String prefixe = UtilChatColor.colorizeString(Language.getString("Language.Prefix"));
		
//		player.sendMessage(prefixe + " " + UtilChatColor.colorizeString(message));
		
        Actionbar actionBar = new Actionbar(prefixe + " " + UtilChatColor.colorizeString(message));
        actionBar.sendToPlayer(player);
		
	}
}
