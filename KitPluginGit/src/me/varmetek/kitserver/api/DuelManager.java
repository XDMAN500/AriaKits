package me.varmetek.kitserver.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelManager implements Listener {
	private static final Set<UUID> playersInMatches = new HashSet<UUID>();
	private static final List<UUID> playersInQueue = new ArrayList<UUID>();
	
	public enum FightStyle{
		NORMAL,STONE,PRO,VANILLA,SPEED;
		
	}
	
	
	public static void checkQueue(){
		if(playersInQueue.size() < 2) return;
			Player p1 = Bukkit.getPlayer(playersInQueue.get(0));
			Player p2 = Bukkit.getPlayer(playersInQueue.get(1));
			if(p1 == null){
				playersInQueue.remove(0);
				return;
			}
			if(p2 == null){
				playersInQueue.remove(1);
				return;
			}
			DuelRequest br = DuelRequest.getRandomDuelRequest();
			addMatchPlayers( p1.getUniqueId(), p2.getUniqueId(),br.getFightStyle(),br.isRefills(),br.isRecrafts() );
			playersInQueue.remove(0);
			playersInQueue.remove(1);
		}
	
	@SuppressWarnings("deprecation")
	public static void addMatchPlayers(UUID p1id, UUID p2id , final FightStyle fs , final boolean refills, final boolean recrafts){
		if(p1id.equals(p2id))return;
		if( ( playersInMatches.contains(p1id) || playersInMatches.contains(p2id) ))return;
		final Player p1 = Bukkit.getPlayer(p1id);
		final Player p2 = Bukkit.getPlayer(p2id);
		if(p1 == null || p2 == null) return;
		playersInMatches.add(p1id);
		playersInMatches.add(p2id);
		///Main.clearInv(p1);
		///Main.clearInv(p2);
		final User p1User = User.getUser(p1.getName());
		final User p2User = User.getUser(p2.getName());
		p1User.clearMatchRequest();
		p2User.clearMatchRequest();
		p1User.setGameMode(User.GameMode.PLAYER);
		p2User.setGameMode(User.GameMode.PLAYER);
		p1User.setGodMode(false);
		p2User.setGodMode(false);
		p1User.setArea(null);
		p2User.setArea(null);
		p1.setGameMode(GameMode.ADVENTURE);
		p2.setGameMode(GameMode.ADVENTURE);
		//Utils.debug("Adding:"+fs.name()+":"+refills+":"+recrafts);
		Utils.cleansePlayer(p1,p2);
		Arena arena = Arena.getRandomArena();
		p1.teleport(arena.getPosition1());
		p2.teleport(arena.getPosition2());
		p1User.changeKit(Kit.PVP);
		p2User.changeKit(Kit.PVP);
		Utils.clearInv(p1);
		Utils.clearInv(p2);
		p1User.setBattling(p2.getUniqueId());
		p2User.setBattling(p1.getUniqueId());
		p1User.setFrozen(true);
		p2User.setFrozen(true);
		for(Player p: Bukkit.getOnlinePlayers()){
			p1.hidePlayer(p);
			p2.hidePlayer(p);
			
		}
		p1.showPlayer(p2);
		p2.showPlayer(p1);
		new BukkitRunnable(){
			int time = 5;
			public void run(){
				if(time == 0){
					p1User.setFrozen(false);
					p2User.setFrozen(false);
					Messenger.sendGroup("GO", Arrays.asList(new Player[] {p1,p2}), Messenger.INFO);
					giveMaterials(fs,p1,refills,recrafts);
					giveMaterials(fs,p2,refills,recrafts);
					this.cancel();
					return;
				}else{
					Messenger.sendGroup(time+"", Arrays.asList(new Player[] {p1,p2}), Messenger.WARN);
				}
				time --;
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 20);

	}
	
	public static  void removeMatchPlayer(UUID id , boolean won){
		Player pl = Bukkit.getPlayer(id);
		
		if(pl == null)return;
		playersInMatches.remove(id);
		playersInQueue.remove(id);
		User user = User.getUser(pl.getName());
	///	if(pl == null)return;
		user.setGameMode(User.GameMode.PLAYER);
		Utils.cleansePlayer(pl);
		pl.teleport(DataManager.getWarp("spawn").getLocation());
		if(won){
			Messenger.send("You won the match.",pl,Messenger.INFO);
		}else{
			Messenger.send("You lost the match.",pl,Messenger.WARN);
		}
		
		for(User u : User.users.values()){
			u.refreshVanish();
		}
		
		user.setBattling(null);
		
	}
	
	public static void giveMaterials(FightStyle fs, Player pl, boolean refs , boolean rcrs){
		if(pl == null)return;
		Utils.cleansePlayer(pl);
		Random ran = new Random();
		
		switch(fs){
	
		case NORMAL:
			//Main.debug("giving normal");
			pl.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
			pl.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			pl.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			pl.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			pl.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			
			break;
		case STONE:
			//Main.debug("giving stone");
			pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
			
			
			break;
		case PRO:
		//	Main.debug("giving pro");
			pl.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
	
			break;
		case SPEED:
			//Main.debug("giving pro");
			pl.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
			ItemStack head = new ItemStack(Material.LEATHER_HELMET);
			ItemStack chest = new ItemStack(Material.LEATHER_HELMET);
			ItemStack legs= new ItemStack(Material.LEATHER_HELMET);
			ItemStack feet = new ItemStack(Material.LEATHER_HELMET);
			LeatherArmorMeta hM = (LeatherArmorMeta) head.getItemMeta();
			LeatherArmorMeta cM = (LeatherArmorMeta) chest.getItemMeta();
			LeatherArmorMeta lM = (LeatherArmorMeta) legs.getItemMeta();
			LeatherArmorMeta fM = (LeatherArmorMeta) feet.getItemMeta();
			hM.setColor(Color.fromBGR(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			cM.setColor(Color.fromBGR(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			lM.setColor(Color.fromBGR(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			fM.setColor(Color.fromBGR(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			head.setItemMeta(hM);
			chest.setItemMeta(cM);
			legs.setItemMeta(lM);
			feet.setItemMeta(fM);
			
			pl.getInventory().setHelmet(head);
			pl.getInventory().setChestplate(chest);
			pl.getInventory().setLeggings(legs);
			pl.getInventory().setBoots(feet);
			pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
			break;
		case VANILLA:
			pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
			pl.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			pl.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			pl.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			pl.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			break;
		}
		if(fs != FightStyle.VANILLA){
			Utils.giveSoup(pl, refs, rcrs);
		}
	}
	
	public static void addPlayerToQueue(UUID id){
		playersInQueue.add(id);
		
		checkQueue();
	}
	
	public static void removePlayerFromQueue(UUID id){
		playersInQueue.remove(id);
		
		checkQueue();
	}
	
	public static Set<UUID> getMatchedPlayers(){
		return playersInMatches;
	}
	
	public static List<UUID> getQueuedPlayers(){
		return playersInQueue;
	}
	
	@EventHandler
	public void killed(PlayerDeathEvent ev){
		final Player dead = ev.getEntity();
		User deadUser = User.getUser(dead.getName());
		
	//	Main.debug("DIED");
		if(deadUser.getBattling() == null)return;
		final Player killer = Bukkit.getPlayer(deadUser.getBattling());
		//Main.debug("?");
		if( !playersInMatches.contains(dead.getUniqueId()) && !playersInMatches.contains(killer.getUniqueId()))return;
		
	
		int deadSoup = 0;
		int killerSoup = 0;
		
		for(ItemStack i: ev.getDrops()){
			if(i!= null){
			if(i.getType() == Material.MUSHROOM_SOUP) deadSoup++;
			}
		}
		
		for(ItemStack i: killer.getInventory()){
			if(i!= null){
			if(i.getType() == Material.MUSHROOM_SOUP) killerSoup++;
			}
		}
		ev.getDrops().clear();
		String killerHP = String.format("%.2f", ((Damageable)killer).getHealth());
		String msg1 = "&3You beat %deadPlayer% &3in a duel who had %soup% &3soup left.";
		Messenger.send(msg1.replace("%soup%", "&a"+deadSoup).replace("%deadPlayer%", "&5&o" + dead.getName()),killer, Messenger.WARN);
		Messenger.send("&3You lost to &5&o" + killer.getName() + " &3in a duel who had &a" + killerSoup + " &3soup and &a" + killerHP +" heath left" +  "&3.", dead, Messenger.WARN);
	
			
		removeMatchPlayer(killer.getUniqueId(), true);
		removeMatchPlayer(dead.getUniqueId(), false);
				
		
	/*	new BukkitRunnable(){
			public void run(){
				//if(killer == null){this.cancel(); return;}
				removeMatchPlayer(killer.getUniqueId(), true);
				removeMatchPlayer(dead.getUniqueId(), false);
			}
		}.runTaskLater(Main.getPluginInstance(), 20*3);*/
			
	}
		
	public static FightStyle getRandomFightStyle(){
		FightStyle[] fz = FightStyle.values();
		return fz[new Random().nextInt(fz.length)];
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent ev){
		
		if(!playersInMatches.contains(ev.getPlayer().getUniqueId()))return;
			
			User user = User.getUser(ev.getPlayer().getName());
			removeMatchPlayer(user.getBattling(),true);
			removeMatchPlayer(user.getPlayerUUID(),false);
			
		
		
		
	}

	@EventHandler
	public void onDamageEV(EntityDamageByEntityEvent ev){
	
		
		if(ev.getEntity().getType() == EntityType.PLAYER && ev.getDamager().getType() == EntityType.PLAYER ){
			Player damagee = (Player)ev.getEntity();
			Player damager = (Player)ev.getDamager();
			UUID deUUID = damagee.getUniqueId();
			UUID drUUID = damager.getUniqueId();
			
			if( (getMatchedPlayers().contains(deUUID) && !getMatchedPlayers().contains(drUUID)) ||
					(getMatchedPlayers().contains(drUUID) && !getMatchedPlayers().contains(deUUID))	){
				ev.setCancelled(true);
				return;
				
			}
			
		
		}
	}

}
