package me.varmetek.kitserver.commands;

import java.util.List;

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

public class KickCommand implements CommandExecutor {

	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(sender instanceof Player){
			User user = User.getUser(((Player)sender).getName() );
			if(!user.hasPermission(Rank.TrialMod)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.INFO);
				return false;
			}
		}
		
		if(args.length > 1){
			Player toBan = Bukkit.getPlayer(args[0]);
			
			if(toBan == null ){
				Messenger.send("Player is not online.", sender, Messenger.WARN);
				return false;
			}

			String reason = "";
			for(int i = 1; i<args.length; i++){
				reason += " "+args[i];
			}

				toBan.kickPlayer("Kicked for "+reason);
				Bukkit.broadcastMessage(Utils.colorCode("&e&o"+toBan.getName()+" &e has been kicked for " + reason + "."));
		//	Bukkit.broadcastMessage(Utils.colorCode("&6&l"+toBan.getName()+" &a has been kicked for " + reason + "."));
		}else{
			Messenger.send("/kick <player> <reason>", sender, Messenger.INFO);
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
			return Utils.matchOnlinePlayers(args[0]);
			
		}
			
		
		return null;
	}
	

}
/*List<String> l = new ArrayList<String>();
//BanList b = Bukkit.getBanList(BanList.Type.NAME);
//Set<BanEntry> be = Bukkit.getBanList(BanList.Type.NAME).getBanEntries();
//b.addBan(arg0, arg1, arg2, arg3)
for(OfflinePlayer e:Bukkit.getOfflinePlayers()){
	l.add(e.getName());
}


return l;*/