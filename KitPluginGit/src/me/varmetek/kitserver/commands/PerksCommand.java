package me.varmetek.kitserver.commands;

import java.util.List;

import me.varmetek.kitserver.api.GraphicalUserInterface;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Perk;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PerksCommand implements CommandExecutor, Listener
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if( !(sender instanceof Player))return false;
		Player pl = (Player)sender;
		User user = User.getUser(pl.getName());
		
		if(user.getOwnedPerks().isEmpty()){
			Messenger.send("&cYou don't own any perks. Get some with &9/gamble perks&c.", pl);
			return false;
		}
		if(user.getCurrentPerk() != null){
			Messenger.send("&cYou have already selecred a perk.", pl);
			return false;
		}
		if(args.length == 0){
			getPerkView(pl);
		}else{
			Perk p = Perk.parse(args[0]);
			if(!user.ownsPerk(p)){
				Messenger.send("&cYou don't own that perk.",pl);
			return false;
			}
			user.setCurrenPerk(p);
			if(p.getType() == Perk.Type.HEALTH){
				pl.setHealthScale(20+Perk.VALUES.get(p.getType())[p.getLevel()-1]*2);
			}
			user.removerOwnedPerk(p);
			Messenger.send("&a You chose the perk "+ p.getType().getName()+" @ Tier "+p.getLevel(), pl);
			pl.closeInventory();
			
			
			
			
		}
		return false;
	}
	
	private static void  getPerkView(Player pl){
		User user = User.getUser(pl.getName());
		if(user.getOwnedPerks().isEmpty())return;
		
		Inventory inv = Bukkit.createInventory(null, 54,Utils.colorCode("Perks"));
		for(String e: Utils.list2set(user.getOwnedPerks()) ){
			Perk perk = Perk.parse(e);
			ItemStack item = perk.getIcon();
			int  num = -1;
			for(String id :user.getOwnedPerks()){
				if(e.equals(id)){
					num++;
				}
			}
			ItemMeta im = item.getItemMeta();
			List<String> lore = Utils.getLore(im);
			lore.add(Utils.colorCode("&3&o"+num+" more"));
			im.setLore(lore);
			item.setItemMeta(im);
			
			
			inv.addItem(item);
		}
		pl.openInventory(inv);
		user.setInv(inv);
		user.setGui(GraphicalUserInterface.PERK_VIEW);
		
		
	}
	
	@EventHandler
	public void getClicked(InventoryClickEvent ev){
		Player player =(Player)ev.getWhoClicked();
		User user = User.getUser(player.getName());
		ItemStack item = ev.getCurrentItem();
		Inventory cinv = ev.getInventory();
		//Random ran = new Random();
		if(cinv == null){return;}
		if(!cinv.equals(user.getInv()))return;
			if(user.getGui() == GraphicalUserInterface.PERK_VIEW){
				ev.setCancelled(true);
				Perk p = Perk.getFromItem(item);
				if(p == null){
					Utils.debug("Perk is null");
				}else{
				player.performCommand("perks "+ p.toString());
				}
			
				return;
			}
	}
	
}
