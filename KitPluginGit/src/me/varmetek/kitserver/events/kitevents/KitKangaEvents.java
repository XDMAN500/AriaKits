package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;


public class KitKangaEvents  implements Listener{
	public static final Kit KIT = Kit.KANGAROO;
	@EventHandler
	public void jump(PlayerInteractEvent ev){
		
		Player pl = ev.getPlayer();
		User user = User.getUser(pl.getName());
		Material item = ev.getMaterial();
		Action act = ev.getAction();
		Block block = pl.getLocation().getBlock();
		Material blockBelow = block.getRelative(BlockFace.DOWN).getType();
		Vector vel = pl.getVelocity();
		
		
		if(Utils.isRightClicked(act)|| Utils.isLeftClicked(act)){
			if(item != KIT.getActivater())return;
			if(user.getCurrentKit() != KIT)return;
			if(user.isGodModed()){ev.setCancelled(true);return;}
					ev.setCancelled(true);
					//Bukkit.broadcastMessage("FLY");
					if(block.getType().isSolid() || blockBelow.isSolid()){
						user.setJumps((byte)1);
						pl.setVelocity(vel.setY(vel.getY()+1));
						vel = pl.getVelocity();
					}else{
						if(user.getJumps() > 0){
							user.setJumps((byte)(user.getJumps()-(byte)1));
							if(pl.isSneaking()){
								pl.setVelocity(pl.getLocation().getDirection().multiply(1.4));
								vel = pl.getVelocity();
								vel.setY(vel.getY()+.5);
								pl.setVelocity(vel);
								
							}else{
								pl.setVelocity(pl.getLocation().getDirection().multiply(0.8));
								vel = pl.getVelocity();
								vel.setY(Math.abs(vel.getY())+.4);
								pl.setVelocity(vel);
							}
						}
					}
				
			
			
		}
	}
	

	@EventHandler
	public void onFall(EntityDamageEvent ev){
		
	if(ev.getEntity() instanceof Player){
		Player pl = (Player)ev.getEntity();
		User user = User.getUser(pl.getName());
		if (ev.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
			if (user.getCurrentKit() == KIT){
				double dmg = ev.getDamage()/8;
				
				if (dmg > 7){
					dmg= 7.0;
				}
				if (dmg < 1){
					dmg= 0.0;
				}
				//double t =3/4;
				//Main.debug(dmg+"");
				ev.setDamage(dmg);
			}
		}
		}
	}
	


}
