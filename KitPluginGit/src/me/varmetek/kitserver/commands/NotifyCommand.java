package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotifyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		
		if( !(sender instanceof Player))return false;
		int len = args.length;
		Player pl = (Player)sender;
	
		if(len == 0){
			Messenger.send("/notify <normal,staff,debug>",pl,Messenger.WARN);
			return false;
		}
		
		if(len > 0){
			User.NotifyMode nMode;
			try{
				nMode = User.NotifyMode.valueOf(args[0].toUpperCase());
				User.getUser(pl.getName()).setNotifyMode(nMode);
				Messenger.send("You will now get notifications at level "+ nMode.name().toLowerCase(),pl,Messenger.INFO);
			}catch(IllegalArgumentException e){
				Messenger.send("/notify <normal,staff,debug>",pl,Messenger.WARN);
				return false;
			}

			
		}
		return false;
	}

}
