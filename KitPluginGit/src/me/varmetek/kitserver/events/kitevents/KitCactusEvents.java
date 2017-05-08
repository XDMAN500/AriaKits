package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitCactusEvents implements Listener{
	private static final Kit KIT = Kit.CACTUS;
	@EventHandler
	public void prick(PlayerMoveEvent ev){
		
			Player pl = ev.getPlayer();
			User user = User.getUser(pl.getName());
			
			if (user.getCurrentKit() == KIT ){
				pl.addPotionEffect(new PotionEffect(PotionEffectType.POISON,Integer.MAX_VALUE,5));
			}
		
			}	
	
	@EventHandler
	public void noFireDamage(EntityDamageEvent ev){
	
		if(ev.getEntity() instanceof Player){
			Player pl = (Player)ev.getEntity();
			User user = User.getUser(pl.getName());
			
			
			if ( user.getCurrentKit() != KIT)return;
			if(ev.getCause() != EntityDamageEvent.DamageCause.POISON){return;}
			ev.setCancelled(true);
			for(Entity e: pl.getNearbyEntities(1.5, 1.5, 1.5)){
				if(e instanceof LivingEntity){
					LivingEntity le = 	((LivingEntity) e);
						if(le.getNoDamageTicks()<10) le.setNoDamageTicks(0);
						
					}
				}
			
			
		}
	}
}
