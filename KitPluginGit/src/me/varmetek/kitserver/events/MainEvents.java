package me.varmetek.kitserver.events;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import me.varmetek.kitserver.api.Area;
import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.DuelManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.HostedEvent;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.mewin.WGRegionEvents.MovementWay;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class MainEvents implements Listener{

	

	@EventHandler
	public void onFall(EntityDamageEvent ev){
		
	if(ev.getEntity() instanceof Player){
		
		if (!ev.getCause().equals(EntityDamageEvent.DamageCause.FALL))return;
		Player pl = (Player)ev.getEntity();
		Block b = Utils.entityGetBlockBelow(pl);
		if(b == null)return;
		
		
			if(b.getType() == Material.SPONGE){
			ev.setCancelled(true);
				pl.setFallDistance(0);
			}
		
		}
	}
	//DataManager m = new DataManager( Utils.getProvidingPlugin(Utils.class));
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void checkTouch(PlayerMoveEvent ev){
			
			final Player pl = ev.getPlayer();
			
			User user = User.getUser(pl.getName());
			
			if (user.getGameMode() != User.GameMode.PLAYER )return;
		
		pl.setDisplayName(user.getRankTitle());
	//Utils.disguisePlayer(pl, user.getRankTitle());
			if(pl.isOnGround()){
				pl.setFallDistance(0);
			}
			
			Block b = Utils.entityGetBlockBelow(pl);
			if(b != null){
			 boolean canLauch = b.getType() == Material.SPONGE ||b.getType() == Material.CLAY ;
			Vector vel = null;
			if(!canLauch)return;
			
				if(b.getType() == Material.SPONGE){
					 vel = pl.getVelocity();
				
				}
				
				if(b.getType() == Material.CLAY){
					vel = pl.getLocation().getDirection().multiply(2);
					
				}
				if(vel == null)return;
				vel.setY(vel.getY()+1);
				pl.setVelocity(vel);
				pl.setFallDistance(-10000);
				new BukkitRunnable(){
					public void run(){	
						pl.setFallDistance(-10000);
					}
				}.runTaskLater(Utils.PLUGIN,10);
			}
		}

	@SuppressWarnings("deprecation")
	
	public void forceJoin(AsyncPlayerPreLoginEvent ev){
		ev.allow();
		
		 Bukkit.getOfflinePlayer(ev.getUniqueId()).setBanned(false);
	}
	
	@EventHandler
	public void ping(ServerListPingEvent ev){
	/*	Random ran = new Random();
		String[] array = new String[] {
				"Nuice","WonderFul","Fruity","Magical","Souper",
				"Join",":o","SuddenlyBlocky is awesome","Love","Blue","SOUL",
				"Wrekt","PvP","Chocolate","&1C&2o&3l&4o&5r&6f&7u&8l",
				"My SandWhich","XRedWool is Bae","XDMAN500 doe","Glory",
				"Beta?","New Kits","Fresh out the pan","1337"
				
				
				
		};*/
		//"&5&lAria&3&lKits&8>> &3+" 
		ev.setMotd(Utils.colorCode(DataManager.getMotd()));
		ev.setMaxPlayers(0);
		
	
		
	}
	
	@EventHandler
	public void onEventRespawn(final PlayerRespawnEvent ev){
		
		
		/*if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId())){
			ev.setRespawnLocation(pl.getEyeLocation());
			user.setGameMode(User.GameMode.SPECTATOR);
			//Main.clearInv(pl);
			pl.setVelocity(new Vector(0,.2,0));
	//	pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,10,1));
			//Main.updateInv(pl);
			return;
		}*/
	
		final Player pl = ev.getPlayer();
		pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,1));
		pl.setVelocity(new Vector(0,0,0));
		User user= User.getUser(pl.getName());
		if(HostedEvent.getCurrent() != null){
		
			if(HostedEvent.getCurrent().isStarted()){
		
				if(HostedEvent.getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())) return;
			}
		}
		
	Utils.debug("Default Respawn");
		ev.setRespawnLocation(DataManager.getWarp("spawn").getLocation());
		user.setGameMode(User.GameMode.PLAYER);
		Utils.giveStartItems(pl);
	
		/*if(HostedEvent.getCurrent() == null)return;
	
		if(!HostedEvent.getCurrent().isStarted())return;
		
		if(!HostedEvent.getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())) return;
		
		if(HostedEvent.getCurrent().getEventType() == EventType.KOTH){
			//user.setGameMode(User.GameMode.PLAYER);
			
			ev.setRespawnLocation(DataManager.getLoc("koth-start"));
		
			new BukkitRunnable(){
				public void run(){
					pl.teleport(DataManager.getLoc("koth-start"));
					HostedEvent.getCurrent().giveInventory(pl);
				}
			}.runTask(Main.getPluginInstance());
			
			/*new BukkitRunnable(){
				public void run(){
					
				}
			}.runTask(Main.getPluginInstance());
		}*/
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void join(PlayerJoinEvent ev){
		final Player pl = ev.getPlayer();
		 ev.setJoinMessage("");
		User user =  User.getUser(pl.getName());
		boolean firstJoin =  ! DataManager.loadUser(user);
		//Bukkit.getBanList(BanList.Type.NAME).
		//pl.setBanned(false);
		 
		if( user.getBanExpire() == null){
			
			 pl.kickPlayer("Banned for "+ user.getBanMessage()+Main.banAppealMsg);
			 return;
		}else{
			if(user.getBanExpire().after(Calendar.getInstance())){
				
				
				 Calendar cd = Calendar.getInstance();
				Calendar bd = user.getBanExpire();
					
				String message = Utils.getCalendarDiff(cd, bd);
				ev.getPlayer().kickPlayer("Temporarily Banned for " +message + Main.banAppealMsg);
				return;
			}
		}
		
		//Main.playerBanList.clear();
		//DataManager.removeBan(pl.getUniqueId().toString(), false);
		 user.refreshVanish();
		
		 if(user.hasPermission(Rank.Mod)){
			user.setNotifyMode(User.NotifyMode.STAFF);
			 for(User p :User.getAllPermedUsers(user.getRank())){
				 ev.setJoinMessage("");
				 Messenger.send("&4&lStaff&a&l>> "+user.getRankTitle(),Bukkit.getPlayer(p.getPlayerUUID()), Messenger.INFO);
				 
			 }
		 }else{
			 if(firstJoin)
				 ev.setJoinMessage( Utils.colorCode("&b&6&l&o" + pl.getName() + " &3has joined for the first time!"));
			 //else
				// ev.setJoinMessage(Utils.colorCode("&a&l>> "+user.getRankTitle()));
			 
		 }
			DataManager.addDataBaseIP(pl.getUniqueId(), pl.getAddress().getAddress().getHostAddress()); 
			DataManager.addDataBaseName(pl.getUniqueId(), pl.getName()); 
		 
		 user.setGameMode(User.GameMode.PLAYER);
		 user.setArea(Area.SPAWN);
		/// pl.performCommand("spawn");
		// pl.performCommand("clearkit");
	
			
		
		 
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent ev){
		Player pl = ev.getPlayer();
		ev.setQuitMessage("");
		User user =User.getUser(pl.getName());
		
		 DataManager.savePlayerUser(user );
		 
		 DuelManager.removePlayerFromQueue(pl.getUniqueId());
		if(user.getCombatLog() > 0){
			 ev.setQuitMessage(Utils.colorCode("&4"+pl.getName() + "&c has Combat Logged.")); 
			 pl.setHealth(0d);
			
		}
		//pl.setPlayerListName("");
		 user.remove();
		/*	if( user.getBanExpire() == null){
				
				
				 return;
			}else{
				if(user.getBanExpire().after(Calendar.getInstance())){
				
					return;
				}
			}*/
		// ev.setQuitMessage(Utils.colorCode("&c<< "+user.getRank().getPrefix()+pl.getName())); 
			/*if( user.getBanExpire() == null){//If permabanned
			user.remove();
			 return;
		}else{
			
			if(user.getBanExpire().after(Calendar.getInstance())){//if tempbanned
				user.remove();
				return;
			}
		}*/
		 
		
	}
	
	@EventHandler
	public void foodCancel(FoodLevelChangeEvent e) { 
	e.setCancelled(true);
	  e.setFoodLevel(20); 
	  }
	@EventHandler
	public void drop(PlayerDropItemEvent ev){
	
		Player pl = ev.getPlayer();
		if(User.getUser(pl.getName()).getGameMode() == User.GameMode.SPECTATOR){ev.setCancelled( true);return;}
		ItemStack item = ev.getItemDrop().getItemStack();
	
		Material mat = item.getType();
		if(pl.getGameMode() == GameMode.CREATIVE){ 
			ev.getItemDrop().setMetadata("creative", new FixedMetadataValue(Utils.PLUGIN,"e"));
			return;
			}
		
		
		if( !(mat == Material.MUSHROOM_SOUP || mat == Material.BOWL)){
		
			ev.setCancelled(true);
		}
		if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId())){
			ev.getItemDrop().remove();
		}else{
			if( mat == Material.BOWL){
				ev.getItemDrop().remove();
			}
		}
		
		
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void drinkSoup(final PlayerInteractEvent ev){
		
		if(ev.getMaterial() != Material.MUSHROOM_SOUP)return;
		if(!Utils.isRightClicked(ev.getAction()))return;
		final Player player = ev.getPlayer();
		if(!(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE))return;
		int hp = (int)((Damageable)player).getHealth();
		int maxHP = (int)((Damageable)player).getMaxHealth();
				
		if(hp != maxHP){
			((Damageable) player).setHealth(Math.min((double)hp+7,maxHP));
			player.getInventory().getItemInHand().setType(Material.BOWL);
			new BukkitRunnable(){
				public void run(){
					player.updateInventory();
				}
			}.runTask(Utils.PLUGIN);
		}	

	}
	@EventHandler
	public void rgLeave(RegionLeaveEvent ev){
	
		State sf;
		Player pl;
		ProtectedRegion rg;
		
		pl = ev.getPlayer();
		rg= ev.getRegion();
		if(rg == null){
			User.getUser(ev.getPlayer().getName()).setArea(null);
			return;
			}
		
		User user;
		user = User.getUser(pl.getName());
		if(user.getGameMode() != User.GameMode.PLAYER)return;
		if(rg.getFlag(DefaultFlag.ENABLE_SHOP) == null)return;
		
		sf = rg.getFlag(DefaultFlag.ENABLE_SHOP);
		
		if(sf == State.ALLOW &&user.isGodModed()){
			User.getUser(ev.getPlayer().getName()).setArea(null);
			Messenger.send("&cYou are no longer protected.", ev.getPlayer());
			
		}
		
		
	}
	@EventHandler
	public void rgEnter(RegionEnterEvent ev){
		Player pl;
		State sf;
		User user;
		ProtectedRegion rg;
		
		pl = ev.getPlayer();
		rg 	= ev.getRegion();
		if(rg == null){
			User.getUser(ev.getPlayer().getName()).setArea(null);
			return;
			}
		
		user = User.getUser(pl.getName());
		if(user.getGameMode() != User.GameMode.PLAYER)return;
		if(rg.getFlag(DefaultFlag.ENABLE_SHOP) == null)return;
	
		sf = rg.getFlag(DefaultFlag.ENABLE_SHOP);
		if(sf == State.ALLOW && (ev.getMovementWay() == MovementWay.TELEPORT || ev.getMovementWay() == MovementWay.SPAWN) ){
			user.setArea(Area.SPAWN);
			Messenger.send("&aYou are now protected.", ev.getPlayer());
			
		}else{
			if(ev.getMovementWay() == MovementWay.MOVE ){
			//pl.teleport(pl.getLocation().subtract(pl.getLocation().getDirection()));
				//Messenger.send("You may not renter spawn", pl, Messenger.WARN);
			}
		}
		
		
	}
	
	//@EventHandler
	public void kick(PlayerKickEvent ev){
		Player pl = ev.getPlayer();
	
		User user =User.getUser(pl.getName());
	 DataManager.savePlayerUser(user );
		 
		 DuelManager.removePlayerFromQueue(pl.getUniqueId());
		//Bukkit.getServer().getPluginManager().callEvent(new PlayerQuitEvent(ev.getPlayer(), ev.getLeaveMessage()));

		
	}
	
/*	@EventHandler
	public void onMove(PlayerMoveEvent ev){
		final Player  dead = ev.getPlayer();
		User deadUser = User.getUser(dead.getName());
		deadUser.setLastLocation(dead.getLocation());
	}*/
	
	@EventHandler
	public void itemBreak(final PlayerItemBreakEvent ev){
		
		final ItemStack item = new ItemStack( ev.getBrokenItem().getType());
		item.setItemMeta(ev.getBrokenItem().getItemMeta());
		///
		item.setAmount(1);
		item.setDurability((short) 0);
		///final PlayerInventory inv = ev.getPlayer().getInventory();
		Utils.debug("Repairing");
		final ItemStack[] contents = ev.getPlayer().getInventory().getArmorContents();
		for(ItemStack i: contents){
			if(i!=null){
				i.setDurability((short) 0);
			}
		}
		//if( new ArrayList<ItemStack>(Arrays.asList(ev.getPlayer().getInventory().getArmorContents())).contains(ev.getBrokenItem()))
		 new BukkitRunnable(){
				public void run(){	
					
	
					if(Utils.ARMOR.contains(ev.getBrokenItem().getType())){
						Utils.debug("IS armor");
						for(int i = 0; i <contents .length; i++){
							Utils.debug("Checking Slot: "+i);
							ItemStack im = contents [i];
							if(im.equals(ev.getBrokenItem())){
								contents [i] = item;
								Utils.debug("giving armor");
								ev.getPlayer().getInventory().setArmorContents(contents);
								break;
							}
						}
					}else{
						Utils.debug("IS hand");
						ev.getPlayer().getInventory().setItem(ev.getPlayer().getInventory().getHeldItemSlot(),item);
					}
					
					ev.getPlayer().updateInventory();
		/*if(ev.getPlayer().getInventory().getBoots().equals(ev.getBrokenItem())){
			ev.getPlayer().getInventory().setBoots(item);
			}else{
				if(ev.getPlayer().getInventory().getChestplate().equals(ev.getBrokenItem())){
					Utils.debug("Not boots");
					ev.getPlayer().getInventory().setChestplate(item);
				}else{
					if(ev.getPlayer().getInventory().getLeggings().equals(ev.getBrokenItem())){
						Utils.debug("not chest");
						ev.getPlayer().getInventory().setLeggings(item);
					}else{
						if(ev.getPlayer().getInventory().getHelmet().equals(ev.getBrokenItem())){
							Utils.debug("Not legs");
							ev.getPlayer().getInventory().setHelmet(item);
						}else{
							Utils.debug("Not hat");
							ev.getPlayer().setItemInHand(item);
						}
					}
				}
			}*/
		

		//Bukkit.broadcastMessage(item.toString());
		//Bukkit.broadcastMessage(item.getDurability()+"");
	
				///ev.getPlayer().setItemInHand(item);
				
			
				 
			}
		 }.runTask(Utils.PLUGIN);

		
	
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void freeSoup(PlayerInteractEvent ev){
	//	ev.setCancelled(false);
		if(ev.hasBlock()){
		Block block = ev.getClickedBlock();
		
		Block below = block.getWorld().getBlockAt(block.getLocation( ).add(0, -1, 0));
		Player player = ev.getPlayer();
	
		if(Utils.isRightClicked(ev.getAction())){
			if(block.getType() == Material.ENDER_PORTAL_FRAME){
			
			if(below.getType() == Material.BEDROCK){
				player.closeInventory();
				ev.setCancelled(true);
				/*Inventory inv  = Bukkit.createInventory(null, 54,"Free Soup");
				for(int i = 0; i < inv.getSize();i++){
					inv.setItem(i, Main.GLOBAL_SOUP);
				
				}*/
				player.openInventory(Utils.getFreeSoup());
			}
		}
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST){
				//Bukkit.broadcastMessage(":D");
				//Bukkit.broadcastMessage("ISIGN");
				Sign sign = (Sign) block.getState();
			//	Bukkit.broadcastMessage(":D");
				String line[] = sign.getLines();
				
				//Bukkit.broadcastMessage("Line1:"+line[0]);
				if(line[0].isEmpty()){return;}
				
				if(line[0].toLowerCase().contains("[soup]")){
					ev.setCancelled(true);
					//Bukkit.ev.setCancelled(true);broadcastMessage("SOUP?");
				
					player.openInventory(Utils.getFreeSoup());
				}
				if(line[0].toLowerCase().contains("[free]")){
					
					if(line[1].isEmpty()){
						line[1]= Utils.colorCode("&4  <ITEM>");
						return;
					}
					ev.setCancelled(true);
					Material m;
					try{
					 m = Material.valueOf(line[1].toUpperCase());
					}catch(IllegalArgumentException e){
						Messenger.send("Invalid item", player,Messenger.WARN);
						return;
					}
					/*if(m == null){
						line[1]= Utils.colorCode("&4  <INVALID");
						return;
					}*/
					
					Inventory inv  = Bukkit.createInventory(null, 54,"Free");
					for(int i = 0; i < inv.getSize();i++){
						inv.setItem(i, new ItemStack(m,m.getMaxStackSize()));
						
					
					}
					player.openInventory(inv);
				
					
				}
			}
		}
		}
	}
	
	 @EventHandler
	 public void onSignChange(SignChangeEvent sign)
	  {
		 String line[] = sign.getLines();
	
		if(!line[0].isEmpty()){
			
			if(line[0].toLowerCase().contains("[soup]")){
				line[0] = "&2[Soup]"; 
			}
			
			if(line[0].toLowerCase().contains("[free]")){
				line[0] = "&3[Free]"; 
				if(line[1].isEmpty()){
					line[1]= Utils.colorCode("&4  <ITEM>");
					return;
				}
				
				try{
					Material.valueOf(line[1].toUpperCase());
				}catch(IllegalArgumentException e){
					line[1]= Utils.colorCode("&4  <INVALID");
					return;
				}
				
			}
		}
		 for(int i = 0; i <4; i++){
			 if (sign.getLine(i).contains("&"));
			    sign.setLine(i, sign.getLine(i).replace("&", "§"));
		 }

	  }
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent ev){
		
		Player player = ev.getPlayer();
		Rank rank = User.getUser(player.getName()).getRank();
		User playerUser = User.getUser(player.getName());
		if(Utils.chatLocked && !playerUser.hasPermission(Rank.Mod) ){
			ev.setCancelled(true);
			Messenger.send("Chat is locked!", player, Messenger.WARN);
			return;
		}
		
		if(playerUser.getMuteExpire() == null ){
			ev.setCancelled(true);
			Messenger.send("Muted forever.", player, Messenger.WARN);
			return;
		}else{
			if( playerUser.getMuteExpire().after(Calendar.getInstance())){
				
				ev.setCancelled(true);
				Messenger.send("Muted for " + Utils.getCalendarDiff(Calendar.getInstance(), playerUser.getMuteExpire()) + ".", player, Messenger.WARN);
				return;
				}
		}
		if(playerUser.getChatCooldown() > System.currentTimeMillis()){
			ev.setCancelled(true);
			Messenger.send("Slow down chat; " + Utils.chatDelay + " milisecond delay.", player, Messenger.WARN);
			//Messenger.send(playerUser.getChatCooldown()+"/"+System.currentTimeMillis(), player, Messenger.WARN);
			playerUser.setChatCooldown(System.currentTimeMillis()+ Utils.chatDelay);
			return;
		}
		{
		String msg =  ev.getMessage().replaceAll("&", "§");
		ev.setMessage(msg);
		String format = "&a["+User.getLevelTitle(playerUser.getLevel()) +"&a]"+ playerUser.getRankTitle() +playerUser.getProdLvL().getIcon()+ "&8&l> &r" + rank.getChatColor() + msg;
		ev.setFormat(format.replaceAll("&", "§"));
		playerUser.setChatCooldown(System.currentTimeMillis() + Utils.chatDelay);
		
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void kill(final PlayerDeathEvent ev){
		ev.setDeathMessage("");
		int deadsSoup = 0;
		int killersSoup = 0;
		double killerHealth ;
		final Player  dead = ev.getEntity();
	
		dead.setVelocity(new Vector(0,0,0));
		dead.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100,2));
		dead.setHealthScale(20);
		boolean skip = DuelManager.getMatchedPlayers().contains(dead.getUniqueId());
		if(!skip){
			World area = dead.getWorld();
			Firework fw = area.spawn(dead.getLocation(), Firework.class);
			FireworkMeta fM = fw.getFireworkMeta();
			FireworkEffect.Builder fb = FireworkEffect.builder();
			fb.withColor(Color.RED);
			fb.with(Type.BURST);
			fM.addEffect(fb.build());
			fw.setFireworkMeta(fM);
			fw.detonate();
		}
		//PlayerRespawnEvent event = new PlayerRespawnEvent(dead, DataManager.getLoc("spawn"), false);
		//Utils.PLUGIN.getServer().getPluginManager().callEvent(event);
		if(skip)return;

		
		final User deadUser = User.getUser(dead.getName());
		deadUser.setCombatLog(0);
		deadUser.setCanChangeKit(true);
		deadUser.setCurrenPerk(null);
	
		
	List<ItemStack> drops = 	new ArrayList<ItemStack>(ev.getDrops());
	
	ev.getDrops().clear();

	
		Player killer;
		
		if(dead.getKiller() instanceof Projectile){
			killer = (Player) ((Projectile)dead.getKiller()).getShooter();
			
		}else{
			killer =  dead.getKiller();
		}
	
		
		if(killer == null || killer == dead){
			return;
		}
		
		for(ItemStack i: drops){
			if(i!= null){
			if(i.getType() == Material.MUSHROOM_SOUP) deadsSoup++;
			}
		}
		
		for(ItemStack i: killer.getInventory()){
			if(i!= null){
			if(i.getType() == Material.MUSHROOM_SOUP) killersSoup++;
			}
		}
			killerHealth = ((Damageable)killer).getHealth();
		User killerUser = User.getUser(killer.getName());
		
		{//Message the deadplayer who killed him/her
			
			Messenger.send("&5================", dead, Messenger.INFO);
			Messenger.send("&2Killed by: " + killerUser.getRankTitle(), dead, Messenger.INFO);
			Messenger.send("&2KillStreak: &a" + killerUser.getKillstreak(), dead, Messenger.INFO);
			Messenger.send("&2Their Soup: &a" + killersSoup, dead, Messenger.INFO);
			Messenger.send("&2Health: &a" + String.format("%.3f",(double)killerHealth), dead, Messenger.INFO);
			Messenger.send("&5================", dead, Messenger.INFO);
			
		}
		int bitGains =  5+ (int)((Math.pow(1.03, killerUser.getKillstreak())) + (deadUser.getKillstreak()/5));

		killerUser.addKillstreak(1);
		long xpGains=  3L*(long)(1.04*killerUser.getKillstreak());
		killerUser.addEXP(xpGains);
		
		{//Message the killer who killed the dead player
			
			Messenger.send("&5================", killer, Messenger.INFO);
			Messenger.send("&2Killed&8: " + deadUser.getRankTitle(), killer, Messenger.INFO);
			Messenger.send("&2New killStreak&8: &a " + killerUser.getKillstreak(),killer, Messenger.INFO);
			Messenger.send("&3-----Gains------", killer, Messenger.INFO);
			Messenger.send("&2Gained Bits&8: &a"+ bitGains, killer, Messenger.INFO);
			Messenger.send("&2Gained Soup&8: &a"+ deadsSoup, killer, Messenger.INFO);
			Messenger.send("&2Gained Xp&8: &a" + xpGains,killer,Messenger.INFO);
			Messenger.send("&5================", killer, Messenger.INFO);
			
		}
		
			
		
		{// Handle messages, penalties, and rewards
			
			
		
			if(deadUser.getKillstreak() >=5){
				ev.setDeathMessage(Utils.colorCode("&b" + killer.getName() + "&7 has ended " + dead.getName()
						+ "'s killstreak of " + deadUser.getKillstreak()));
			}
			deadUser.setDeaths(deadUser.getDeaths()+1);
			deadUser.setKillstreak(0);
			
			killerUser.setKills(killerUser.getKills()+1);
			killerUser.addMoney(bitGains);
	
		
			if(killerUser.getKillstreak() % 5 == 0 && killerUser.getKillstreak() != 0  ){
				int ksRewards =(int) ((Math.pow(1.075, killerUser.getKillstreak())));
				Messenger.sendAll("&b"+killer.getName()+"&7 has gotten a killstreak of &6&l"+ killerUser.getKillstreak() + " &7and recieved &6&l"+ ksRewards+" &5&oBits&7.");
				killerUser.addMoney(ksRewards);
			}

		}
		
		
		{//Give the killer the dead player's soup
			while(deadsSoup >0 && Arrays.asList(killer.getInventory().getContents()).contains(null)){
				killer.getInventory().addItem(Main.GLOBAL_SOUP);
				deadsSoup --;
			}
			
		}
		
		
	}
	
	@EventHandler
	public void killArrows(ProjectileHitEvent ev ){
		if(ev.getEntity() instanceof Arrow)
			ev.getEntity().remove();
		
	}
	
	@EventHandler(priority =  EventPriority.HIGH)
	public void onHit(EntityDamageByEntityEvent ev){
		if(!(ev.getEntity() instanceof Player)) return;
		if(Utils.getAttacker(ev.getDamager()) == null) return;
		//Player hurt = (Player) ev.getEntity();
		Player damager = Utils.getAttacker(ev.getDamager());
		Player damagee =(Player) ev.getEntity();
		
		if(damager == null || damagee == null) return;
		
		User drUser = User.getUser(damager.getName());
		User deUser = User.getUser(damagee.getName());
		
		if( drUser.isGodModed()|| deUser.isGodModed()){ev.setCancelled(true); return;}
	
		if(drUser.getCombatLog() <1){
			Messenger.send("&7You have combat tagged &c" +damagee.getName() ,damager,Messenger.WARN);
		}
		
		if(deUser.getCombatLog() <1){
			Messenger.send("&7You have been combat tagged by &c" +damager.getName() ,damagee,Messenger.WARN);
		}
		drUser.setCombatLog(8);
		deUser.setCombatLog(8);
	
	}
	/*@EventHandler
	public void onReload(PluginReloadEvent ev){
		Utils.broadcast("RELOADING");
		Utils.shutDownSystem();
		return;
	}*/
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent ev){
		String msg = ev.getMessage().toLowerCase();
		String [] parts = msg.split(" ");
		Player pl =ev.getPlayer();
		User user = User.getUser(pl.getName());
	
		
	
	if(parts[0].equalsIgnoreCase("/reload") && pl.isOp()){
		
			Utils.broadcast("RELOADING");
			//Main.shutDownSystem();
			return;
		}
		if(parts[0].contains("bukkit:")){
			ev.setCancelled(true);
			return;
		}
		//Utils.debug(parts[0]);
		if(parts[0].equalsIgnoreCase("/me")){
			ev.setCancelled(true);
			return;
		}
	
		boolean cantTeleport =  !Main.getPlayersNear(pl, 10,10,10).isEmpty() && !user.isGodModed();
		boolean rejectCommand;
		if(HostedEvent.getCurrent() == null){
			
			rejectCommand= ( user.getCombatLog() > 0
					|| DuelManager.getMatchedPlayers().contains(pl.getUniqueId())
					);
		
		}else{
			rejectCommand= ( user.getCombatLog() > 0
				|| DuelManager.getMatchedPlayers().contains(pl.getUniqueId()))
				|| HostedEvent.getCurrent().getEnrolledPlayers().contains(pl.getUniqueId()
		
						);
		}
		
		if(parts[0].equals("/spawn") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/spawn") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
	
		if(parts[0].equals("/renew") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/renew") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		
		if(parts[0].equals("/warp") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		
		if(parts[0].equals("/warp") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/mode") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/mode") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/m") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/m") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/repair") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/repair") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/soup") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/soup") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
	}
	
	
	
	@EventHandler 
	public void onWeatherChange(WeatherChangeEvent ev){
		/*ev.getWorld().setThunderDuration(8);
		ev.getWorld().set
		ev.getWorld().setWeatherDuration(10);*/
		ev.setCancelled(true);
	}
	
	@EventHandler 
	public void onThunderChange(ThunderChangeEvent ev){
		ev.setCancelled(true);
	//	ev.getWorld().setThunderDuration(10);
	}
	
	@EventHandler
	public void onDamage(final EntityDamageEvent ev){
		if(ev.getEntityType() != EntityType.PLAYER)return;
			final Player pl = (Player)ev.getEntity();
			User user = User.getUser(pl.getName());
			if(ev.isCancelled()){return;}
			if(user.isGodModed()){
				ev.setDamage(0.0);
				
			}
			if(ev.getDamage() == 0){
				ev.setCancelled(true);
				return;
			}

	}
	
	
	@EventHandler
	public void freeze(PlayerMoveEvent ev){
		final Player pl = (Player)ev.getPlayer();
		User user = User.getUser(pl.getName());
		if(user.isFrozen()){
			ev.setTo(ev.getFrom());
		}
	}
	
	@EventHandler
	public void onInteract(final PlayerInteractEvent ev){
		final Player pl = ev.getPlayer();
		User user = User.getUser(pl.getName());
		
		if(ev.getItem() == null)return;
		
		
		if(ev.getItem().equals(Main.SPEC_CANCEL)){
			
			if(user.getGameMode() != User.GameMode.SPECTATOR)return;
			if(!ev.hasItem()){
			
			}
			ev.setCancelled(true);
		 
			
			
			if(Utils.isRightClicked(ev.getAction())){
					//user.setGameMode(User.GameMode.PLAYER);
				pl.performCommand("mode player");
				new BukkitRunnable(){
					public void run(){
						pl.updateInventory();
					}
				}.runTask(Utils.PLUGIN);
			}
			
			
		}
		
		
		if(ev.getItem().equals(Main.KIT_SELECTION)){
			if(user.getGameMode() != User.GameMode.PLAYER)return;
			if(!ev.hasItem()){
				ev.setCancelled(true);
				return;
			}
			
			
			ev.getPlayer().performCommand("kit");
			
		}
		
		if(ev.getItem().equals(Main.SHOP_SHORTCUT)){
			if(user.getGameMode() != User.GameMode.PLAYER)return;
			if(!ev.hasItem()){
				ev.setCancelled(true);
				return;
			}
			
			
			ev.getPlayer().performCommand("shop kits");
			
		}
		
		if(ev.getItem().equals(Main.SPEC_ENTER)){
			if(user.getGameMode() != User.GameMode.PLAYER)return;
			ev.setCancelled(true);
			
			
			
			//user.setGameMode(User.GameMode.SPECTATOR);
			pl.performCommand("mode spectator");
			
		}
		if(ev.getItem().equals(Main.WARP_GUI)){
			if(user.getGameMode() != User.GameMode.PLAYER)return;
			ev.setCancelled(true);
			
			
			
			//user.setGameMode(User.GameMode.SPECTATOR);
			pl.performCommand("warp");
			
		}
		
		if(ev.getItem().equals(Main.BUYCRAFT)){
			if(user.getGameMode() != User.GameMode.PLAYER)return;
			ev.setCancelled(true);
			
			
			
			//user.setGameMode(User.GameMode.SPECTATOR);
			Messenger.send("&cNot just yet.", pl);
			
		}
		
		
		
	}
	@EventHandler
	public void itemSpawn(final ItemSpawnEvent ev){
		if(!ev.getEntity().hasMetadata("creative")){
			if(ev.getEntity().getItemStack().getType() == Material.MUSHROOM_SOUP){
				ev.getEntity().setPickupDelay(30);
			}else{
				ev.getEntity().setPickupDelay(1000);
			}
			Bukkit.getScheduler().runTaskLater(Utils.PLUGIN,new Runnable(){
				public void run(){
				ev.getEntity().remove();
				}
			 }, 60L);
		}
	}
	
	@EventHandler
	 public void cancelPickUp(PlayerPickupItemEvent ev){
		Item e = ev.getItem();
		if(User.getUser(ev.getPlayer().getName()).getGameMode() == User.GameMode.SPECTATOR){ev.setCancelled( true);return;}
		if(e.hasMetadata("creative") || e.getItemStack().getType() == Material.MUSHROOM_SOUP) return ;
		
		ev.setCancelled( true);
	}
	
	@EventHandler
	public void clearInvOnClose(InventoryCloseEvent ev){
		User user = User.getUser(ev.getPlayer().getName());
		if(user.isInvSee()){
			user.setInvSee(false);
		}
		if(ev.getInventory().equals(user.getInv())){
			user.setInv(null);
			user.setGui(null);
			
		}
		
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent ev){
		ev.setCancelled(!(User.getUser(ev.getPlayer().getName()).getGameMode() == User.GameMode.BUILDER));
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent ev){
		ev.setCancelled(!(User.getUser(ev.getPlayer().getName()).getGameMode() == User.GameMode.BUILDER));
	}
	
	

	/*public void interactTest(PlayerInteractEvent ev){
		
		if(ev.hasItem()){
			if(ev.getMaterial() == Material.BOOK_AND_QUILL){
				Utils.debug("No item");
				ev.setUseItemInHand(Result.DENY);
				ev.setCancelled(true);
			//	Player pl = ev.getPlayer();

				
			}
		}
	}*/

	@EventHandler
	public void denySpectatorInvEdit(InventoryCreativeEvent ev){
		User user = User.getUser(ev.getWhoClicked().getName());
		if(user.getGameMode() == User.GameMode.SPECTATOR){
			ev.setCancelled(true);
			ev.getWhoClicked().closeInventory();
		}
	}
	
	@EventHandler
	public void denyRegen(EntityRegainHealthEvent ev){
	
		List<EntityRegainHealthEvent.RegainReason> list = 
				Arrays.asList( new EntityRegainHealthEvent.RegainReason[] 
						{EntityRegainHealthEvent.RegainReason.CUSTOM, 
						EntityRegainHealthEvent.RegainReason.MAGIC, 
						EntityRegainHealthEvent.RegainReason.MAGIC_REGEN});
	
		if(!list.contains(ev.getRegainReason())){	
			ev.setCancelled(true);
		}
		
	}

	
	public void onRegionEnter(RegionEnterEvent ev){
		if (ev.getRegion().getId().equals("spawn")){
			if (ev.getMovementWay().equals(MovementWay.MOVE)){
				Player pl = ev.getPlayer();
				//Block loc = ev.getPlayer().getLocation().getBlock();
				User user = User.getUser(pl.getName());
				if(user.getGameMode() == User.GameMode.PLAYER){
					pl.teleport(pl.getLocation().subtract(pl.getLocation().getDirection()));
					Messenger.send("You may not renter spawn", pl, Messenger.WARN);
			
				}
			}	
		}
		
	}
	//@EventHandler
	public void onRegionLeave(RegionLeaveEvent e){
		Player player = e.getPlayer();
		User user = User.getUser(player.getName());
		if (user.getArea()== Area.SPAWN){
			if (e.getMovementWay() == MovementWay.MOVE ||e.getMovementWay() ==  MovementWay.TELEPORT ){	
				user.setArea(null);
			}
		}
	
	}
}
	/*@EventHandler
	public void onHit(EntityDamageByEntityEvent ev){
		if(!(ev.getEntity() instanceof Player)) return;
		if(!(ev.getDamager() instanceof Player)) return;
		Player damager = (Player)ev.getDamager();
		Player damagee = (Player)ev.getEntity();
		User drUser = User.getUser(damager.getName());
		User deUser = User.getUser(damagee.getName());
		
		if(drUser.getCombatLog() <1){
			Messenger.send("&7You have combat tagged &c" +damagee.getName() ,damager,Messenger.WARN);
		}
		
		if(deUser.getCombatLog() <1){
			Messenger.send("&7You have been combat tagged by &c" +damager.getName() ,damagee,Messenger.WARN);
		}
		drUser.setCombatLog(8);
		deUser.setCombatLog(8);
	
		
	}*/
	

/*if (sign.getLine(0).contains("&"));
sign.setLine(0, sign.getLine(0).replace("&", "§"));
if (sign.getLine(1).contains("&"));
sign.setLine(1, sign.getLine(1).replace("&", "§"));
if (sign.getLine(2).contains("&"));
sign.setLine(2, sign.getLine(2).replace("&", "§"));
if (sign.getLine(3).contains("&"));
sign.setLine(3, sign.getLine(3).replace("&", "§"));*/