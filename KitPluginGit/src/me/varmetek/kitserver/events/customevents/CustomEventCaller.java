package me.varmetek.kitserver.events.customevents;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class CustomEventCaller implements Listener{
	
	@EventHandler
	public void callPluginReloadEvent(PlayerCommandPreprocessEvent ev){
		if(ev.getPlayer().isOp() && ev.getMessage().toLowerCase().equalsIgnoreCase("/reload")){
			PluginReloadEvent e = new PluginReloadEvent();
			Bukkit.getServer().getPluginManager().callEvent(e);
					
		}

	}
}
