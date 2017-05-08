package me.varmetek.kitserver.api.hostedeventapi;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class HostedEventsListener implements Listener {
	
	/*@EventHandler
	public void onDeath(PlayerDeathEvent ev){
		UUID id = ev.getEntity().getUniqueId();
		
		if(HostedEvent.getCurrent() == null)return;
		if(!HostedEvent.getCurrent().isStarted())return;
		
		if(HostedEvent.getCurrent().getEventType() != EventType.KOTH){
			HostedEvent.getCurrent().removePlayer(Bukkit.getPlayer(id));
			
		}
	}*/
	
	
	



	
	@EventHandler
	public void leave(PlayerQuitEvent ev){
		if(HostedEvent.getCurrent() == null)return;
		if(!HostedEvent.getCurrent().isStarted())return;
		
		if(HostedEvent.getCurrent().getEnrolledPlayers().contains(ev.getPlayer().getUniqueId())){
			HostedEvent.getCurrent().removePlayer(ev.getPlayer());
			ev.getPlayer().setHealth(0.0);
		}
		
	}
}
