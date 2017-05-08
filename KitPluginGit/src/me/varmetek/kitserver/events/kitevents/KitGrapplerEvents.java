package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class KitGrapplerEvents implements Listener{
	private static final Kit KIT = Kit.GRAPPLER; 
	
	@EventHandler
	public void prick(PlayerMoveEvent ev){
		
			Player pl = ev.getPlayer();
			User user = User.getUser(pl.getName());
			
			if (user.getCurrentKit()  != KIT ) return;
			 double damage =( pl.getVelocity().getY()*10)*-1;
			 damage = Math.max((damage-5),0);
	
				pl.setFallDistance((float) damage);

			}
	
	@EventHandler
	public void goFish(PlayerFishEvent ev){
		Player pl = ev.getPlayer();
		User user = User.getUser(pl.getName());
	
		if (user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		ev.getHook().setVelocity(pl.getLocation().getDirection().multiply(2));
		if(ev.getState() == PlayerFishEvent.State.FISHING)return;
		//if(ev.getHook() == null) return;
		
		if( entityGetBlockBelow(ev.getHook(),.1,1,.1 ).getType()  == Material.AIR){
			return;
		}
		
		
	
		//Main.debug(ev.getState() .name());
		Vector vel =  ev.getHook().getLocation().toVector().subtract(pl.getLocation().toVector());
		{
			double limit = 7D;
		vel.setX(Math.min(limit, Math.max(vel.getX(),-limit)));
		vel.setY(Math.min(limit, Math.max(vel.getY(),-limit)));
		vel.setZ(Math.min(limit, Math.max(vel.getZ(),-limit)));
		}
		
		if(ev.getHook().getLocation().getY() < pl.getLocation().getY())vel.add(new Vector(0,1,0));
		pl.setVelocity(vel);
	}
	
	
	@SuppressWarnings("unused")
	public static Block entityGetBlockBelow(Entity e,double minDist ,double maxDist, double interval){
		Block b = null;
		for(double i = minDist*-1; i >maxDist*-1; i-= interval ){
			b = e.getLocation().add(0, i, 0).getBlock();
		
				return b;
			}
		return b;
		}
	
		
	
	
	

}
