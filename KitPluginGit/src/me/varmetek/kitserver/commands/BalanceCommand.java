package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor{
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		OfflinePlayer targPl = null;
		
		int len = args.length;
		if(len == 0){
			if(!(sender instanceof Player)){
				Messenger.send("/balance <player>",sender,Messenger.WARN);
				return false;
			}
			

			targPl	 = (Player)sender;
			
		}else{
			if(len == 1){
			targPl = Bukkit.getPlayer(args[0]);
			if(targPl == null){
				targPl = Bukkit.getOfflinePlayer(args[0]);
			}
			
		
			
			}
		}
		User user = User.getUser(targPl.getName());
		if(!targPl.isOnline()){
			DataManager.loadUser(user);
		}
		Messenger.send(targPl.getName() + "'s balance: " + user.getMoney(),sender,Messenger.INFO);
		if(!targPl.isOnline()){
			user.remove();
		}
		return false;
	}
}
