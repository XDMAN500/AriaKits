package me.varmetek.kitserver.commands;

import java.util.List;
import java.util.UUID;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class UnbanCommand implements CommandExecutor, TabCompleter{
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(sender instanceof Player){
			User user = User.getUser(((Player)sender).getName() );
			if(!user.hasPermission(Rank.ModPlus)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.INFO);
				return false;
			}
		}
		
		if(args.length > 0){
			
		 
			if(args[0].length()<16){
				OfflinePlayer	toBan = Utils.getPlayer(args[0]);
				if(toBan == null ){
					Messenger.send("Player has never joined.", sender, Messenger.WARN);
					return false;
				}
				User user ;//= User.getUser(toBan.getName());
				user = User.getUser(toBan.getName());
				if(!toBan.isOnline()){
				
					DataManager.loadUser(user);
				}
				user.setBanMessage("");
				user.resetBanExpire();
				if(toBan.getUniqueId() != null)
				//Bukkit.getBanList(BanList.Type.NAME).pardon(toBan.getName());
			//DataManager.removeBan(toBan.getUniqueId().toString(), false);
		
				if(!toBan.isOnline()){
					
					DataManager.savePlayerUser(user);
					user.remove();
				}
				Bukkit.broadcastMessage(Utils.colorCode("&8&o"+toBan.getName()+" &8 has been unbanned."));
				//Bukkit.broadcastMessage(Main.colorCode("&6&l"+toBan.getName()+" &a has been unbanned."));
				
			}else{
				String toBan =args[0];
				if(UUID.fromString(toBan) == null){
					if(toBan == null){
						Messenger.send("Invid UUID", sender, Messenger.WARN);
						return false;
					}
				}
				DataManager.removeBan(toBan, false);
				Messenger.send("UUID unbanned", sender, Messenger.INFO);
			}
			
			Main.playerBanList.clear();
			//toBan.kickPlayer(reason+Main.banAppealMsg);
		
		}else{
			Messenger.send("/unban <player>", sender, Messenger.WARN);
		}
		return false;
	}
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
	
			return Utils.matchString(Main.playerBanList, args[0]);

		}
		return null;
	

	}	
}
