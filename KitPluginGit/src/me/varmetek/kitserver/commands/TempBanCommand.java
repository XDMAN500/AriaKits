package me.varmetek.kitserver.commands;

import java.util.Calendar;
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
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TempBanCommand implements TabCompleter, CommandExecutor {

	@SuppressWarnings("deprecation")
public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(sender instanceof Player){
			User user = User.getUser(((Player)sender).getName() );
			if(!user.hasPermission(Rank.TrialMod)){
				Messenger.send(Main.NO_PERM_MSG, sender);
				return false;
			}
		}
		
		if(args.length > 1){
			int time = 0;
			try{
				time =Math.abs(Integer.parseInt(args[1]));
			}catch(NumberFormatException e){
				Messenger.send("The Argument requires a number.", sender, Messenger.WARN);
				return false;
			}
			OfflinePlayer toBan = Bukkit.getOfflinePlayer(args[0]);
			if(toBan == null){
				Messenger.send("Player has never joined.", sender, Messenger.WARN);
				return false;
			}
			//User user = User.getUser(toBan.getName());
			User user ;//= User.getUser(toBan.getName());
			if(toBan.isOnline()){
				toBan = Bukkit.getPlayer(args[0]);
				user = User.getUser(toBan.getName());
			}else{
				user = User.getUser(args[0]);
				DataManager.loadUser(user);
			}
			
			{
			User user1 = User.getUser(((Player)sender).getName() );
			if(!user1.hasPermission(Rank.Mod)){
				if(time > 86400){
					time  = 86400;
				}
				return false;
			}
			user.addBanExpire(time);
			}
			 Calendar cd = Calendar.getInstance();
			Calendar bd = user.getBanExpire();
			
			String message = Utils.getCalendarDiff(cd, bd);
			
			user.setBanMessage("Temporarily Banned for "+message );
			if(toBan.getUniqueId() != null)
				
				//Bukkit.getBanList(BanList.Type.NAME).addBan(toBan.getName(), message, bd.getTime(), Main.banAppealMsg);
			DataManager.addBan(toBan.getUniqueId().toString(), false);
			
			if(toBan.isOnline()){
				//((Player)toBan).kickPlayer("Temporarily Banned for " +message + Main.banAppealMsg);
				((CraftPlayer)toBan).disconnect("Temporarily Banned for " +message + Main.banAppealMsg);
			}
			
			Bukkit.broadcastMessage(Utils.colorCode("&c&o"+toBan.getName()+" &c has been temporarily banned for "+ message+"."));
		}else{
			Messenger.send("/tempban <player> <time>", sender, Messenger.WARN);
		}
		return false;
}
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 0){
			
			return Utils.matchOfflinePlayers(args[0]);
			
	
		}
		return null;
	}
}
//Long banTime = user.getBanExpire().getTime().getTime();
//Long currentTime = Calendar.getInstance().getTime().getTime();

//Long diff = banTime - currentTime;
//Calendar c = Calendar.getInstance();
/*
 Calendar cd = Calendar.getInstance();
Calendar bd = user.getBanExpire();
//	c.setTimeInMillis(diff);
int years = bd.get(Calendar.YEAR)-cd.get(Calendar.YEAR);
int months = bd.get(Calendar.MONTH)-cd.get(Calendar.MONTH);
int weeks = bd.get(Calendar.WEEK_OF_MONTH)-cd.get(Calendar.WEEK_OF_MONTH);
int days = bd.get(Calendar.DAY_OF_WEEK)-cd.get(Calendar.DAY_OF_WEEK);
int hours = bd.get(Calendar.HOUR_OF_DAY)-cd.get(Calendar.HOUR_OF_DAY);
int minutes = bd.get(Calendar.MINUTE)-cd.get(Calendar.MINUTE);
int seconds = bd.get(Calendar.SECOND)-cd.get(Calendar.SECOND);

if(seconds < 0) minutes-= -1;
if(minutes < 0) hours-= -1;
if(hours < 0) days-= -1;
if(days < 0) weeks-= -1;
if(weeks < 0) months-= -1;
if(months < 0) years-= -1;

String message = "";
if(years != 0){
	message += Math.abs(years)+" years ";
}
if(months != 0){
	message += Math.abs(months)+" months ";
}

if(weeks != 0){
	message += Math.abs(weeks)+" weeks ";
}
if(days!= 0){
	message += Math.abs(days)+" days ";
}
if(hours != 0){
	message += Math.abs(hours)+" hours ";
}
if(minutes!= 0){
	message += Math.abs(minutes)+" minutes ";
}
if(seconds != 0){
	message += Math.abs(seconds)+" seconds ";
}*/