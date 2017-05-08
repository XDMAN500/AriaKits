package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player pl  = (Player)sender;
			User user = User.getUser(pl.getName());
			
			if(!user.hasPermission(Rank.TrialMod)){
				Messenger.send(Main.NO_PERM_MSG, pl, Messenger.WARN);
				return false;
			}
			
			if(user.getGameMode() != User.GameMode.STAFF){
				pl.performCommand("mode staff");
				//user.setGameMode(User.GameMode.STAFF);
			}
			if(args.length == 0){	
			
				if(user.isVanished()){
					user.setVanishedTo(null);
					Messenger.send("You are now unvanished.", pl, Messenger.INFO);
				}else{
					Rank c = user.getRank();
					user.setVanishedTo(c.getDemote());
				
					if(c.getDemote() == null){
						Messenger.send("You are now unvanished.", pl, Messenger.INFO);
					}else{
						Messenger.send("You are now vanished to ranks below " + c.getPrefix()+c.name() + "&a.", pl, Messenger.INFO);
					}
				}
			}else{
				if(args[0].equalsIgnoreCase("on")|| args[0].equalsIgnoreCase("off")){
					if(args[0].equalsIgnoreCase("on")){
						user.setVanishedTo(user.getRank().getDemote());
						Messenger.send("You are now vanished to ranks below " + user.getRank().getPrefix() + user.getRank().name() + "&a.", pl, Messenger.INFO);
					}
					if(args[0].equalsIgnoreCase("off")){
						user.setVanishedTo(null);
						Messenger.send("You are now unvanished.", pl, Messenger.INFO);
					}
				}else{
					Rank vRank = Rank.getRankByName(args[0]);
					if(vRank == null){
						Messenger.send("No such rank.", pl, Messenger.WARN);
						return false;
					}
					user.setVanishedTo(vRank.getDemote());
					
					if(vRank.getDemote() == null){
						Messenger.send("You are now unvanished.", pl, Messenger.INFO);
					}else{
						Messenger.send("You are now vanished to ranks below " + vRank.getPrefix()+vRank.name() + "&a.", pl, Messenger.INFO);
					}
					
				}
			}
		
	}
	return false;
	}
}
