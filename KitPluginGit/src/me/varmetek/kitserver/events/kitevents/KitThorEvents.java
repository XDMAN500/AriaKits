package me.varmetek.kitserver.events.kitevents;

import java.util.ArrayList;
import java.util.List;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KitThorEvents implements Listener{
	
	private static final Kit KIT = Kit.THOR;
	@EventHandler
	public void freezeAll(PlayerInteractEvent ev){
		if(ev.getItem() == null) return;
			if(ev.getItem().getType() != KIT.getActivater())return;
				if(!Utils.isRightClicked(ev.getAction()))return;
					ev.setCancelled(true);
					
					final Player pl = ev.getPlayer();
		
					
					final User user = User.getUser(pl.getName());
					if(user.getCurrentKit() !=KIT)return;
					if(user.isGodModed()){ev.setCancelled(true);return;}
					final int delay = 15;
					
					if(!user.isDelayOver()){
						Messenger.send("Wait " + user.getLeftOverDelay()+  " more seconds", pl, Messenger.WARN);
						return;
					}
					final List<LivingEntity> list = new ArrayList<LivingEntity>();
					for(Entity e: pl.getNearbyEntities(5, 5, 5)){
						if(e instanceof LivingEntity){
							list.add((LivingEntity)e);
						}
					}
					
					if(list.isEmpty()){
						Messenger.send("No one in range to Smite.", pl, Messenger.WARN);
						return;
					}
					Messenger.send(Main.ABILITY_USED, pl, Messenger.INFO);
					user.setDelay(delay);
					
					for(LivingEntity p : list){
						p.getWorld().strikeLightning(p.getLocation());
						p.getWorld().strikeLightning(p.getLocation());
						p.getWorld().strikeLightning(p.getLocation());
						p.getWorld().strikeLightning(p.getLocation());
						p.getWorld().strikeLightning(p.getLocation());

					}
				
					
					
					new BukkitRunnable(){
						public void run(){
							if(user.getKit() != KIT){this.cancel();return;}
							if(user.isDelayOver()){
								Messenger.send(Main.ABILITY_READY, pl, Messenger.INFO);
								this.cancel();
								return;
							}
						}
					}.runTaskTimer(Utils.PLUGIN, 0, 10);
				
			
		
	}
	
	/*@EventHandler
	public void noFireDamage(EntityDamageEvent ev){
	
		if(ev.getEntity() instanceof Player){
			Player pl = (Player)ev.getEntity();
			User user = User.getUser(pl.getName());
			
			
			if ( user.getCurrentKit() != KIT)return;
			if(ev.getCause() != EntityDamageEvent.DamageCause.LIGHTNING){return;}
			ev.setCancelled(true);
		}
	}*/
}
