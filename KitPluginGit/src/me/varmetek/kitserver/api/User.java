package me.varmetek.kitserver.api;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.varmetek.kitserver.api.DuelManager.FightStyle;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import ca.wacos.nametagedit.NametagAPI;



public class User {
	
	public static Map<String , User> users = new HashMap< String , User>();

	private String userID;
	private BigInteger money = BigInteger.valueOf(0L);
	private int killstreak = 0;
	private int combo = 0;
	private int kills=0;
	private int deaths=0;
	private byte jumps=1;
	private Long chatCooldown = 0L;
	private Long kitCooldown = 0L;
	private Calendar muteTime = Calendar.getInstance();
	private Kit kit = null;
	private Rank rank = Rank.Default;
	private GraphicalUserInterface gui = null;
	private Set<Kit> boughtKits = new HashSet<Kit>();
	private OfflinePlayer player;
	private UUID id;
	private Inventory inv = null;
	private String banMessage = "";
	private Calendar banExpire = Calendar.getInstance();
	private GameMode gameMode = GameMode.PLAYER;
	private Location lastLocation = DataManager.getWarp("spawn").getLocation();
	private Rank vanishedTo = null;
	private boolean godmode = false;
	private boolean frozen = false;
	private Map<UUID,DuelRequest> matchRequest = new HashMap<UUID,DuelRequest>();
	private Player lastMsgReciever = null;
	private UUID battling = null;
	private int combatTimer =-1;
	private Area area = null;
	private DuelRequest duelReq = new DuelRequest(DuelManager.FightStyle.NORMAL, false, false);
	private boolean allowKitChange = true;
	private Scoreboard scoreB = Bukkit.getScoreboardManager().getNewScoreboard();
	private Objective sbStats;
	private boolean isInvSee = false;
	private List<String> ownedPerks = new ArrayList<String>();
	private Perk currentPerk = null;
	private ProducerLevel prodLvL = ProducerLevel.NONE;
	private NotifyMode  nMode = NotifyMode.NORMAL;
	private long exp = 0L;
	private int lvl = 0;
	
	public enum GameMode{
		PLAYER(Rank.Default,"p","pl","play"),
		SPECTATOR(Rank.Default,"spec"," s"),
		BUILDER(Rank.Admin,"b","build"),
		STAFF(Rank.TrialMod,"a","admin");
		
		public String[] alias;
		public Rank requiredRank;
		private GameMode(Rank r,String...s){
			alias = s;
			requiredRank = r;
		}
		public static GameMode getByName(String mode){
			
			for(GameMode temp : GameMode.values()){
				if(Arrays.asList(temp.alias).contains(mode.toLowerCase()) || temp.name().toLowerCase().equalsIgnoreCase(mode.toLowerCase())){
					return temp;
				}
			}
			return null;
		}
	}
	
	public enum ProducerLevel{
		YOUTUBE("&9(&f&lYou&4&lTube&9)"),
		TWITCH("&9(&5&lTwitch&9)"),
		HITBOX("&9(&2HitBox&9)"),
		DAILYMOTION("&9(&3DailyMotion&9)"),
		NONE("");
		
		private String icon;;
		private ProducerLevel(String suf){
		icon = suf;
		}
		
		public String getIcon(){
			return icon;
		}
	}
	
	public enum NotifyMode{
		
		NORMAL(null),
		STAFF(NotifyMode.NORMAL),
		DEBUG(NotifyMode.STAFF);
		
		
		private NotifyMode child;
		private NotifyMode( NotifyMode child){
			this.child = child;
		}
		
		public boolean doesInherient(NotifyMode checkRank){
			NotifyMode  tempRank  = this;
			
			
			while (tempRank != checkRank) {
				tempRank = tempRank.child;
				if( tempRank == null){
					return false;
				}
			}
			
			if(tempRank == checkRank){
				return true;
			}
			
			return false;
		}
	}
 	@SuppressWarnings("deprecation")
	private User (String name){
		
			userID = name;
			player = Bukkit.getOfflinePlayer(name);
			id = player.getUniqueId();
			if(player.isOnline()){
				((Player)player).setScoreboard(scoreB);
			}
			refreshListName();
			users.put(userID, this);
			updateScoreBoard();
			
			
			
		
	}
	
 	
////////////////////////////////////////////////////////////////
	
	/*@SuppressWarnings("deprecation")
	private static User createUser( String playerName){

		if(!users.containsKey(playerName)){
			
			final User user = new User(playerName);
			user.player = Bukkit.getOfflinePlayer(playerName);
			
			user.money = 0;
			user.killstreak = 0;
			user.combo = 0;
			user.kills = 0;
			user.deaths = 0;
			user.rank = Rank.Default;
			user.kitCooldown = 0L;
			user.chatCooldown = 0L;
			user.kit = null;
			user.gui = null;
			user.jumps = 1;
			user.id = user.player.getUniqueId();
			user.ownedKits = new HashSet<Kit>();
			user.inv = null;
			user.banMessage = "";	
			user.banExpire = Calendar.getInstance();
			user.muteTime= Calendar.getInstance();
			user.gameMode = GameMode.PLAYER;
			user.vanishedTo = null;
			user.godmode = false;
			user.frozen = false;
			user.matchRequest = new HashMap<UUID,DuelRequest>();
			user.lastMsgReciever = null;
			user.nMode = NotifyMode.NORMAL;
			user.battling = null;
			user.combatTimer =-1;
			user.area = null;
			user.duelReq = new DuelRequest(DuelManager.FightStyle.NORMAL, false, false);
	
		
			user.lastLocation = DataManager.getWarp("spawn");
			
			for(Kit k : Kit.getKits(Kit.Type.DEFAULT)){
				user.ownedKits.add(k);
				
				
			}
		
			user.refreshListName();
			users.put(playerName, user);
			
			return user;
		}
		return null;
		
	}*/
	
	public static User getUser(String playerName){
		
		if(playerName.equals(null)){
			return null;
		}
	
		User i = users.get(playerName);
		
		if(i == null){
			i = new User(playerName);
		}
		/* i.sbStats.getScore(Utils.colorCode("&6Kills")).setScore(i.kills);
		 i.sbStats.getScore(Utils.colorCode("&6Deaths")).setScore(i.deaths);
		 i.sbStats.getScore(Utils.colorCode("&6Killstreak")).setScore(i.killstreak);
		 i.sbStats.getScore(Utils.colorCode("&5Bits")).setScore(i.money);*/
		
		//i.updateScoreBoard();
		 if(!NametagAPI.getPrefix(i.player.getName()).equals(Utils.colorCode(i.getRank().getPrefix())))
			 NametagAPI.setPrefix(i.player.getName(), Utils.colorCode(i.getRank().getPrefix()));
		i.refreshListName();
		return i;
		
	}
	
	public static User[] getAllUsers(){
		return  users.values().toArray(new User[0]);
		
	}
	
	public static List<User> getAllPermedUsers(Rank rank){
		List<User> list = new ArrayList<User>();
		for(User u : getAllUsers()){
			if(u.player.isOnline()){
				if(u.hasPermission(rank)){
					list.add(u);
				}
			}
		}
		return list;
	}
	
	public static List<User> getAllRankedUsers(Rank rank){
		List<User> list = new ArrayList<User>();
		
		for(User u : getAllUsers()){
			if(u.player.isOnline()){
				if(u.getRank() == rank ){
					list.add(u);
				}
			}
		}
		return list;
	}
	
	public static List<User> getAllVanishedUsers(Rank rank){
		List<User> list = new ArrayList<User>();
		
		for(User u : getAllUsers()){
			if(u.player.isOnline()){
				if(u.isVanished() ){
					list.add(u);
				}
			}
		}
		return list;
	}
	public static List<User> getUsersWithNotify(NotifyMode mode){
		List<User> list = new ArrayList<User>();
		
		for(User u : getAllUsers()){
			if(u.player.isOnline()){
				if(u.nMode == mode){
					list.add(u);
				}
			}
		}
		return list;
	}
	public static String getLevelTitle(int lvl){
		if(lvl == 100){
			return "&9*&f&l"+lvl+"&9*";
		}else{
			if( lvl<100 && lvl>=80){
				return "&e&l"+lvl;
			}else{
				if( lvl<80 && lvl>=60){
					return "&6&l"+lvl;
				}else{
					if( lvl<60 && lvl>=40){
						return "&3&l"+lvl;
					}else{
						if( lvl<40 && lvl>=20){
							return "&7"+lvl;
						}else{
							return "&8"+lvl;
						}
					}
				}
			}
		}
		
	}

	public static long getExpNeeded(int lvl){
	
		return   30L*((long)Math.floor(Math.pow(1.3, lvl)));
			
		
	}
	/////////////////////////////////////////////////////////////////	
	public String getUserID(){
		return userID;
	}
	
	
	public OfflinePlayer getPlayer(){
		
		return player;
	}
	
	public String getRankTitle(){
		return Utils.colorCode(rank.prefix + player.getName() + rank.suffix);
	}
 	public boolean isVanished(){
		return (vanishedTo != null);
	}
	
	
	public Rank getVanishedTo(){
		return vanishedTo;
	}
	
	
	public User setVanishedTo(Rank r){
		if(!player.isOnline()){
			
			return this;
		}
		vanishedTo = r;
		refreshVanish();
			return this;	
	}
	
	
	@SuppressWarnings("deprecation")
	public User refreshVanish(){
		if(!player.isOnline())return this;
		if(vanishedTo != null){
			for(User u : getAllUsers()){
				if(vanishedTo.doesInherient(u.getRank())){
					if(u.player.isOnline()){
						Player pl = (Player)u.player;
						if(!DuelManager.getMatchedPlayers().contains(pl.getUniqueId()))
							pl.hidePlayer(((Player)this.player));
					}
				}
			}
		}else{
			for(Player e: Bukkit.getOnlinePlayers()){
			if(!DuelManager.getMatchedPlayers().contains(e.getUniqueId()))
				e.showPlayer((Player)this.player);
			}
		}
		
		return this;
	}
 	
	
	public boolean hasPermission(Rank rank){
		
		return this.rank.doesInherient(rank) || this.getPlayer().isOp();
		
		
	}
	
	
 	public UUID getPlayerUUID(){
		
		return id;
	
	}
 		

 	public Calendar getBanExpire(){
 		
		return banExpire;
	}
	public User addBanExpire( int j){
 		Calendar temp =Calendar.getInstance();
 		temp.add(Calendar.SECOND, j);
		 banExpire = temp;
		 return this;
	}
 	public User setBanExpire( Calendar j){
 		
		 banExpire = j;
		 return this;
	
 	}
 	public User resetBanExpire(){
 		
		 banExpire = Calendar.getInstance();
		 return this;
	}
 	public User indefiniteBanExpire(){
 		
		 banExpire = null;
		 return this;
	}
 	
 	
	public Calendar getMuteExpire(){
 		
		return muteTime;
	}
	public User addMuteExpire( int j){
 		Calendar temp =Calendar.getInstance();
 		temp.add(Calendar.SECOND, j);
 		muteTime = temp;
		 return this;
	}
 	public User setMuteExpire( Calendar j){
 		
 		muteTime = j;
		 return this;
	
 	}
 	public User resetMuteExpire(){
 		
 		muteTime = Calendar.getInstance();
		 return this;
	}
 	public User indefiniteMuteExpire(){
 		
		 muteTime = null;
		 return this;
	}
 	
	
 	public String getBanMessage(){
 		
		return banMessage;
	}
 	
 	
 	public User setBanMessage( String msg){
 		
		 banMessage = msg;
		 return this;
	}
 	
 	
	public Location getLastLocation(){
 		
		return lastLocation;
	}
 	
	
 	public User setLastLocation( Location loc){
 		
		 lastLocation = loc;
		 return this;
	}
 

 	public byte getJumps(){
 		
		return jumps;
	}
 	public User setJumps( byte j){
 		
		 jumps = j;
		 return this;
	}
 	
 	
	public GameMode getGameMode(){
 		
		return gameMode;
	}
 	public User setGameMode( GameMode mode){
 		 Player pl = (Player)player;
 		if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId()))return this;
		 gameMode = mode;
		 pl.setHealth(20.0);
		 pl.setSaturation( 20.0f);
		 for(PotionEffect e:  pl.getActivePotionEffects()){
			 pl.removePotionEffect(e.getType());
		 }
	
		 pl.setFireTicks(0);
		
		 godmode = false;
		 frozen = false;
		 changeKit(null);
		 setVanishedTo(null);
		clearMatchRequest();
		 switch(mode){
		 case SPECTATOR:
			
			 
			 pl.setCanPickupItems(false);
			 Utils.clearInv(pl);
				//Main.debug("User: Giving Mode: "+ mode.name());
			 pl.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,100), true);
			 pl.setGameMode(org.bukkit.GameMode.CREATIVE);
			 setVanishedTo(Rank.Admin);
			 pl.getInventory().setItem(8, Main.SPEC_CANCEL);
			 PlayerToggleFlightEvent ev = new PlayerToggleFlightEvent(pl, true);
				Main.getPluginInstance().getServer().getPluginManager().callEvent(ev);
			
			 break;
		 case BUILDER:
			 //Utils.debug("User: Giving Mode: "+ mode.name());
			 pl.setCanPickupItems(true);
			 pl.setAllowFlight(true);
			 pl.setGameMode(org.bukkit.GameMode.CREATIVE);
			 break;
		 case STAFF:
			// Utils.debug("User: Giving Mode: "+ mode.name());
			 pl.setCanPickupItems(true);
			 pl.setAllowFlight(true);
			 pl.setGameMode(org.bukkit.GameMode.CREATIVE);
			 break;
		 case PLAYER:
			//Utils.debug("User: Giving Mode: "+ mode.name());
			 pl.setAllowFlight(false);
			 pl.setCanPickupItems(true);
			 Utils.cleansePlayer(pl);
			 pl.teleport(DataManager.getWarp("spawn").getLocation());
			 pl.setGameMode(org.bukkit.GameMode.ADVENTURE);
			 Utils.giveStartItems(pl);
			 break;
		 }
		 return this;
	}
 	

 	public Inventory getInv(){
 		return inv;
 	}
 	public User setInv(Inventory inv){
 		this.inv = inv;
 		return this;
 	}

 	
 	public Long getChatCooldown(){
		return chatCooldown;
	}
 	public User setChatCooldown(Long cd){
		this.chatCooldown = cd;
 		return this;
	}
 		
 	
 	public int getKills(){
		return kills;
	}
	public User setKills( int value){
		kills = value;
		return this;
	}
	public  User addKills( int value){
		kills += value;
		return this;
	}
	public User subtractKills( int value){

		kills -= value;
		return this;
	}

	
	public int getDeaths(){
		return deaths;
	}
	public User setDeaths( int value){
		deaths = value;
		return this;
	}
	public  User addDeaths( int value){
		deaths += value;
		return this;
	}
	public User subtractDeaths( int value){

		deaths -= value;
		return this;
	}

	
	public int getKillstreak(){
		return killstreak;
	}
	public User setKillstreak( int value){
		killstreak = value;
		return this;	
	}
	public  User addKillstreak( int value){
		killstreak += value;
		return this;
	}
	public User subtractKillstreak( int value){

		killstreak -= value;
		return this;
	}
	
	
	public BigInteger getMoney(){
		return money;
	}	
	public  User setMoney( int value){
	money  = BigInteger.valueOf(value);
		return this;
	}
	public  User addMoney( int value){
		//int bits = money + value;
		if(currentPerk != null ){
			if(currentPerk.getType() == Perk.Type.CASHFLOW){
				value += (value*(Perk.VALUES.get(currentPerk.getType())[currentPerk.getLevel()-1])/100D);
			}
		}
	money =	money.add(BigInteger.valueOf(value)) ;
		return this;
	}
	public User subtractMoney( int value){
		if(currentPerk != null ){
			if(currentPerk.getType() == Perk.Type.CASHFLOW){
				value-=(value*(Perk.VALUES.get(currentPerk.getType())[currentPerk.getLevel()-1])/100D);
			}
		}
	money =money.subtract(BigInteger.valueOf(value));
		return this;
	}
	
	public  User setMoney( BigInteger value){
		money= value;
		return this;
	}
	public  User addMoney( BigInteger value){
		//int bits = money + value;
		if(currentPerk != null ){
			if(currentPerk.getType() == Perk.Type.CASHFLOW){
				value.add( (value.multiply(BigInteger.valueOf(Perk.VALUES.get(currentPerk.getType())[currentPerk.getLevel()-1])).divide(BigInteger.valueOf(100))));
			}
		}
		
	money =	money.add(value) ;
		return this;
	}
	public User subtractMoney( BigInteger value){
		if(currentPerk != null ){
			if(currentPerk.getType() == Perk.Type.CASHFLOW){
				value.subtract( (value.multiply(BigInteger.valueOf(Perk.VALUES.get(currentPerk.getType())[currentPerk.getLevel()-1])).divide(BigInteger.valueOf(100))));

			}
		}
	money =	money.subtract(value);
		return this;
	}
	
	
	public Rank getRank(){
		return rank;
	}
	public User setRank(Rank rank){
		
		this.rank = rank;
		refreshVanish();
		 refreshListName();
		;
		return this;
	}
	
	
	public Long getDelay(){
		return kitCooldown;
	}
	public User setDelay(int seconds){
		Long amount = System.currentTimeMillis()+ (seconds*1000);
		if(currentPerk != null){
			if(currentPerk.getType() == Perk.Type.RECOVERY){
				amount =  (long) (amount - (amount*(Perk.VALUES.get(currentPerk.getType())[currentPerk.getLevel()-1])/100D));	
			}
			
		}
		kitCooldown = amount;
		return this;
	}
	
	
	public boolean isDelayOver(){
		return (kitCooldown/System.currentTimeMillis()) < 1 ;
	}
	public int getLeftOverDelay(){
		return (int)Math.max((kitCooldown-System.currentTimeMillis())/1000, 0) ;
	}
	
	public double getKDR(){
		return (double)(kills/Math.max((double)deaths, 1D));
	}
	
	
	public Kit getKit(){
		return kit;
	}
	public User changeKit(Kit kit){
			kitCooldown = 0L;
			((HumanEntity) player).getInventory().clear();
			((HumanEntity) player).getInventory().setArmorContents(null);
			this.kit = kit;
			return this;
	}	
	private void checkBoughtKits(){
		for(Kit k: boughtKits){
			if(k.getType() == Kit.Type.DEFAULT || (k.getType() == Kit.Type.DONOR &&!hasPermission(k.getRequiredRank() )
					||(k.getType() == Kit.Type.NONDONOR && !k.isEnabled() ))){boughtKits.remove(k);money.subtract(BigInteger.valueOf( k.getPrice()));
					}
		}
			/*if(k.getType() == Kit.Type.DEFAULT){
				boughtKits.remove(k);
			}else{
				if(k.getType() == Kit.Type.DONOR){
					if(!hasPermission(k.getRequiredRank())){
						boughtKits.remove(k);
						money += k.getPrice();
					}
				}else{
					if(k.getType() == Kit.Type.NONDONOR && !k.isEnabled()){
						boughtKits.remove(k);
						money += k.getPrice();
					}
				}
			}
			
		}*/
	}
	public Set<Kit> getOwnedKits(boolean donor){
		checkBoughtKits();
		Set<Kit> ks = new HashSet<Kit>(boughtKits);
		for(Kit k: Kit.getKits(Kit.Type.DEFAULT)){
			ks.add(k);
		}
		if(donor){
			for(Kit k : Kit.getKits(Kit.Type.DONOR)){
			
				if(this.hasPermission(k.getRequiredRank()) && boughtKits.contains(k)){
					ks.add(k);
				}
			}
		}
		return ks;
	}
	public User setBoughtKits(Set<Kit> kits){
		boughtKits = new HashSet<Kit>(kits);
		checkBoughtKits();
		return this;
	}
	public Set<Kit>getBoughtKits(){
		checkBoughtKits();
		
		
		return boughtKits;
	}
	public boolean hasKit(Kit k){
		if(k!= null){	
				return this.getOwnedKits(true).contains(k);
		}
		return false;
	}
	public boolean usingKit(){
	
		return this.kit != null;
	}
	public Kit getCurrentKit(){
		
		return kit;
	}
	public boolean canBuyKit(Kit k){
		if(k!= null){
			if( hasPermission(k.getRequiredRank()) && money.compareTo(BigInteger.valueOf(k.getPrice()))!=-1 && ( k.getType() != Kit.Type.DEFAULT )){
				return true;
			
			}
		}
		return false;
	}
	public User addOwnedKit(Kit kit){
		
			boughtKits.add(kit);
			checkBoughtKits();
		
		return this;
	}
		
	
	public User setGui(GraphicalUserInterface u){
		gui = u;
		return this;
	}
	public GraphicalUserInterface getGui(){
		return gui;	
	}
	
	
	public void refreshListName(){
		if(player.isOnline()){
			Player pl = (Player)player;
			String listName = Utils.colorCode(getRank().getPrefix() + pl.getName());
			if(listName.length() >15){
				listName = listName.substring(0, 15);
			}
			pl.setPlayerListName(listName);
			//NametagAPI.setPrefix(player.getName(),Utils.colorCode( getRank().getPrefix()));
		
		}
	}
	
	public int getCombo(){
		return combo;
	}
	public User setCombo(int i){
		combo = i;
		return this;
	}
	public User addCombo(int i){
		combo += i;
		return this;
	}
	public User subtractCombo(int i){
		combo -= i;
		return this;
	}

	
	public boolean isGodModed(){
		return (gameMode != GameMode.PLAYER || godmode || !Area.allowPvP(area));
	}
	public User setGodMode(boolean g){
		godmode =  g;
		return this;
	}
	
	
	public boolean isFrozen(){
		return frozen;
	}
	public User setFrozen(boolean g){
		frozen =  g;
		return this;
	}

	public Map<UUID,DuelRequest> getMatchRequests(){
		return matchRequest;
	}
	
	public User addMatchRequest(final UUID u, FightStyle fs ,  boolean refills,  boolean recrafts){
		
		DuelRequest brNew =  new DuelRequest(fs,refills,recrafts);
		matchRequest.put(u,brNew);
		new BukkitRunnable(){
			public void run(){
					Player pl = Bukkit.getPlayer(u);
					matchRequest.remove(u);
					if(pl!= null && player.isOnline() && !DuelManager.getMatchedPlayers().contains(getPlayerUUID())){
						Messenger.send("&7You duel request with &o%player% &7has expired.".replace("%player%", pl.getName())  , (Player)player, Messenger.WARN);
					}
			}
		}.runTaskLater(Main.getPluginInstance(), 20*10);
	//	Utils.debug("Making Request:"+fs.name()+":"+refills+":"+recrafts);
		checkMatchRequest(User.getUser(Bukkit.getPlayer(u).getName()),fs,refills,recrafts);
		return this;
	}
	public User removeMatchRequest(UUID u){
		matchRequest.remove(u);
		return this;
	}
	public User clearMatchRequest(){
		matchRequest.clear();;
		return this;
	}
	
	public User checkMatchRequest(User u,FightStyle fs ,boolean refills,  boolean recrafts){
		if(u.getMatchRequests().keySet().contains(getPlayerUUID())){
			//Utils.debug("CHECKING:"+fs.name()+":"+refills+":"+recrafts);
			DuelRequest br = u.getMatchRequests().get(getPlayerUUID());
			DuelManager.addMatchPlayers(
					getPlayerUUID(),
					u.getPlayerUUID(),
					br.getFightStyle(),
					br.isRefills() ,
					br.isRecrafts()
				);
		}
		return this;
	}
	
	public Player getLastMsgReciever(){
		return lastMsgReciever;
	}
	public User setLastMsgReciever(Player pl){
		
		lastMsgReciever= pl;
		return this;
	}
	
	
	public NotifyMode getNotifyMode(){
		return nMode;
	}
	public User setNotifyMode( NotifyMode nm){
		nMode = nm;
		return this;
	}
	
	
	public User setDuelRequest(DuelRequest e){
		duelReq = e;
		return this;
		
	}
	public DuelRequest getDuelRequest(){
		return duelReq ;
	}


	
	public UUID getBattling(){
		return battling;
	}
	public User setBattling(UUID u){
		battling= u;
		return this;
		
	}
	
	public User setCombatLog(int time){
			combatTimer = time;
			return this;
	}
	public int getCombatLog(){
		return combatTimer;
}
	
	public User setArea(Area time){
		area = time;
		return this;
	}
	public Area getArea(){
		return area;
		
	}

	public void remove(){
		NametagAPI.resetNametag(player.getName());
		//DataManager.savePlayerUser(this);
		users.remove(userID);
	}
	public boolean getCanChangeKit(){
		return allowKitChange;
	}
	public User setCanChangeKit(boolean can){
		 allowKitChange = can;
		 return this;
	}


	public User setInvSee(boolean is){
		isInvSee = is;
		return this;
	}
	
	public boolean isInvSee(){
		return isInvSee;
	}
	
	public void updateScoreBoard(){
		if(!player.isOnline()){
			return;
		}
	//	scoreB.clearSlot(DisplaySlot.SIDEBAR);
		if(sbStats!= null){
			sbStats.unregister();
		}
		
		sbStats = scoreB.registerNewObjective("stats", "dummy");
		sbStats.setDisplayName(Utils.colorCode("&5&o"+player.getName()));
		sbStats.setDisplaySlot(DisplaySlot.SIDEBAR);
		sbStats.getScore(Utils.colorCode("&6Kills")).setScore(14);
		sbStats.getScore(Utils.colorCode("&e&l"+Utils.getAbrv(kills))).setScore(13);
		sbStats.getScore(Utils.colorCode("&6Deaths")).setScore(12);
		sbStats.getScore(Utils.colorCode("&e&l"+Utils.getAbrv(deaths))).setScore(11);
		sbStats.getScore(Utils.colorCode("&6Killstreak")).setScore(10);
		sbStats.getScore(Utils.colorCode("&e&l"+Utils.getAbrv(killstreak))).setScore(9);
		sbStats.getScore(Utils.colorCode("&5Bits")).setScore(8);
		sbStats.getScore(Utils.colorCode("&e&l"+Utils.getAbrv(money))).setScore(7);
		//sbStats.getScore(Utils.colorCode("&5CombatTag")).setScore(6);
		//sbStats.getScore(Utils.colorCode("&e&c"+getCombatLog()+"s")).setScore(5);
		sbStats.getScore(Utils.colorCode("&e&c      ")).setScore(4);
		
		
		sbStats.getScore(Utils.colorCode(isGodModed() ? "&aProtected":"&cVulnerable")).setScore(3);
		
	}
	public List<String> getOwnedPerks(){
		return ownedPerks;
	}
	public User addOwnedPerk(Perk p){
		ownedPerks.add(p.toString());
		return this;
		
	}
	public User removerOwnedPerk(Perk p){
		ownedPerks.remove(p.toString());
		return this;
		
	}
	public User resetOwnedPerks(){
		ownedPerks = new ArrayList<String>();
		return this;
	}
	public User setOwnedPerks(List<String> list){
		if(list == null){
			resetOwnedPerks();
		}else{
		
			ownedPerks = new ArrayList<String>(list);
		}
		return this;
	}
	public  boolean ownsPerk(Perk p){
		return ownedPerks.contains(p.toString());
	}
	
	public 	Perk getCurrentPerk(){
		return currentPerk;
	}
	public User setCurrenPerk(Perk p){
		currentPerk = p;
		return this;
	}


	public ProducerLevel getProdLvL() {
		return prodLvL;
	}


	public void setProdLvL(ProducerLevel prodLvL) {
		this.prodLvL = prodLvL;
	}

 	
 	public long getEXP(){
 		updateXPLevel();
		return exp;
	}
	public User setEXP( long value){
		exp = Math.min(Math.max(0L, value),Long.MAX_VALUE);
		updateXPLevel();
		return this;
	}
	public  User addEXP( long value){
		if(getLevel() == 100)return this;
		exp = Math.min(Math.max(0L, exp+value),Long.MAX_VALUE);
		updateXPLevel();
		return this;
	}
	public User subtractEXP( long value){

		exp = Math.min(Math.max(0L, exp-value),Long.MAX_VALUE);
		updateXPLevel();
		return this;
	}
	
	public int getLevel(){
		updateXPLevel();
		return lvl;
		
	}
	public User setLevel( int value){
		lvl = Math.min(Math.max(0, value),100);
		updateXPLevel();
		return this;
	}
	public  User addLevels( int value){
		lvl = Math.min(Math.max(0, lvl+value),100);
		updateXPLevel();
		return this;
	}
	public User subtractLevels( int value){

		lvl = Math.min(Math.max(0, lvl-value),100);
		updateXPLevel();
		return this;
	}
	private User updateXPLevel(){
			if(lvl == 100)return this;
			if(exp >= getExpNeeded(lvl)){
				exp-=getExpNeeded(lvl);
				lvl++;
				updateXPLevel();
			}
			return this;
	}
	public float getProgress(){
		if(lvl == 100)return 1.0f;
		return (float)(exp)/(float)(getExpNeeded(lvl));
	}


	
}



