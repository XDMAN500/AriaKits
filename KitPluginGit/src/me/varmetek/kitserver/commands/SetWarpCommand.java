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

public class SetWarpCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player  = (Player)sender;
			User playerUser  = User.getUser(player.getName());
			if(playerUser.hasPermission(Rank.ModPlus)){
				if(args.length == 1){
					String warp =args[0].toLowerCase(); 
					DataManager.setWarp(warp, player.getLocation());
					DataManager.setWarpChangeKit(warp, true);
					DataManager.setWarpKit(warp, null);
					Messenger.send("You have set warp &6"+args[0]+Messenger.INFO.colorCode+".", player, Messenger.INFO);
				
				}else{
					Messenger.send("/setwarp <warp>", player,Messenger.WARN);
				}
			}else{
				Messenger.send(Main.NO_PERM_MSG, player,Messenger.WARN);
			}
		}
		return false;
	}
}
