package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.List;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor,TabCompleter{



	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player  = null;
		OfflinePlayer targetPlayer = null ;
		int len = args.length;
		User playerUser = null;
		if(sender instanceof Player){
			player = (Player)sender;
			playerUser = User.getUser(player.getName());
				
		}
			if(len == 0){
				if(player != null){
					player.performCommand("rank "+player.getName());
					return false;
					
				}else{
					Messenger.send("&cConsole doesn't have a rank.",sender);
					return false;
				}
			}else{
				if(len>0){
					targetPlayer = Utils.getPlayer(args[0]);
					
					User tUser = User.getUser(targetPlayer.getName());
					if(!targetPlayer.isOnline()){
						DataManager.loadUser(tUser);
					}
					
					if(len == 1){
						
						Messenger.send("Rank:" + tUser.getRank().getFullName(),sender, Messenger.INFO);
						return false;
						
					}else{
						if(len >1){
							if(len == 2){
								
								if(player != null){
									if(!playerUser.hasPermission(Rank.Owner) ){
										Messenger.send(Main.NO_PERM_MSG, sender, Messenger.WARN);
										return false;
										}
									if(!playerUser.hasPermission((tUser.getRank()))){
										Messenger.send("You may not change that players rank." , sender, Messenger.INFO);
										return false;
									}
								}
								Rank rank = Rank.getRankByName(args[1]);
								
								
								if(rank != null){
									
									Utils.broadcastRank(tUser.getRankTitle()+"&e's rank has been set to "+ rank.getFullName()+"&e.", Rank.Mod);
									tUser.setRank(rank);
									if(targetPlayer.isOnline()){
										Messenger.send("&aYour rank has been set to "+ rank.getFullName()+"&a." , (Player)targetPlayer, Messenger.INFO);
									}

							
								}else{
									Messenger.send("No such rank as "+args[1], sender, Messenger.INFO);
									return false;
								
								}
							if(!targetPlayer.isOnline()){
								DataManager.savePlayerUser(tUser);
								tUser.remove();
							}
								
								
							}
						}
					}
					
				}
			}
			return false;
		}
			
			
			
				
		
			

		
	

	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		
		if(args.length == 1){
			/*List<String> tabs = new ArrayList<String>();
			for(Player p : Bukkit.getOnlinePlayers()){
				tabs.add(p.getName());
			}
			return tabs;*/
			return Utils.matchOnlinePlayers(args[0]);
		}
		
		if(args.length == 2){
			List<String> tabs = new ArrayList<String>();
			for(Rank r  : Rank.getRanks()){
				tabs.add(r.name());
			}
			return Utils.matchString(tabs, args[1]);
		}
		return null;
	}
}
/*if(args.length == 0){

if(sender instanceof Player){
	Messenger.send("Rank:" + playerUser.getRank().getFullName(),sender, Messenger.INFO);
}
}
//get another players rank
if(args.length == 1){
	targetPlayer = Bukkit.getPlayer(args[0]);
	if(targetPlayer != null){
		User targetPlayerUser = User.getUser(targetPlayer.getName());
		Messenger.send("Rank:" + targetPlayerUser.getRank().getFullName(), sender, Messenger.INFO);
	}
}

//setanother players rank
if(args.length == 2){
	
	
	if(sender instanceof Player){
		if(!playerUser.hasPermission(Rank.Owner) ){
			Messenger.send(Main.NO_PERM_MSG, sender, Messenger.WARN);
			return false;
			}
		}

		targetPlayer = Bukkit.getPlayer(args[0]);
		if(targetPlayer == null){
			targetPlayer = Bukkit.getOfflinePlayer(args[0]);
		}
		if(targetPlayer != null){
			User targetPlayerUser = User.getUser(targetPlayer.getName());
			if(!targetPlayer.isOnline()){
				DataManager.loadUser(targetPlayerUser);
			}
		
			if(sender instanceof Player){
				if(!playerUser.hasPermission((targetPlayerUser.getRank()))){
					Messenger.send("You may not change that players rank." , sender, Messenger.INFO);
					return false;
				}
			}
			Rank rank = Rank.getRankByName(args[1]);
			
		if(rank != null){
			
				Utils.broadcastRank(targetPlayerUser.getRankTitle()+"&e's rank has been set to "+ rank.getFullName()+"&e.", Rank.Mod);
				targetPlayerUser.setRank(rank);
				if(targetPlayer.isOnline()){
				Messenger.send("&aYour rank has been set to "+ rank.getFullName()+"&a." , (Player)targetPlayer, Messenger.INFO);
				}

				
			
		}else{
			Messenger.send("No such rank as "+args[1], sender, Messenger.INFO);
			
		}
		if(!targetPlayer.isOnline()){
			DataManager.savePlayerUser(targetPlayerUser);
			targetPlayerUser.remove();
		}
	}
	
}
return false;
}else{
Player targetPlayer ;
if(args.length == 0){
	sender.sendMessage(Main.commandCorrections.get(commandLabel));
}


//get another players rank
if(args.length == 1){
	targetPlayer = Bukkit.getPlayer(args[0]);
	User targetPlayerUser = User.getUser(targetPlayer.getName());
	//Messenger.send("Rank:" + targetPlayerUser.getRank().name(), player, Messenger.INFO);
	sender.sendMessage("Rank:" + targetPlayerUser.getRank().name());
}*/


//setanother players rank
/*if(args.length == 2){
	
		targetPlayer = Bukkit.getPlayer(args[0]);
		User targetPlayerUser = User.getUser(targetPlayer.getName());
		Rank rank = Rank.getRankByName(args[1]);
		if(rank != null){
			targetPlayerUser.setRank(rank);
			sender.sendMessage("Rank:" + targetPlayerUser.getRank().name());
			//Messenger.send("Rank:" + targetPlayerUser.getRank().name(), player, Messenger.INFO);
		}else{
			sender.sendMessage("No such rank as "+args[1]);
		}
}*/



