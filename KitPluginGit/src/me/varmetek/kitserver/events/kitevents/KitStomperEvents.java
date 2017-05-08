package me.varmetek.kitserver.events.kitevents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class KitStomperEvents implements Listener {
	private static final Kit KIT = Kit.STOMPER;
	
	
	@EventHandler
	public void onFall(EntityDamageEvent ev){
		
	if(!(ev.getEntity() instanceof Player))return;
		
	Player pl = (Player)ev.getEntity();
		User user = User.getUser(pl.getName());
		if (!ev.getCause().equals(EntityDamageEvent.DamageCause.FALL))return;
		if (user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
				
				double dmg = pl.getFallDistance()*3;
				ev.setDamage(0.0);
				if(dmg <7 )return;
				pl.getWorld().playSound(pl.getLocation(), Sound.ANVIL_LAND, 1, 1);
				List<LivingEntity> toDamage = new ArrayList<LivingEntity>();
				for(Entity e: pl.getNearbyEntities(3, 3, 3)){
					if(e instanceof LivingEntity){
						toDamage.add((LivingEntity)e);
					
					}
				}
				double toDmg = (dmg-7)/toDamage.size();
				for(LivingEntity e: toDamage){
				
					if(e instanceof Player){
						Player human = (Player)e;
						Map<String,String> transfer = new HashMap<String,String>();
						transfer.put("%stomper%", "&7&o"+pl.getName());
						Messenger.send(Utils.format("&7You have been stomped by %stomper% &7.", transfer), human, Messenger.INFO);
						if(!(human.isSneaking() && human.isBlocking())){
							if(human.isSneaking() || human.isBlocking()){
								toDmg /= 2;
							}
						}else{
							toDmg = 0;
						}
					}else{
						
					}
					e.damage(toDmg, pl);
				}
				
				String dS =String.format("%.2f", toDmg);
				if(toDamage.size() > 0){
					Messenger.send("Your damage per player was &6" + dS + "/"+ toDamage.size() + "&a.", pl, Messenger.INFO);
				}
				
				
				ev.setCancelled(true);
	}
}
