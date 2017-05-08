package me.varmetek.kitserver.commands;

import java.util.List;

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

public class BanCommand implements CommandExecutor, TabCompleter{
	

	

	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(sender instanceof Player){
			User user = User.getUser(((Player)sender).getName() );
			if(!user.hasPermission(Rank.Mod)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.INFO);
				return false;
			}
		}
		
		if(args.length > 1){
			OfflinePlayer toBan = Utils.getPlayer(args[0]);
			User user;
			
			user = User.getUser(toBan.getName());
			if(!toBan.isOnline()){
			//	toBan = Bukkit.getPlayer(args[0]);
				DataManager.loadUser(user);
			}
			
			
			//Bukkit.broadcastMessage(toBan.getName());
			String reason = "";
			for(int i = 1; i<args.length; i++){
				reason += " "+args[i];
			}
			//Bukkit.getBanList(BanList.Type.NAME).addBan(toBan.getName(), reason, null, Utils.banAppealMsg);
		user.setBanMessage(reason);
		user.setBanExpire(null);
		if(!toBan.isOnline()){
			DataManager.savePlayerUser(user);
			user.remove();
		}
			//if(toBan.getUniqueId() != null)
		//	DataManager.addBan(toBan.getUniqueId().toString(), false);
			//Utils.playerBanList.clear();
			Bukkit.broadcastMessage(Utils.colorCode("&c&o"+toBan.getName()+" &c has been banned for " + reason + "."));			
			//toBan.setBanned(true);
			if(toBan.isOnline()){
			//	((CraftPlayer)toBan).disconnect("Banned for" + reason + Utils.banAppealMsg);
				((Player)toBan).kickPlayer("Banned for" + reason + Main.banAppealMsg);
			}
			
			
			//Utils.broadcastCustom("Server", "&6&l"+toBan.getName()+" &a has been banned for " + reason + ".");
			///Bukkit.broadcastMessage(Utils.colorCode("&6&l"+toBan.getName()+" &a has been banned for " + reason + Utils.banAppealMsg + "."));
		}else{
			Messenger.send("/ban <player> <reason>", sender, Messenger.INFO);
		}
			/*if(args.length > 2){
				int time = 0;
				try{
					time =Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					Messenger.send("The Argument requires a number.", sender, Messenger.WARN);
					return false;
				}
				OfflinePlayer toBan = Bukkit.getOfflinePlayer(args[0]);
				if(toBan == null){
					Messenger.send("Player has never joined.", sender, Messenger.WARN);
					return false;
				}
				User user = User.getUser(toBan.getName());
				String reason = args[1];
				
				user.setBanMessage(reason);
				//user.resetBanExpire();
				user.addBanExpire(time);
				//toBan.setBanned(true);
				if(toBan.isOnline()){
					((Player)toBan).kickPlayer(reason+Utils.banAppealMsg);
				}
				Bukkit.broadcastMessage(Utils.colorCode("&6&l"+toBan.getName()+" &a has been banned for "+ time+" seconds."));
			}else{
				Messenger.send("/ban <player> <reason> (seconds)", sender, Messenger.WARN);
			}*/
		

	
	return false;


	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
			/*(List<OfflinePlayer>s=new ArrayList<OfflinePlayer>( Arrays.asList(Bukkit.getOfflinePlayers()));
			if(args[0].length()>0){
			
				return Utils.getPlayerTabList(true,  s,args[0]);
			}else{
				return Utils.getPlayerTabList(false, s,args[0]);
			}*/
			return Utils.matchOfflinePlayers(args[0]);
			
		}
		return null;
		
	}
	
}

	
	