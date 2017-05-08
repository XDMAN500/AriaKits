package me.varmetek.kitserver.api.hostedeventapi.eventypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.varmetek.kitserver.api.Area;
import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EventTypeBrackets extends HostedEvent implements Listener
{
	public final List<UUID> fighting = new ArrayList<UUID>();
	@Override
	protected void update() {
		if(getCurrent() == null)return;
		Utils.debug("Event is on");
		if(!getCurrent().isStarted())return;
		Utils.debug("Event is started");
		if(getCurrent().getEventType() != EventType.BRACKETS)return;
		Utils.debug("Event is brackets");
//		Iterator<UUID> i = getCurrent().getEnrolledPlayers().iterator();
		
		if(getCurrent().getEnrolledPlayers().size() <2 ){
			Player pl =Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0));
			 //pl =Bukkit.getPlayer(currentHostedEvent.fighting.get(0));
			Utils.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
			//pl.performCommand("spawn");
			stop();
			
			return;
			
		}
		Utils.debug("not over yet");
		if(fighting.size() < 2){
			if(!fighting.isEmpty()){
				UUID id = fighting.get(0);
				Player winner = Bukkit.getPlayer(id);
				
				fighting.remove(id);
				winner.teleport(DataManager.getLoc("brack-start"));
				Utils.broadcast(winner.getName() + " has advanced to the next round." );
				Utils.clearInv(winner);
				User.getUser(winner.getName()).setArea(Area.SPAWN);
				//winner.getInventory().setArmorContents(null);
				//winner.getInventory().setContents(null);
			}
			
			
			//Iterator<UUID> i = currentHostedEvent.enrolledPlayers.iterator();
			UUID eid ;
			eid =new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0);
			final Player p1 = Bukkit.getPlayer(eid);
			User.getUser(p1.getName()).setArea(null);
			fighting.add(eid);
		
			p1.teleport(DataManager.getLoc("brack-point1"));
			
		
			
			eid =new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(1);
			final Player p2 = Bukkit.getPlayer(eid);
			User.getUser(p2.getName()).setArea(null);
			fighting.add(eid);
			p2.teleport(DataManager.getLoc("brack-point2"));
			
			new BukkitRunnable(){
				public void run(){
					giveInventory(p2);
					giveInventory(p1);
					
				}
			}.runTaskLater(Utils.PLUGIN, 5L);
			
			Utils.broadcast(p1.getName() + " vs " + p2.getName());
		}
		
	}
	protected void start(){
		for(UUID e: enrolledPlayers){
			final Player pl = Bukkit.getPlayer(e);
			if( pl == null){
				enrolledPlayers.remove(e);
			}else{
				User.getUser(pl.getName()).setArea(Area.SPAWN);
				/*
				new BukkitRunnable(){
					public void run(){
						giveInventory(pl);
						
					}
				}.runTaskLater(Utils.PLUGIN, 5L);
				*/
				Bukkit.getPlayer(e).teleport(DataManager.getLoc("brack-start"));
	
			}
		}
		update();
		/*Iterator<UUID> i = enrolledPlayers.iterator();
		UUID eid =i.next();
		fighting.add(eid);
		Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point1"));
		
		eid =i.next();
		fighting.add(eid);
		Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point2"));
		break;*/
	
	}
	public void removePlayer(Player pl){
		fighting.remove(pl.getUniqueId());
		getCurrent().getEnrolledPlayers().remove(pl.getUniqueId());
		update();
	}
	protected void giveInventory(Player pl){
		
		User user  = User.getUser(pl.getName());
		user.changeKit(Kit.PVP);
		
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
		Utils.giveSoup(pl,getCurrent().doesAllowRefills(), getCurrent().doesAllowRecafts());
		if(getCurrent().getFightType() != HostedEvent.FightType.STONE){
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
	@Override
	public void stop() {
		HostedEvent.parentStop();
		
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent ev){
		UUID id = ev.getEntity().getUniqueId();
		
		if(getCurrent() == null)return;
		if(!getCurrent().isStarted())return;
		
		if(getCurrent().getEventType() == EventType.BRACKETS){
		removePlayer(Bukkit.getPlayer(id));
			
		}
	
	}
	
	@EventHandler
	public void onEventRespawn(final PlayerRespawnEvent ev){
		
		final Player pl = ev.getPlayer();
	
		User user= User.getUser(pl.getName());

		if(getCurrent() == null)return;

		if(!getCurrent().isStarted())return;

		if( !getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())) return;
		if(getCurrent().getEventType() != EventType.BRACKETS)return;
		removePlayer(pl);
		ev.setRespawnLocation(DataManager.getWarp("spawn").getLocation());
		user.setGameMode(User.GameMode.PLAYER);
		Utils.giveStartItems(pl);

		
	}
}
