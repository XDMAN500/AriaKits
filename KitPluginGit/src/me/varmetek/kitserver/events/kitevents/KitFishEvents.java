package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;


public class KitFishEvents implements Listener{
	public static final Kit KIT = Kit.FISH;
	@EventHandler
	public void swim(PlayerMoveEvent ev){
		
			Player pl = ev.getPlayer();
			User user = User.getUser(pl.getName());
			boolean bTop = pl.getEyeLocation().getBlock().isLiquid();
			boolean bBot = pl.getLocation().getBlock().isLiquid();
		
			boolean inWater = (bTop || bBot) ;
			
			if (user.getCurrentKit() != KIT)return;
			if(!pl.isBlocking())return;
			if(user.isGodModed()){return;}
			
			if(inWater){
			Vector vel = pl.getLocation().getDirection().multiply(.5);
			pl.setVelocity(vel);
			}
			/*for(Entity e: pl.getNearbyEntities(1.5, 1.5, 1.25)){
				if(e instanceof LivingEntity){
							((LivingEntity)e).damage(2.5, pl);
						
					}
				}*/
			}	
	@EventHandler
	public void noDrown(EntityDamageEvent ev){
		if(! (ev.getEntity() instanceof Player)){return;}
		 if(ev.getCause() != EntityDamageEvent.DamageCause.DROWNING ) return;
		 Player pl = (Player)ev.getEntity();
		 if(pl == null) return;
		 User  user = User.getUser(pl.getName());
		 if (user.getCurrentKit() != Kit.FISH)return;
		
		 pl.setRemainingAir(20);
		 ev.setCancelled(true);
	}
}
