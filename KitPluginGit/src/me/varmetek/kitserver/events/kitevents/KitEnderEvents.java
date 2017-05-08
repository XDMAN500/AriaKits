package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
@SuppressWarnings("deprecation")
public class KitEnderEvents implements Listener{
	
	private static final Kit KIT = Kit.ENDER;
	
	@EventHandler
	public void warp(PlayerInteractEvent ev){
		
		if(ev.getItem() == null) return;
		if(ev.getItem().getType() != KIT.getActivater())return;
		if(!Utils.isRightClicked(ev.getAction()))return;
					
					
					final Player pl = ev.getPlayer();
		
					
					final User user = User.getUser(pl.getName());
				
					if(user.getCurrentKit() != KIT)return;
					if(user.isGodModed()){ev.setCancelled(true);return;}
					ev.setCancelled(true);
					final int delay = 11;
					Block bl = pl.getTargetBlock(null, 200);
					if(!user.isDelayOver()){
						Messenger.send("Wait " + user.getLeftOverDelay()+  " more seconds", pl, Messenger.WARN);
						return;
					}
					if( bl.getType() == Material.AIR){
						Messenger.send("No block to teleport too.", pl, Messenger.WARN);
						return;
					}
				
					float pitch = pl.getLocation().getPitch();
					float  yaw  = pl.getLocation().getYaw();
					
					Location saveLoc = getSafe(bl.getRelative(BlockFace.UP).getLocation());
					if(saveLoc == null){
						Messenger.send("No safe block to teleport too.", pl, Messenger.WARN);
						return;
					}
				///	saveLoc.setY(bl.getWorld().getHighestBlockYAt(saveLoc.getBlockX(),saveLoc.getBlockZ()));
					saveLoc.setPitch(pitch);
					saveLoc.setYaw(yaw);
					pl.setFallDistance(0);
					pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
					pl.getWorld().playEffect(pl.getLocation(), Effect.EXTINGUISH, 0);
					pl.teleport(saveLoc);
					pl.getWorld().playEffect(pl.getLocation(), Effect.EXTINGUISH, 0);
					pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
				
					Messenger.send(Main.ABILITY_USED, pl, Messenger.INFO);
					user.setDelay(delay);
					
				
			
					
					
					new BukkitRunnable(){
						public void run(){
							if(user.getKit() != KIT){this.cancel();return;}
							if(user.isDelayOver()){
								Messenger.send(Main.ABILITY_READY, pl, Messenger.INFO);
								this.cancel();
								return;
							}
						}
					}.runTaskTimer(Main.getPluginInstance(), 0, 10);
				
			
		
	}

	public Location getSafe(Location loc){
			//int y = 0;
			for(int y = 0;y<10;y++){
				y++;
				if(loc.add(0,y,0).getBlock().getType() == Material.AIR){
					return loc.add(0,y,0);
				}else{
					if(y==9 ){
						return null;
					}
				}
			}
		return null;
	}
}
