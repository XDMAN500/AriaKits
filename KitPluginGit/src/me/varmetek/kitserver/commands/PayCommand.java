package me.varmetek.kitserver.commands;

import java.math.BigInteger;
import java.util.List;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor,TabCompleter{


	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(sender instanceof Player){
			Player giver = (Player) sender;
			
				
				if(args.length == 2){
					OfflinePlayer receipent  = Utils.getPlayer(args[0]);
					int value;
					
					//if(receipent != null){
						User receipentUser  = User.getUser(receipent.getName());
						if(!receipent.isOnline()){
							DataManager.loadUser(receipentUser );
						}
						User giverUser = User.getUser(giver.getName());
						
						try{
						value = Integer.parseInt(args[1]);
						}catch(NumberFormatException e){
							Messenger.send("&3" + args[1] + "&a is not a number!", giver, Messenger.WARN);
							return false;
							
						}
						
						if(giverUser.getMoney().compareTo(BigInteger.valueOf(value))!= -1){
							giverUser.subtractMoney(value);
							receipentUser.addMoney(value);
							Messenger.send("You payed "+ receipentUser.getPlayer().getName()+" "+value+".", giver, Messenger.INFO);
							if(receipent.isOnline()){
								Messenger.send(giverUser.getPlayer().getName()+" has payed you "+value+".", (Player)receipent, Messenger.INFO);
							}
						}else{
							Messenger.send("&cYou need &e"+(BigInteger.valueOf(value).subtract(giverUser.getMoney())) +" &cmore &5&obits &cin order to complete the transaction.", giver);
						}
						if(!receipent.isOnline()){
							DataManager.savePlayerUser(receipentUser);
							receipentUser.remove();
						}
						
				/*	}else{
						Messenger.send("That player is not currently online.", giver, Messenger.WARN);
					}*/
					
				}else{
					Messenger.send(Main.commandCorrections.get(commandLabel), giver, Messenger.WARN);
				}
			
		}
	
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		
		if(args.length == 1){
			return Utils.matchOnlinePlayers(args[0]);
		}
		return null;
	}
}

