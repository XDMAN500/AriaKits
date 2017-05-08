package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.User.GameMode;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)){
			return false;
		}

	
		
		if(args.length  == 0){
			Messenger.send("/mode <player,spectator,builder>", sender, Messenger.WARN);
			return false;
		}
		GameMode mode = User.GameMode.getByName(args[0]);
		if(mode == null){
			Messenger.send("/mode <player,spectator,builder>", sender, Messenger.WARN);
			return false;
		}
		User user = null;
		if(args.length ==1){
			user = User.getUser( ((Player)sender).getName() );
			if(user.hasPermission(mode.requiredRank)){
				//Main.debug("ModeCommand: Giving Mode: "+ mode.name());
				user.setGameMode(mode);
			}else{
				Messenger.send("You don't have permission to change to mode " + mode.name().toLowerCase()+".", sender, Messenger.WARN);
				return false;
			}
			Messenger.send("Your Game mode has been set to "+ mode.name().toLowerCase()+".", sender, Messenger.INFO);
				return false;
		}else{
			if(args.length ==2){
			User	cuser = User.getUser( ((Player)sender).getName() );
			if(!cuser.hasPermission(Rank.Admin)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.WARN);
				return false;
			}
				Player targ = Bukkit.getPlayer(args[1]);
				if(targ == null){
					Messenger.send("That player is not online.", sender, Messenger.WARN);
					return false;
				}
				user = User.getUser(targ.getName());
				if(cuser.hasPermission(mode.requiredRank)){
				//	Main.debug("ModeCommand: Giving Mode: "+ mode.name());
					user.setGameMode(mode);
					Messenger.send("Your Game mode has been set to "+ mode.name().toLowerCase()+".", targ, Messenger.INFO);
				}else{
					Messenger.send("You don't have permission to change to mode " + mode.name().toLowerCase()+".", sender, Messenger.WARN);
					return false;
				}
				Messenger.send("Their Game mode has been set to "+ mode.name().toLowerCase()+".", sender, Messenger.INFO);
					return false;
			}
		}
		
	return false;
	}
	
}
