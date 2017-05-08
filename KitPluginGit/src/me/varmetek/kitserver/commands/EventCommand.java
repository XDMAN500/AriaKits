package me.varmetek.kitserver.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.varmetek.kitserver.api.DuelManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		int len = args.length;
		List<String> argList = new ArrayList<String>(Arrays.asList(new String[] {"host","leave","join","end","j","h","l","e","start","fs"}));
		if(sender instanceof Player){
			Player pl = (Player)sender;
			User user = User.getUser(pl.getName());
			if(len == 0){
				Messenger.send("/event <host,leave,join,end>", pl, Messenger.WARN);
			}
			if(len > 0){
				if(!argList.contains(args[0])){
					Messenger.send("/event <host,leave,join,end>", pl, Messenger.WARN);
				}
				
				if(args[0].equals("leave")){
					if(HostedEvent.getCurrent() == null){
						Messenger.send("No event is running.",pl, Messenger.WARN);
						return false;
					}
					//HostedEvent curEv = HostedEvent.getCurrent();
					if(HostedEvent.getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())){
						HostedEvent.getCurrent().removePlayer(pl);
						Messenger.send("You have left " + HostedEvent.getCurrent().getEventType().name(),pl, Messenger.INFO);
						if(HostedEvent.getCurrent().isStarted()){
							user.setGameMode(User.GameMode.SPECTATOR);
						}
					}else{
						Messenger.send("You are not in " + HostedEvent.getCurrent().getEventType().name(),pl, Messenger.WARN);
					}
				}
				
				if(args[0].equals("join") || args[0].equals("j") ){
					if(HostedEvent.getCurrent() == null){
						Messenger.send("No event is running.",pl, Messenger.WARN);
						return false;
					}
					if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId())){
						Messenger.send("Cant join an event while in a 1v1.",pl, Messenger.WARN);
						return false;
					}
					if(DuelManager.getQueuedPlayers().contains(pl.getUniqueId())){
						Messenger.send("Cant join an event while in the 1v1 queue.",pl, Messenger.WARN);
						return false;
					}
					if(HostedEvent.getCurrent().isStarted()){
						Messenger.send(HostedEvent.getCurrent().getEventType().name() + " has already started.",pl, Messenger.WARN);
						return false;
					}
					//HostedEvent curEv = HostedEvent.getCurrent();
					if(HostedEvent.getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())){
						Messenger.send("You are already enrolled for " + HostedEvent.getCurrent().getEventType().name(),pl, Messenger.WARN);
					}else{
						HostedEvent.getCurrent().addPlayer(pl);
						Messenger.send("You have joined " + HostedEvent.getCurrent().getEventType().name(),pl, Messenger.INFO);
						
					}
				}
				if(args[0].equals("start") || args[0].equals("fs") ){
					if(!user.hasPermission(Rank.ModPlus)){
						Messenger.send(Main.NO_PERM_MSG,pl, Messenger.WARN);
						return false;
					}
					if(HostedEvent.getCurrent() == null){
						Messenger.send("No event is currently running.",pl, Messenger.WARN);
						return false;
					}
					HostedEvent.getCurrent().setStartDelay((short) 0);
					Messenger.send("&aEvent force started.", pl);
					
				}
				if(args[0].equals("host") || args[0].equals("h") ){
					if(!user.hasPermission(Rank.Donator)){
						Messenger.send(Main.NO_PERM_MSG,pl, Messenger.WARN);
						return false;
					}
					if(HostedEvent.getCurrent() != null){
						Messenger.send("And event is currently running.",pl, Messenger.WARN);
						return false;
					}
					if(len<5){
						Messenger.send("/event host <eventtype> <fighttype> refills<on,off>  recrafts<on,off>",pl, Messenger.WARN);
						return false;
					}
					
					HostedEvent.EventType eType = HostedEvent.EventType.getEventTypeByName(args[1]);
					HostedEvent.FightType fType = HostedEvent.FightType.getEventTypeByName(args[2]);
					boolean refs = args[3].equalsIgnoreCase("on");
					boolean rec = args[4].equalsIgnoreCase("on");
					
					if(eType == null){
						Messenger.send("No such event type.", pl, Messenger.WARN);
						return false;
					}
					
					if(fType == null){
						Messenger.send("No such fight type.", pl, Messenger.WARN);
						return false;
					}
					HostedEvent.hostEvent(eType, fType, refs, rec);
					pl.performCommand("event join");
				}
				
				if(args[0].equals("end")|| args[0].equals("e") ){
					if(!user.hasPermission(Rank.Mod)){
						Messenger.send(Main.NO_PERM_MSG,pl, Messenger.WARN);
						return false;
					}
					if(HostedEvent.getCurrent() == null){
						Messenger.send("No event is running.",pl, Messenger.WARN);
						return false;
					} 
					HostedEvent.getCurrent().stop();
				
				}
			}
		}
		return false;
	}
}
