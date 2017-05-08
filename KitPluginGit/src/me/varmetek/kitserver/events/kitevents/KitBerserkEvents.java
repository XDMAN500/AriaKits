package me.varmetek.kitserver.events.kitevents;

import java.util.Arrays;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitBerserkEvents implements Listener{
	private static final Kit KIT = Kit.BERZERK;
	private static final int MAX_TIME = 20*15;
	
	
	@EventHandler
	public void onKill(EntityDeathEvent ev){
		
		if(ev.getEntity().getKiller() == null) return;
		if(!(ev.getEntity().getKiller() instanceof Player))return;
		
		Player killer = (Player)ev.getEntity().getKiller();
		User killerUser = User.getUser(killer.getName());
		if(killerUser.getCurrentKit() != KIT) return;
		if(killer.getActivePotionEffects().isEmpty()){
			Messenger.send(Main.ABILITY_USED, killer, Messenger.INFO);
		}else{
			Utils.removePotionEffects(killer);
		}
		killer.addPotionEffects(Arrays.asList(new PotionEffect[] {
				new PotionEffect(PotionEffectType.INCREASE_DAMAGE,MAX_TIME,0),
				new PotionEffect(PotionEffectType.SPEED,MAX_TIME,1),
				
		}));
		
	}
}
