package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.List;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			int len = args.length;
			Player pl = (Player)sender;
			if(len <2){
				Messenger.send("/msg <player> <message>", pl,Messenger.WARN);
				return false;
			}
			Player plTarg = Bukkit.getPlayer(args[0]);
			if(plTarg == null){
				Messenger.send("That player is not online.", pl,Messenger.WARN);
				return false;
			}
			String msg = "";
			for(int i = 1; i< len; i++){
				msg += " " + Utils.colorCode(args[i]);
			}
			Messenger.send("&9(&6" + pl.getName() + " &8»&7 me&9)&3:&r" +  msg , plTarg,Messenger.INFO);
			Messenger.send("&9(&7me &8»&6 " + plTarg.getName()+ "&9)&3:&r" +  msg , pl,Messenger.INFO);
			List<User> l = new ArrayList<User>( User.getAllPermedUsers(Rank.ModPlus));
			User msgSender = User.getUser(pl.getName());
			User msgReciever = User.getUser(plTarg.getName());
			l.remove(msgSender);
			l.remove(msgReciever);
			msgSender.setLastMsgReciever(plTarg);
			msgReciever.setLastMsgReciever(pl);
			
			for(User u : l){
				if(u.getPlayer() instanceof Player){
					if(u.getNotifyMode() == User.NotifyMode.STAFF){
						Player y0 = (Player)u.getPlayer();
						Messenger.send("&8[&7" + pl.getName() + " &8>&7 " + plTarg.getName()+ "&8]&3:&r" +  msg , y0,Messenger.INFO);
					}
				}
				
			}
					
		}
		return false;
	}
	
	
	
	
	
	
	
}
