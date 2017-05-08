package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfigCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(sender instanceof Player)	{
			User user = User.getUser(sender.getName());
			if(!user.hasPermission(Rank.Admin)){
				Messenger.send(Main.NO_PERM_MSG, sender,Messenger.WARN);
				return false;
			}
		}
		Main.config();
		Messenger.send("Config succssfully reloaded.", sender, Messenger.INFO);
		return false;
	}

}
