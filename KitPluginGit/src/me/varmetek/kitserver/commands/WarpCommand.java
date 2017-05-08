package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.List;

import me.varmetek.kitserver.api.Area;
import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.GraphicalUserInterface;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Warp;
import me.varmetek.kitserver.main.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class WarpCommand implements CommandExecutor, TabCompleter, Listener {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			final Player player = (Player)sender;
			final User user = User.getUser(player.getName());
			if(args.length == 0){
			/*	String format = "Warps:&b" +DataManager.getWarpList();
				Messenger.send(format
						//.replaceAll("[","")
						//.replaceAll("]","")
											  .replaceAll(","," "), */
				new BukkitRunnable(){
					public void run(){
				Warp.openWarpGUI(player);
				user.setGui(GraphicalUserInterface.WARP);
					}
				}.runTaskAsynchronously(Main.getPluginInstance());
											  
				return false;
			}
				if(args.length > 0){
					String warp = args[0].toLowerCase();
					if(args.length == 1){
					
					if(DataManager.doesWarpExist(warp)){
						//User user = User.getUser(player.getName());
						if(user.getCombatLog() >0 ){
							Messenger.send("You may not warp in combat.", player, Messenger.WARN);
							return false;
						}
					if(!Main.getPlayersNear(player, 10, 10,10).isEmpty() && user.getGameMode()  == User.GameMode.PLAYER && !Area.allowWarp(user.getArea())){
							Messenger.send("You may not when players are with in 10 blocks of you.", player, Messenger.WARN);
							return false;
					}
						boolean change = DataManager.getWarpChangeKit(warp);
						if(user.getGameMode() == User.GameMode.PLAYER){
							user.setCanChangeKit(change);
							if(!change){
								Kit k = DataManager.getWarpKit(warp);
								user.changeKit(k);
								k.getMaterials(player);
							}
						}
						player.setHealth(20.0);
						player.teleport(DataManager.getWarp(warp).getLocation());
						
						Messenger.send("You have warped to &6"+ args[0]+"&a.", player, Messenger.INFO);
						
						if(warp.equals("spawn")){
							user.setCanChangeKit(true);
						}
				
					}else{
						Messenger.send("No such warp.", player, Messenger.WARN);
					}
					}
					/*if(args.length >1){
						if(args.length == 2){
							if(args[1].equalsIgnoreCase("flag")){
								Messenger.send("&cValid flags are&8:&a changekits , warpkit", player, Messenger.WARN);
							}
						}
						if(args.length >2){
							if(args.length == 3){
								if(args[2].equalsIgnoreCase("changekits")){
									Messenger.send("&cValid flags are&8:&a allow, deny", player, Messenger.WARN);
								}
								if(args[2].equalsIgnoreCase("warpkit")){
									Messenger.send("&cValid flags are&8:&aTeh kis", player, Messenger.WARN);
								}
							}
							if(args.length >3){
								if(args.length == 4){
									if(args[2].equalsIgnoreCase("changekits")){
										boolean result = args[3].equalsIgnoreCase("allow");
										DataManager.setWarpChangeKit(warp, result);
										Map<String,String> tree = new HashMap<String,String>();
										tree.put("%warp%", warp);
										tree.put("%result%", result+"");
										String msg = "Flag for warp&o %warp% &aset to&e %result%&a.";
										Messenger.send(msg.replace("%warp%", warp).replace( "%result%", result+""), player, Messenger.INFO);
									}
									if(args[2].equalsIgnoreCase("warpkit")){
										Kit result = Kit.getKitByName(args[3]);
										DataManager.setWarpKit(warp, result);
										Map<String,String> tree = new HashMap<String,String>();
										tree.put("%warp%", warp);
										if(result == null){
											tree.put("%result%", "none");
											}else{
												tree.put("%result%", result.getName());	
											}
										String msg = "Flag for warp&o %warp% &aset to&e %result% &a.";
										Messenger.send(msg.replace("%warp%", warp).replace( "%result%", result+""), player, Messenger.INFO);
									}
								}
							}
						}
					}*/
				}
			
			
		}
		
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
			return new ArrayList<String>(DataManager.getWarpList());
		}
		return null;
	}
	
	@EventHandler
	public void getClicked(InventoryClickEvent ev){
		Player player =(Player)ev.getWhoClicked();
		User user = User.getUser(player.getName());
		ItemStack item = ev.getCurrentItem();
		Inventory cinv = ev.getInventory();
		if(cinv == null)return;
			if(cinv.equals(user.getInv()) && user.getGui() == GraphicalUserInterface.WARP){
				if(!item.hasItemMeta()) return;
				if(!item.getItemMeta().hasDisplayName()) return;
				player.performCommand("warp "+ChatColor.stripColor(item.getItemMeta().getDisplayName()));
				player.closeInventory();
				ev.setCancelled(true);
			}
		/*if(ev.getInventory().equals(inv)){
		 Kit.getKitByName(ev.getCurrentItem().getItemMeta().getDisplayName()).getMaterials((Player)ev.getWhoClicked());
		// ev.setCancelled(true);
		 Messenger.send("You have chosen a kit", (Player)ev.getWhoClicked(), Messenger.INFO);
		 ev.getWhoClicked().closeInventory();
		}*/
	}
}
