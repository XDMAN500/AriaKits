package me.varmetek.kitserver.api.hostedeventapi.eventypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.User.GameMode;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EventTypeKoth extends HostedEvent implements Listener
{
	public final Map<UUID,Integer> points = new HashMap<UUID,Integer>();
	
	protected void update() {
		new BukkitRunnable(){
			public void run(){
				if(getCurrent() == null){this.cancel(); return;}
				if(!isStarted()){this.cancel(); return;}
			
				if(getEnrolledPlayers().size() < getMinimumPlayers() ){
					Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0)).performCommand("spawn");
					stop();
					return;
				}
			
				for(UUID u: getEnrolledPlayers()){
					Player pl = Bukkit.getPlayer(u);
					User user = User.getUser(pl.getName());
					if(!pl.isDead()){
						Block b = pl.getLocation().add(0, -0.1, 0).getBlock();
				
						if(b.getType() == Material.DIAMOND_BLOCK && user.getGameMode() == GameMode.PLAYER){
							points.put(u,points.get(u)+1);
							pl.getWorld().playEffect(pl.getEyeLocation(), Effect.HAPPY_VILLAGER,0);
							pl.getWorld().playSound(pl.getLocation(), Sound.ORB_PICKUP, 1, 1);
						}
				
						int score = points.get(u);
					
						if(score % 10 == 0 && score != 0 && b.getType() == Material.DIAMOND_BLOCK){
							Messenger.sendGroupU("&5&l&o" + getEventType().name()+"&7&l>> " + "&6"+pl.getName() + " &bhas reached a score of " + "&a"+score + "&b/" + kothMaxScore + "." ,
									new ArrayList<UUID>(getEnrolledPlayers()), Messenger.INFO);
						}
				
						if(score >= kothMaxScore){
							Utils.broadcast(pl.getName() + " has won " + getEventType().name() + "." );
							stop();
							this.cancel();
							break;
						}
					}
				}
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 20);
		
	}
	@Override
	protected void start(){
	
		for(UUID e: enrolledPlayers){
			final Player pl = Bukkit.getPlayer(e);
			if(pl== null){
				enrolledPlayers.remove(e);
			}else{
				points.put(e, 0);
				new BukkitRunnable(){
					public void run(){
						giveInventory(pl);
						User.getUser(pl.getName()).setArea(null);
					}
				}.runTaskLater(Utils.PLUGIN, 10L);
				pl.teleport(DataManager.getLoc("koth-start"));
				
			}
		}
		//checkKOTH();
		update();
		
	}
	protected void giveInventory(final Player pl){
		//HostedEvent.parentGiveInventory(pl);
		//Utils.clearInv(pl);
		//pl.getInventory().clear();
		User user  = User.getUser(pl.getName());
		user.changeKit(Kit.PVP);
		user.setCanChangeKit(false);
		Utils.debug("Giving sword");
		///Utils.broadcast("Tesing: Giving sword");
		
		switch (getCurrent().getFightType()){
		case BOW:
			pl.getInventory().setItem(0, new ItemStack(Material.BOW));	
			pl.getInventory().setItem(18, new ItemStack(Material.ARROW,64));	
			
			break;
		case NORMAL:
			pl.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));	
		
			
	
			break;
		case STONE:
			pl.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
			break;
	
		}
		Utils.giveSoup(pl, getCurrent().doesAllowRefills(), getCurrent().doesAllowRecafts());
		Utils.debug("Giving Armor");
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
	public void removePlayer(Player pl){
		points.remove(pl.getUniqueId());
		getCurrent().getEnrolledPlayers().remove(pl.getUniqueId());
	}

	@EventHandler
	public void onEventRespawn(final PlayerRespawnEvent ev){
		
		final Player pl = ev.getPlayer();
	
		final User user= User.getUser(pl.getName());

		if(getCurrent() == null)return;

		if(!getCurrent().isStarted())return;

		if( !getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())) return;
		if( !(getCurrent().getEventType() == EventType.KOTH)) return;
	
		user.setGameMode(User.GameMode.PLAYER);
		
		ev.setRespawnLocation(DataManager.getLoc("koth-start"));
		//giveInventory(pl);
	new BukkitRunnable(){
			public void run(){
				giveInventory(pl);
				user.changeKit(Kit.PVP);
		
			}
		}.runTaskLater(Main.getPluginInstance(), 10L);

		
	}
	@Override
	public void stop() {
		HostedEvent.parentStop();
		
	}
	
	

}
