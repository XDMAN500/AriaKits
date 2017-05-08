package me.varmetek.kitserver.events.kitevents;

import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class KitEnchanterEvents implements Listener{
	
	private static final Kit KIT =Kit.ENCHANTER;
	@EventHandler
	public void Enchant(PlayerInteractEvent ev){
		final Player player = ev.getPlayer();
		final User user = User.getUser(player.getName());
	

		final PlayerInventory inv = player.getInventory();
		final int delay = 2;
	
		if(user.getCurrentKit() !=KIT) return;
		if(user.isGodModed()){ev.setCancelled(true);return;}
		if(ev.getMaterial() != KIT.getActivater())return;
		if( !(Utils.isLeftClicked(ev.getAction()) || Utils.isRightClicked(ev.getAction())))return;
				
		if(!user.isDelayOver()){
				Messenger.send("Wait "+user.getLeftOverDelay() +" more seconds.", player, Messenger.WARN);
				return;
		}
					
			user.setDelay(delay + 25);
			Messenger.send(Main.ABILITY_USED, player, Messenger.INFO);
			 for(ItemStack item : inv.getArmorContents()){
			 if(item != null){
									
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
				itemMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
				itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 4, true);
				itemMeta.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
				itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
				itemMeta.addEnchant(Enchantment.DURABILITY ,10, true);
				item.setItemMeta(itemMeta);
								 
							
								
				}
			}
						
						
		 for(ItemStack item : inv.getContents()){
			 if(item.getType() == Material.DIAMOND_SWORD){
				ItemStack sword =item;
				ItemMeta swordm = sword.getItemMeta();
				swordm.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
				 sword.setItemMeta(swordm);
				 break;
								 
				}
		}
						 
		 player.updateInventory();
						
		 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPluginInstance(), new Runnable() {
								
			public void run() {
			if(user.getCurrentKit() == KIT) {
		
				Messenger.send("Ability wore off!", player, Messenger.INFO);
				for(ItemStack item : inv.getArmorContents()){
						 if(item != null){
							 for(Enchantment e :Enchantment.values()){
								 item.removeEnchantment(e);
							 }
									
						 }
				}						
				for(ItemStack item : inv.getContents()){
					if(item != null){
						for(Enchantment e :Enchantment.values()){
							item.removeEnchantment(e);
						}
									
					}
											 	
				}
				
				{
											 
											
					ItemStack item =  player.getItemOnCursor();
					if(item != null){
						for(Enchantment e :Enchantment.values()){
							item.removeEnchantment(e);
						}
									
					}
											 
				}
			}
			player.updateInventory();
			}
						
		 }, 20L*5);
						 
		 new BukkitRunnable(){
			 public void run(){
				 if(user.getKit() != KIT){this.cancel();return;}
				 if(user.isDelayOver()){
					 Messenger.send(Main.ABILITY_READY, player, Messenger.INFO);
							this.cancel();
							return;
						}
					}
			}.runTaskTimer(Main.getPluginInstance(), 0, 10);
		
	}

}