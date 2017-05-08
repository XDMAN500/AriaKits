package me.varmetek.kitserver.commands;

import java.util.HashSet;
import java.util.Set;

import me.varmetek.kitserver.api.GraphicalUserInterface;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;



public class ShopCommand implements CommandExecutor, Listener{
	//Inventory inv  =  Bukkit.createInventory(null, 45, "Shop");
	//Inventory invSel  =  Bukkit.createInventory(null, 45, "Shop Selection");
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
			if(sender instanceof Player){
				final Player player = (Player)sender;
				final User playerUser =User.getUser(player.getName());
				
				Set<String> legalArgs = new HashSet<String>();
				legalArgs.add("kits");
			
				if(args.length == 0){
					
					player.openInventory(Main.SHOP_MENU_INV);
					playerUser.setGui(GraphicalUserInterface.SHOP);
		
					/*for(String s:legalArgs){
						ItemStack item = new ItemStack(Material.ENDER_PORTAL);
						ItemMeta itemM = item.getItemMeta();
						itemM.setDisplayName(s);
						item.setItemMeta(itemM);
						invSel.addItem(item);
					}*/
					
				
				}else{
					if(args.length == 1 ){
						if(legalArgs.contains(args[0].toLowerCase())){
							if(args[0].equalsIgnoreCase("kits")){
								final Inventory inv  =  Bukkit.createInventory(null, 6*9, "Shop");
								inv.clear();
								playerUser.setInv(inv);
								playerUser.setGui(GraphicalUserInterface.SHOPKIT);
								new BukkitRunnable(){
									public void run(){	
								if(playerUser.getOwnedKits(true).containsAll(Kit.getAllKits())){
									if(Utils.getItemNumber(inv, null) == inv.getSize()){
										while(Utils.getItemNumber(inv, null) != 0){
											inv.addItem(Main.SHOP_EMPTY);
										}
									}
								}else{
									
									Kit.addKits(player, playerUser.getInv(), false);
									
									
								}
									}
								}.runTaskAsynchronously(Main.getPluginInstance());
								player.openInventory(playerUser.getInv());
								
							}else{
								Messenger.send("Not an existing shop", player, Messenger.INFO);
							}
						}
						
					}else{
						if(args.length >1){
							if(Kit.getKitByName(args[1]) != null){
								Kit kit = Kit.getKitByName(args[1]);
								if(playerUser.hasKit(kit)){
									player.performCommand("kit "+ kit.name());
									return false;
								}
								
								if(playerUser.canBuyKit(kit)){
									 playerUser.addOwnedKit(kit);
									 playerUser.subtractMoney(kit.getPrice());
									 Messenger.send("You have bought " + kit.getFullName()+ "&a for " + kit.getPrice() + "Bits.", player, Messenger.INFO);
									 return true;
								}else{
									if(kit.getType() == Kit.Type.NONDONOR){
										Messenger.send("You don't have enough funds to buy "+kit.getFullName()+"&c for "+kit.getPrice()+" Bits.", player, Messenger.WARN);
										return false;
									}else{
										Messenger.send("You need "+kit.getRequiredRank().getFullName()+"&c rank in order to use " + kit.getFullName()+".", player, Messenger.WARN);
										return false;
										
									}
								}
								
								
								
							}
						}
					}
				}
			}
		
		return false;
	}



@EventHandler
	public void click(InventoryClickEvent ev){
	final Player player = (Player)ev.getWhoClicked();
	User user = User.getUser(player.getName());
	ItemStack item = ev.getCurrentItem();
	
	if(ev.getInventory() == null) return;

	
	if(user.getGui() == GraphicalUserInterface.SHOPKIT){
		if(user.getInv() == null) return ;
		if(item== null) return;
		if(item == Main.SHOP_EMPTY){
			ev.setCancelled(true);
			return;
		}
		if(item.getItemMeta() == null) return;
		if(!item.getItemMeta().hasDisplayName()) return;
		
		player.performCommand("shop kits " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
		ev.setCancelled(true);
		player.performCommand("shop kits");
		/*Bukkit.getScheduler().runTask(Main.getPluginInstance(),new Runnable(){
			public void run(){
				player.closeInventory();
			}
		 });*/
			
		
		
	}
	
	if(ev.getInventory().equals(Main.SHOP_MENU_INV)){
		
		ev.setCancelled(true);
		if(item== null) return;
		if(item.getItemMeta() == null) return;
		if(!item.getItemMeta().hasDisplayName()) return;
		player.closeInventory();
		player.performCommand("shop "+item.getItemMeta().getDisplayName());
		
		

	}
	

	/*ItemStack canBuy = new ItemStack(Material.WOOL,5);
	if(ev.getClickedInventory().equals(null)) return;
	if(ev.getClickedInventory().equals(inv)){
		//ev.setCancelled(true);
		if(item.isSimilar(canBuy)){
			
				User playerUser = User.getUser( ((Player)ev.getWhoClicked()).getName());
				 Kit kit = Kit.getKitByName(ev.getCurrentItem().getItemMeta().getDisplayName());
				 playerUser.addOwnedKit(kit);
				 playerUser.subtractMoney(kit.getPrice());
				 inv.clear();
				 Kit.addKits(player, inv, false, false);
				 Kit.addKits(player, inv, true, false);
			
	
		}else{
			//ev.setCancelled(true);
		}
	}else{
		if(ev.getClickedInventory().equals(invSel)){
			ev.setCancelled(true);
			player.closeInventory();
			player.performCommand("shop "+ ev.getCurrentItem().getItemMeta().getDisplayName());
		}
	}*/
}






}