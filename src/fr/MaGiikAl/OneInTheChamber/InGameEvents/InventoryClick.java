package fr.MaGiikAl.OneInTheChamber.InGameEvents;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.MaGiikAl.OneInTheChamber.Arena.ArenaManager;
import fr.MaGiikAl.OneInTheChamber.Main.OneInTheChamber;

public class InventoryClick implements Listener{

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerClickInventory(InventoryClickEvent e){

		HumanEntity he = e.getWhoClicked();

		if(he.getType() == EntityType.PLAYER){
			final Player p = (Player) e.getWhoClicked();
			
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
}
