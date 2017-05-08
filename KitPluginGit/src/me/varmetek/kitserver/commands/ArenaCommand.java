package me.varmetek.kitserver.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.varmetek.kitserver.api.Arena;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(!(sender instanceof Player))return false;
		Player pl = (Player)sender;
		User user = User.getUser(pl.getName());
		int len = args.length;
		List<String> validArg;
		if(!user.hasPermission(Rank.ModPlus)){
			Messenger.send(Main.NO_PERM_MSG, pl, Messenger.WARN);
			return false;
		}
		Arena arena = null;
		Map<String,String> argMap = new HashMap<String,String>();
		argMap.put("arena help","Shows all arena commands.");
		argMap.put("arena list","Shows all arenas.");
		argMap.put("arena <name>","Shows info of arena.");
		argMap.put("arena <name> create","Creates an area if it doesn't exist.");
		argMap.put("arena <name> setpos <1,2>","Sets the position of a Arena.");
		argMap.put("arena <name> disable", "Disables an arena.");
		argMap.put("arena <name> enable", "Enable an arena.");
		argMap.put("arena <name> remove", "Removes an arena.");
		argMap.put("arena <name> tppos <1,2>", "Teleports to the position of an arena.");
		
		if(len == 0){
			Messenger.send("&1-------&4Arena&7-&aHelp&1-------", pl, Messenger.INFO);
			for(String arg: argMap.keySet()){
				String desc = argMap.get(arg);
				Messenger.send("&4/&6" + arg + " &8:&3 " + desc, pl, Messenger.INFO);
			}
		}
		if(len > 0){
			arena =   Arena.getArea(args[0], false);
			if(len == 1){
				validArg = Arrays.asList(new String[] {"help","list"} );
			
				if(validArg.contains(args[0].toLowerCase())){
					if(args[0].equals("help")){
						pl.performCommand("arena");
						
					}
					if(args[0].equals("list")){
						Messenger.send("&1-------&4Arena&7-&aArena List&1-------",pl,Messenger.INFO);
						if(Arena.getArenas().isEmpty()){
							Messenger.send("No arenas",pl,Messenger.INFO);
							return false;
						}
						
						for(Arena a: Arena.getArenas()){
							
							Messenger.send("&2"+a.getName()+ " &7: "+ booleanToString(a.isAvaliable(),new String[] {"&aReady","&cNot Ready"}),pl,Messenger.INFO);
						}
						
					}
				}else{
					
						if(arena == null){
							Messenger.send("That arena doesn't exist.", pl, Messenger.WARN);
							return false;
						
					}else{
						Messenger.send("&1-------&4Arena&7-&a"+arena.getName()+"&1-------", pl, Messenger.INFO);
						Messenger.send("&2&lPosition 1&7:&a" +  booleanToString(arena.getPosition1()!= null,new String[] {"&aSet","&cNot Set"}), pl, Messenger.INFO);
						Messenger.send("&2&lPosition 2&7:&a" +  booleanToString(arena.getPosition2()!= null,new String[] {"&aSet","&cNot Set"}), pl, Messenger.INFO);
						Messenger.send("&2&lEnabled &7:&a" +  booleanToString(arena.isEnabled(),new String[] {"&aYes","&cNo"}), pl, Messenger.INFO);
					}
				
				}
			}
			
			if(len >1){
				if(len == 2){
				validArg = Arrays.asList(new String[] {"create","setpos","disable","enable","remove","tppos"} );
				if(!validArg.contains(args[1].toLowerCase())){
					Messenger.send("&4Valid Parameters:&a"+ Utils.listToString(validArg), pl, Messenger.WARN);
					return false;
				}
				if(args[1].equalsIgnoreCase("create")){
						if(arena == null){
								arena = Arena.getArea(args[0], true);
								Messenger.send("&4You have created arena:&a "+arena.getName(),pl,Messenger.WARN);
						}else{
							Messenger.send("That arena already exists.",pl,Messenger.WARN);
							return false;
						}
				}
				
				if(args[1].equalsIgnoreCase("setpos")){
					Messenger.send("/arena <name> setpos <1,2>",pl,Messenger.WARN);
					return false;
				}
				if(args[1].equalsIgnoreCase("tppos")){
					Messenger.send("/arena <name> tppos <1,2>",pl,Messenger.WARN);
					return false;
				}
				if(args[1].equalsIgnoreCase("disable")){
					if(arena == null){
						Messenger.send("That arena doesn't exist.", pl, Messenger.WARN);
						return false;
					
					}else{
							arena.setEnabled(false);
							Messenger.send("&2You have disabled arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
					}
				}
				
				
				
				if(args[1].equalsIgnoreCase("enable")){
					if(arena == null){
						Messenger.send("That arena doesn't exist.", pl, Messenger.WARN);
						return false;
					
					}else{
							arena.setEnabled(true);
							Messenger.send("&2You have enabled arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
					}
				}
				
				
				if(args[1].equalsIgnoreCase("remove")){
					if(arena == null){
						Messenger.send("That arena doesn't exist.", pl, Messenger.WARN);
						return false;
					
					}else{
							Messenger.send("&2You have removed arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
							arena.remove();
					}
				}
				}
				if(len >2){
					if(len == 3){
						validArg = Arrays.asList(new String[] {"1","2"} );
						if(!validArg.contains(args[2].toLowerCase())){
							Messenger.send("&4Valid Parameters:&a"+ Utils.listToString(validArg), pl, Messenger.WARN);
							return false;
						}
					if(args[1].equalsIgnoreCase("setpos")){
						if(arena == null){
							Messenger.send("That arena doesn't exist.", pl, Messenger.WARN);
							return false;
						
						}else{
								if(args[2].equalsIgnoreCase("1")){
									arena.setPosition1(pl.getLocation());
									Messenger.send("&2You have set the position 1 for arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
								}else{
									if(args[2].equalsIgnoreCase("2")){
										arena.setPosition2(pl.getLocation());
										Messenger.send("&2You have set the position 2 for arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
									}else{
										Messenger.send("/arena <name> setpos <1,2>", pl, Messenger.WARN);
										return false;
									}
								}
						}
					}
					
					
					if(args[1].equalsIgnoreCase("tppos")){
						if(arena == null){
							Messenger.send("That arena doesn't exist.", pl, Messenger.WARN);
							return false;
						
						}else{
								if(args[2].equalsIgnoreCase("1")){
									if(arena.getPosition1() == null){
										Messenger.send("&4Position 1 is not set for arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
									}else{
										Messenger.send("&2You have teleported to the position 1 of arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
										pl.teleport(arena.getPosition1());
									}
								}else{
									if(args[2].equalsIgnoreCase("2")){
										if(arena.getPosition2() == null){
											Messenger.send("&4Position 2 is not set for arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
										}else{
											Messenger.send("&2You have teleported to the position 2 of arena&8:&a&o "+arena.getName(), pl, Messenger.WARN);
											pl.teleport(arena.getPosition2());
										}
									}else{
										Messenger.send("/arena <name> setpos <1,2>", pl, Messenger.WARN);
										return false;
									}
								}
						}
					}
					
					
					
					}
					}
			}
		}
		return false;
	}
	private  String booleanToString(boolean val ,String[] returns){
		if(val == false){
			return returns[1];
		}else{
			return returns[0];
		}
	}
}
