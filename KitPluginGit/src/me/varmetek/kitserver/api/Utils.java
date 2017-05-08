package me.varmetek.kitserver.api;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
@SuppressWarnings("deprecation")
public class Utils {
	public static Plugin PLUGIN = Main.getPluginInstance();
	public static boolean chatLocked = false;
	public static boolean translateBanList = true;
	public static Long noPvPTime = 0L;
	public static final StepRunnable STEP = new StepRunnable();
	public static int chatDelay = 750;
	private final static String abr[] = new String[] {"","K","M","B","T","Q","Qn","S","Sp","O","N","D","Ud","Dud","Trd","Qad","Qid","Sd","Spd"};
	public static final List<Material> ARMOR = Arrays.asList(new Material[] {
		Material.LEATHER_BOOTS,
		Material.LEATHER_LEGGINGS,
		Material.LEATHER_CHESTPLATE,
		Material.LEATHER_HELMET,
		
		Material.GOLD_BOOTS,
		Material.GOLD_LEGGINGS,
		Material.GOLD_CHESTPLATE,
		Material.GOLD_HELMET,
		
		Material.CHAINMAIL_BOOTS,
		Material.CHAINMAIL_LEGGINGS,
		Material.CHAINMAIL_CHESTPLATE,
		Material.CHAINMAIL_HELMET,
		
		Material.IRON_BOOTS,
		Material.IRON_LEGGINGS,
		Material.IRON_CHESTPLATE,
		Material.IRON_HELMET,
		
		Material.DIAMOND_BOOTS,
		Material.DIAMOND_LEGGINGS,
		Material.DIAMOND_CHESTPLATE,
		Material.DIAMOND_HELMET
		});
	
	public static String getCalendarDiff(Calendar calendar, Calendar compareTo){
		
		
		
		
		
		int years = compareTo.get(Calendar.YEAR)-calendar.get(Calendar.YEAR);
		int months = compareTo.get(Calendar.MONTH)-calendar.get(Calendar.MONTH);
		int weeks = compareTo.get(Calendar.WEEK_OF_MONTH)-calendar.get(Calendar.WEEK_OF_MONTH);
		int days = compareTo.get(Calendar.DAY_OF_WEEK)-calendar.get(Calendar.DAY_OF_WEEK);
		int hours = compareTo.get(Calendar.HOUR_OF_DAY)-calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = compareTo.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE);
		int seconds = compareTo.get(Calendar.SECOND)-calendar.get(Calendar.SECOND);
		
		if(seconds < 0){ minutes--; seconds = 60-Math.abs(seconds);}
		if(minutes < 0){hours--; minutes = 60-Math.abs(minutes);}
		if(hours < 0){ days--; hours = 24-Math.abs(hours);}
		if(days < 0) {weeks--; weeks = 7- Math.abs(weeks);}
		if(weeks < 0){ months--; months = 12-Math.abs(months);}
		if(months < 0) {years--;}
		
		String message = "";
		if(years != 0){
			message += Math.abs(years)+" years ";
		}
		if(months != 0){
			message += Math.abs(months)+" months ";
		}
		
		if(weeks != 0){
			message += Math.abs(weeks)+" weeks ";
		}
		if(days!= 0){
			message += Math.abs(days)+" days ";
		}
		if(hours != 0){
			message += Math.abs(hours)+" hours ";
		}
		if(minutes!= 0){
			message += Math.abs(minutes)+" minutes ";
		}
		if(seconds != 0){
			message += Math.abs(seconds)+" seconds ";
		}
		//
		return message;
	}
	
	public static OfflinePlayer getPlayer(String s){
		OfflinePlayer pl = Bukkit.getPlayer(s);
		if(pl == null){
			pl = Bukkit.getOfflinePlayer(s);
		}
		return pl;
	}
	public static <T> Set<T> list2set(List<T> e){
		Set<T> set = new HashSet<T>();
		for(T l : e){
			set.add(l);
		}
		return set;
	}
	public static <a> List<a> cleanseList(List<a> e){
		return new ArrayList<a>(list2set(e));
	}
	
	public static Player getAttacker(Entity entity){
		if(entity instanceof Player){
			return (Player)entity;
		}else{
			if(entity instanceof Projectile){
				if(((Projectile)entity).getShooter() instanceof Player){
					return (Player) ((Projectile)entity).getShooter();
					
				}else{
					return null;
				}
			}else{
				return null;
			}
		}
	}
	
/*	public static void cleansePlayer(Player player){
		User playerUser = User.getUser(player.getName());
		playerUser.changeKit(null);
		clearInv(player);
		player.setFireTicks(0);
		player.setHealth(20D);

		removePotionEffects(player);
	}
	*/
	public static double[] getPercentTier(int num)throws IllegalArgumentException{
	
		double[] per = new double[num];
			for(int i = num;i>0; i-- ){
				per[i-1]= (-Math.pow(i/(num+.5), 1D/3D)+1)*100;
			}
			return per;
		
	}
	public static int getTeirPercent(double[] per){
		Random ran = new Random();
		for(int i = per.length-1;i>=0;i--){
			int factor = (int) Math.ceil(per[i]);	
			if(ran.nextInt(100) <= factor){return i;}
		}
		return 0;

	}
	
	public static void cleansePlayer(Player ...plz){
		
		for(Player player : plz){
			cleansePlayer(player);
		}
	}
 	public static Inventory getFreeSoup(){
		Inventory inv  = Bukkit.createInventory(null, 54,"Free Soup");
		
		for(int i = 0; i < inv.getSize();i++){
			inv.setItem(i, Main.GLOBAL_SOUP);
		
		}
		return inv;
	}
 	
	public static String colorCode(String s){
		return s.replaceAll("&", "§");
		
	}
	public static String[] colorCode(String[] stringList){
		
		String[] list = new String[stringList.length]; 
	
		for(int i = 0; i < stringList.length; i++){
			list[i]= colorCode(stringList[i]);
		}
		return list;
	}
	public static void debug(Object error){
		for(User u : User.getUsersWithNotify(User.NotifyMode.DEBUG)){
			Messenger.send("&7&lDEBUG:&r"+error,(Player)u.getPlayer(),Messenger.INFO);
		}
		PLUGIN.getLogger().info(Utils.colorCode("DEBUG:"+error));
	}
	
	public static boolean isRightClicked(Action action){
		return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
	}
	
	public static boolean isLeftClicked(Action action){
		return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
	}
	

	
	public static void removePotionEffects(Player player){
		 for(PotionEffect e:  player.getActivePotionEffects()){
			 player.removePotionEffect(e.getType());
		 }
	}
	
	public static int getItemNumber(Inventory inv, ItemStack item){
		int amount = 0;
		for(ItemStack i: inv){
		if(item == null){
			if(i==item){
				amount++;
			}
			
		}else{
			if(i.equals(item)){
				amount++;
			}
			}
		}
		return amount;
		
	}
	
	public static void giveStartItems(final Player player){
		User user = User.getUser(player.getName());
		if(user.getGameMode() != User.GameMode.PLAYER)return;
		clearInv(player);
	//	broadcast("Giving starter items to "+ player.getName());
		new BukkitRunnable(){
			public void run(){
				player.getInventory().setItem(0,Main.BUYCRAFT);
				player.getInventory().setItem(2,Main.WARP_GUI);
			
				player.getInventory().setItem(4,Main.KIT_SELECTION);
			
				
				player.getInventory().setItem(6, Main.SPEC_ENTER);
				player.getInventory().setItem(8,Main.SHOP_SHORTCUT);
				
				player.updateInventory();
			}
		}.runTask(Utils.PLUGIN);
		
	}/**/
	
   public static void clearInv(Player pl){
    	pl.getInventory().setContents(null);
    	pl.getInventory().clear();
    
    }
    
    
    public static void repairInv(PlayerInventory pi){
    	for(ItemStack i: pi){
    		if(i!= null){
    			i.setDurability((short)0);
    			}
    	
    	}
    }
	public static List<String> matchString(Collection<? extends String> list, String input){
		
		List<String> output = new ArrayList<String>();
		if(list == null || input == null)return output;
		if(list.isEmpty())return output;
		for(String avalue:list){
			//debug("in Loop");
			if(avalue != null){
				String sub;
				if(avalue.length() > input.length()){sub = avalue.substring(0, input.length());}
				else  {sub = avalue;}
			
		
				if(sub.equalsIgnoreCase(input.toLowerCase())){
					output.add(avalue);
				}
			}
		}
		return output;
	}
	public static List<String> matchOfflinePlayers(String input){
		List<String> tabs = new ArrayList<String>();
		if(input == null)return tabs;
		for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
			tabs.add(p.getName());
		}
		return matchString(tabs, input.toLowerCase());
	}
	public static Set<Player> getOnlinePlayers(){
		Set<Player>  list = new HashSet<Player>();
		for(World w: Bukkit.getWorlds()){
			for(Player p: w.getPlayers()){
				list.add(p);
			}
		
		}
			
		return list;
	}
	public static List<String> matchOnlinePlayers(String input){
		List<String> tabs = new ArrayList<String>();
		if(input == null)return tabs;
			for(Player p : getOnlinePlayers()){
			tabs.add(p.getName());
		}
		return matchString(tabs, input.toLowerCase());
	}
	
	public static String listToString(List<String> list){
		
		String l = "Empty";
		if(list == null){
			return l;
		}
		if(list.isEmpty()){
			return l;
		}
		
		l = list.get(0);
		
		if(list.size() < 2){
			return l;
		}
		
		for(String s :list){
			l += ", "+s;
		}
		
		
		return l;
	}
	
	public static void broadcast(String s){
		Bukkit.broadcastMessage(colorCode("&5&o&lBroadCast&7>> &e"+s));
		
	}
	public static void broadcastCustom(String heading ,String message){
		Bukkit.broadcastMessage(colorCode("&5&o&l" + heading + "&7>> &e" + message));
		
	}
	public  static boolean isIntInList(int num,int...list){
		for(int i : list){
			if(num == i){
				return true;
			}
			
		}
		return false;
		
	}
	
	public static void giveSoup(Player reciever, boolean refills, boolean recraft){
		if(refills){
			while( Arrays.asList(reciever.getInventory().getContents()).contains(null)){
				reciever.getInventory().addItem(Main.GLOBAL_SOUP);
			}
		
			for(int i = 0; i< 8; i++){
				reciever.getInventory().addItem(Main.GLOBAL_SOUP);
			}
		}
		if(recraft){
			reciever.getInventory().setItem(reciever.getInventory().getSize()-1, new ItemStack( Material.RED_MUSHROOM,64));
			reciever.getInventory().setItem(reciever.getInventory().getSize()-2, new ItemStack(Material.BOWL,64));
			reciever.getInventory().setItem(reciever.getInventory().getSize()-3, new ItemStack(Material.BROWN_MUSHROOM,64));
		}
		
	}
	public static boolean NumberToBoolean(int i){
		return i == 1;
		
	}
	
	public static ItemStack[] getEmptyInv(int size){
		ItemStack[] iz = new ItemStack[size];
		for(int i = 0; i<size ;i++){
			iz[i] = null;
		}
		return iz;
	}
	public static String format(String s,Map<String,String> tree){
		String wow = s;	
		for(String toReplace: tree.keySet()){
				String replacement = tree.get(toReplace);
				wow.replaceAll(toReplace, replacement);
			}
			return wow;
	}
	
	public static void broadcastRank(String s,Rank rank){
		
		Messenger.sendGroup(s, Rank.getPlayersWithPemission(rank), Messenger.INFO);
	
	
}
public static boolean entityOnGround(Entity e){
	if(e == null)return true;
	return entityGetBlockBelow(e,0.1,0.9,.1).getType() != null;
	/*Block b;
	for(double i = 0; i >-1; i-=.1 ){
		b = e.getLocation().add(0, i, 0).getBlock();
		if(b.getType() != Material.AIR){
			return true;
		}
	}
	return false;*/

}

public static Block entityGetBlockBelow(Entity e,double minDist ,double maxDist, double interval){
	Block b;
	for(double i = minDist*-1; i >maxDist*-1; i-= interval ){
		b = e.getLocation().add(0, i, 0).getBlock();
		if(b.getType() != Material.AIR){
			return b;
		}
	}
	return null;

}

public static Block entityGetBlockBelow(Entity e){
	return entityGetBlockBelow(e,.1 ,.9, .1);
}


	public static int getBase10(double num){
		int i =1;
		//num = (double)num;
		for(; (num/Math.pow(10, i*3))>1;i++){}
		return i-1;
	}
	
	public static int getBase10(BigInteger num){
		int i =1;
		//num = (double)num;
		for(; (num.divide(BigInteger.valueOf((long) Math.pow(10, i*3)))).compareTo(BigInteger.valueOf(1))!=-1;i++){}
		return i-1;
	}
	public static double getBase10Value(double num){
		//num = (double)num;
		return num/Math.max(Math.pow(10, getBase10(num)*3), 1);
	}
	
	public static BigInteger getBase10Value(BigInteger num){
		//num = (double)num;
		return num.divide(BigInteger.valueOf((long) Math.max(Math.pow(10, getBase10(num)*3),1)));
	}
	public static String getAbrv(double num){
		//num = (double)num;
		//String abr[] = new String[] {"","K","M","B","T","Q","Qn","S","Sp","O","N","D","Ud","Dud","Trd","Qad","Qid","Sd","Spd"};
		return String.format("%.2f", getBase10Value(num))+"&6&l"+abr[getBase10(num)];
		
	}
	public static String getAbrv(BigInteger num){
		//num = (double)num;
		//String abr[] = new String[] {"","K","M","B","T","Q","Qn","H","Hp","O","N","DD"};
		return  getBase10Value(num)+"&6&l"+abr[getBase10(num)];
		
	}

	
	public static boolean validateParam(CommandSender sender, List<String> validArgs,String param){
		
		if(!validArgs.contains(param.toLowerCase())){
			Messenger.send("&4Invalid Param. &7Try "+Utils.listToString(validArgs), sender);
			return false;
		}
		return true;
	}
	
	public static boolean isInt(String e){
		try{
			Integer.parseInt(e);
			return true;
		}catch(NumberFormatException ex){
			return false;
			
		}
		
		
	}
	
	public static List<String> getLore(ItemMeta im){
	List<String> lore = im.getLore();
	if(lore == null){
		lore = new ArrayList<String>();
	}
	return lore;
	}
	public static void setupPerkValues(){
		Perk.VALUES.put(Perk.Type.CASHFLOW, new int[]{5,10,15});
		Perk.VALUES.put(Perk.Type.RECOVERY, new int[]{10,15,20});
		Perk.VALUES.put(Perk.Type.CUSHION, new int[]{50,75,100});
		Perk.VALUES.put(Perk.Type.PEIRCING, new int[]{7,15,23});
		Perk.VALUES.put(Perk.Type.STONE_SKIN, new int[]{7,15,23});
		Perk.VALUES.put(Perk.Type.HEALTH, new int[]{2,5,10});
		Perk.VALUES.put(Perk.Type.LUCK, new int[]{2,5,8});
		Perk.VALUES.put(Perk.Type.INSIDER, new int[]{5,15,25});

	}
}
