package me.varmetek.kitserver.events.kitevents;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class KitAnchorEvents implements Listener {
	private static final Kit KIT  = Kit.ANCHOR;
	private static final List<UUID> nokb = new ArrayList<UUID>();
	@EventHandler
	public void onDamage(EntityDamageEvent ev){
		if(!(ev.getEntity() instanceof Player))return;
		Player pl = (Player)ev.getEntity();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		nokb.add(pl.getUniqueId());
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent ev){
		if(!(ev.getEntity() instanceof Player))return;
		if(!(ev.getDamager() instanceof Player))return;
		Player pl = (Player)ev.getDamager();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() == KIT){
		nokb.add(((Player)ev.getEntity()).getUniqueId());
		}else{
			nokb.remove(((Player)ev.getEntity()).getUniqueId());
		}
		
	}
	@EventHandler
	public void  cancelkb(PlayerVelocityEvent ev){
		if(nokb.contains(ev.getPlayer().getUniqueId())){
			ev.setCancelled(true);
			
		}
		nokb.remove(ev.getPlayer().getUniqueId());
	}
	

}
