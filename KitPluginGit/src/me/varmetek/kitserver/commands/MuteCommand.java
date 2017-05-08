package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.Calendar;
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

public class MuteCommand implements CommandExecutor {

public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(sender instanceof Player){
			User user = User.getUser(((Player)sender).getName() );
			if(!user.hasPermission(Rank.TrialMod)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.INFO);
				return false;
			}
		}
		
		if(args.length == 2){
			int time = 0;
			try{
				time =Math.abs(Integer.parseInt(args[1]));
			}catch(NumberFormatException e){
				Messenger.send("The Argument requires a number.", sender, Messenger.WARN);
				return false;
			}
			Player toMute = Bukkit.getPlayer(args[0]);
			if(toMute == null){
				Messenger.send("Player is not online.", sender, Messenger.WARN);
				return false;
			}
			User user = User.getUser(toMute.getName());
			
			user.addMuteExpire(time);
		
			 Calendar cd = Calendar.getInstance();
			Calendar bd = user.getMuteExpire();
			
			String message = Utils.getCalendarDiff(cd, bd);
			
			
			
			
			
			Bukkit.broadcastMessage(Utils.colorCode("&8&o"+toMute.getName()+" &8 has been muted for "+ message+"."));
		}else{
			if(args.length == 1){
				Player toMute = Bukkit.getPlayer(args[0]);
				if(toMute == null){
					Messenger.send("Player is not online.", sender, Messenger.WARN);
					return false;
				}
				User user = User.getUser(toMute.getName());
				if(user.getMuteExpire() == null || user.getMuteExpire().after(Calendar.getInstance())){
					user.resetMuteExpire();
					Bukkit.broadcastMessage(Utils.colorCode("&8&o"+toMute.getName()+" &8 has been unmuted."));
					//Bukkit.broadcastMessage(Utils.colorCode("&6&l"+toMute.getName()+" &a has been unmuted."));
				}else{
					Bukkit.broadcastMessage(Utils.colorCode("&8&o"+toMute.getName()+" &8 has been permently muted."));
					user.setMuteExpire(null);
				}
			}else{
				Messenger.send("/mute <player> <time>", sender, Messenger.WARN);
			}
		}
		return false;
}
	@SuppressWarnings("deprecation")
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 0){
			List<String> l = new ArrayList<String>();
			//BanList b = Bukkit.getBanList(BanList.Type.NAME);
			//Set<BanEntry> be = Bukkit.getBanList(BanList.Type.NAME).getBanEntries();
			//b.addBan(arg0, arg1, arg2, arg3)
			for(Player e:Bukkit.getOnlinePlayers()){
				l.add(e.getName());
			}
			
			
			return l;
		}
		return null;
	}

}
