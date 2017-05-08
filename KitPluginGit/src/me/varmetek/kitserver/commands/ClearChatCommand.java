package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			User playerUser = User.getUser(player.getName());
			if(playerUser.hasPermission(Rank.ModPlus)){
				for(int i = 0;i < 201; i++){
					Bukkit.broadcastMessage("         ");
				}
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+playerUser.getRank().getPrefix()+player.getName()));
			}else
				Messenger.send(Main.NO_PERM_MSG, player, Messenger.WARN);
		}else{
			for(int i = 0;i<101;i++){
				Bukkit.broadcastMessage("         ");
			}
			
			Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+"&r&lCONSOLE"));
			
		}
	
		return false;
	}
	
}
