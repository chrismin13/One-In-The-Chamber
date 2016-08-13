package fr.MaGiikAl.OneInTheChamber.InGameEvents;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.MaGiikAl.OneInTheChamber.Arena.Arena;
import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;
import fr.MaGiikAl.OneInTheChamber.Arena.PlayerArena;
import fr.MaGiikAl.OneInTheChamber.Arena.Status;
import fr.MaGiikAl.OneInTheChamber.Arena.Type;
import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;
import fr.MaGiikAl.OneInTheChamber.Utils.UtilChatColor;

public class PlayerDeath implements Listener{

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e){

		if(e.getEntityType() == EntityType.PLAYER){

			final Player player = e.getEntity();

			if(ArenaManager.getArenaManager().isInArena(player)){

				Arena arena = ArenaManager.getArenaManager().getArenaByPlayer(player);

				if(arena.getStatus() == Status.INGAME){

					PlayerArena pa = PlayerArena.getPlayerArenaByPlayer(player);
					
					player.setHealth(20);

					e.setDeathMessage("");
					e.getDrops().removeAll(e.getDrops());
					e.setDroppedExp(0);
					if(pa.getArena().getType() == Type.LIVES){
						pa.setLives(pa.getLives() -1);
					}

					if(arena.getPlayers().contains(PlayerArena.getPlayerArenaByPlayer(player.getKiller()))){

						File fichier_language = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Language.yml");
						FileConfiguration Language = YamlConfiguration.loadConfiguration(fichier_language);

						final String PartiePerdue = UtilChatColor.colorizeString(Language.getString("Language.Arena.Player_lost"));

						if(ArenaManager.getArenaManager().isInArena(player)){

							if(arena.getStatus() == Status.INGAME){

								new BukkitRunnable() {
									@Override
									public void run() {

										if(pa.getLives() < 1){

											String JoueurAPerdu = UtilChatColor.colorizeString(Language.getString("Language.Arena.Broadcast_player_lost")).replaceAll("%player", player.getName());

											arena.removePlayer(player, PartiePerdue, JoueurAPerdu);

										}else{
											Random rand = new Random();
											int nbAlea = rand.nextInt(arena.getSpawnsLocations().size());

											pa.getPlayer().teleport(arena.getSpawnsLocations().get(nbAlea));
											pa.loadGameInventory();
											pa.getArena().updateScores();
										}

									}
								}.runTaskLater(OneInTheChamber.instance, 1L);

							}else if(arena.getStatus() == Status.STARTING || arena.getStatus() == Status.JOINABLE){

								new BukkitRunnable() {
									@Override
									public void run() {
										player.teleport(arena.getStartLocation());
									}
								}.runTaskLater(OneInTheChamber.instance, 1L);

							}
						}

						if(player.getKiller().getName() != player.getName()){

							PlayerArena killer = PlayerArena.getPlayerArenaByPlayer(player.getKiller());

							killer.addArrow();

							File fichier_killer = new File(OneInTheChamber.instance.getDataFolder() + File.separator + "Players" + File.separator + killer.getName() + ".yml");
							FileConfiguration Killer = YamlConfiguration.loadConfiguration(fichier_killer);

							Killer.set("Kills", Killer.getInt("Kills") +1);
							Killer.set("Coins", Killer.getInt("Coins") +2);

							killer.setScore(killer.getScore() + 1);

							try {
								Killer.save(fichier_killer);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							arena.updateScores();

							String DeathMessage = UtilChatColor.colorizeString(Language.getString("Language.Arena.Death_message")).replaceAll("%killer", player.getKiller().getName()).replaceAll("%player", player.getName());

							arena.broadcast(DeathMessage);

							if(killer.getArena().getType() == Type.POINTS){

								if(killer.getScore() == killer.getArena().getMaxPoints()){

									String pourJoueurs = UtilChatColor.colorizeString(Language.getString("Language.Arena.Broadcast_player_win")).replaceAll("%player", player.getKiller().getName());

									killer.getArena().stop(pourJoueurs, Status.JOINABLE);
									killer.getArena().win(killer.getPlayer());
								}
							}

						}

					}
				}
			}

		}

	}
}

