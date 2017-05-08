package me.varmetek.kitserver.commands;

import java.util.HashMap;
import java.util.Map;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MotdCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		int len = args.length;
		
		Map<String,String> argMap = new HashMap<String,String>();
		argMap.put("motd get","Retrieves the message of the day.");
		argMap.put("motd set <msg>","Sets the message of the day.");

		if(sender instanceof Player){
			if(!User.getUser(sender.getName()).hasPermission(Rank.Admin)){
				Messenger.send(Main.NO_PERM_MSG, sender,Messenger.WARN);
				return false;
			}
		}
		
		if(len == 0){
			Messenger.send("&1-------&4MOTD&7-&aHelp&1-------", sender, Messenger.INFO);
			for(String arg: argMap.keySet()){
				String desc = argMap.get(arg);
				Messenger.send("&4/&6" + arg + " &8:&3 " + desc,sender, Messenger.INFO);
			}
		}else{
			if(len == 1){
				if(args[0].equalsIgnoreCase("get")){
					Messenger.send("&9MOTD&8: &r" + DataManager.getMotd(), sender, Messenger.WARN);
				}else{
					if(args[0].equalsIgnoreCase("set")){
						Messenger.send("motd set <msg>", sender, Messenger.WARN);
					}else{
						Messenger.send("&cValid args are&8:&7 set, get.", sender, Messenger.WARN);
						return false;
					}
				}
			}
			if(len > 1){
				
					if(args[0].equalsIgnoreCase("set")){
						String msg = "";
						for(int i = 1; i< len; i++){
							msg += " " +args[i];
						}
						DataManager.setMotd(msg);
						Main.config();
						Messenger.send("&9MOTD has been set to&8: &r" + DataManager.getMotd(), sender, Messenger.WARN);
					}else{
						Messenger.send("&cValid args are&8:&7 set.", sender, Messenger.WARN);
						return false;
					}
				
			}
		}
		return false;
	}

}
