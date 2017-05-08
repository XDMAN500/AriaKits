package me.varmetek.kitserver.commands;

import java.util.Arrays;
import java.util.List;

import me.varmetek.kitserver.api.Arena;
import me.varmetek.kitserver.api.DuelManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		int len = args.length;
		if( !(sender instanceof Player)) return false;
		Player pl = (Player)sender;
		if(len == 0){
			Messenger.send("/queue <join,leave>", pl, Messenger.WARN);
			return false;
		}
		if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId())){
			Messenger.send("You are already in a match.", pl, Messenger.WARN);
			return false;
		}

		
		if(DuelManager.getQueuedPlayers().contains(pl.getUniqueId())){
			Messenger.send("You cannot duel while in the 1v1 queue.", pl, Messenger.WARN);
			return false;
		
		}
		
		if(Arena.getAvaliableArenas().isEmpty()){
			Messenger.send("No avaliable arenas.", pl, Messenger.WARN);
			return false;
		
		}
		if(HostedEvent.getCurrent() != null){
			if(HostedEvent.getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())){
				Messenger.send("You can't join the queue while enrolled for a event.", pl, Messenger.WARN);
				return false;
			}
			
		
			
		}
		List<String> validArgList = Arrays.asList(new String[] {"join","leave"});
		if(!validArgList.contains(args[0].toLowerCase())){
			Messenger.send("/queue <join,leave>", pl, Messenger.WARN);
			return false;
		}
		if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId())){
			Messenger.send("You cannot be added to the queue while in a battle.", pl, Messenger.WARN);
			return false;
		}
		if(args[0].equalsIgnoreCase("join")){
			if(DuelManager.getQueuedPlayers().contains(pl.getUniqueId())){
				Messenger.send("You are already in the 1v1 queue.", pl, Messenger.WARN);
				return false;
			}else{
				DuelManager.addPlayerToQueue(pl.getUniqueId());
				Messenger.send("You have been added to the 1v1 queue.", pl, Messenger.INFO);
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("leave")){
			if(DuelManager.getQueuedPlayers().contains(pl.getUniqueId())){
				
				DuelManager.removePlayerFromQueue(pl.getUniqueId());
				
				Messenger.send("You have left the 1v1 queue.", pl, Messenger.INFO);
				return true;
				
			}else{
				Messenger.send("You are already not in the 1v1 queue.", pl, Messenger.WARN);
				return false;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

}
