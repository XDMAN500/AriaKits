package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LookUpCommand implements CommandExecutor,TabCompleter {

	

	public boolean onCommand(final CommandSender sender, Command cmd, String label,String[] args) {
		int len = args.length;
		if(len == 0){
			Messenger.send("/lookup <player>", sender, Messenger.WARN);
			return false;
		}
		if(sender instanceof Player){
			if(!User.getUser(sender.getName()).hasPermission(Rank.Mod)){
				Messenger.send(Main.NO_PERM_MSG, sender,Messenger.WARN);
				return false;
			}
		}
		final OfflinePlayer pl = Utils.getPlayer(args[0]);
		new BukkitRunnable(){
			public void run(){
				UUID id = pl.getUniqueId();
				DataManager.checkForMatches(id);
				Messenger.send("&5====&9LookUp&5======", sender, Messenger.WARN);
				if(DataManager.isBanListExist(false)){
					String name = "";
					if(Main.playerBanList.contains(pl.getName())){
						name = "&4"+pl.getName();
					}else{
						name = "&a"+pl.getName();
					}
						Messenger.send("&3Player&8: "+name, sender, Messenger.WARN);
				}else{
					Messenger.send("&3Player&8:&a "+pl.getName(), sender, Messenger.WARN);
				}
				List<String> list;
				list =new ArrayList<String>(DataManager.getDataBaseEntryNames(id));
				list.remove(pl.getName());
				if(DataManager.isBanListExist(false)){
					for(int i = 0 ;i< list.size(); i++){
						String s = list.get(i);
						if(Main.playerBanList.contains(s)){
							list.set(i, "&4"+s+"&a");
						}else{
							list.set(i, "&a"+s+"&a");
						}
					}
				}
				Messenger.send("&3Alts&8:&a "+list, sender, Messenger.WARN);
				Messenger.send("&3Ips&8:&a "+DataManager.getDataBaseEntryIps(id), sender, Messenger.WARN);
				if(pl.isOnline()){
					Messenger.send("&3Address&8:&a" + ((Player)pl).getAddress().getAddress().getHostAddress(), sender, Messenger.INFO);
				}else{
					Messenger.send("&3Address:&8&a " + "Not online", sender, Messenger.INFO);
				}
				Messenger.send("&5====================", sender, Messenger.WARN);
		}
	}.runTaskAsynchronously(Main.getPluginInstance());
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
		
			return Utils.matchOfflinePlayers(args[0]);
			
		}
		return null;
		
	}

}
