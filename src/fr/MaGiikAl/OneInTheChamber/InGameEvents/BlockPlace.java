package fr.MaGiikAl.OneInTheChamber.InGameEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;
import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;

public class BlockPlace implements Listener{

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent e){

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
