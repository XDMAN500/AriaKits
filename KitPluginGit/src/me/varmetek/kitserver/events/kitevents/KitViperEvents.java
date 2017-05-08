package me.varmetek.kitserver.events.kitevents;

import java.util.Random;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitViperEvents implements Listener{
	private static final Random ran = new Random();
	private static final Kit KIT = Kit.VIPER;
	
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
		if(ran.nextInt(3) != 0)return;
		damagee.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*10,3));
	
		
	}
}
