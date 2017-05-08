package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.List;

import me.varmetek.kitserver.api.Arena;
import me.varmetek.kitserver.api.DuelManager;
import me.varmetek.kitserver.api.DuelRequest;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		int len = args.length;
		if(!(sender instanceof Player))return false;
		Player pl = (Player)sender;
		User user = User.getUser(pl.getName());
		if(len == 0){
			Messenger.send("/duel <player>", pl, Messenger.WARN);
			return false;
		}
		
		Player plTarg = Bukkit.getPlayer(args[0]);
		
		DuelRequest br = DuelRequest.getRandomDuelRequest();
		DuelManager.FightStyle fs = br.getFightStyle();
		boolean refs = br.isRefills();
		boolean recraft = br.isRecrafts();
		if(plTarg == null){
			Messenger.send("That Player is not online.", pl, Messenger.WARN);
			return false;
		}
		User userTarg = User.getUser(plTarg.getName());
		if(plTarg.getUniqueId().equals(pl.getUniqueId())){
			Messenger.send("Silly, you cannot 1v1 yourself.", pl, Messenger.INFO);
			return false;
			
		}
		if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId())){
			Messenger.send("You are already in a match.", pl, Messenger.WARN);
			return false;
		}
		
		if(DuelManager.getMatchedPlayers().contains(plTarg.getUniqueId())){
			Messenger.send("That player is already in a match.", pl, Messenger.WARN);
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
				Messenger.send("You can't request a 1v1 while enrolled for a event.", pl, Messenger.WARN);
				return false;
			}
			
			if(HostedEvent.getCurrent().getEnrolledPlayers().contains(plTarg.getUniqueId())){
				Messenger.send("You can't request a 1v1 withs this player while they are enrolled for a event.", pl, Messenger.WARN);
				return false;
			}
			
		}
		if(user.getGameMode() != User.GameMode.PLAYER){
			Messenger.send("You can only send duel request in player mode.", pl, Messenger.WARN);
			return false;
		}
		if(len >1){
			try{
				fs= DuelManager.FightStyle.valueOf(args[1].toUpperCase());
			}catch(IllegalArgumentException e){
				List<String> el = new ArrayList<String>();
				for(DuelManager.FightStyle fse :DuelManager.FightStyle.values() ){
					el.add(fse.name().toLowerCase());
				}
				Messenger.send("Valid Fighting Syles: "+Utils.listToString(el), pl, Messenger.WARN);
				return false;
			}
			
			fs= DuelManager.FightStyle.valueOf(args[1].toUpperCase());
			
		}
		
		if(len == 3){
			Messenger.send("/duel <player> <style> <refills> <recrafts>", pl, Messenger.WARN);
			return false;
		}
		if(len > 3){
			refs = args[2].equalsIgnoreCase("on");
			//Utils.debug(refs);
			recraft = args[3].equalsIgnoreCase("on");
			//Utils.debug(recraft);
		}
		if(!user.getMatchRequests().keySet().contains(plTarg.getUniqueId())){
			user.addMatchRequest(plTarg.getUniqueId() ,fs,refs,recraft);

			if(!userTarg.getMatchRequests().keySet().contains(pl.getUniqueId())){
				Messenger.send("You have challenged &6" + plTarg.getName() + "&a a 1v1.", pl, Messenger.INFO);
				Messenger.send("&6"+pl.getName()+ " has chellenged you to a 1v1. /duel " + pl.getName(), plTarg, Messenger.INFO);
			}else{
				String msg = "&6==== &9&0&l<p1> &a&lVS &9&o&l<p2> &6====";
				msg.replace("<p1>", pl.getName()).replace("<p2>", plTarg.getName());
				Messenger.send(msg, pl, Messenger.WARN);
				Messenger.send(msg, plTarg, Messenger.WARN);
			}
			}else{
			Messenger.send("You have already challenged &6" +plTarg.getName()+"&a." , pl, Messenger.INFO);
		}
		return false;
	}
	
}
