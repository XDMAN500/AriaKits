package me.varmetek.kitserver.commands;

import java.util.HashMap;
import java.util.Map;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;




public class TeleportCommand implements CommandExecutor  {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(sender instanceof Player){
			User user = User.getUser(sender.getName());
			if(user.getGameMode() == User.GameMode.PLAYER){
				if(user.hasPermission(Rank.TrialMod)){
					((Player) sender).performCommand("m a");
				}else{
					Messenger.send("You may teleport in spectator mode. /mode spec", sender,Messenger.INFO);
					return false;
				}
			}
		}
		
		int len = args.length;
		//List<String> validArg;
		Player plTarg = null;
		World toWorld = null;
		double toX = 0;
		double toY = 0;
		double toZ = 0;
		Player toTele =null;
		
		Map<String,String> argMap = new HashMap<String,String>();
		argMap.put("teleport <player>","Teleports you to a player.");
		argMap.put("teleport <player> <player2>","Teleports player to player2.");
		argMap.put("teleport <player> <x> <y> <z>","Teleports player that location.");
		argMap.put("teleport <player> <x> <y> <z> <world>","Teleports player that location in that world.");

		
		if(len == 0){
			Messenger.send("&1-------&4Teleport&7-&aHelp&1-------", sender, Messenger.INFO);
			for(String arg: argMap.keySet()){
				String desc = argMap.get(arg);
				Messenger.send("&4/&6" + arg + " &8:&3 " + desc,sender, Messenger.INFO);
			}
		}else{
			plTarg = Bukkit.getPlayer(args[0]);
			if(plTarg == null){
				Messenger.send("That player is not online.",sender, Messenger.WARN);
				return false;
			}
			if(len == 1){
				if(! (sender instanceof Player)){
					Messenger.send("You cannot teleport.",sender, Messenger.WARN);
					return false;
				}
				toWorld = plTarg.getWorld();
				toX = plTarg.getLocation().getX();
				toY = plTarg.getLocation().getY();
				toZ = plTarg.getLocation().getZ();
				toTele = (Player)sender;
				
				Messenger.send("Teleporting to &7" + plTarg.getName()+"&a.",sender, Messenger.INFO);
			}
			if(len >1){
				if(sender instanceof Player){
					User user = User.getUser(sender.getName());
					if(user.getGameMode() == User.GameMode.SPECTATOR){
						Messenger.send("Command not availiable in Spectator Mode.", sender,Messenger.WARN);
						return false;
					}
				}
				if(len == 2){
				
					Player plTarg2 = Bukkit.getPlayer(args[1]);
					
					if(plTarg2== null){
						Messenger.send("The target player is not online.",sender, Messenger.WARN);
						return false;
					}
					toWorld = plTarg2.getWorld();
					toX = plTarg2.getLocation().getX();
					toY = plTarg2.getLocation().getY();
					toZ = plTarg2.getLocation().getZ();
					toTele = plTarg;
					
					//plTarg.teleport(plTarg2);
					Messenger.send("&aTeleporting &7" + plTarg.getName() + " &ato &7" + plTarg2.getName()+"&a.",sender, Messenger.INFO);
				}
				if(len > 2){
					if(len == 3){
					
						Messenger.send("/teleport <player> <x> <y> <z>", sender, Messenger.WARN);
						return false;
					}
					
					if(len >3){
						try{
							toX = Double.parseDouble(args[1]);
							toY = Double.parseDouble(args[2]);
							toZ = Double.parseDouble(args[3]);
						}catch(NumberFormatException e){
							Messenger.send("The coordinates need a valid number.", sender, Messenger.WARN);
							return false;
						}
						toTele = plTarg;
						if(len == 4){
							toWorld = plTarg.getWorld();
							
						}
						if(len>4){
							if(len == 5){
								toWorld = Bukkit.getWorld(args[0]);
								if(toWorld == null){
									Messenger.send("No world by that name.", sender, Messenger.WARN);
									return false;
								}
							}
						}
						
					}
					}
				}
				if(toTele == null || toWorld == null){
					Messenger.send("There was an error Teleporting.", sender, Messenger.WARN);
				}
					toTele.teleport(new Location(toWorld,toX,toY,toZ,toTele.getLocation().getYaw(),toTele.getLocation().getPitch()));
				
			} 
		
		
		
		return false;
	}
	
}
							
