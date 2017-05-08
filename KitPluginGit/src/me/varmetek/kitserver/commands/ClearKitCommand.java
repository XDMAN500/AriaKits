package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.User.GameMode;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class ClearKitCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
			if(!(sender instanceof Player))return false;
				
				Player player = (Player) sender;
				User playerUser = User.getUser(player.getName());
				if(playerUser.getCanChangeKit() != true){
					Messenger.send("You may not change your kit.", player, Messenger.WARN);
					return false;
				}
				if(playerUser.getCombatLog() >0 ){
					Messenger.send("You may not change your kit in combat.", player, Messenger.WARN);
					return false;
				}
				if(playerUser.getGameMode() == GameMode.PLAYER)
					playerUser.changeKit(null);
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.setFireTicks(0);
			
					 for(PotionEffect e:  player.getActivePotionEffects()){
						 player.removePotionEffect(e.getType());
					 }
					Messenger.send("You have reset your kit.", player, Messenger.INFO);
					Utils.giveStartItems(player);
					
			
			
		
		return false;
	}

}
