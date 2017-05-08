package me.varmetek.kitserver.commands;


import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Warp;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
public class WarpSettingsCommand implements CommandExecutor, Listener

{
		
		public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
			if(sender instanceof Player){
				if(!User.getUser(sender.getName()).hasPermission(Rank.ModPlus)){
					Messenger.send(Main.NO_PERM_MSG, sender, Messenger.WARN);
					return false;
				}
			}
			int len = args.length;
		
			if(len == 0){
				Messenger.send("/kitsettings <kit>", sender, Messenger.WARN);
				String format = "&7Warps:&b" +DataManager.getWarpList();
				Messenger.send(format.replaceAll(","," "), sender);
			
				return false;
			}else{
				Warp warp = Warp.getWarp(args[0]);
				if(!warp.exists()){
					Messenger.send("No such warp", sender, Messenger.WARN);
					return false;
				}
				if(len == 1){
					Messenger.send("&9===&2&lWarp&8- "+warp.getID() + " &9===", sender, Messenger.WARN);
				
					Messenger.send("&2Change Kit&8: &a"+warp.allowKitChange(), sender,Messenger.INFO);
					Messenger.send("&2Icon&8: &a"+warp.getIcon().name(), sender,Messenger.INFO);
					if(warp.getWarpKit() == null){
						Messenger.send("&2Warp Kit&8: &cNone", sender,Messenger.INFO);
					}else{
						Messenger.send("&2Warp Kit&8: &a"+warp.getWarpKit().getFullName(), sender,Messenger.INFO);
					}
				}
				if(len >1){
					if(len == 2){
						if(args[1].equalsIgnoreCase("changekits")){
							Messenger.send("&cValid flags are&8:&a allow, deny", sender, Messenger.WARN);
						}
						if(args[1].equalsIgnoreCase("warpkit")){
							Messenger.send("&cValid flags are&8:&aTeh kis", sender, Messenger.WARN);
						}
						if(args[1].equalsIgnoreCase("icon")){
							Messenger.send("&cValid flags are&8: valid item name", sender, Messenger.WARN);
						}
					}
					if(len > 2){
						if(len == 3){
							if(args[1].equalsIgnoreCase("changekits")){
								boolean result = (args[2].equalsIgnoreCase("allow")||args[2].equalsIgnoreCase("yes")||args[2].equalsIgnoreCase("true"));
								warp.setKitChange(result);
								String msg = "&e"+warp.getID() + ".changekit has been set to &9"+result;
								Messenger.send(msg, sender, Messenger.INFO);
							}
							if(args[1].equalsIgnoreCase("warpkit")){
								Kit result = Kit.getKitByName(args[2]);
								warp.setWarpKit(result);
								//DataManager.setWarpKit(warp, result);
							
								String msg;
								if(result == null){
									 msg = "&e"+warp.getID() + ".warpkit has been set to &9none";
									}else{
										 msg = "&e"+warp.getID() + ".warpkit has been set to &9"+result.getFullName();
									}
							
								Messenger.send(msg, sender, Messenger.INFO);
							}
							if(args[1].equalsIgnoreCase("icon")){
								String msg;
								try{
								Material result = Material.valueOf(args[2].toUpperCase());
								warp.setIcon(result);
								//DataManager.setWarpKit(warp, result);
							
							
								if(result == null){
									 msg = "&e"+warp.getID() + ".icon has been set to &9none";
									}else{
										 msg = "&e"+warp.getID() + ".icon has been set to &9"+result.name();
									}
								}catch(IllegalArgumentException e){
									msg = "&c not an item name.";
								}
							
								Messenger.send(msg, sender, Messenger.INFO);
							}
						}
					}
					warp.save();
				}

			}
			
		
			return false;
		}

}


