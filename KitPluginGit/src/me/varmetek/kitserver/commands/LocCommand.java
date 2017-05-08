package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player pl = (Player)sender;
			User user = User.getUser(pl.getName());
			
			if(!user.hasPermission(Rank.Owner)){
				Messenger.send(Main.NO_PERM_MSG,pl,Messenger.WARN);
				return false;
			}
			int len = args.length;
			
			List<String> argList = new ArrayList<String>(Arrays.asList(new String[] {"set","del","tp"}));
			if(len == 0){
				Messenger.send("/loc <set,del,tp>", pl, Messenger.WARN);
			}
			if(len > 0){
				if(!argList.contains(args[0].toLowerCase())){
					Messenger.send("/loc <set,del,tp>", pl, Messenger.WARN);
					return false;
				}
				
				if(args[0].equalsIgnoreCase("set")){
					if(len<2){
						Messenger.send("/loc set <name>", pl, Messenger.WARN);
						return false;
					}
					DataManager.setLoc(args[1], pl.getLocation());
					Messenger.send(args[1] + " has been set.", pl, Messenger.INFO);
				}
				if(args[0].equalsIgnoreCase("del")){
					if(len<2){
						Messenger.send("/loc del <name>", pl, Messenger.WARN);
						return false;
					}
					DataManager.removeLoc(args[1]);
					Messenger.send(args[1] + " has been removed.", pl, Messenger.INFO);
				}
				if(args[0].equalsIgnoreCase("tp")){
					if(len<2){
						Messenger.send("/loc tp <name>", pl, Messenger.WARN);
						return false;
					}
					pl.teleport(DataManager.getLoc(args[1]));
					Messenger.send(args[1] + " has been teleported to.", pl, Messenger.INFO);
				}
				if(args[0].equalsIgnoreCase("list")){
					
					
					Messenger.send("&aLocs&8: "+Utils.listToString(new ArrayList<String>(DataManager.getLocList())), pl, Messenger.INFO);
				}
			}
		}
		return false;
	}
}
