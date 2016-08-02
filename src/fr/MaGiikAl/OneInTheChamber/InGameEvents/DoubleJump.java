package fr.MaGiikAl.OneInTheChamber.InGameEvents;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;

public class DoubleJump implements Listener {

	HashSet<String> enabled_users = new HashSet<String>();
	
	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event){
		Player player = event.getPlayer();
		if (!ArenaManager.getArenaManager().isInArena(player))
			return;
		enabled_users.add(player.getName());
		event.setCancelled(true);
		player.setAllowFlight(false);
		player.setFlying(false);
		player.setVelocity(player.getLocation().getDirection().multiply(1.8).setY(1.2));
		player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
		player.playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1, 1);
		enabled_users.remove(player.getName());
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if((ArenaManager.getArenaManager().isInArena(player))&&(player.getLocation().subtract(0, 0.1, 0).getBlock().getType()!=Material.AIR)&&(player.getLocation().subtract(0, 1, 0).getBlock().getType()!=Material.WATER)&&(!player.isFlying())){
			if(!enabled_users.contains(player.getName())){
			player.setAllowFlight(true);
			}
		}
	}
	
}
