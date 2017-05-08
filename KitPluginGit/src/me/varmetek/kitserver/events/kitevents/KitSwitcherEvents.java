package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KitSwitcherEvents implements Listener{
	
	public static final Kit KIT = Kit.SWITCHER;
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSwitch(EntityDamageByEntityEvent ev){
		if(! (ev.getEntity() instanceof LivingEntity)) return;
		if(!(ev.getDamager() instanceof Snowball) )return;
		if(!( ((Snowball)ev.getDamager()).getShooter() instanceof Player))return;
		Player damager = (Player) ((Snowball)ev.getDamager()).getShooter();
		LivingEntity e;
		
		if(ev.getEntity() instanceof Player){
			Player	damagee = (Player)ev.getEntity();
			User drUser = User.getUser(damager.getName());
			User deUser = User.getUser(damagee.getName());
			if( (deUser.getGameMode() != User.GameMode.PLAYER) || (drUser.getGameMode() != User.GameMode.PLAYER))return;
			if(drUser.getCurrentKit() != KIT) return;
			e = damagee;
			Messenger.send("You have switched places with " + damagee.getName() + ".", damager, Messenger.INFO);
			Messenger.send("You have switched places with " + damager.getName() + ".", damagee, Messenger.INFO);
		}else{
			e= (LivingEntity)ev.getEntity();
		}
		Messenger.send(Main.ABILITY_USED, damager, Messenger.INFO);
		Location dr1 = damager.getLocation();
		Location de1 = e.getLocation();
		damager.teleport(de1);
		e.teleport(dr1);
		ev.setCancelled(true);
	}
	
	@EventHandler
	public void useItem(PlayerInteractEvent ev){
		if(ev.hasItem()){
			if(ev.getItem().getType() == KIT.getActivater()){
				if(Utils.isRightClicked(ev.getAction())){
					
					
					final Player pl = ev.getPlayer();
		
					
					final User user = User.getUser(pl.getName());
					if(user.getCurrentKit() != KIT)return;
					if(user.isGodModed()){ev.setCancelled(true);return;}
					ev.setCancelled(true);
					final int delay = 21;
					
					if(!user.isDelayOver()){
						Messenger.send("Wait " + user.getLeftOverDelay()+  " more seconds", pl, Messenger.WARN);
						return;
					}
				
					user.setDelay(delay);
					pl.launchProjectile(Snowball.class);
				
					new BukkitRunnable(){
						public void run(){
							if(user.getKit() != Kit.SWITCHER){this.cancel();return;}
							if(user.isDelayOver()){
								Messenger.send(Main.ABILITY_READY, pl, Messenger.INFO);
								this.cancel();
								return;
							}
						}
					}.runTaskTimer(Utils.PLUGIN, 0, 10);
				}
			}
		}
	}

}
