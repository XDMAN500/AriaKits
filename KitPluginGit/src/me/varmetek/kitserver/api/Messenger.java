package me.varmetek.kitserver.api;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Messenger {
	WARN ("&c"),
	INFO ("&a"),
	PLAIN ("") ;//§ Isn't usable in game there for can be used to set a non-translated text.
	
	public String colorCode;
	
	Messenger(String colorCode ){
		this.colorCode = colorCode;

	} 

	
	private String pluginPrefix = Utils.colorCode( "&b[&6KitServer&b]&r");
	

	public String getPluginPrefix(){
		return pluginPrefix;
	}
	
	public static void send(String message , CommandSender player, Messenger type){

		if(player != null){
				player.sendMessage( Utils.colorCode(type.colorCode + message));		
			}
	}
	public static void send(String message , CommandSender player){

			send(message,player,PLAIN);
	}
	
	
	
	public static void sendGroup(String message , CommandSender[] playerList, Messenger type){
		for(CommandSender player : playerList ){
			if(player != null){
					send(message,player,type);		
				}
			}
	}
	
	public static void sendGroup(String message , Collection<? extends Player> playerList, Messenger type){
		for(CommandSender player : playerList ){
			if(player != null){
				send(message,player,type);		
				}
			}
	}
	
	public static void sendGroupU(String message , Collection<? extends UUID> uuidList ,Messenger type){
		for(UUID e : uuidList ){
			Player player = Bukkit.getPlayer(e);
			if(player != null){
					send(message,player,type);		
				}
			}
	}
	

	
	public static void sendGroup(String message , CommandSender[] playerList){
		sendGroup(message ,playerList,PLAIN);
	}
	
	public static void sendGroup(String message ,  Collection<? extends Player> playerList){
		sendGroup(message ,playerList,PLAIN);
	}
	public static void sendGroupU(String message , Collection<? extends UUID> playerList){
		sendGroupU(message ,playerList,PLAIN);
	}
	
	public static void sendAll(String message){
		sendGroup(message,Utils.getOnlinePlayers());
	}
}
	
	