package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender ,Command cmd, String label,String[] args) {
		if(sender instanceof Player){
			Player pl = (Player)sender;
		
			for(ItemStack i: pl.getInventory().getContents()){
				i.setDurability(Short.MAX_VALUE);
			}
			
			for(ItemStack i: pl.getInventory().getArmorContents()){
				i.setDurability(Short.MAX_VALUE);
			}
			
			Messenger.send("Repaired Your Gear.", pl, Messenger.INFO);
		}
		
		return false;
	}



}
