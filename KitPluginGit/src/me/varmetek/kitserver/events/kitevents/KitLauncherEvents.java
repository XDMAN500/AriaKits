package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KitLauncherEvents implements Listener{
	public static final Kit KIT = Kit.LAUNCHER;
	@EventHandler
	public void useItem(PlayerInteractEvent ev){
		if(ev.hasItem()){
			if(ev.getItem().getType() == KIT.getActivater()){
				if(Utils.isRightClicked(ev.getAction())){
					
					final Player pl = ev.getPlayer();
				
					final User plUser = User.getUser(pl.getName());
					
					if(plUser.getCurrentKit() != KIT)return;
					if(plUser.isGodModed()){ev.setCancelled(true);return;}
					ev.setCancelled(true);
					pl.updateInventory();
					final int delay = 21;
					if(!plUser.isDelayOver()){
						Messenger.send("Wait " + plUser.getLeftOverDelay()+  " more seconds", pl, Messenger.WARN);
						return;
					}
					
					{
						Location loc = pl.getLocation();
				
						pl.getWorld().createExplosion(loc, 0);
					
					}
					Messenger.send(Main.ABILITY_USED, pl, Messenger.INFO);
					plUser.setDelay(delay);
					final Silverfish end = pl.getWorld().spawn(pl.getEyeLocation(), Silverfish.class);
					end.setVelocity(pl.getLocation().getDirection().multiply(3));
					pl.setCustomName("Y00000");
					pl.setCustomNameVisible(true);
					end.setTarget(null);
					
					end.setPassenger(pl);
					end.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,100), true);
			
					
					new BukkitRunnable(){
						public void run(){
							if(end.getPassenger() == null || end.isOnGround()){
								end.remove();
								end.setPassenger(null);
								//placeHolder.remove();
								this.cancel();
							}
							end.setFallDistance(0);
							end.setTarget(null);
							end.getWorld().playEffect(end.getLocation(), Effect.INSTANT_SPELL, 10);
						}
					}.runTaskTimer(Main.getPluginInstance(), 0, 1);
					new BukkitRunnable(){
						public void run(){
							if(plUser.getCurrentKit() == KIT)
								Messenger.send(Main.ABILITY_READY, pl, Messenger.INFO);
						}
					}.runTaskLater(Main.getPluginInstance(), 20L*delay);
				}
			}
		}
	}
	
	/*@EventHandler
	public void dontHitEm(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player){
			
			if(ev.getDamager() instanceof Arrow){
				if(ev.getDamager().getPassenger().equals(ev.getEntity())){
					ev.setCancelled(true);
					 Snowball end = ((Player)ev.getEntity()).launchProjectile(Snowball.class);
						//final Slime placeHolder = pl.getWorld().spawn(pl.getEyeLocation(), Slime.class);
						//placeHolder.setSize(1);
						end.setPassenger(ev.getEntity());
						end.setPassenger(ev.getEntity());
				}
			}
		}
	}*/
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void launch(ProjectileLaunchEvent ev){
		if(ev.getEntity().getType() == EntityType.PIG){
			if(ev.getEntity().getShooter().getType() == EntityType.PLAYER){
				//Player pl = (Player)ev.getEntity().getShooter();
				final Player pl =  (Player)ev.getEntity().getShooter();
				
				final User plUser = User.getUser(pl.getName());
				if(plUser.getCurrentKit() != KIT)return;
				ev.getEntity().setPassenger(pl);
			}
			
		}
	}
}
