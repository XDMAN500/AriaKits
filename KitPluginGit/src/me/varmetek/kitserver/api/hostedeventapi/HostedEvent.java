package me.varmetek.kitserver.api.hostedeventapi;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.DuelManager;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeBrackets;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeClanWar;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeKoth;
import me.varmetek.kitserver.api.hostedeventapi.eventypes.EventTypeLMS;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class HostedEvent {
	public static HostedEvent hostedEvent = null;
	public static final int kothMaxScore = 60;
	
	public enum EventType{
		BRACKETS(2,EventTypeBrackets.class),
		CLANWAR(4,EventTypeClanWar.class),
		KOTH(1,EventTypeKoth.class),
		LMS(2,EventTypeLMS.class);
		
		
		int minPlayers;
		Class<?> type;
		private EventType(int minp, Class<?> a){
			minPlayers = minp;
			type = a;
		}
	
		public static EventType getEventTypeByName(String eType){
			
			for(EventType temp : EventType.values()){
				if(temp.name().toLowerCase().equalsIgnoreCase(eType)){
					return temp;
				}
			}
			return null;
		}
	}
	public enum FightType{
		NORMAL,STONE,BOW;
		public static FightType getEventTypeByName(String fType){
			
			for(FightType temp : FightType.values()){
				if(temp.name().toLowerCase().equalsIgnoreCase(fType)){
					return temp;
				}
			}
			return null;
		}
	}

	//public final List<UUID> team1 = new ArrayList<UUID>();
//	public final List<UUID> team2 = new ArrayList<UUID>();
//	public final List<UUID> fighting = new ArrayList<UUID>();
	//public final Map<UUID,Integer> points = new HashMap<UUID,Integer>();
	public EventType eType;
	public FightType fType;
	boolean allowRefills;
	boolean allowRecrafts;	
	short secondsLeft = 60;
	protected boolean started = false;
	protected Set<UUID> enrolledPlayers = new HashSet<UUID>();
	
	
	public static HostedEvent getCurrent(){
		return hostedEvent;
	}
	public int getMinimumPlayers(){
		return getEventType().minPlayers;
		
	}
	public EventType getEventType(){
		return eType;
	}
	public FightType getFightType(){
		return fType;
	}
	public boolean doesAllowRefills(){
		return allowRefills;
	}
	public boolean doesAllowRecafts(){
		return allowRecrafts;
	}
	public boolean isStarted(){
		return started;
		
	}
	public Set<UUID> getEnrolledPlayers(){
		return enrolledPlayers;
	}
	public HostedEvent setStartDelay(short i){
		secondsLeft = i;
		return this;
	}

	public static boolean hostEvent(EventType e, FightType f, boolean r, boolean rc){
		if(hostedEvent != null){
			return false;
		}
		try {
			hostedEvent = (HostedEvent) e.type.newInstance();
			hostedEvent.eType = e;
			hostedEvent.fType = f;
			hostedEvent.allowRefills = r;
			hostedEvent.allowRecrafts = rc;
			hostedEvent.secondsLeft = 60;

			hostedEvent.countdown();
			return true;
		} catch (InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	
		
	}
	
	protected static void parentStop(){
		
		if(hostedEvent ==  null)return;
		
		if(hostedEvent.started){
			Utils.broadcast("&e" + hostedEvent.eType.name()+" is finished.");
			for(UUID e: hostedEvent.getEnrolledPlayers()){
				Player pl =Bukkit.getPlayer(e);
				User user= User.getUser(pl.getName());
			//	user.setGameMode(User.GameMode.SPECTATOR);
				user.setGameMode(User.GameMode.PLAYER);
				user.setGameMode(User.GameMode.PLAYER);
				//pl.getInventory().setContents(null);
				//pl.getInventory().setArmorContents(null);
				//user.changeKit(null);
				pl.teleport(DataManager.getWarp("spawn").getLocation());
				
			}
		}else{
			Utils.broadcast(hostedEvent.eType.name()+" has been canceled.");
		}
	
		hostedEvent = null;
	}
/*	protected static void parentGiveInventory(Player pl){
		User user = User.getUser(pl.getName());
		user.setArea(null);
		//user.setGameMode(GameMode.PLAYER);
		Utils.clearInv(pl);
		//pl.getInventory().setArmorContents(Utils.getEmptyInv(4));
		//pl.getInventory().setContents(Utils.getEmptyInv(36));
	}*/
	
	protected abstract void start();
	public abstract void removePlayer(Player pl);
	protected abstract void update() ;
	protected abstract  void giveInventory(Player pl);
	public abstract void stop();
	
	protected static void load(){
		hostedEvent.started = true;
		Utils.debug("Loading Event");
		for(UUID e: hostedEvent.enrolledPlayers){
		
			Player pl = Bukkit.getPlayer(e);
			if(DuelManager.getMatchedPlayers().contains(e)){
				Messenger.send("You have been kicked from the event for being in a 1v1 battle.",pl, Messenger.WARN);
				hostedEvent.removePlayer(pl);
				return;
			}
			if(DuelManager.getQueuedPlayers().contains(e)){
				Messenger.send("You have been kicked from the event for being in the 1v1 queue.",pl, Messenger.WARN);
				hostedEvent.removePlayer(pl);
				return;
			}
			User user = User.getUser(pl.getName());
			user.changeKit(null);
			user.setGameMode(User.GameMode.PLAYER);
			Utils.clearInv(pl);
			//pl.getInventory().setArmorContents(Utils.getEmptyInv(4));
			//pl.getInventory().setContents(Utils.getEmptyInv(36));
			
			//giveInventory(pl);
			
		}
	
	

		
	}
	
	public  void countdown(){
		
		new BukkitRunnable(){
			public void run(){
				
				if(getCurrent() == null){
					this.cancel();
				}
				
				if(Utils.isIntInList(secondsLeft, 60,30,10,5))
					Utils.broadcast("&e" + getEventType().name() + " will begin in " + secondsLeft + " seconds. /ev j to join.");
				
				if(secondsLeft== 0 ){
					if(getEnrolledPlayers().size() <getMinimumPlayers()){
						Utils.broadcast(getEventType().name() + " didn't have enough players to start.");
						hostedEvent = null;
						this.cancel();
					
					}else{
						Utils.broadcast(getEventType().name()+" has begun.");
						load();
						start();
						hostedEvent.started = true;
						this.cancel();
					}
					this.cancel();
				}
				if(getCurrent() !=null){
					secondsLeft -=1;
				}
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 20);
		
	}
	public void addPlayer(Player pl){
	getCurrent().getEnrolledPlayers().add(pl.getUniqueId());
	
}



	
	
	
}
//getCurrent().getEnrolledPlayers().remove(pl.getUniqueId());
//getCurrent().team1.remove(pl.getUniqueId());
//getCurrent().team2.remove(pl.getUniqueId());
//getCurrent().points.remove(pl.getUniqueId());

//getCurrent().fighting.remove(pl.getUniqueId());
//checkBrackets();
//checkClanWar();
//checkLMS();
/*
public static void  checkBrackets(){
if(getCurrent() == null)return;
Utils.debug("Event is on");
if(!getCurrent().isStarted())return;
Utils.debug("Event is started");
if(getCurrent().getEventType() != EventType.BRACKETS)return;
Utils.debug("Event is brackets");
//Iterator<UUID> i = getCurrent().getEnrolledPlayers().iterator();

if(getCurrent().getEnrolledPlayers().size() <2 ){
	Player pl =Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0));
	 //pl =Bukkit.getPlayer(hostedEvent.fighting.get(0));
	Utils.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
	//pl.performCommand("spawn");
	getCurrent().stop();
	
	return;
	
}
Utils.debug("not over yet");
if(getCurrent().fighting.size() < 2){
	if(!getCurrent().fighting.isEmpty()){
		UUID id = getCurrent().fighting.get(0);
		Player winner = Bukkit.getPlayer(id);
		
		getCurrent().fighting.remove(id);
		winner.teleport(DataManager.getLoc("brack-start"));
		Utils.broadcast(winner.getName() + " has advanced to the next round." );

		winner.getInventory().setArmorContents(null);
		winner.getInventory().setContents(null);
	}
	
	
	//Iterator<UUID> i = hostedEvent.enrolledPlayers.iterator();
	UUID eid ;
	eid =new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0);
	Player p1 = Bukkit.getPlayer(eid);
	getCurrent().fighting.add(eid);

	p1.teleport(DataManager.getLoc("brack-point1"));
	getCurrent().giveInventory(p1);

	
	eid =new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(1);
	Player p2 = Bukkit.getPlayer(eid);
	getCurrent().fighting.add(eid);
	p2.teleport(DataManager.getLoc("brack-point2"));
	getCurrent().giveInventory(p2);
	
	Utils.broadcast(p1.getName() + " vs " + p2.getName());
}
}

/*	
public static void  checkKOTH(){
	
	/*new BukkitRunnable(){
		public void run(){
			if(getCurrent() == null){this.cancel(); return;}
			if(!getCurrent().isStarted()){this.cancel(); return;}
		
			if(getCurrent().getEnrolledPlayers().size() < getCurrent().getMinimumPlayers() ){
				Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0)).performCommand("spawn");
				getCurrent().stop();
				return;
			}
		
			for(UUID u: getCurrent().getEnrolledPlayers()){
				Player pl = Bukkit.getPlayer(u);
				if(!pl.isDead()){
					Block b = pl.getLocation().add(0, -0.1, 0).getBlock();
			
					if(b.getType() == Material.DIAMOND_BLOCK){
						getCurrent().points.put(u,getCurrent().points.get(u)+1);
						pl.getWorld().playEffect(pl.getEyeLocation(), Effect.HAPPY_VILLAGER,0);
						pl.getWorld().playSound(pl.getLocation(), Sound.ORB_PICKUP, 1, 1);
					}
			
					int score =  getCurrent().points.get(u);
				
					if(score % 10 == 0 && score != 0 && b.getType() == Material.DIAMOND_BLOCK){
						Messenger.sendGroupU("&5&l&o" + getCurrent().getEventType().name()+"&7&l>> " + "&6"+pl.getName() + " &bhas reached a score of " + "&a"+score + "&b/" + kothMaxScore + "." ,
								new ArrayList<UUID>(getCurrent().getEnrolledPlayers()), Messenger.INFO);
					}
			
					if(score >= kothMaxScore){
						Utils.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
						getCurrent().stop();
						this.cancel();
						break;
					}
				}
			}
		}
	}.runTaskTimer(Utils.getPluginInstance(), 0, 20);
}

public static void checkClanWar(){
/*	if(getCurrent() == null)return;
	if(!getCurrent().isStarted())return;
	if(getCurrent().getEventType() != EventType.CLANWAR)return;
	
	if(getCurrent().team1.isEmpty()){
		
		for(UUID u:getCurrent().team2){
			Player pl = Bukkit.getPlayer(u);
			pl.performCommand("spawn");
			Messenger.send("You have won " + getCurrent().getEventType().name(), pl, Messenger.INFO);
		}
		getCurrent().stop();
	}
	
	if(getCurrent().team2.isEmpty()){
		
		for(UUID u : getCurrent().team1){
			Player pl = Bukkit.getPlayer(u);
			pl.performCommand("spawn");
			Messenger.send("You have won " + getCurrent().getEventType().name(), pl, Messenger.INFO);
		}
		
		getCurrent().stop();
	}
}

public static void checkLMS(){
	/*if(getCurrent() == null)return;
	if(!getCurrent().isStarted())return;
	if(getCurrent().getEventType() != EventType.LMS)return;

	if(getCurrent().getEnrolledPlayers().size() < getCurrent().getMinimumPlayers() ){
		Player pl =Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().enrolledPlayers).get(0));
		pl.performCommand("spawn");
		Utils.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
		getCurrent().stop();
		return;
		
	}
}

*/

/*switch(eType){
case BRACKETS:
	/*for(UUID e: enrolledPlayers){
		if(Bukkit.getPlayer(e) == null){
			enrolledPlayers.remove(e);
		}else{
			Bukkit.getPlayer(e).teleport(DataManager.getLoc("brack-start"));

		}
	}
	Iterator<UUID> i = enrolledPlayers.iterator();
	UUID eid =i.next();
	fighting.add(eid);
	Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point1"));
	
	eid =i.next();
	fighting.add(eid);
	Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point2"));
	break;
case CLANWAR:
	/*for(UUID e: enrolledPlayers){
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
			team1.add(l.get(f));
		}else{
			team2.add(l.get(f));
		}
		
		for(UUID e: team1){
			Bukkit.getPlayer(e).teleport(DataManager.getLoc("cw-point1"));
		}
		for(UUID e: team2){
			Bukkit.getPlayer(e).teleport(DataManager.getLoc("cw-point2"));
		}
	}
	break;
case KOTH:
	/*for(UUID e: enrolledPlayers){
		if(Bukkit.getPlayer(e) == null){
			enrolledPlayers.remove(e);
		}else{
			points.put(e, 0);
			Bukkit.getPlayer(e).teleport(DataManager.getLoc("koth-start"));
		}
	}
	checkKOTH();
	break;
case LMS:
	/*for(UUID e: enrolledPlayers){
		if(Bukkit.getPlayer(e) == null){
			enrolledPlayers.remove(e);
		}else{
			Bukkit.getPlayer(e).teleport(DataManager.getLoc("lms-start"));
		}
	}
	break;

}*/




/*if(hostedEvent.enrolledPlayers.size() <2){
Bukkit.getPlayer(l.get(0)).performCommand("spawn");
hostedEvent.cancelEvent();

}else{
hostedEvent.fighting.remove(id);
hostedEvent.fighting.remove(l.get(0));
Bukkit.getPlayer(l.get(0)).teleport(DataManager.getLoc("brack-start"));
Iterator<UUID> i = hostedEvent.enrolledPlayers.iterator();
UUID eid =i.next();
hostedEvent.fighting.add(eid);
Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point1"));

eid =i.next();
hostedEvent.fighting.add(eid);
Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point2"));
}*/