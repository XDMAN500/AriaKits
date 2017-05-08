package me.varmetek.kitserver.api;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.varmetek.kitserver.api.User.ProducerLevel;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
@SuppressWarnings("unchecked")
public class DataManager {
	//static FileConfiguration playerConfig = null;
	//static FileConfiguration warpConfig = null;
	
	private static Plugin plugin  = Main.getPluginInstance();
	private final static String WARP_DIR = "warps.yml";
	private final static String LOC_DIR = "loc.yml";
	private final static String CONFIG_DIR = "config.yml";
	public final static File WARP_LIST_FILE = new File(plugin.getDataFolder(), WARP_DIR);
	private final static File LOC_LIST_FILE = new File(plugin.getDataFolder(), LOC_DIR);
	private final static File CONFIG_FILE = new File(plugin.getDataFolder(), CONFIG_DIR);
	private final static File DATA_BASE_FILE= new File(plugin.getDataFolder(), "DataBase.yml");
	private final static File KIT_META_FILE = new File(plugin.getDataFolder(), "KMeta.yml");
	private final static File BAN_ENTRY_FILE = new File(plugin.getDataFolder(), "BanEntries.yml");
	private final static File RANK_FILE = new File(plugin.getDataFolder(), "ranks.yml");

	private static final File[] FILES = new File[] {WARP_LIST_FILE,LOC_LIST_FILE,CONFIG_FILE};
	public static void createFiles(){
	
		
	for(File f : FILES){
		if (!f.exists()) {
	            try {
	                f.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	             //   return null;
	            }
	            
	        }
		}
	}
	public static void setMotd(String s){
		File file = new File(plugin.getDataFolder(), "config.yml");
		  if (!file.exists()) {
	            try {
	                file.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	                return ;
	            }
	            
	        }
		 
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("motd", s);
		try {
			config.save(file);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public static String getMotd(){
		File file = new File(plugin.getDataFolder(), "config.yml");
		  if (!file.exists()) {
	           return "";
	            
	        }
		 
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		String s = config.getString("motd");
		if(s == null){
			return "";
		}else{
			return s;
		}
		
	}

	
	public static Warp getWarp(String s){
		if (!WARP_LIST_FILE.exists()) {
	        return null;
	       }
		return new Warp(s, getWarpIcon(s), getWarpChangeKit(s),getWarpKit(s));
	}
	
	
	public static void addDataBaseName(UUID u , String name){
		
		
		  if (!DATA_BASE_FILE.exists()) {
	            try {
	            	DATA_BASE_FILE.createNewFile();
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
		 
		  String path = u.toString()+".names";
		
		Set<String> names = getDataBaseEntryNames(u);
		names.add(name);

		data.set(path, new ArrayList<String>(names));
		

		
		try {
			data.save(DATA_BASE_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  
	}
	public static void addDataBaseIP(final UUID u , String ip){
		
		
		  if (!DATA_BASE_FILE.exists()) {
	            try {
	            	DATA_BASE_FILE.createNewFile();
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
		
		
		String path = u.toString()+".ips";
		Set<String> ips = getDataBaseEntryIps(u);
		ips.add(ip);

		data.set(path,new ArrayList<String>(ips));
		
		try {
			data.save(DATA_BASE_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  
	}
	public static Set<String> getDataBaseEntryIps(UUID u){
	
		  if (!DATA_BASE_FILE.exists()) {
	         return new HashSet<String>();
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
			
			 Set<String> ips =new HashSet<String>();
			String path = u.toString()+".ips";
			
			if(data.contains(path)){
				List<String> array = data.getStringList(path);
				if(array != null){
					for(String s : array){
						if(s != null){
							ips.add(s);
						}
					}	
				}
			}
		  return ips;
	}
	public static Set<String> getDataBaseEntryNames(UUID u){
		
		  if (!DATA_BASE_FILE.exists()) {
	         return new HashSet<String>();
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
		  Set<String> names =new HashSet<String>();
			
				

				
				String path = u.toString()+".names";
					
					if(data.contains(path)){
						List<String> array = data.getStringList(path);
						if(array != null){
							for(String s : array){
								if(s != null){
									names.add(s);
								}
							}	
						}
					}
		  return names;
	}
	
	public  static void checkForMatches(UUID u){

		  if (!DATA_BASE_FILE.exists()) {
		         return;
			  }
			  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
			 Set<String> entrys = data.getKeys(false);
			if(entrys.isEmpty())return;
			
			for(String f : entrys){
				UUID ud = UUID.fromString(f);
				
				for(String ip : getDataBaseEntryIps(ud)){
					if(getDataBaseEntryIps(u).contains(ip)){
					
						for(String toAdd : getDataBaseEntryIps(ud)){
							addDataBaseIP(u,toAdd);
						}
						for(String toAdd : getDataBaseEntryNames(ud)){
							addDataBaseName(u,toAdd);
						}
						break;
					}
				}
			}
	}
	
	public static void nukeUser(User user){
		
		if(user == null){return;}
		saveUserRank(user,user.getRank());
		String name = user.getUserID();
		
		String filename = user.getPlayerUUID().toString();
		//	Main.debug("Saving User:"+playerUser.getPlayer().getName());
			File myFile = new File(plugin.getDataFolder(), "/Players/"+filename+".yml");
		 
        if (myFile.exists()) {
         	myFile.delete();
         	
        }
        user.remove();
       user = User.getUser(name);
        loadUserRank(user);
        
        
	}
	
	public static Rank getKitRank(Kit k){
		Rank rank = Rank.Default;
		if(!KIT_META_FILE.exists()){
			setKitRank(k,rank);
			
		}
		String path = k.getName()+".rank";
		FileConfiguration meta = YamlConfiguration.loadConfiguration(KIT_META_FILE);
		if(meta.contains(path)){
			rank = Rank.getRankByName(meta.getString(path));
		}
		if(rank == null){
			rank = Rank.Default;
			setKitRank(k,rank);
		}
		return rank;
	}
	public static void setKitRank(Kit k,Rank rank){
		if(!KIT_META_FILE.exists()){
			try {
				KIT_META_FILE.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String path = k.getName()+".rank";
		FileConfiguration meta = YamlConfiguration.loadConfiguration(KIT_META_FILE);
		meta.set(path, rank.name());
		try {
			meta.save(KIT_META_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getKitPrice(Kit k){
		int cost = 0;
		if(!KIT_META_FILE.exists()){
			setKitPrice(k,cost);
			
		}
		String path = k.getName()+".price";
		FileConfiguration meta = YamlConfiguration.loadConfiguration(KIT_META_FILE);
		if(meta.contains(path)){
			if(meta.isInt(path)){
				cost = meta.getInt(path);
			}else{
				
				setKitPrice(k,cost);
			}
		}else{
			setKitPrice(k,cost);
		}
		
		return cost;
	}
	public static void setKitPrice(Kit k,int cost){
		if(!KIT_META_FILE.exists()){
			try {
				KIT_META_FILE.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String path = k.getName()+".price";
		FileConfiguration meta = YamlConfiguration.loadConfiguration(KIT_META_FILE);
		meta.set(path, cost);
		try {
			meta.save(KIT_META_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isKitEnabled(Kit k){
		boolean enabled = true;
		if(!KIT_META_FILE.exists()){
			setKitEnabled(k,enabled);
			
		}
		String path = k.getName()+".enabled";
		FileConfiguration meta = YamlConfiguration.loadConfiguration(KIT_META_FILE);
		if(meta.contains(path)){
			if(meta.isBoolean(path)){
				enabled  = meta.getBoolean(path);
			}else{
				
				setKitEnabled(k,enabled);
			}
		}else{
			setKitEnabled(k,enabled);
		}
		
		return enabled;
	}
	public static void setKitEnabled(Kit k, boolean enabled){	if(!KIT_META_FILE.exists()){
		try {
			KIT_META_FILE.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	String path = k.getName()+".enabled";
	FileConfiguration meta = YamlConfiguration.loadConfiguration(KIT_META_FILE);
	meta.set(path, enabled);
	try {
		meta.save(KIT_META_FILE);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}
	
 	public static void saveLocation(File file,String path,Location loc){
		 
		 if (!file.exists()) {
			 return;
		 
		 }
		 if(loc == null){
			 return;
		 }
		 FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		  config.set(path+".x", loc.getX());
		  config.set(path+".y", loc.getY());
		  config.set(path+".z", loc.getZ());
		  config.set(path+".pitch",loc.getPitch()+"f");
		  config.set(path+".yaw",loc.getYaw()+"f");
		  config.set(path+".world", loc.getWorld().getName());
			try {
				config.save(file);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	 }
	public static Location loadLocation(File file,String path){
		 
		 if (!file.exists()) {
			 return null;
		 
		 }
		 
		 FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if(config.isConfigurationSection(path)){
				return new Location(
					Bukkit.getWorld(config.getString(path+".world")), 			
						
					config.getDouble(path+".x"),
					config.getDouble(path+".y"),
					config.getDouble(path+".z"), 
					Float.parseFloat((String)config.get(path+".yaw")),
					Float.parseFloat((String)config.get(path+".pitch"))
								   
						);
						
					
		}
			return null;
	 }
	 

	public static void loadArenas(){
		 File file = new File(plugin.getDataFolder(), "arenas.yml");
		 FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		 if(config.getKeys(false).isEmpty())return;
		 for(String arena : config.getKeys(false)){
			 	loadArena(Arena.getArea(arena, true));
		 }
	 }
	public static void saveArenas(){
		 if(Arena.getArenas().isEmpty())return;
		 
		 for(Arena a: Arena.getArenas()){
			 saveArena(a);
		 }
		
	 }

	public static void saveArena(Arena a){
		File file = new File(plugin.getDataFolder(), "arenas.yml");
		  if (!file.exists()) {
	            try {
	                file.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	                return ;
	            }
	            
	        }
		saveLocation(file,a.getName()+".pos1",a.getPosition1());
		saveLocation(file,a.getName()+".pos2",a.getPosition2());
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set(a.getName()+".enabled", a.isEnabled());
		try {
			config.save(file);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	public static void removeArea(Arena a){
		File file = new File(plugin.getDataFolder(), "arenas.yml");
		  if (!file.exists()) {
	        return;
	       }
		  FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		  config.set(a.getName(), null);
		  try {
			config.save(file);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	public static void loadArena(Arena a){
		File file = new File(plugin.getDataFolder(), "arenas.yml");
		  if (!file.exists()) {
	        return;
	       }
		a.setPosition1(loadLocation(file,a.getName()+".pos1"));
		a.setPosition2(loadLocation(file,a.getName()+".pos2"));
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		a.setEnabled(config.getBoolean((a.getName()+".enabled")));
	
		
	}
	
	//Bans
	public static void addBan(String value, boolean ip ){
		String path =""; 
		if(value.isEmpty() || value == null)return;
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
			  List<String> l = (List<String>) banList.get(path);
			
			  if(l == null){
				  l = new ArrayList<String>();
			  }
			  l.add(value);
			  banList.set(path, l);  
		 
		
		 
		  try {
			banList.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		  
	}
	public static void removeBan(String value, boolean ip ){
		String path =""; 
		if(value.isEmpty() || value == null)return;
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	                return;
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
			
		  
		  List<String> l = (List<String>) banList.get(path);
			  if(l == null){
				 return;
			  }
			 if(!l.contains(value)){
				 return;
			 }
			
			  l.remove(l.indexOf(value));
			  
			  
			  banList.set(path, l);  
		  
		
		 
		  try {
			banList.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		  
	}
	public static List<String> getBanList( boolean ip ){
		String path =""; 
		
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	                return null;
	            } catch (IOException e) {
	                e.printStackTrace();
	                return null;
	            }
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
			
		 
		  
			  if((List<String>) banList.get(path) == null){
				 return null;
			  }
			  return (List<String>) banList.get(path);

	}
	public static boolean isBanned(String value ,boolean ip ){
		if(value.isEmpty() || value == null)return false;
		List<String> l = getBanList(ip);
		if(l == null)
			return false;
		
		if(l.contains(value)){
			return true;
		}
		return false;
	}
	public static boolean isBanListExist(boolean ip){
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	           return false;
		  }
		  String path = "";
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  return banList.contains(path);
	}
	//Warps
	public static Location getWarpLoc(String warp){
		
		return loadLocation(WARP_LIST_FILE,warp);
	}
	public static Set<String> getWarpList(){
		
		File myFile = new File(plugin.getDataFolder(), WARP_DIR);
		if (myFile.exists()) {
			FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
			if(warpConfig.getConfigurationSection("").getKeys(false)== null){return null;}
			if(warpConfig.getConfigurationSection("").getKeys(false).size() == 0){return null;}
				
			
			return warpConfig.getConfigurationSection("").getKeys(false);
				 
		}
		return null;
	}
	
	public static Set<Warp> getShopWarps(){
		Set<Warp> set = new HashSet<Warp>();
		for(String s: getWarpList()){
			Warp warp = getWarp(s);
			if(warp.getLocation() != null && warp.getIcon() != null && warp.getIcon() != Material.AIR){
				set.add(warp);
				
			}
			
		}
		return set;
	}
	public static boolean doesWarpExist(String warp){
		return getWarpList().contains(warp);
	}
	public static boolean setWarp(String warp,Location loc){

		  createFiles();
		  saveLocation(WARP_LIST_FILE,warp,loc);
		  return true;
	}
	public static void  setWarpKit(String warp, Kit kit){
	
		  if (!WARP_LIST_FILE.exists()) {
	           return ;
		  }
		
		 FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		 if(kit == null){
			 warpConfig.set(warp+".kit", kit);
		 }else{
		 warpConfig.set(warp+".kit", kit.name());
		 }
		 
		 try {
			warpConfig.save(WARP_LIST_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void  setWarpChangeKit(String warp, boolean can){
		
		  if (!WARP_LIST_FILE.exists()) {
	           return ;
		  }
		
		 FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		 warpConfig.set(warp+".change", can);
		 
		 try {
			warpConfig.save(WARP_LIST_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Kit getWarpKit(String warp){
		if (!WARP_LIST_FILE.exists()) {
	           return null;
		  }
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		if(!warpConfig.contains(warp+".kit"))return null;
		
	
			Kit kit= Kit.getKitByName(warpConfig.getString(warp+".kit"));
			if(kit == null){
				setWarpKit(warp,  kit);
			}
			return kit;
		
	}
	public static boolean getWarpChangeKit(String warp){
		if (!WARP_LIST_FILE.exists()) {
	           return true;
		  }
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		if(!warpConfig.contains(warp+".change"))return true;
		
	
			boolean can = warpConfig.getBoolean(warp+".change", true);
			
			return can;
		
	}
 	public static boolean removeWarp(String warp){
		File myFile = new File(plugin.getDataFolder(), WARP_DIR);
		  if (myFile.exists()) {
			  FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
			  warpConfig.set(warp, null);
			  try {
				warpConfig.save(myFile);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			  return true;
	            
	        }
		 return false;
		//User
	}
	public static Material getWarpIcon(String warp){
		Material mat = null;
		if (!WARP_LIST_FILE.exists()) {
	           return mat;
		  }
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		if(!warpConfig.contains(warp+".icon"))return mat;
		
		try{
		mat = Material.valueOf(warpConfig.getString(warp+".icon"));	
		}catch(NumberFormatException e){
			 warpConfig.set(warp+".icon", mat);
		}
			
			return mat;
	}
	
	public static void setWarpIcon(String warp, Material mat){

		  if (!WARP_LIST_FILE.exists()) {
	           return ;
		  }
		
		 FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		 if(mat == null){
			 warpConfig.set(warp+".icon", mat);
		 }else{
			 warpConfig.set(warp+".icon", mat.name());
		 }
		 
		 try {
			warpConfig.save(WARP_LIST_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadUserRank(User user){
	//	File myFile = new File(plugin.getDataFolder(), "/Players/" + user.getPlayerUUID() + ".yml");
		 FileConfiguration config;
		 Rank rank;
		 if (RANK_FILE.exists()) {
			 config = YamlConfiguration.loadConfiguration(RANK_FILE);
			 
	         	if(config.contains(user.getPlayerUUID().toString()));{
	         		rank = Rank.getRankByName(config.getString(user.getPlayerUUID().toString()));
	         		if(rank ==  null){
	         			
	         			saveUserRank(user,rank);
	         			rank = Rank.Default;
	         		}
	         	
	         	}
	        }else{
	        	rank = Rank.Default;
	        	/*if(myFile.exists()){
	        		config = YamlConfiguration.loadConfiguration(myFile);
	        		  rank = Rank.getRankByName(config.getString("rank"));
	        		 if(rank ==  null){
		         			rank =Rank.Default;
		         		}
	        			
	        	}else{
	        		rank =Rank.Default;

	        	}*/
	        }
		 user.setRank(rank);
	///	 return Rank.Default;
	}
	public static void saveUserRank(User user, Rank rank){
		//File myFile = new File(plugin.getDataFolder(), "/Players/" + user.getPlayerUUID() + ".yml");
		//if(rank == Rank.Default){}
		 FileConfiguration config;
		 if (RANK_FILE.exists()) {
			 config = YamlConfiguration.loadConfiguration(RANK_FILE);
			 if(rank == null){
				 config.set(user.getPlayerUUID().toString(), rank);
			 }else{
				 if(rank == Rank.Default){
					 
					 config.set(user.getPlayerUUID().toString(), null);
				 }else{
					 config.set(user.getPlayerUUID().toString(), rank.name());
				 }
			 }
			 
			 try {
				config.save(RANK_FILE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
	       }else{
	    	   try {
	    		   RANK_FILE.createNewFile();
	    	   } catch (IOException e1) {
				// TODO Auto-generated catch block
	    		   e1.printStackTrace();
	    	   }
	        	/*if(myFile.exists()){
	        		config = YamlConfiguration.loadConfiguration(myFile);
	        		config.set("rank", rank.name());
	        		try {
						config.save(myFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}*/
	        }
		 
	}
	
	
	//User
	public static  boolean loadUser(User user){
	
	
		File myFile = new File(plugin.getDataFolder(), "/Players/" + user.getPlayerUUID() + ".yml");
		//Utils.debug("LoadingUser:"+user.getPlayer().getName());
	        if (!myFile.exists()) {
	           return false;
	        }
	        FileConfiguration config = YamlConfiguration.loadConfiguration(myFile);
	
	      user.setEXP(config.getLong("exp"));
	      user.setLevel(config.getInt("lvl"));
	      user.setKills(config.getInt("kills")); 
	      user.setDeaths(config.getInt("deaths")); 
	      user.setKillstreak(config.getInt("killstreak")); 
	      user.setMoney(config.getInt("money"));
	   
	      loadUserRank(user);
	   
	      user.setOwnedPerks(config.getStringList("ownedperks"));
	      user.setBanMessage(config.getString("bannedmessage"));
	  
	      user.setProdLvL(config.getString("pLvL") ==null?ProducerLevel.NONE:ProducerLevel.valueOf(config.getString("pLvL")));
	      {
	    	  String j = config.get("bannedexpire")+"";
	      
	    	  if(j.isEmpty()){
	    		  user.setBanExpire(Calendar.getInstance() );
	    	  	config.set("bannedexpire", "0l");
	    	  }else{
	    	 
	    		  if(j.equalsIgnoreCase("E")){
	    			  user.setBanExpire(null);
	    		  }else{
	    			  Long time = 0l;
	    			  try{
	    				  time = Long.parseLong(j);
	    			  }catch(NumberFormatException e){
	    				  user.setBanExpire(Calendar.getInstance());
	    				  config.set("bannedexpire", "0l");
	    			  
	    		  		}	
	    	  
	    		  		Calendar c = Calendar.getInstance();
	    		  	c.setTimeInMillis(time );
	      		
	    		  	user.setBanExpire(c );
	    		  }
	    	  }
	      }
	      
	      {
	    	  String j = config.get("mutetime")+"";
	      
	    	  if(j.isEmpty()){
	    		  Bukkit.broadcastMessage("EMPTY");
	    		  user.setMuteExpire(Calendar.getInstance() );
	    	  	config.set("mutetime", "0l");
	    	  }else{
	    	 // Calendar c; //c.
	    		  if(j.equalsIgnoreCase("E")){
	    			  user.setMuteExpire(null);
	    		  }else{
	    		  Long time = 0l;
	    		  try{
	    			  time = Long.parseLong(j);
	    		  }catch(NumberFormatException e){
	    			  Bukkit.broadcastMessage("Error");
	    			  user.setMuteExpire(Calendar.getInstance());
	    			  config.set("mutetime", "0l");
	    			  
	    		  	}
	    	  
	    		  Calendar c = Calendar.getInstance();
	    		  c.setTimeInMillis(time );
	      		
	    		  user.setMuteExpire(c );
	    		  }
	    	  }
	      }
	      
	      String s = config.getString("ownedkits");
	      for(Kit k : Kit.getAllKits()){
	    	  
	    		  if(s.contains(k.name())){
	    			  user.addOwnedKit(k);
	    		  }
	    	  
	      }
	    	  
	      
	     // Set<?> l= (Sets.newHashSet(config.getList("ownedkits")));
	      //user.setOwnedKits( e);//config.getInt("kills"));config.set("ownedkits", new ArrayList<Kit>(user.getOwnedKits(false)));
	     
	      return true;
	}
	public static void savePlayer(OfflinePlayer p){
		savePlayerUser(User.getUser(p.getName()));
	}	
	public static void savePlayerUser(User playerUser){
		String filename = playerUser.getPlayerUUID().toString();
	//	Main.debug("Saving User:"+playerUser.getPlayer().getName());
		File myFile = new File(plugin.getDataFolder(), "/Players/"+filename+".yml");
		 
	        if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(myFile);
	      
	    ;
	      
	      playerConfig.set("kills", playerUser.getKills());
	      playerConfig.set("deaths", playerUser.getDeaths());
	      playerConfig.set("killstreak", playerUser.getKillstreak());
	      playerConfig.set("money", playerUser.getMoney());
	      playerConfig.set("exp", playerUser.getEXP());
	      playerConfig.set("lvl", playerUser.getLevel());
	      saveUserRank(playerUser,playerUser.getRank());
	      playerConfig.set("ownedkits", new ArrayList<Kit>(playerUser.getBoughtKits()).toString());
	      playerConfig.set("bannedmessage", playerUser.getBanMessage());
	      playerConfig.set("ownedperks", playerUser.getOwnedPerks());
	      playerConfig.set("pLvL", playerUser.getProdLvL().name());
	      if(playerUser.getBanExpire() == null){
	    	  playerConfig.set("bannedexpire", "E");
	      }else{
	    	  playerConfig.set("bannedexpire",playerUser.getBanExpire().getTime().getTime()+"");
	      }
	      playerConfig.set("mutetime", playerUser.getMuteExpire().getTimeInMillis()+"");
	      try {
			playerConfig.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	     
		
	}
	public static void saveAllUsers(){
		
		for(User user: User.users.values()){
			DataManager.savePlayerUser(user);
		
		
		}
		
	}
	@SuppressWarnings("deprecation")
	public  static void saveAll(){
		for(Player p :Bukkit.getOnlinePlayers()){
			savePlayer(p);
		}
	}
	@SuppressWarnings("deprecation")
	public  static void loadAll(){
		for(Player p :Bukkit.getOnlinePlayers()){
			loadUser(User.getUser(p.getName()));
		}
	}
@SuppressWarnings("deprecation")
	public static void reloadUsers(){
		for(Player p :Bukkit.getOnlinePlayers()){
			savePlayer(p);
			loadUser(User.getUser(p.getName()));
		}
	}

	public static Location getLoc(String warp){
	
	/*File myFile = new File(plugin.getDataFolder(), LOC_DIR);
	if (myFile.exists()) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
		
		if(warpConfig.getKeys(false).contains(warp))
			return new Location( 
					
					Bukkit.getWorld((String)warpConfig.get(warp+".world")), 			
					
					(double)warpConfig.get(warp+".x"),
					(double)warpConfig.get(warp+".y"),
				    (double)warpConfig.get(warp+".z"), 
					Float.parseFloat((String) warpConfig.get(warp+".yaw")),
					Float.parseFloat((String)warpConfig.get(warp+".pitch"))
							   
								);
	}
	return null;*/
		return loadLocation(LOC_LIST_FILE,warp);
}
	public static Set<String> getLocList(){
	
	File myFile = new File(plugin.getDataFolder(), LOC_DIR);
	if (myFile.exists()) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
		if(warpConfig.getConfigurationSection("").getKeys(false)== null){Utils.debug("No list");return null;}
		if(warpConfig.getConfigurationSection("").getKeys(false).size() == 0) {Utils.debug("No locs");return null;}
			
		
		return warpConfig.getConfigurationSection("").getKeys(false);
			 
	}
	return null;
}
	public static boolean doesLocExist(String warp){
	return getLocList().contains(warp);
}
	public static boolean setLoc(String warp,Location loc){
	
	File myFile = new File(plugin.getDataFolder(), LOC_DIR);
	  if (!myFile.exists()) {
            try {
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            
        }
	  FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);

	  warpConfig.set(warp+".x", loc.getX());
	  warpConfig.set(warp+".y", loc.getY()+1);
	  warpConfig.set(warp+".z", loc.getZ());
	  warpConfig.set(warp+".pitch",loc.getPitch()+"f");
	  warpConfig.set(warp+".yaw",loc.getYaw()+"f");
	  warpConfig.set(warp+".world", loc.getWorld().getName());
	 
	  try {
		warpConfig.save(myFile);
	} catch (IOException e) {
		
		e.printStackTrace();
		return false;
	}
	 // Bukkit.broadcastMessage("WARP CREATED");
	  return true;
}
	public static boolean removeLoc(String loc){
	File myFile = new File(plugin.getDataFolder(), "loc.yml");
	  if (myFile.exists()) {
		  FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
		  warpConfig.set(loc, null);
		  try {
			warpConfig.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		  return true;
            
        }
	 return false;
	//User
}

	

	public static void addBanEntry(BanEntry eb){
		if(!BAN_ENTRY_FILE.exists()){
			try {
				BAN_ENTRY_FILE.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileConfiguration config =  YamlConfiguration.loadConfiguration(BAN_ENTRY_FILE); 
		List<String> list = config.getStringList("bans");
		if(list == null){
			list = new ArrayList<String>();
			list.add(eb.toString());
			return;
		}
		list.add(eb.toString());
		config.set("bans", Utils.cleanseList(list));
		try {
			config.save(BAN_ENTRY_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<String> getRawBanList(){
		List<String> list;
		
		
		list = new ArrayList<String>();
		if(!BAN_ENTRY_FILE.exists()){
			return list;
		}
		
		FileConfiguration config =  YamlConfiguration.loadConfiguration(BAN_ENTRY_FILE); 
		list = config.getStringList("bans");
		if(list == null){
			list = new ArrayList<String>();
			
		}
		return Utils.cleanseList(list);
		
		
	}
	public static Set<BanEntry> getBanEntryList(){
		Set<BanEntry> set = new HashSet<BanEntry>();
		if(getRawBanList().isEmpty())return set;
		
		for(String s : getRawBanList()){
			String[] parts = s.split("|");
			if(parts.length == 3){
				set.add(new BanEntry(UUID.fromString(parts[0]),Date.valueOf(parts[1]),parts[2]  ) );
			}
		}
		return set;
		
	}
	public synchronized static List<String> getBanTab(){
		final Set<String> set = new HashSet<String>();
		new Thread(){
			public void run(){
				for(BanEntry eb : getBanEntryList()){
					set.add(Bukkit.getOfflinePlayer(eb.getUUID()).getName());
				}
				
			}
		};
		return new ArrayList<String>(set);
	}
	
}
