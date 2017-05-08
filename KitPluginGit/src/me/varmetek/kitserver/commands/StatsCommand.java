package me.varmetek.kitserver.commands;

import java.util.List;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;


public class StatsCommand implements CommandExecutor, TabCompleter {
	
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		
			
				
			
				//Messenger.send("========", sender, Messenger.INFO);
				User user;
				OfflinePlayer plTarg ;
				
				if(args.length == 0){
					if(!(sender instanceof Player)){
						Messenger.send("Console doesn't have stats :)", sender, Messenger.INFO);
						return false;
						
						
					}
					plTarg = (Player)sender;
					 user = User.getUser(plTarg.getName());
				}else{
						plTarg =   Bukkit.getPlayer(args[0]);
						if(plTarg == null){
							plTarg =    Bukkit.getOfflinePlayer(args[0]);;
						}
						if(plTarg == null){
							Messenger.send("That player is not online.", sender, Messenger.WARN);
							return false;	
						}
					 user = User.getUser(plTarg.getName());
				
					}
				
				if(!plTarg.isOnline()){
					DataManager.loadUser(user);
				}
				
			if(user.equals(null)){
				Messenger.send("That player is not online.", sender, Messenger.WARN);
				return false;
			}
			
			Messenger.send("&1========", sender, Messenger.INFO);
			Messenger.send("&bName:&a " +  user.getRankTitle(), sender,Messenger.INFO);
			Messenger.send("&bKills:&a " + user.getKills(), sender, Messenger.INFO);
			Messenger.send("&bDeaths:&a " + user.getDeaths(), sender, Messenger.INFO);
			Messenger.send("&bKillStreak:&a " + user.getKillstreak(), sender, Messenger.INFO);
			Messenger.send("&bRank:&a " + user.getRank().name(), sender, Messenger.INFO);
			Messenger.send("&bBits:&a " + user.getMoney(), sender, Messenger.INFO);
			Messenger.send("&bKDR:&a " + String.format("%.3f", (double)user.getKDR()), sender, Messenger.INFO);
			/*Messenger.send("&bLevel:&a " + user.getLevel(), sender, Messenger.INFO);
			Messenger.send("&bNeeded Xp:&a " + user.getNeededXp() , sender, Messenger.INFO);
			Messenger.send("&bXp:&a " + user.getXp() , sender, Messenger.INFO);
			*/
			
			if(user.getKit() != null)
				Messenger.send("&bCurrentKit:&a" + user.getKit().name().toLowerCase(), sender, Messenger.INFO);
			else
				Messenger.send("&bCurrentKit:&a" + "none", sender, Messenger.INFO);
			Messenger.send("&1========", sender, Messenger.INFO);
		
			if(!plTarg.isOnline()){
				user.remove();
			}
		
		return false;
	}
	
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		
		if(args.length == 1){
		
			return Utils.matchOfflinePlayers(args[0]);
		}
		return null;
	}
}
