package me.varmetek.kitserver.events.kitevents;

import java.util.Random;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitScoutEvents implements Listener {
	private static final Kit KIT  = Kit.SCOUT;
	/*@EventHandler
	public void onDamage(EntityDamageEvent ev){
		if(!(ev.getEntity() instanceof Player))return;
		Player pl = (Player)ev.getEntity();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		if(!pl.isSneaking())return;
		ev.setCancelled(true);
			
		
	}*/
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent ev){
		if(!(ev.getDamager() instanceof Player))return;
		Player pl = (Player)ev.getDamager();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		removeEffects(pl);
	}
	@EventHandler
	public void onHit(EntityDamageEvent ev){
		if(!(ev.getEntity() instanceof Player))return;
		Player pl = (Player)ev.getEntity();
		User user = User.getUser(pl.getName());
		if(user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		removeEffects(pl);
	}
	
	
	@EventHandler

	public void sneak(PlayerToggleSneakEvent ev){
		Player pl = ev.getPlayer();
		User user = User.getUser(pl.getName());
		
		ItemStack it = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta itm = (LeatherArmorMeta) it.getItemMeta();
		Random ran = new Random();
		itm.setColor(Color.fromBGR(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
		it.setItemMeta(itm);
		
		if(user.getCurrentKit() != KIT)return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		if(ev.isSneaking()){
		
			pl.getInventory().setArmorContents(new ItemStack[] {null,null,null,null});
			pl.updateInventory();
			pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,20));
			pl.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,2));
			pl.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,0));
		}else{
			
			pl.getInventory().setArmorContents(new ItemStack[] {it,it,it,it});
			removeEffects(pl);
			pl.updateInventory();
		}
	}
	private static void removeEffects(Player pl){
		pl.removePotionEffect(PotionEffectType.SPEED);
		pl.removePotionEffect(PotionEffectType.INVISIBILITY);
		pl.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
	}

}
