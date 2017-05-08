package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitTurtleEvents implements Listener {
	private static final Kit KIT  = Kit.TURTLE;
	/*@EventHandler
	public void onDamage(EntityDamageEvent ev){
		if(!(ev.getEntity() instanceof Player))return;
		Player pl = (Player)ev.getEntity();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		if(!pl.isSneaking())return;
		ev.setCancelled(true);
			
		
	}*/
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent ev){
		if(!(ev.getDamager() instanceof Player))return;
		Player pl = (Player)ev.getDamager();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		if(user.isGodModed())return;
		if(!pl.isSneaking())return;
		ev.setCancelled(true);
	}
	
	
	@EventHandler

	public void sneak(PlayerToggleSneakEvent ev){
		Player pl = ev.getPlayer();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		if(ev.isSneaking()){
			pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,3));
		}else{
			pl.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		}
	}
	

}
