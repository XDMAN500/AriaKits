package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveWarpCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			if(!User.getUser(((Player)sender).getName()).hasPermission(Rank.Owner)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.WARN);
				return false;
			}
		}
					if(args.length == 1){
				boolean result =DataManager.removeWarp(args[0]);
				if(result){
					Messenger.send("Warp &6"+args[0]+" &aremoved.", sender, Messenger.INFO);
					return true;
				}
				
				Messenger.send("Warp doesn't exist.", sender, Messenger.WARN);
				return false;
			}
			Messenger.send("Warps:"+DataManager.getWarpList().toString(), sender, Messenger.INFO);
		
		return false;
	}
}
