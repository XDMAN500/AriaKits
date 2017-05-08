package me.varmetek.kitserver.events;

import me.varmetek.kitserver.api.Perk;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PerkEvents implements Listener
{
	@EventHandler
	public void onFall(EntityDamageEvent ev){
		
		if(ev.getEntity() instanceof Player){
			Player pl = (Player)ev.getEntity();
			User user = User.getUser(pl.getName());
			if(user.getCurrentPerk() == null)return;
			Perk p = user.getCurrentPerk();
			double dmg = ev.getDamage();
			
				if(p.getType() == Perk.Type.STONE_SKIN){
					dmg =  dmg - (dmg*(Perk.VALUES.get(p.getType())[p.getLevel()-1])/100D);	
				}
			if (ev.getCause()== EntityDamageEvent.DamageCause.FALL && p.getType() == Perk.Type.CUSHION){
				dmg =  dmg - (dmg*(Perk.VALUES.get(p.getType())[p.getLevel()-1])/100D);	
			}
			ev.setDamage(dmg);
	
		}
	
	}
	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev){
		if(!(ev.getEntity() instanceof Player)) return;
		if(Utils.getAttacker(ev.getDamager()) == null) return;
		//Player hurt = (Player) ev.getEntity();
		Player damager = Utils.getAttacker(ev.getDamager());
		if(damager == null)return;
		User user =User.getUser(damager.getName());
		if(user.getCurrentPerk() == null)return;
		Perk p = user.getCurrentPerk();
		if(p.getType() != Perk.Type.PEIRCING)return;
		ev.setDamage( ev.getDamage() + (ev.getDamage()*(Perk.VALUES.get(p.getType())[p.getLevel()-1])/100D));
	}
	
}
