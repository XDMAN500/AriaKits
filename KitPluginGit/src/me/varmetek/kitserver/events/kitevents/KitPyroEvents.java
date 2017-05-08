package me.varmetek.kitserver.events.kitevents;

import java.util.Arrays;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KitPyroEvents implements Listener{
	public static final Kit KIT = Kit.PYRO;
	
	@EventHandler
	public void checkTouch(PlayerMoveEvent ev){
			
			Player pl = ev.getPlayer();
			
			User user = User.getUser(pl.getName());
			
			if (user.getCurrentKit() != KIT )return;
			if(user.isGodModed()){return;}
				burnNear(pl);
		
			}
	
	@EventHandler
	public void noFireDamage(EntityDamageEvent ev){
		if(ev.getEntity() instanceof Player){
			Player pl = (Player)ev.getEntity();
			User user = User.getUser(pl.getName());
			if (!(ev.getCause()== EntityDamageEvent.DamageCause.FIRE || ev.getCause()== EntityDamageEvent.DamageCause.FIRE_TICK || ev.getCause()== EntityDamageEvent.DamageCause.LAVA))return;
			if (! (user.getCurrentKit() == KIT))return;
			burnNear(pl);
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void shootFireBall(PlayerInteractEvent ev){
		
		final Player pl = ev.getPlayer();
		final User user = User.getUser(pl.getName());
		Material item = ev.getMaterial();
		//Action act = ev.getAction();
		
		
		
		
		if(item != KIT.getActivater())return;
		if(user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		final int delay = 7;
		if(!user.isDelayOver()){
			Messenger.send("Wait " + user.getLeftOverDelay()+  " more seconds", pl, Messenger.WARN);
			return;
		}
		Messenger.send(Main.ABILITY_USED, pl, Messenger.INFO);
		user.setDelay(delay);
	
		// final Location start = pl.getEyeLocation();//.add(pl.getLocation().getDirection().multiply(1.2));
		 
		//final WitherSkull fb = pl.getWorld().spawn(start, WitherSkull.class);
	///	fb.setDirection(pl.getLocation().getDirection().multiply(0));
		//fb.setVelocity(pl.getLocation().getDirection().multiply(1));
		//fb.setIsIncendiary(false);
		//fb.setYield(0);
	
		ev.setCancelled(true);
		final Silverfish end = pl.getWorld().spawn(pl.getEyeLocation(), Silverfish.class);
		end.setVelocity(pl.getLocation().getDirection().multiply(3));
		pl.setCustomName("Y00000");
		pl.setCustomNameVisible(true);
		end.setTarget(null);
		
		
		end.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,100), true);
		for(Entity e: pl.getNearbyEntities(4,4, 4)){
			
			if(e instanceof LivingEntity){
		
						((LivingEntity)e).damage(3.0, pl);
						((LivingEntity)e).setNoDamageTicks(0);
				
				
						
					
				}
			}
		
		new BukkitRunnable(){
			public void run(){
				if(end.isDead()|| end.isOnGround()){
					
					end.setPassenger(null);
					//placeHol.remove();
					for(Entity e: end.getNearbyEntities(6,6, 6)){
						if(e instanceof LivingEntity){
									((LivingEntity)e).damage(12.0, pl);
								
							}
						}
					
					
				
					end.remove();
					this.cancel();
				}
				end.setFallDistance(0);
				end.setTarget(null);
				end.getWorld().playEffect(end.getLocation(), Effect.MOBSPAWNER_FLAMES, 10,100);
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 1);
		//FireBall
		/*new BukkitRunnable(){
			int time = 20*5;
			public void run(){
				for(Entity e: fb.getNearbyEntities(4,4, 4)){
					Main.debug(fb.getNearbyEntities(4,4, 4));
					if(e instanceof LivingEntity){
						if(e == pl){Main.debug("Entity is a player");}else{
								((LivingEntity)e).damage(3.0, pl);
								((LivingEntity)e).setNoDamageTicks(0);
						}
						
								
							
						}
					}
				if(fb.isDead()){
					for(Entity e: pl.getNearbyEntities(6,6, 6)){
						if(e instanceof LivingEntity){
									((LivingEntity)e).damage(10.0, pl);
								
							}
						}
					this.cancel();
					return;
				}
				
				
			
				
				
				
				
				burnNear(fb);
	
				fb.getWorld().playEffect(fb.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
				
				if(fb.getLocation().distance(start) > 100 || time == 0) {
					fb.remove();
					return;
				}
				time -= 1;
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 1);*/
		
		
		new BukkitRunnable(){
			public void run(){
				if(user.getKit() != Kit.PYRO){this.cancel();return;}
				if(user.isDelayOver()){
					Messenger.send("Ability replenished.", pl, Messenger.INFO);
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 10);
	}

	public void burnNear(Player pl){
		pl.setFireTicks(Integer.MAX_VALUE);
		for(Entity e: pl.getNearbyEntities(4, 4, 4)){
			if(e instanceof LivingEntity){
						((LivingEntity)e).setFireTicks(400);;
					
				}
			}
	}
	public void burnNear(Entity pl, Entity ...exlude){
		
		for(Entity e: pl.getNearbyEntities(4, 4, 4)){
			if(e instanceof LivingEntity){
				if(!Arrays.asList(exlude).contains(e))
						((LivingEntity)e).setFireTicks(400);
					
				}
			}
	}
}
