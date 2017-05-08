package me.varmetek.kitserver.events.kitevents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KitMuckRakerEvents implements Listener{
	private static final Random ran = new Random();
	private static final Kit KIT = Kit.MUCKRAKER;

	
		@EventHandler
 	public void onHit(EntityDamageByEntityEvent ev){
		if(!(ev.getEntity() instanceof LivingEntity)) return;
		if(!(ev.getDamager() instanceof Player)) return;
		
		Player damager = (Player)ev.getDamager();
		LivingEntity damagee = (LivingEntity)ev.getEntity();
		User drUser = User.getUser(damager.getName());
		
		
		if(damager.getItemInHand().getType() != KIT.getActivater())return;
		if(drUser.getCurrentKit() != KIT)return;
		if(drUser.isGodModed()){ev.setCancelled(true);return;}
		if(ran.nextInt(5) != 0)return;
		damagee.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*10,2));
	
		
	}

	
	@EventHandler
	public void freezeAll(PlayerInteractEvent ev){
		if(ev.getItem() == null) return;
			if(ev.getItem().getType() != KIT.getActivater())return;
				if(!Utils.isRightClicked(ev.getAction()))return;
					ev.setCancelled(true);
					
					final Player pl = ev.getPlayer();
		
					
					final User user = User.getUser(pl.getName());
					
					if(user.getCurrentKit() !=KIT)return;
					if(user.isGodModed()){ev.setCancelled(true);return;}
					final int delay = 21;
					
					if(!user.isDelayOver()){
						Messenger.send("Wait " + user.getLeftOverDelay()+  " more seconds", pl, Messenger.WARN);
						return;
					}
					final List<Player> list = new ArrayList<Player>();
					for(Entity e: pl.getNearbyEntities(5, 5, 5)){
						if(e instanceof Player){
							list.add((Player)e);
						}
					}
					
					if(list.isEmpty()){
						Messenger.send("No one in range to freeze.", pl, Messenger.WARN);
						return;
					}
					Messenger.send(Main.ABILITY_USED, pl, Messenger.INFO);
					user.setDelay(delay);
					Map<String,String> tree = new HashMap<String,String>();
					tree.put("%raker%", "&8&o"+pl.getName());
					for(Player p : list){
						Messenger.send(Utils.format("&7You have been frozen for 7 seconds by %raker%&7.",tree), p, Messenger.INFO);
						User.getUser(p.getName()).setFrozen(true);
					}
					new BukkitRunnable(){
						public void run(){
							for(Player p : list){
								if(p!= null){
									if(p.isOnline()){
										if(!User.getUser(p.getName()).isFrozen())return;
											Messenger.send("You have been unfrozen.", p, Messenger.INFO);
										
											User.getUser(p.getName()).setFrozen(false);
										
									}
								}
							}
						}
					}.runTaskLater(Utils.PLUGIN, 20*7);
					
					
					new BukkitRunnable(){
						public void run(){
							if(user.getKit() != KIT){this.cancel();return;}
							if(user.isDelayOver()){
								Messenger.send(Main.ABILITY_READY, pl, Messenger.INFO);
								this.cancel();
								return;
							}
						}
					}.runTaskTimer(Utils.PLUGIN, 0, 10);
				
			
		
	}
}
