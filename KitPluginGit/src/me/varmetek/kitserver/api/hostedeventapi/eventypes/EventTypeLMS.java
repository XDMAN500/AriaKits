package me.varmetek.kitserver.api.hostedeventapi.eventypes;

import java.util.ArrayList;
import java.util.UUID;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class EventTypeLMS extends HostedEvent implements Listener{

	@Override
	protected void update() {
	
		if(!isStarted())return;
		if(getEventType() != EventType.LMS)return;
	
		if(getEnrolledPlayers().size() <getMinimumPlayers() ){
			Player pl =Bukkit.getPlayer(new ArrayList<UUID>(enrolledPlayers).get(0));
			pl.performCommand("spawn");
			Utils.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
			stop();
			return;
			
		}
		
	}
	protected void start(){
		for(UUID e: enrolledPlayers){
			Player pl = Bukkit.getPlayer(e) ;
			if(pl == null){
				enrolledPlayers.remove(e);
			}else{
				pl.teleport(DataManager.getLoc("lms-start"));
				giveInventory(pl);
			}
		}
		
	}
	protected void giveInventory(Player pl){
		//HostedEvent.parentGiveInventory(pl);
		User user  = User.getUser(pl.getName());
		user.changeKit(Kit.PVP);
		
		switch (getFightType()){
		case BOW:
			pl.getInventory().setItem(0, new ItemStack(Material.BOW));	
			pl.getInventory().setItem(9, new ItemStack(Material.ARROW,64));	
			
			break;
		case NORMAL:
			pl.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));	
		
			
	
			break;
		case STONE:
			pl.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
			break;
	
		}
		Utils.giveSoup(pl, getCurrent().doesAllowRefills(), getCurrent().doesAllowRecafts());
		if(getFightType() != HostedEvent.FightType.STONE){
			Color c = Color.GRAY;
			ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);	
			LeatherArmorMeta lm = (LeatherArmorMeta) armor.getItemMeta();
			lm.setColor(c);
			lm.setColor(c);
			armor.setItemMeta(lm);
			pl.getInventory().setHelmet(armor);
			pl.getInventory().setChestplate(armor);	
			pl.getInventory().setLeggings(armor);	
			pl.getInventory().setBoots(armor);
		}
		
	
	
	}
	public void removePlayer(Player pl){
		getEnrolledPlayers().remove(pl.getUniqueId());
		update();
	
	}
	@Override
	public void stop() {
		HostedEvent.parentStop();
		
	}
}
