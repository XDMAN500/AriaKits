package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.Arrays;

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

public class ChatSettingsCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		int len = args.length;
		boolean chatMode = Utils.chatLocked;
		if(len>0  ){

			if(sender instanceof Player){
				User user = User.getUser(((Player)sender).getName());
				if(!user.hasPermission(Rank.ModPlus)){
					Messenger.send(Main.NO_PERM_MSG, sender, Messenger.INFO);
					return false;
				}
			}
			
			if(!(new ArrayList<String>( Arrays.asList( new String[] {"lock","unlock","delay","clear"} ) ).contains( args[0].toLowerCase() )) ){
				Messenger.send("lock,unlock,delay", sender, Messenger.WARN);
				return false;
			}
			if(args[0].equalsIgnoreCase("clear")){
				for(int i = 0;i < 201; i++){
					Bukkit.broadcastMessage("         ");
				}
				if(sender instanceof Player){
					User user = User.getUser(((Player)sender).getName());
					Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+user.getRankTitle()));

				}else{
					Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+"&r&lCONSOLE"));
				}
			}
			
			if(args[0].equalsIgnoreCase("lock")){
				if(len >1){
					if(args[1].equalsIgnoreCase("on")){
						Utils.chatLocked = true;
					}else{
						if(args[1].equalsIgnoreCase("off")){
							Utils.chatLocked = false;
						}else{
							Messenger.send("Invalid imput: must be off or on.", sender, Messenger.WARN);
							return false;
						}
					}
				}else{
					Utils.chatLocked = !Utils.chatLocked;
					}	
			}
			
			
			if(args[0].equalsIgnoreCase("unlock")){
				Utils.chatLocked = false;
			}
			
			if(args[0].equalsIgnoreCase("delay")){
				if(len >1){
					int amount;
					try{
						amount= Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						Messenger.send("Invalid imput: Must be a whole number.", sender, Messenger.WARN);
						return false;
					}
					amount = (int) Math.abs(amount);
					Utils.chatDelay = amount;
					Messenger.send("Chat delay has been set to "+(amount)+" miliseconds." , sender, Messenger.INFO);
				}else{
					Messenger.send("/chatsettings delay <number>" , sender, Messenger.WARN);
				}
			}
			if(!args[0].equalsIgnoreCase("delay")){
				if(chatMode == Utils.chatLocked){
				
					Messenger.send("Chat is already set to that mode." , sender, Messenger.INFO);
					return false;
				}else{
					if(sender  instanceof Player){
						User playerUser = User.getUser( ((Player)sender).getName() );
						if(Utils.chatLocked)
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by "+ playerUser.getRankTitle()));
						else
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by "+ playerUser.getRankTitle()));
						
							
					}else{
						if(Utils.chatLocked)
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by &r&LCONSOLE"));
						else
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by &r&LCONSOLE"));
						
						
					}
				}
			}
		}else{
			Messenger.send("lock,unlock,delay", sender, Messenger.WARN);
		}
	
		
	
		
		return false;
	}
}
