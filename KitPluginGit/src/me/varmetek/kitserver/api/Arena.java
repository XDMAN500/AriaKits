package me.varmetek.kitserver.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;

public class Arena {
	private static Map<String,Arena> arenaTree = new HashMap<String,Arena>();
	
	
	private Location position1;
	private Location position2;
	private boolean enabled;
	private String name;
	
	private Arena(String s){
		name = s;
		
	}
	
	public static Arena getArea(String name ,boolean ignoreNull){
		if(arenaTree.get(name) != null){
			return arenaTree.get(name);
			
		}
		if(ignoreNull){
			Arena newArena = new Arena(name);
			newArena.enabled = true;
			newArena.position1 = null;
			newArena.position2 = null;
			arenaTree.put(name, newArena);
			return newArena;
		}
		return null;
		
	
	}
	
	
	public static Set<Arena> getAvaliableArenas(){
		if(getArenas().isEmpty()){
			return null;
		}
		Set<Arena> list = new HashSet<Arena>();
		for(Arena a : getArenas()){
			if(a.isAvaliable()){
				list.add(a);
			}
		}
		return list;
	}
	
	public static Arena getRandomArena(){
		Random ran = new Random();
		if(getAvaliableArenas().isEmpty()){
			return null;
		}
		List<Arena> list = new ArrayList<Arena>(getAvaliableArenas());
		return list.get(ran.nextInt(getAvaliableArenas().size()));
		
		
	}
	public static Collection<? extends Arena> getArenas(){
		return arenaTree.values();
	}
	/////////////////////////////////////////////////////////////
	public boolean isEnabled(){
		return enabled;
	}
	public Location getPosition1(){
		return position1;
	}
	public Location getPosition2(){
		return position2;
	}
	
	public boolean isAvaliable(){
		return (enabled && (position1 != null && position2 != null));
	}
	
	public String getName(){
		return name;
	}
	
	public Arena setPosition1(Location loc){
		position1 = loc;
		return this;
		
	}
	public Arena setPosition2(Location loc){
		position2 = loc;
		return this;	
	}
	public Arena setPositions(Location loc1,Location loc2){
		setPosition1(loc1).setPosition2(loc2);
		return this;
		
	}
	
	public Arena setEnabled(boolean enable){
		enabled = enable;
		return this;
	}
	
	
	public void remove(){
		DataManager.removeArea(this);
		arenaTree.remove(name);
	}
	

	
	
}
