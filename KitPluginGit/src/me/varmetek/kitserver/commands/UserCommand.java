package me.varmetek.kitserver.commands;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.User.ProducerLevel;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.main.Main;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class UserCommand implements CommandExecutor, TabCompleter{
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
			
				if(sender instanceof Player){
					Player player = (Player)sender;
					User user = User.getUser(player.getName());
					if(!user.hasPermission(Rank.Admin)){
						Messenger.send(Main.NO_PERM_MSG , player, Messenger.WARN);
						return false;
					}
				}
					if(args.length == 0){
						
						
						Messenger.send(Main.commandCorrections.get("user") ,sender, Messenger.WARN);
						return false;
					}
					if(args.length > 0){
						OfflinePlayer info = Utils.getPlayer(args[0]);
						User infoUser = User.getUser(info.getName());
						if(!info.isOnline()){
							DataManager.loadUser(infoUser);
						}
						if(info != null){
							if(args.length == 1){
								
								Messenger.send("&1===================",sender, Messenger.INFO);
								Messenger.send("&bName:&a" +  infoUser.getPlayer().getName(), sender,Messenger.INFO);
								if(info.isOnline()){
									Messenger.send("&bip:&a" + ((Player)info).getAddress().toString(), sender, Messenger.INFO);
								}else{
									Messenger.send("&bip:&a" + "Not online", sender, Messenger.INFO);
								}
								Messenger.send("&bUUID:&a" + info.getUniqueId(), sender, Messenger.INFO);
								Calendar c = Calendar.getInstance();
								Date d = new Date();
								 d.setTime(info.getFirstPlayed());
								c.setTimeInMillis(info.getFirstPlayed());
								c.setTime(d);
							
								
								Messenger.send("&bRank:&a" + infoUser.getRank().name(), sender, Messenger.INFO);
								Messenger.send("&bOp:&a" + info.isOp(), sender, Messenger.INFO);
								
								Messenger.send("&bFirst Joined:&a" + d, sender, Messenger.INFO);
								Messenger.send("&1===================",sender, Messenger.INFO);
								return false;
							}
						}		
						
							if(args.length >1){
								if(args[1].equalsIgnoreCase("edit")){
									Object value;
									if(infoUser.hasPermission(Rank.Admin) ){
										
										if(args.length==4 && !args[2].equalsIgnoreCase("rank")){
											Messenger.send(Main.commandCorrections.get("user"), sender, Messenger.WARN);
											return false;
										}
										
										
										if(args.length == 2 ||args.length == 3){
											Messenger.send(Main.commandCorrections.get("user"), sender, Messenger.WARN);
											return false;
										}
										
										
										
								if(args[2].equalsIgnoreCase("kills")){
										
										try{
											value = Integer.parseInt(args[4]);
										}catch(NumberFormatException e){
											Messenger.send(args[4]+" is not a number." , sender, Messenger.WARN);
											return false;
										}
										
										if(args[3].equalsIgnoreCase("add")){
											infoUser.setKills(infoUser.getKills( )+ (int)value);
											Messenger.send(value +" kills have been added to "+infoUser.getPlayer().getName()+"'s kills." , sender, Messenger.WARN);
											
										}else{
											if(args[3].equalsIgnoreCase("subtract")){
												infoUser.setKills(infoUser.getKills( )- (int)value);
												Messenger.send(value +" kills have been added to "+infoUser.getPlayer().getName()+"'s kills." , sender, Messenger.WARN);
											
											}else{
												if(args[3].equalsIgnoreCase("set")){
													infoUser.setKills((int)value);
													Messenger.send(infoUser.getPlayer().getName()+"'s kills have been set to "+ value+"." , sender, Messenger.WARN);
											
												}else{
													Messenger.send("Only add, subtract, and set are valid arguments." , sender, Messenger.WARN);
												}	
											}
										}
										
										
									}
								if(args[2].equalsIgnoreCase("exp")){
									long xp;
									try{
										xp = Long.parseLong(args[4]);
									}catch(NumberFormatException e){
										Messenger.send(args[4]+" is not a valid number." , sender, Messenger.WARN);
										return false;
									}
									
									if(args[3].equalsIgnoreCase("add")){
										infoUser.addEXP(xp);
										Messenger.send(xp +" xp have been added to "+infoUser.getPlayer().getName()+"'s xp." , sender, Messenger.WARN);
										
									}else{
										if(args[3].equalsIgnoreCase("subtract")){
											infoUser.subtractEXP(xp);
											Messenger.send(xp +" xp have been added to "+infoUser.getPlayer().getName()+"'s xp." , sender, Messenger.WARN);
										
										}else{
											if(args[3].equalsIgnoreCase("set")){
												infoUser.setEXP(xp);
												Messenger.send(infoUser.getPlayer().getName()+"'s xp have been set to "+ xp+"." , sender, Messenger.WARN);
										
											}else{
												Messenger.send("Only add, subtract, and set are valid arguments." , sender, Messenger.WARN);
											}	
										}
									}
									
									
								}
								if(args[2].equalsIgnoreCase("level")){
									int lvl;
									try{
										lvl = Integer.parseInt(args[4]);
									}catch(NumberFormatException e){
										Messenger.send(args[4]+" is not a valid number." , sender, Messenger.WARN);
										return false;
									}
									
									if(args[3].equalsIgnoreCase("add")){
										infoUser.addLevels(lvl);
										infoUser.setEXP(0L);
										Messenger.send(lvl +" levels have been added to "+infoUser.getPlayer().getName()+"'s levels." , sender, Messenger.WARN);
										
									}else{
										if(args[3].equalsIgnoreCase("subtract")){
											infoUser.subtractLevels(lvl);
											infoUser.setEXP(0L);
											
											Messenger.send(lvl +" levels have been added to "+infoUser.getPlayer().getName()+"'s levels." , sender, Messenger.WARN);
										
										}else{
											if(args[3].equalsIgnoreCase("set")){
												infoUser.setLevel(lvl);
												infoUser.setEXP(0L);
												Messenger.send(infoUser.getPlayer().getName()+"'s levels have been set to "+ lvl+"." , sender, Messenger.WARN);
										
											}else{
												Messenger.send("Only add, subtract, and set are valid arguments." , sender, Messenger.WARN);
											}	
										}
									}
									
									
								}
									}		
									if(args[2].equalsIgnoreCase("deaths")){
										try{
											value = Integer.parseInt(args[4]);
										}catch(NumberFormatException e){
											Messenger.send(args[4]+" is not a number." , sender, Messenger.WARN);
											return false;
										}
										
										if(args[3].equalsIgnoreCase("add")){
											infoUser.addDeaths((int)value);
											Messenger.send(value +" deaths have been added to "+infoUser.getPlayer().getName()+"'s deaths." , sender, Messenger.WARN);
											
										}else{
											if(args[3].equalsIgnoreCase("subtract")){
												infoUser.subtractDeaths((int)value);
												Messenger.send(value +" deaths have been added to "+infoUser.getPlayer().getName()+"'s deaths." , sender, Messenger.WARN);
											
											}else{
												if(args[3].equalsIgnoreCase("set")){
													infoUser.setDeaths((int)value);
													Messenger.send(infoUser.getPlayer().getName()+"'s deaths have been set to "+ value+"." , sender, Messenger.WARN);
											
												}else{
													Messenger.send("Only add, subtract, and set are valid arguments." , sender, Messenger.WARN);
												}	
											}
										}
									}
									
									/*if(args[2].equalsIgnoreCase("xp")){
										try{
											value = Integer.parseInt(args[4]);
										}catch(NumberFormatException e){
											Messenger.send(args[4]+" is not a number." , player, Messenger.WARN);
											return false;
										}
										
										if(args[3].equalsIgnoreCase("add")){
											Main.debug(infoUser.getXp()+" + "+(int)value);
											infoUser.setXp(infoUser.getXp() + (int)value);
											infoUser.exp += (int)value;
											Messenger.send(value +" xp has been added to "+infoUser.getPlayer().getName()+"'s xp." , player, Messenger.WARN);
											
										}else{
											if(args[3].equalsIgnoreCase("subtract")){
												infoUser.subtractXp((int)value);
												Messenger.send(value +" xp has been added to "+infoUser.getPlayer().getName()+"'s xp." , player, Messenger.WARN);
											
											}else{
												if(args[3].equalsIgnoreCase("set")){
													infoUser.setXp((int)value);
													Messenger.send(infoUser.getPlayer().getName()+"'s xp have been set to "+ value+"." , player, Messenger.WARN);
											
												}else{
													Messenger.send("Only add, subtract, and set are valid arguments." , player, Messenger.WARN);
												}	
											}
										}
									}
									if(args[2].equalsIgnoreCase("levels")){
										try{
											value = Integer.parseInt(args[4]);
										}catch(NumberFormatException e){
											Messenger.send(args[4]+" is not a number." , player, Messenger.WARN);
											return false;
										}
										
										if(args[3].equalsIgnoreCase("add")){
											infoUser.addLevel((int)value);
											Messenger.send(value +" levels have been added to "+infoUser.getPlayer().getName()+"'s levels." , player, Messenger.WARN);
											
										}else{
											if(args[3].equalsIgnoreCase("subtract")){
												infoUser.subtractLevel((int)value);
												Messenger.send(value +" levels have been added to "+infoUser.getPlayer().getName()+"'s levels." , player, Messenger.WARN);
											
											}else{
												if(args[3].equalsIgnoreCase("set")){
													infoUser.setLevel((int)value);
													Messenger.send(infoUser.getPlayer().getName()+"'s levels have been set to "+ value+"." , player, Messenger.WARN);
											
												}else{
													Messenger.send("Only add, subtract, and set are valid arguments." , player, Messenger.WARN);
												}	
											}
										}
									}*/
									if(args[2].equalsIgnoreCase("killstreak")){
										try{
											value = Integer.parseInt(args[4]);
										}catch(NumberFormatException e){
											Messenger.send(args[4]+" is not a number." , sender, Messenger.WARN);
											return false;
										}
										
										if(args[3].equalsIgnoreCase("add")){
											infoUser.addKillstreak((int)value);
											Messenger.send(value +" has been added to "+infoUser.getPlayer().getName()+"'s killstreak." , sender, Messenger.WARN);
											
										}else{
											if(args[3].equalsIgnoreCase("subtract")){
												infoUser.subtractKillstreak((int)value);//setKillStreak(infoUser.getKillStreak( )- (int)value);
												Messenger.send(value +" has been subtracted from "+infoUser.getPlayer().getName()+"'s killstreak." , sender, Messenger.WARN);
											
											}else{
												if(args[3].equalsIgnoreCase("set")){
													infoUser.setKillstreak((int)value);
													Messenger.send(infoUser.getPlayer().getName()+"'s killstreak has been set to "+ value+"." , sender, Messenger.WARN);
											
												}else{
													Messenger.send("Only add, subtract, and set are valid arguments." , sender, Messenger.WARN);
												}	
											}
										}
										
									}
									
									if(args[2].equalsIgnoreCase("rank")){
										if((Rank.getRankByName(args[4]))== null){
											Messenger.send("There is no rank that goes by that name." , sender, Messenger.WARN);
											return false;
										}
									value = Rank.getRankByName(args[4]);
										if(args[3].equalsIgnoreCase("promote")){
											if(infoUser.getRank().getPromote()== null){
												Messenger.send("No higher rank to promote this player too" , sender, Messenger.WARN);
												//if(infoUser.getPlayer() != null && infoUser.getPlayer().isOnline() && infoUser.getPlayer().getName().length()<15){
													//((Player)info).setPlayerListName(((Rank)value).getPrefix()+info.getName());
											//	}
												return false;
											}
											infoUser.setRank(infoUser.getRank().getPromote());
											Messenger.send(infoUser.getPlayer().getName() + " has been promoted to " + infoUser.getRank().name() , sender, Messenger.WARN);
											
										}else{
											if(args[3].equalsIgnoreCase("demote")){
												if(infoUser.getRank().getDemote()== null){
													Messenger.send("No lower rank to demote this player too" , sender, Messenger.WARN);
													
												//	info.setPlayerListName(((Rank)value).getPrefix()+info.getName());
													return false;
												}
												infoUser.setRank(infoUser.getRank().getDemote());
												Messenger.send(infoUser.getPlayer().getName() + " has been demoted to " + infoUser.getRank().name() , sender, Messenger.WARN);
												
											}else{
												if(args[3].equalsIgnoreCase("set")){
													infoUser.setRank((Rank)value);
													//info.setPlayerListName(((Rank)value).getPrefix()+info.getName());
													Messenger.send(infoUser.getPlayer().getName()+"'s rank has been set to "+ ((Rank)value).name()+"." , sender, Messenger.WARN);
											
												}else{
													Messenger.send("Only promote, demote, and set are valid arguments." , sender, Messenger.WARN);
												}	
										
											}
										}
										
									}	
									
									if(args[2].equalsIgnoreCase("money")){
										BigInteger val;
										try{
										
											val = new BigInteger(args[4]);
										}catch(NumberFormatException e){
											Messenger.send(args[4]+" is not a number." ,sender, Messenger.WARN);
											return false;
										}
										//Utils.broadcast(val.toString());
										if(args[3].equalsIgnoreCase("add")){
											
											infoUser.addMoney(val);
											Messenger.send(val+" has been added from "+infoUser.getPlayer().getName()+"'s money." , sender, Messenger.WARN);
											
										}else{
											if(args[3].equalsIgnoreCase("subtract")){
												
												infoUser.subtractMoney(val);
												Messenger.send(val +"has been subtracted to "+infoUser.getPlayer().getName()+"'s money." , sender, Messenger.WARN);
											
											}else{
												if(args[3].equalsIgnoreCase("set")){
													infoUser.setMoney(val);
													Messenger.send(infoUser.getPlayer().getName()+"'s money has been set to "+ val+"." , sender, Messenger.WARN);
											
												}else{
													Messenger.send("Only add, subtract, and set are valid arguments." , sender, Messenger.WARN);
												}	
											}
										}
										
										
									}
								}else
									if(args[1].equalsIgnoreCase("nuke")){
										DataManager.nukeUser(infoUser);
										Messenger.send("User has been reset." , sender, Messenger.WARN);
										User.getUser(info.getName());
									}else{
										if(args[1].equalsIgnoreCase("reset")){
										infoUser.setKills(0);
										infoUser.setCombatLog(0);
										infoUser.addKillstreak(0);
										infoUser.setDeaths(0);
										infoUser.addCombo(0);
										infoUser.setMoney(0);
										infoUser.setBoughtKits(new HashSet<Kit>());
											Messenger.send("User has been reset." , sender, Messenger.WARN);
											User.getUser(info.getName());
										}else{
											if(args[1].equalsIgnoreCase("producer")){
												if(args.length>2){
													try{
														infoUser.setProdLvL(ProducerLevel.valueOf(args[2].toUpperCase()));
														Messenger.send("&7User is now a "+infoUser.getProdLvL().name()+ " producer", sender);
													}catch(IllegalArgumentException e){
														Messenger.send("&cInvalid Flag",sender);
													}
												}else{
												
													Messenger.send("&cValied args are: &7"+ProducerLevel.values().toString(),sender);
												}
											}else{
												Messenger.send("You don't have permission for this command." , sender, Messenger.WARN);
												return false;
											}
										}}
								}else{
									Messenger.send(Main.commandCorrections.get("user") , sender, Messenger.WARN);
									return false;
								}
							
							
						}else{
							Messenger.send("That player isnt online", sender, Messenger.WARN);
						}
					
				
			
		return false;
	}
	
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
			/*List<String> tabs = new ArrayList<String>();
			for(Player p : Bukkit.getOnlinePlayers()){
				tabs.add(p.getName());
			}
			return tabs;*/
			return Utils.matchOfflinePlayers(args[0]);
		}
		if(args.length == 2){
			return Arrays.asList(new String[] {"edit"});
		}
		if(args.length == 3){
			return Arrays.asList(new String[] {"kills","deaths","money","killstreak","rank"});
		}
		
		if(args.length == 4){
			if(args[2].equalsIgnoreCase("rank"))
			return Arrays.asList(new String[] {"promote","demote","set"});
			else
				return Arrays.asList(new String[] {"add","subtract","set"});
		}
	
		return null;
	}
}
