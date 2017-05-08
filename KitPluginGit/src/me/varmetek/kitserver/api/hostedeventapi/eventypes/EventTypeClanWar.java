package me.varmetek.kitserver.api.hostedeventapi.eventypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class EventTypeClanWar extends HostedEvent implements Listener  {
	public final List<UUID> teamRed = new ArrayList<UUID>();
	public final List<UUID> teamBlue = new ArrayList<UUID>();
	@Override
	protected void update() {
		
	
		if(isStarted())return;
		if(getEventType() != EventType.CLANWAR)return;
		
		if(teamRed.isEmpty()){
			
			for(UUID u: teamBlue){
				Player pl = Bukkit.getPlayer(u);
				pl.performCommand("spawn");
				Messenger.send("You have won " + getEventType().name(), pl, Messenger.INFO);
			}
			stop();
		}
		
		if(teamBlue.isEmpty()){
			
			for(UUID u : teamRed){
				Player pl = Bukkit.getPlayer(u);
				pl.performCommand("spawn");
				Messenger.send("You have won " + getEventType().name(), pl, Messenger.INFO);
			}
			
			stop();
		}
	}
	protected void start(){
		for(UUID e: enrolledPlayers){
			if(Bukkit.getPlayer(e) == null){
				enrolledPlayers.remove(e);
			}else{
				if(enrolledPlayers.size() % 2 !=  0){
					enrolledPlayers.remove(e);
				}
				//Bukkit.getPlayer(e).teleport(DataManager.getLoc("brack-start"));
			}
		}
		
		List<UUID> l = new ArrayList<UUID>(new HashSet<UUID>(enrolledPlayers));
		
		for(int f = 0 ; f< l.size(); f++){
			if(f+1 <=  l.size()/2){
				teamRed.add(l.get(f));
			}else{
				teamBlue.add(l.get(f));
			}
			
			for(UUID e: teamRed){
				Bukkit.getPlayer(e).teleport(DataManager.getLoc("cw-pointRed"));
			}
			for(UUID e: teamBlue){
				Bukkit.getPlayer(e).teleport(DataManager.getLoc("cw-pointBlue"));
			}
		}
	}
	public void removePlayer(Player pl){
		teamRed.remove(pl.getUniqueId());
		teamBlue.remove(pl.getUniqueId());
		getEnrolledPlayers().remove(pl.getUniqueId());
	}
	
	@EventHandler
	public void onDamageEV(EntityDamageByEntityEvent ev){
		
		if(!isStarted())return;
		
		if(ev.getEntity() instanceof Player && ev.getEntity() instanceof Player){
			Player damagee = (Player)ev.getEntity();
			Player damager = (Player)ev.getDamager();
			UUID deUUID = damagee.getUniqueId();
			UUID drUUID = damager.getUniqueId();
			
			if( (getEnrolledPlayers().contains(deUUID) && !getEnrolledPlayers().contains(drUUID)) ||
					(getEnrolledPlayers().contains(drUUID) && !getEnrolledPlayers().contains(deUUID))	){
				ev.setCancelled(true);
				return;
				
			}
			
			if(getEventType() == EventType.CLANWAR){
				if( (teamRed.contains(deUUID) && teamRed.contains(drUUID)) ||
						(teamBlue.contains(deUUID) && teamBlue.contains(drUUID))){
					ev.setCancelled(true);
					return;
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	protected void giveInventory(Player pl){
		//HostedEvent.pa rentGiveInventory(pl);
		
	User user  = User.getUser(pl.getName());
		user.changeKit(Kit.PVP);
		Color c = Color.GRAY;
			ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);	
			LeatherArmorMeta lm = (LeatherArmorMeta) armor.getItemMeta();
			lm.setColor(c);
		switch (getCurrent().getFightType()){
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
		ItemStack wool = new ItemStack(Material.WOOL,1,DyeColor.GREEN.getData());
	{
		if(getEventType() == EventType.CLANWAR){
		
			if(getEventType() == EventType.CLANWAR){
				
				if(teamRed.contains(pl.getUniqueId())){
					wool= new ItemStack(Material.WOOL,1,DyeColor.RED.getData());
					c = Color.RED;
				}else{
					c = Color.BLUE;
					if(teamBlue.contains(pl.getUniqueId())){
					wool= new ItemStack(Material.WOOL,1,DyeColor.BLUE.getData());
					}
				}
			}
		
		
		
		}
	}
	if(getFightType() == FightType.STONE){
		pl.getInventory().setArmorContents(new ItemStack[] {});
		if(getEventType() == EventType.CLANWAR )
			pl.getInventory().setHelmet(wool);
	}else{
		lm.setColor(c);
		armor.setItemMeta(lm);
		pl.getInventory().setHelmet(armor);
		pl.getInventory().setChestplate(armor);	
		pl.getInventory().setLeggings(armor);	
		pl.getInventory().setBoots(armor);
	}
	Utils.giveSoup(pl, getCurrent().doesAllowRefills(), getCurrent().doesAllowRecafts());
	}
	@Override
	public void stop() {
		HostedEvent.parentStop();
		
	}

}
