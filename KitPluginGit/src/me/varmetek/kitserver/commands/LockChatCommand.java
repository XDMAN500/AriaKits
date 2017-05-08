package me.varmetek.kitserver.commands;

import java.util.Arrays;
import java.util.List;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class LockChatCommand implements CommandExecutor, TabCompleter {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			User playerUser = User.getUser(player.getName());
			if(playerUser.hasPermission(Rank.Admin)){
				if(args.length >0){
					if(args[0].equalsIgnoreCase("on")){
						Utils.chatLocked = true;
					}else{
						if(args[0].equalsIgnoreCase("off")){
							Utils.chatLocked = false;
						}else{
							Messenger.send("Invalid imput: must be off or on.", player, Messenger.WARN);
							return false;
						}
					}
				}else{
					Utils.chatLocked = !Utils.chatLocked;
					
				}
			}else{
				Messenger.send(Main.NO_PERM_MSG, player, Messenger.WARN);
			}
			if(Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by "+ playerUser.getRank().getPrefix() + player.getName()));
			}
			if(!Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by "+ playerUser.getRank().getPrefix() + player.getName()));
			}
		}else{
			if(args.length >0){
				if(args[0].equalsIgnoreCase("on")){
					Utils.chatLocked = true;
				}else{
					if(args[0].equalsIgnoreCase("off")){
						Utils.chatLocked = false;
					}else{
						return false;
					}
				}
			}else{
				Utils.chatLocked = !Utils.chatLocked;
				
			}
			if(Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by &r&LCONSOLE"));
			}
			if(!Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by &r&LCONSOLE"));
			}
				
			
			
		}
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
			return Arrays.asList(new String[] {"on","off"});
		}
		return null;
	}
}
