package me.varmetek.kitserver.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Perk {
	private int level;
	private Type type;
	
	public enum Type
	{
		LUCK("Luck",Material.BEACON,//
				"&7 2% More Gamble Boost",
				"&7 5% Gamble Boost",
				"&7 8% Gamble Boost"),
		HEALTH("Health",Material.REDSTONE_BLOCK,
				"&7 +2 Hearts",
				"&7 +5 Hearts",
				"&7 +10 Hearts"),
		STONE_SKIN("Stone Skin",Material.STONE,//
				"&7 Take 7% Less Damage",
				"&7 Take 15% Less Damage",
				"&7 Take 23% Less Damage"),
		PEIRCING("Peircing",Material.REDSTONE,//
				"&7 Give 7% More Damage",
				"&7 Give 15% More Damage",
				"&7 Give 23% More Damage"),
		CASHFLOW("Cash Flow",Material.EMERALD,//
				"&7 5% More Cash",
				"&7 10% More Cash",
				"&7 15% More Cash"),
		RECOVERY("Recovery",Material.ARROW,//
				"&7 10% Smaller Cooldowns",
				"&7 15% Smaller Cooldowns",
				"&7 20% Smaller Cooldowns"),
		CUSHION("Cushion",Material.BREAD,//
				"&7 50% Less Fall Damage",
				"&7 75% Less Falldamage",
				"&7 No Falldamage"),	
		INSIDER("Insider",Material.DIAMOND,//
				"&7 5% Less Expenses",
				"&7 15% Less Expenses",
				"&7 25% Less Expenses");
		/*
		 * 		LUCK("Luck",Material.BEACON,"&2T1:&7 2% more Gamble Boost","&4T2:&7 5% Gamble Boost","&6T3:&7 8% Gamble Boost"),
		HEALTH("Health",Material.REDSTONE_BLOCK,"&2T1:&7 +2 hearts","&4T2:&7 +5 hearts","&6T3:&7 +10 hearts"),
		STONE_SKIN("Stone Skin",Material.STONE,"&2T1:&7 take 7% less damage","&4T2:&7 take 15% less damage","&6T3:&7 take 23% less damage"),
		PEIRCING("Peircing",Material.IRON_SWORD,"&2T1:&7 give 7% more damage","&4T2:&7 give 15% more damage","&6T3:&7 give 23% more damage"),
		CASHFLOW("Cash Flow",Material.EMERALD,"&2T1:&7 5% more cash","&4T2:&7 10% more cash","&6T3:&7 15% more cash"),
		RECOVERY("Recovery",Material.ARROW,"&2T1:&7 10% smaller cooldowns","&4T2:&7 15% smaller cooldowns","&6T3:&7 20% smaller cooldowns"),
		CUSHION("Cushion",Material.CAKE,"&2T1:&7 50% less fal damage","&4T2:&7 75% less falldamage","&6T3:&7 No falldamage");*/
		private String name;
		private Material icon;
		private String[] desc;
		private Type(String name, Material mat,String ... desc){
			 this.name = name;
			 icon = mat;
			
			 
			 this.desc = Utils.colorCode(desc);
		 }
		
		public String getName(){
			return name;
		}
		public Material getItem(){
			return icon;
		}
		public String[] getDesc(){
			return desc;
		}
		
		
	}
	public Type getType(){
		return type;
	}
	
	public final static Map<Type,int[]> VALUES = new HashMap<Type,int[]>();
	
	
 	public int getLevel(){
		return level;
	}
	public ItemStack getIcon(){
		ItemStack item = new ItemStack(type.getItem());
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(Utils.colorCode("&" + (level*2)+"&l"+type.name +"&8&l-"+"&9&lT"+level ));
		List<String> lore = Utils.getLore(im);
		lore.add(Utils.colorCode(type.desc[level-1]));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
		
	}
	public static Perk getFromItem(ItemStack item){
		if(item == null)return null;
		if(item.getItemMeta() == null)return null;
		if(!item.getItemMeta().hasDisplayName())return null;
		String title = item.getItemMeta().getDisplayName();
		String number = title.substring(title.length()-1, title.length());
	//	Utils.debug(number);
		if(!Utils.isInt(number))return null;
		return new Perk(Perk.getByName(ChatColor.stripColor(title.split("-")[0])),Integer.parseInt(number));
		
		
	}

	public Perk(Type type,int level){
		this.type = type;
		this.level = level;
	}

	public static double[] getPercentTier(int num){
		
		double[] per = new double[3];
			for(int i =1;i<4; i++ ){
				per[i-1]=  (-Math.pow(i/(3D+num), 1D/(8D-num))+1)*100;
			}
			return per;
			
		
	}
	
	public static Type getByName(String name){
		for(Type p: Type.values()){
			if(p.name().equalsIgnoreCase(name) || p.getName().equalsIgnoreCase(name)){
				return p;
			}
		}
		return null;
	}
	
	public static int[] getPrices(){
		int[] per = new int[6];
		for(int i =1;i<7; i++ ){
			per[i-1]=  (int) (Math.pow(.23,-i)+i*500);
		}
		return per;
	}
	
	public String toString(){
		return type.name()
				+":"+level;
	}
	public static Perk parse(String ee){
		if(!ee.contains(":"))return null;
		String[] a = ee.split(":");
		return new Perk(Perk.getByName(a[0]),Integer.parseInt(a[1]));
			
		
	}
	public static Perk getRandom(int level, int bonus){
		double[] per = getPercentTier(level);
		Random ran = new Random();
		Perk omg = null;
		Type perk;
		for(int i = 3;i>0; i--){
			perk = Type.values()[ran.nextInt(Type.values().length)];
			if(ran.nextInt(100)<per[i-1]+bonus){
				omg = new Perk(perk,i);
				break;
			}
		}
		return omg;
		
	}
	
	
}
