package fr.MaGiikAl.OneInTheChamber.InGameEvents;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.MaGiikAl.OneInTheChamber.Arena.Arena;
import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;
import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilChatColor;

public class PlayerQuit implements Listener{

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent e){

		Player p = e.getPlayer();
		
		if(ArenaManager.getArenaManager().isInArena(p)){
			File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
			FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

			String QuitteArene = UtilChatColor.colorizeString(Language.getString("Language.Arena.Leave"));
			String JoueurQuitteArene = UtilChatColor.colorizeString(Language.getString("Language.Arena.Broadcast_leave")).replaceAll("%player", p.getName());

			Arena arena = ArenaManager.getArenaManager().getArenaByPlayer(p);
			arena.removePlayer(p, QuitteArene, JoueurQuitteArene);

		}
	}
}
