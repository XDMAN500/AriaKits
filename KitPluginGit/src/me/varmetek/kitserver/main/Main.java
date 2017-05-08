package me.varmetek.kitserver.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.varmetek.kitserver.api.Area;
import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.DuelManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.HostedEventsListener;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeBrackets;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeClanWar;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeKoth;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeLMS;
import me.varmetek.kitserver.commands.ArenaCommand;
import me.varmetek.kitserver.commands.BalanceCommand;
import me.varmetek.kitserver.commands.BanCommand;
import me.varmetek.kitserver.commands.ChatSettingsCommand;
import me.varmetek.kitserver.commands.ClearChatCommand;
import me.varmetek.kitserver.commands.ClearKitCommand;
import me.varmetek.kitserver.commands.DuelCommand;
import me.varmetek.kitserver.commands.EventCommand;
import me.varmetek.kitserver.commands.GambleCommand;
import me.varmetek.kitserver.commands.InvseeCommand;
import me.varmetek.kitserver.commands.KickCommand;
import me.varmetek.kitserver.commands.KitCommand;
import me.varmetek.kitserver.commands.KitSettingsCommand;
import me.varmetek.kitserver.commands.LocCommand;
import me.varmetek.kitserver.commands.LockChatCommand;
import me.varmetek.kitserver.commands.LookUpCommand;
import me.varmetek.kitserver.commands.ModeCommand;
import me.varmetek.kitserver.commands.MotdCommand;
import me.varmetek.kitserver.commands.MsgCommand;
import me.varmetek.kitserver.commands.MuteCommand;
import me.varmetek.kitserver.commands.NotifyCommand;
import me.varmetek.kitserver.commands.PayCommand;
import me.varmetek.kitserver.commands.PerksCommand;
import me.varmetek.kitserver.commands.QueueCommand;
import me.varmetek.kitserver.commands.RankCommand;
import me.varmetek.kitserver.commands.ReloadConfigCommand;
import me.varmetek.kitserver.commands.RemoveWarpCommand;
import me.varmetek.kitserver.commands.RenewCommand;
import me.varmetek.kitserver.commands.ReplyCommand;
import me.varmetek.kitserver.commands.SetWarpCommand;
import me.varmetek.kitserver.commands.ShopCommand;
import me.varmetek.kitserver.commands.SpawnCommand;
import me.varmetek.kitserver.commands.StatsCommand;
import me.varmetek.kitserver.commands.TeleportCommand;
import me.varmetek.kitserver.commands.TempBanCommand;
import me.varmetek.kitserver.commands.UnbanCommand;
import me.varmetek.kitserver.commands.UserCommand;
import me.varmetek.kitserver.commands.VanishCommand;
import me.varmetek.kitserver.commands.WarpCommand;
import me.varmetek.kitserver.commands.WarpSettingsCommand;
import me.varmetek.kitserver.events.MainEvents;
import me.varmetek.kitserver.events.PerkEvents;
import me.varmetek.kitserver.events.customevents.CustomEventCaller;
import me.varmetek.kitserver.events.kitevents.KitAnchorEvents;
import me.varmetek.kitserver.events.kitevents.KitBerserkEvents;
import me.varmetek.kitserver.events.kitevents.KitCactusEvents;
import me.varmetek.kitserver.events.kitevents.KitEnchanterEvents;
import me.varmetek.kitserver.events.kitevents.KitEnderEvents;
import me.varmetek.kitserver.events.kitevents.KitFishEvents;
import me.varmetek.kitserver.events.kitevents.KitFishermanEvents;
import me.varmetek.kitserver.events.kitevents.KitGrapplerEvents;
import me.varmetek.kitserver.events.kitevents.KitKangaEvents;
import me.varmetek.kitserver.events.kitevents.KitLauncherEvents;
import me.varmetek.kitserver.events.kitevents.KitMuckRakerEvents;
import me.varmetek.kitserver.events.kitevents.KitPyroEvents;
import me.varmetek.kitserver.events.kitevents.KitScoutEvents;
import me.varmetek.kitserver.events.kitevents.KitSniperEvents;
import me.varmetek.kitserver.events.kitevents.KitStomperEvents;
import me.varmetek.kitserver.events.kitevents.KitSwitcherEvents;
import me.varmetek.kitserver.events.kitevents.KitThorEvents;
import me.varmetek.kitserver.events.kitevents.KitTurtleEvents;
import me.varmetek.kitserver.events.kitevents.KitViperEvents;
import me.varmetek.kitserver.events.kitevents.KitWitherEvents;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
@SuppressWarnings("deprecation")

public class Main extends JavaPlugin{
	public static Map<String,String> commandCorrections = new HashMap<String,String>();
  
	//The soup item that will be used when ever the plugin gives soup
	
	public static final List<String> SHOP_MENUES_LIST = Arrays.asList(new String[] {"kits"});
	public static String motdz;
	public static final Inventory SHOP_MENU_INV =  Bukkit.createInventory(null, 54, "Shop Selection");
	

	
	public static final String banAppealMsg = " Appeal @ google.com";
	public static final String NO_PERM_MSG = "&cYou don't have permission to execute this command.";
	public static final String  ABILITY_READY = "&aAbility replenished.";
	public static final String  ABILITY_USED = "&aAbility has been activated!";

	
	public static final Set<String> playerBanList = new HashSet<String>();
	
	public static boolean shutdown_async = false;
	
	public static final ItemStack KIT_SELECTION = new ItemStack(Material.NETHER_STAR);
	public static final ItemStack SHOP_SHORTCUT = new ItemStack(Material.EMERALD);
	public static final ItemStack SPEC_CANCEL = new ItemStack(Material.COMPASS);
	public static final ItemStack  GLOBAL_SOUP = new ItemStack(Material.MUSHROOM_SOUP);
	public static final ItemStack  WARP_GUI = new ItemStack(Material.ENDER_PEARL);
	public static final ItemStack  BUYCRAFT = new ItemStack(Material.CHEST);
	
	public static final ItemStack SHOP_EMPTY = new ItemStack(Material.BED,1,(short) 14);
	public static final ItemStack SPEC_ENTER =  new ItemStack(Material.REDSTONE);
	
	
	static Plugin plugin;


	public void onEnable(){
		Utils.setupPerkValues();
		Utils.PLUGIN = this;
	//	checkDepends();
		plugin = this;
		config();
		new BukkitRunnable(){
			public void run(){
			for(Player p : Bukkit.getOnlinePlayers()){
				User u = User.getUser(p.getName());
				
				DataManager.loadUser(u);
				if(p.getGameMode() !=  org.bukkit.GameMode.CREATIVE){
			
					Utils.clearInv(p);
					Utils.giveStartItems(p);	
					u.setGameMode(User.GameMode.PLAYER);
					//u.setGameMode(User.GameMode.PLAYER);
					u.setArea(Area.SPAWN);
				}
				
				
			}
			
		}
			}.runTask(this);
	
		//Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Tps(),0L , 1L);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, Utils.STEP.new CleanUserSet(),0L , 20L);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, Utils.STEP.new UpdateAllScoreboards(),0L , 20L);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, Utils.STEP.new UpdateEXPBar(), 0L , 20L);
		for(String s:SHOP_MENUES_LIST){
				ItemStack item = new ItemStack(Material.ENDER_PORTAL);
				ItemMeta itemM = item.getItemMeta();
				itemM.setDisplayName(s);
				item.setItemMeta(itemM);
				SHOP_MENU_INV.addItem(item);
		}
		
		ItemMeta sM;
		{//setting up Soup	
			ItemMeta soupM = GLOBAL_SOUP.getItemMeta();
			soupM.setDisplayName( Utils.colorCode("&6Soup") );
			GLOBAL_SOUP.setItemMeta( soupM);
		}
		
		
		{//Setting up Spectator item
			sM = SPEC_CANCEL.getItemMeta();
			sM.setDisplayName(Utils.colorCode("&6Leave Spectator Mode."));
			sM.setLore(Arrays.asList(Utils.colorCode(new String[] {"&7Right click this to leave","&7Spectator Mode"})));
			SPEC_CANCEL.setItemMeta(sM);
		}
		
		{//Setting up Kit selection item
		 sM = KIT_SELECTION.getItemMeta();
			sM.setDisplayName(Utils.colorCode("&6Kit Selection."));
			sM.setLore(Arrays.asList(Utils.colorCode(new String[] {"&7Right click this to choose a kit."})));
			KIT_SELECTION.setItemMeta(sM);
		}
		
		{//Setting up Kit selection item
		sM = SHOP_SHORTCUT.getItemMeta();
			sM.setDisplayName(Utils.colorCode("&6Kit Shop."));
			sM.setLore(Arrays.asList(Utils.colorCode(new String[] {"&7Left click this to buy Kits."})));
			SHOP_SHORTCUT.setItemMeta(sM);
		}
		
		{//Setting up Kit selection item
		 sM = SHOP_EMPTY.getItemMeta();
			sM.setDisplayName(Utils.colorCode("&6"));
			
			SHOP_EMPTY.setItemMeta(sM);
		}
		{//Setting up spec join item
			sM = SPEC_ENTER.getItemMeta();
			sM.setDisplayName(Utils.colorCode("&9Spectator Mode"));
			sM.setLore(Arrays.asList(Utils.colorCode(new String[] {"&7Left click this to go into spectator mode."})));
			SPEC_ENTER.setItemMeta(sM);
		}
		
		{//Setting up buycraft join item
			sM = BUYCRAFT.getItemMeta();
			sM.setDisplayName(Utils.colorCode("&2Donations"));
			sM.setLore(Arrays.asList(Utils.colorCode(new String[] {"&7Left click this to see donation options."})));
			BUYCRAFT.setItemMeta(sM);
		}
		
		{//Setting up buycraft join item
			sM = WARP_GUI.getItemMeta();
			sM.setDisplayName(Utils.colorCode("&eWarps"));
			sM.setLore(Arrays.asList(Utils.colorCode(new String[] {"&7Left click this to see the warps."})));
			WARP_GUI.setItemMeta(sM);
		}
		
		
		
		
	
		//noPvPTime = System.currentTimeMillis()+(10*1000);
		//Bukkit.broadcastMessage(Main.colorCode("&a&l All pvp has been disabled for 10 seconds."));
		
		 /*----------------------------------------------------------------------
		  * 	Commadn Executors
		  * -------------------------------------------------------------------------*/	
	 getCommand("stats").setExecutor(new StatsCommand());
	 getCommand("pay").setExecutor(new PayCommand());
	 getCommand("rank").setExecutor(new RankCommand());
	 getCommand("kit").setExecutor(new KitCommand());
	 getCommand("shop").setExecutor(new ShopCommand());
	 getCommand("user").setExecutor(new UserCommand());
	 getCommand("clearkit").setExecutor(new ClearKitCommand());
	 getCommand("lockchat").setExecutor(new LockChatCommand());
	 getCommand("clearchat").setExecutor(new ClearChatCommand());
	 getCommand("setwarp").setExecutor(new SetWarpCommand());
	 getCommand("warp").setExecutor(new WarpCommand());
	 getCommand("removewarp").setExecutor(new RemoveWarpCommand());
	 getCommand("chatsettings").setExecutor(new ChatSettingsCommand());
	 getCommand("ban").setExecutor(new BanCommand());
	 getCommand("unban").setExecutor(new UnbanCommand());
	 getCommand("tempban").setExecutor(new TempBanCommand());
	 getCommand("spawn").setExecutor(new SpawnCommand());
	 getCommand("mute").setExecutor(new MuteCommand());
	 getCommand("invsee").setExecutor(new InvseeCommand());
	 getCommand("kick").setExecutor(new KickCommand());
	 getCommand("mode").setExecutor(new ModeCommand());
	 getCommand("vanish").setExecutor(new VanishCommand());
	 getCommand("event").setExecutor(new EventCommand());
	 getCommand("loc").setExecutor(new LocCommand());
	 getCommand("balance").setExecutor(new BalanceCommand());
	 getCommand("msg").setExecutor(new MsgCommand());
	 getCommand("renew").setExecutor(new RenewCommand());
	 getCommand("duel").setExecutor(new DuelCommand());
	 getCommand("queue").setExecutor(new QueueCommand());
	 getCommand("reply").setExecutor(new ReplyCommand());
	 getCommand("notify").setExecutor(new NotifyCommand());
	 getCommand("arena").setExecutor(new ArenaCommand());
	 getCommand("reloadconfig").setExecutor(new ReloadConfigCommand());
	 getCommand("teleport").setExecutor(new TeleportCommand());
	 getCommand("motd").setExecutor(new MotdCommand());
	 getCommand("lookup").setExecutor(new LookUpCommand());
	 getCommand("kitsettings").setExecutor(new KitSettingsCommand());
	 getCommand("warpsettings").setExecutor(new WarpSettingsCommand());
	 getCommand("gamble").setExecutor(new GambleCommand());
	 getCommand("perks").setExecutor(new PerksCommand());
	 /*----------------------------------------------------------------------
	  * 	Command Tabcompleter
	  * -------------------------------------------------------------------------*/
	 getCommand("rank").setTabCompleter(new RankCommand());
	 getCommand("stats").setTabCompleter(new StatsCommand());
	 getCommand("pay").setTabCompleter(new PayCommand());
	 getCommand("user").setTabCompleter(new UserCommand());
	 getCommand("lockchat").setTabCompleter(new LockChatCommand());
	 getCommand("warp").setTabCompleter(new WarpCommand());
	 getCommand("ban").setTabCompleter(new BanCommand());
	 getCommand("unban").setTabCompleter(new UnbanCommand());
	 getCommand("tempban").setTabCompleter(new TempBanCommand());
	 getCommand("lookup").setTabCompleter(new LookUpCommand());
	 
	 
	 
	 
	 /*----------------------------------------------------------------------
	  * 	Command Events	
	  * -------------------------------------------------------------------------*/
	 getServer().getPluginManager().registerEvents(new MainEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitCommand(), this);	
	 getServer().getPluginManager().registerEvents( new ShopCommand(), this);
	 getServer().getPluginManager().registerEvents( new InvseeCommand(), this);
	 getServer().getPluginManager().registerEvents( new HostedEventsListener(), this);
	 getServer().getPluginManager().registerEvents(new DuelManager(), this);
	 getServer().getPluginManager().registerEvents(new GambleCommand(), this);
	 getServer().getPluginManager().registerEvents(new CustomEventCaller(), this);
	 getServer().getPluginManager().registerEvents(new EventTypeClanWar(), this);
	 getServer().getPluginManager().registerEvents(new EventTypeKoth(), this);
	 getServer().getPluginManager().registerEvents(new EventTypeBrackets(), this);
	 getServer().getPluginManager().registerEvents(new EventTypeLMS(), this);
	 getServer().getPluginManager().registerEvents(new WarpCommand(), this);
	 getServer().getPluginManager().registerEvents(new PerksCommand(), this);
	 getServer().getPluginManager().registerEvents(new PerkEvents(), this);
	// getServer().getPluginManager().registerEvents(new RegionMovement(), this);
	 /*----------------------------------------------------------------------
	  * 	Kit Events	
	  * -------------------------------------------------------------------------*/
	 getServer().getPluginManager().registerEvents( new KitKangaEvents(), this);
	 getServer().getPluginManager().registerEvents( new KitEnchanterEvents(), this);
	 getServer().getPluginManager().registerEvents( new KitSniperEvents(), this);
	 getServer().getPluginManager().registerEvents( new KitLauncherEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitStomperEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitCactusEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitPyroEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitGrapplerEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitFishEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitFishermanEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitSwitcherEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitBerserkEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitWitherEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitViperEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitMuckRakerEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitEnderEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitThorEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitTurtleEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitAnchorEvents(), this);
	 getServer().getPluginManager().registerEvents(new KitScoutEvents(), this);

	
	  
	  
	  commandCorrections.put("stats", "/stats (player)");
	  commandCorrections.put("pay", "/pay [player] [ammount]");
	  commandCorrections.put("rank", "/rank (player) (rank)");
	  commandCorrections.put("shop", "/shop (shop) ");
	  commandCorrections.put("kit", "/kiy (kit)");
	  commandCorrections.put("user", "/user [player] [edit] [stat] [modifier] [value]");
	//  step();
	combatStep();
	
	  DataManager.loadArenas();
	 // DataManager.loadAll();

  }	
    public void onDisable(){
    	shutDownSystem();
  }
  
    public static Plugin getPluginInstance(){
		return plugin;
	}
    public static String getCorrection (String command){
	  return commandCorrections.get(command);
  }


	


	

	

 	

	@SuppressWarnings("unused")
	private static void step(){
		new BukkitRunnable(){
			public void run(){
				
				if(shutdown_async){
					this.cancel();
					return;
				}
			
				if(Utils.translateBanList){
					///Set<OfflinePlayer> s = new HashSet<OfflinePlayer>()
					if(!DataManager.getBanList(false).isEmpty()){
					playerBanList.clear();
						for(String name :DataManager.getBanList(false)){
							UUID id = UUID.fromString(name);
							OfflinePlayer p = Bukkit.getOfflinePlayer(id);
							//debug(id.toString());
							
								playerBanList.add(p.getName());
							
						}
				
					}
				}
			}
		}.runTaskTimerAsynchronously(getPluginInstance(), 0, 1);
	}
	

	


	
	
	private static void combatStep(){
		new BukkitRunnable(){
			public void run(){
				

						for(User name : User.users.values()){
							if(name.getPlayer().isOnline()){
								if(name.getCombatLog() > -1){
									name.setCombatLog(name.getCombatLog()-1);
									
								}
								if(name.getCombatLog() == 0){
										if(name.getPlayer().isOnline()){
											Messenger.send("&7You are no longer in combat.", (Player)name.getPlayer(), Messenger.WARN);
										}
								}
								
							}
							
					
				}
			}
		}.runTaskTimer(getPluginInstance(), 0, 20);
	}
	

    

    
    public static void shutDownSystem(){
    	shutdown_async = true;
    	DataManager.saveAllUsers();
    	DataManager.saveArenas();
    }
    
    public static Set<Player> getPlayersNear(Entity e,double x,double y,double z){
    	final Set<Player> list = new HashSet<Player>();
		for(Entity j: e.getNearbyEntities(x, y, z)){
			if(j instanceof Player){
				list.add((Player)j);
			}
		}
    	return list;
    }
    
    public static void config(){
    	motdz = DataManager.getMotd();
    }
    public static void updateInv(Player pl){
    	pl.openInventory(Bukkit.createInventory(null, 9));
    	pl.closeInventory();
    	
    }
  
	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

	// WorldGuard may not be loaded
	if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
		return null;
		}
		return (WorldGuardPlugin) plugin;
	}
	


	public static void checkDepends(){
		List<Plugin> dependencies = new ArrayList<Plugin>();
		PluginDescriptionFile pdf = getPluginInstance().getDescription();
		for(String s : pdf.getDepend()){
			Plugin p = Bukkit.getPluginManager().getPlugin(s);
			if(p== null){
				System.out.println("Could not find dependency " + s+". Shutting down.");
				Bukkit.getServer().shutdown();
			}else{
				dependencies.add( p);
			}
			
		}
		//dependencies.add( Bukkit.getPluginManager().getPlugin("NametagEdit"));
		//dependencies.add( Bukkit.getPluginManager().getPlugin("WorldGuard"));
		//dependencies.add( Bukkit.getPluginManager().getPlugin("WorldEdit"));
	
		/*for(Plugin p: dependencies){
			if(p == null){
				System.out.println("Couldn't load Plugin: A dependency was not found.");
				
				
			
				return;
			}
		}*/
	}
	



	/*    @SuppressWarnings("deprecation")
	public static void disguisePlayer(Player p, String newName){
        EntityHuman eh = ((CraftPlayer)p).getHandle();
        PacketPlayOutEntityDestroy p29 = new PacketPlayOutEntityDestroy(new int[]{p.getEntityId()});
        PacketPlayOutNamedEntitySpawn p20 = new PacketPlayOutNamedEntitySpawn(eh);
       /* try {
            Field profileField = p20.getClass().getDeclaredField("b");
            profileField.setAccessible(true);
            profileField.set(p20, new GameProfile(""+p.getEntityId(), newName));
        } catch (Exception e) {
            Bukkit.broadcastMessage("Not Work!");
        }
        for(Player o : Bukkit.getOnlinePlayers()){
            if(!o.getName().equals(p.getName())){
                ((CraftPlayer)o).getHandle().playerConnection.sendPacket(p29);
                ((CraftPlayer)o).getHandle().playerConnection.sendPacket(p20);
            }
        }
    }*/
	

}
/*
public static List<String>  getPlayerTabList(boolean relative, List<OfflinePlayer> s,String relativeTo){
	List<String> l = new ArrayList<String>();
	//List<OfflinePlayer> s;
	//BanList b = Bukkit.getBanList(BanList.Type.NAME);
	//Set<BanEntry> be = Bukkit.getBanList(BanList.Type.NAME).getBanEntries();
	//b.addBan(arg0, arg1, arg2, arg3)
/*	if(offline){
		s=new ArrayList<OfflinePlayer>( Arrays.asList(Bukkit.getOfflinePlayers()));
	}else{
		s=new ArrayList<OfflinePlayer>( Arrays.asList(Bukkit.getOnlinePlayers()));
	}
	if(s == null){return null;}
	if(relative){
		
		//if(relativeTo.isEmpty()){Bukkit.broadcastMessage("Empty");return null;}
			//debug("Starting Loop "+ s.size());
		for(OfflinePlayer e:s){
			//debug("in Loop");
			String sub;
			if(e.getName().length() > relativeTo.length()){sub = e.getName().substring(0, relativeTo.length());}
			else  {sub = e.getName();}
			
			if(!sub.isEmpty()){
				//debug (sub);
			}
				if(sub.equalsIgnoreCase(relativeTo.toLowerCase())){
					
					//debug(e.getName().substring(0, relativeTo.length()-1));
					l.add(e.getName());
				}
			}
		}else{
			for(OfflinePlayer e:s){
				l.add(e.getName());
			}
		}
		
	
	
	
	return l;
}

public static List<String>  getPlayerTabList(boolean relative, Set<OfflinePlayer> s,String relativeTo){
	List<String> l = new ArrayList<String>();
	//List<OfflinePlayer> s;
	//BanList b = Bukkit.getBanList(BanList.Type.NAME);
	//Set<BanEntry> be = Bukkit.getBanList(BanList.Type.NAME).getBanEntries();
	//b.addBan(arg0, arg1, arg2, arg3)
/*	if(offline){
		s=new ArrayList<OfflinePlayer>( Arrays.asList(Bukkit.getOfflinePlayers()));
	}else{
		s=new ArrayList<OfflinePlayer>( Arrays.asList(Bukkit.getOnlinePlayers()));
	}
		if(s == null){return null;}
	if(relative){
		
		//if(relativeTo.isEmpty()){Bukkit.broadcastMessage("Empty");return null;}
			//debug("Starting Loop "+ s.size());
		for(OfflinePlayer e:s){
			//debug("in Loop");
			String sub;
			if(e.getName().length() > relativeTo.length()){sub = e.getName().substring(0, relativeTo.length());}
			else  {sub = e.getName();}
			
			if(!sub.isEmpty()){
				//debug (sub);
			}
				if(sub.equalsIgnoreCase(relativeTo.toLowerCase())){
					
					//debug(e.getName().substring(0, relativeTo.length()-1));
					l.add(e.getName());
				}
			}
		}else{
			for(OfflinePlayer e:s){
				l.add(e.getName());
			}
		}
		
	
	
	
	return l;
}

public static List<String>  getStringTabList(boolean relative, List<String> completeOptions,String relativeTo){
	
	if(completeOptions == null){return null;}
	
	List<String> list = new ArrayList<String>();
	if(relative){
		
		for(String avalue:completeOptions){
			//debug("in Loop");
			String sub;
			if(avalue.length() > relativeTo.length()){sub = avalue.substring(0, relativeTo.length());}
			else  {sub = avalue;}
			
		
			if(sub.equalsIgnoreCase(relativeTo.toLowerCase())){
					list.add(avalue);
			}
		}
	
	}else{
			for(String  avalue:completeOptions){
				list.add(avalue);
			}
		}
		
	
	
	
	return list;
}

public static List<String>  getStringTabList(boolean relative, Set<String> completeOptions,String relativeTo){
	
	if(completeOptions == null){return null;}
	
	List<String> list = new ArrayList<String>();
	if(relative){
		
		for(String avalue:completeOptions){
			//debug("in Loop");
			String sub;
			if(avalue.length() > relativeTo.length()){sub = avalue.substring(0, relativeTo.length());}
			else  {sub = avalue;}
			
		
			if(sub.equalsIgnoreCase(relativeTo.toLowerCase())){
					list.add(avalue);
			}
		}
	
	}else{
			for(String  avalue:completeOptions){
				list.add(avalue);
			}
		}
		
	
	
	
	return list;
}


*/
