package me.varmetek.kitserver.commands;

import java.util.Arrays;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RenewCommand implements CommandExecutor{
	

	@Override
	public boolean onCommand(CommandSender sender ,Command cmd, String label,String[] args) {
		
		if(sender instanceof Player){
			final Player pl = (Player)sender;
			final User user =User.getUser(pl.getName());
			if(user.getKit() == null)
			{
				Messenger.send("No kit to renew.", pl, Messenger.WARN);
				return false;
			}
			user.setDelay(0);
			pl.getInventory().setArmorContents(null);
			pl.getInventory().clear();
			pl.addPotionEffects(Arrays.asList(new PotionEffect[] 
					{
					new PotionEffect(PotionEffectType.BLINDNESS,20*8,3),
					new PotionEffect(PotionEffectType.SLOW,20*8,3),
					new PotionEffect(PotionEffectType.WEAKNESS,20*8,3)
				}));
			
			new BukkitRunnable(){
				int ticks = 20*8;
				public void run(){
					if(ticks == 0){
						if(!pl.isDead()){
							//Kit.getKitMaterials(pl, user.getKit(), true,false);
							user.getKit().getMaterials(pl);
							Messenger.send("Renewed Your Kit.", pl, Messenger.INFO);
						}
						this.cancel();
						return;
					
				
						
					}
					ticks -= 10;
				}
				
			}.runTaskTimer(Main.getPluginInstance(),0,10);
			
		
			
		
			
		}
		
		return false;
	}



}
