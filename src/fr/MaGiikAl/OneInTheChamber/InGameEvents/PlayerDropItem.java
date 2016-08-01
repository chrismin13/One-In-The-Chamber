package fr.MaGiikAl.OneInTheChamber.InGameEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;
import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;

public class PlayerDropItem implements Listener{

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent e){

		final Player p = e.getPlayer();

		if(ArenaManager.getArenaManager().isInArena(p)){

			e.setCancelled(true);

			new BukkitRunnable() {

				@Override
				public void run() {
					p.updateInventory();
					this.cancel();
				}
			}.runTaskLater(OneInTheChamber.instance, 2L);
		}
	}
}
