package me.varmetek.kitserver.events.kitevents;

import java.util.HashMap;
import java.util.Map;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class KitSniperEvents implements Listener {
	public static final Kit KIT = Kit.SNIPER;
	
	
	@EventHandler
	public void shoot(EntityShootBowEvent ev){
		
		if(!(ev.getEntity() instanceof Player))return;
		
		final Player pl = (Player) ev.getEntity();
		User user =User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		if(ev.getForce() != 1){
				ev.setProjectile(null);
				return;
			}
				
					pl.playSound(pl.getLocation(), Sound.CREEPER_DEATH, 1f, 1f);
					final Arrow arrow = (Arrow) ev.getProjectile();
					
					
					final Vector arrowVel = arrow.getLocation().getDirection().multiply(10);
					final Location arrowStartLoc = arrow.getLocation();
					arrow.setCritical(false);
					arrow.setKnockbackStrength(1);
					
				
					new BukkitRunnable(){

						@Override
						public void run() {
						
							double dist = arrow.getLocation().distance(arrowStartLoc);
							
							if(arrow.isDead()){
								this.cancel();
							}
							if(dist > 200){
								arrow.remove();
							}
							arrow.setVelocity(arrowVel);
					
						}
						
					}.runTaskTimer(Utils.PLUGIN, 0L,0L);
		}
		
	@EventHandler
		@SuppressWarnings("deprecation")
		public void shot(EntityDamageByEntityEvent ev){
			
			if(!(ev.getEntity() instanceof LivingEntity))return;
			if( !(ev.getDamager() instanceof Projectile))return;
			if(  !(((Projectile)ev.getDamager()).getShooter() instanceof Player ))return;
					
			Player attacker = (Player)((Projectile)ev.getDamager()).getShooter();
			Projectile proj = (Projectile) ev.getDamager();
			LivingEntity victim = (LivingEntity)ev.getEntity();
			User attkUser = User.getUser(attacker.getName());
			
			if(attkUser.getCurrentKit() != KIT)return;
			if(attkUser.isGodModed()){ev.setCancelled(true);return;}
			Location eyeLoc = victim.getEyeLocation();
			eyeLoc.setY(eyeLoc.getY()-.5);
			double dist =  proj.getLocation().distance(eyeLoc)/4;
			boolean head = dist <= 2.08;
			Map<String,String> tree = new HashMap<String,String>();
			tree.put("%attacker%", "&8&o"+attacker.getName());
			if(head){
				ev.setDamage(30.0);
				attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
				Messenger.send("&7You got a head shot", attacker, Messenger.INFO);
				if(victim instanceof Player){
					Messenger.send(Utils.format("%attacker% &7got a head shot on you.", tree), (Player)victim, Messenger.WARN);
				}
			}else{
				ev.setDamage(15.0);
			}
					 
		}
		
	/*	@SuppressWarnings("deprecation")
		@EventHandler(priority = EventPriority.NORMAL)
		public void onArrowColision(ProjectileHitEvent ev ){
			if(ev.getEntity() instanceof Arrow){
				if(ev.getEntity().getShooter() instanceof Player){
					if(User.getUser( ((Player)ev.getEntity().getShooter()).getName()).getCurrentKit() == Kit.SNIPER){
						
						ev.getEntity().getWorld().playEffect(ev.getEntity().getLocation(), Effect.CRIT, 0);
					}
				}
				
			}
			
		}*/
}
