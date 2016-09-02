package fr.MaGiikAl.OneInTheChamber.Arena;

import java.io.File;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilChatColor;

public class Timer extends BukkitRunnable{

	public Arena arena;
	public int countdown;

	public Timer(Arena arena, int countdown){
		this.arena = arena;
		this.countdown = countdown;
	}

	File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
	final FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

	final String PasAssezJoueurs = UtilChatColor.colorizeString(Language.getString("Language.Arena.Not_enough_players_to_launch"));

	@Override
	public void run() {
		if(this.arena.getPlayers().size() >= this.arena.getMinPlayers() && !this.arena.getPlayers().isEmpty()){
			if(countdown > 0){
				for(PlayerArena pa : this.arena.getPlayers()){
					pa.setlevel(countdown);
				}
				if(countdown == this.arena.getCountdownBeforeStart() || countdown == 10){
					String demarrageArene = UtilChatColor.colorizeString(Language.getString("Language.Arena.StartsIn")).replaceAll("%time", countdown + "");
					this.arena.broadcast(demarrageArene);
				}

				if(countdown <= 5){
					for(PlayerArena pa : this.arena.getPlayers()){
						pa.playSound(Sound.BLOCK_GLASS_BREAK, 1.0F);;
					}
				}
				countdown--;
			}else if(countdown == 0){
				for(PlayerArena pa : this.arena.getPlayers()){
					pa.setlevel(0);
				}
				this.arena.start();
				this.cancel();
			}

		}else{
			for(PlayerArena pa : this.arena.getPlayers()){
				pa.setlevel(0);
				pa.sendMessage(PasAssezJoueurs);
			}
			if(this.arena.getStatus() != Status.NOTJOINABLE){
				this.arena.setStatus(Status.JOINABLE);
			}
			this.cancel();
		}
	}

}
