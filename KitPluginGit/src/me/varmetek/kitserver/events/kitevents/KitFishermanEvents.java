package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class KitFishermanEvents implements Listener{
	public static final Kit KIT = Kit.FISH;
	
	@EventHandler
	public void goFish(PlayerFishEvent ev){
		Player pl = ev.getPlayer();
		User user = User.getUser(pl.getName());
		
		if (user.getCurrentKit() != KIT)return;
		
		if(ev.getState() == PlayerFishEvent.State.FISHING)return;
		
		if(ev.getState() != PlayerFishEvent.State.CAUGHT_ENTITY ) return;
		
		if(!(ev.getCaught() instanceof LivingEntity))return;
		LivingEntity entity = (LivingEntity)ev.getCaught();
		
		if(ev.getCaught() instanceof Player){
			entity = pl.getPlayer();
		}
			
	
		
		entity.teleport(pl);
			
		
	
	}
}
