package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(!(sender instanceof Player)) return false;
		int len = args.length;
		Player pl = (Player)sender;
		User user = User.getUser(pl.getName());
		Player toReply = user.getLastMsgReciever();
		if( toReply == null){
			Messenger.send("No one to reply to.", pl, Messenger.WARN);
			return false;
		}
		String msg = "";
		for(int i = 0; i< len; i++){
			msg += " " + Utils.colorCode(args[i]);
		}
		pl.performCommand("msg " + toReply.getName()+ " "+ msg);
		return false;
	}

}
