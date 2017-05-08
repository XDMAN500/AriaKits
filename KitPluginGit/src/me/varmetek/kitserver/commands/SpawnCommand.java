package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Messenger;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player send = (Player)sender;
		
			if(args.length == 0){
				if(DataManager.doesWarpExist("spawn")){
				send.performCommand("warp spawn");
				send.performCommand("clearkit");
			
				send.playSound(send.getLocation(),Sound.ENDERDRAGON_WINGS, 1f, 1f);
				}else{
				Messenger.send("Spawn warp has not been set.", send, Messenger.WARN);
				}
			}
			
			if(args.length ==1){
				if(args[0].equalsIgnoreCase("set")){
					send.performCommand("setwarp spawn");
				}else{
					if(args[0].equalsIgnoreCase("del")){
						send.performCommand("delwarp spawn");
					}
				}
			}
		}
	return false;
	}
}
