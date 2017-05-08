package me.varmetek.kitserver.api;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.varmetek.kitserver.main.Main;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Wool;

public enum Kit {
	PVP("PvP",Material.DIAMOND_SWORD,null),
	ARCHER("Archer",Material.ARROW,Material.BOW),
	ENCHANTER("Enchanter",Material.ENCHANTED_BOOK,Material.ENCHANTED_BOOK),
	KANGAROO("Kangaroo",Material.FIREWORK,Material.FIREWORK),
	SNIPER("Sniper",Material.DIAMOND_BARDING,Material.BOW),
	LAUNCHER("Launcher",Material.ENDER_PEARL,Material.ENDER_PEARL),
	STOMPER("Stomper",Material.ANVIL,null),
	CACTUS("Cactus",Material.CACTUS,null),
	PYRO("Pyro",Material.BLAZE_POWDER,Material.BLAZE_POWDER),
	GRAPPLER("Grappler",Material.LEASH,Material.FISHING_ROD),
	SWITCHER("Switcher",Material.FIREWORK_CHARGE,Material.FIREWORK_CHARGE),
	FISHERMAN("FisherMan",Material.FISHING_ROD,Material.FISHING_ROD),
	FISH("Fish",Material.COOKED_FISH,null),
	BERZERK( "Berzerk", Material.COAL, null),
	VIPER( "Viper", Material.SLIME_BALL, Material.SLIME_BALL),
	WITHER("Wither", Material.SKULL_ITEM, Material.STONE_HOE),
	MUCKRAKER("MuckRaker", Material.DIRT, Material.WOOD_HOE),
	ENDER("Ender", Material.EYE_OF_ENDER, Material.EYE_OF_ENDER),
	THOR("Thor", Material.WOOD_AXE, Material.WOOD_AXE),
	TURTLE("Turtle", Material.DIAMOND_CHESTPLATE, null),
	ANCHOR("Anchor", Material.BEDROCK, null),
	SCOUT("Scout", Material.GOLD_RECORD, null),
	
	;
	
	
	public enum Type {
		DEFAULT("&7"),
		NONDONOR("&3"),
		DONOR("&5&l")
		;
		String colorCode;
		private Type(String cc){
			colorCode = cc;
		}
		public String getColorCode(){
			return colorCode;
		}
	}
	
	 private String name;
	 private Material icon;
	 private Material activater;
	 
	 private Kit(String name,Material icon,Material a){
		//this.enabled = enabled;
	//	this.price = price;
	///	this.requiredRank = requiredRank;
		this.name = name;
		this.icon = icon;
		this.activater = a;
	}
	 
	
	 
	 
	  public  boolean isEnabled(){
		
		  return DataManager.isKitEnabled(this);
		  
	  }
	  
	  public int getPrice(){
		  return DataManager.getKitPrice(this);
	  }
	  
	  public Rank getRequiredRank(){
		 
		  return DataManager.getKitRank(this);
	  }
	  
	  public String getName(){
		  return name;
	  }
	  
	  public Material getIcon(){
		  return icon;
	  }
	
	  public  boolean isDonorKit(){
		  if(getRequiredRank() == Rank.Default){
			  return false;
		  }
		  return true;
	  }
	  
	  public void getMaterials(Player reciever ){
		  
		  getKitMaterials(reciever,this,true,false,false);
			
		}
	  public void getMaterials(Player reciever,boolean force ){
		  
		  getKitMaterials(reciever,this,true,false,force);
			
		}
	
	  public Type getType(){
		  if(getRequiredRank() == Rank.Default){
			  if(getPrice() == 0){return Type.DEFAULT; }else{ return Type.NONDONOR;}
		  }else{
			  return Type.DONOR;
		  }
	  }
	  
	  
	 
	 public ItemStack getItem(){
			ItemStack item = new ItemStack(getIcon());
			ItemMeta itemM = item.getItemMeta();
			itemM.setDisplayName(Utils.colorCode(getType().getColorCode()+getName()));
			item.setItemMeta(itemM);
			return item;
	 }
	  //CLassss variables
	 
	  public static Type getKitType(Kit kit){
		return kit.getType();
	  }
	 
	  public static boolean getKitEnabled(Kit kit){
		  return kit.isEnabled();
	  }
	  
	  public static int getKitPrice(Kit kit){
		  return kit.getPrice();
	  }
	  
	  public static Rank getKitRequiredRank(Kit kit){
		 
		  return kit.getRequiredRank();
	  }
	  
	  public static String getKitName(Kit kit){
		  return kit.name;
	  }
	  
	  public static Material getKitIcon(Kit kit){
		  return kit.icon;
	  }
	
	  public static boolean isKitDonorKit(Kit kit){
		return kit.isDonorKit();
	  }
	  

	  
	@SuppressWarnings("unused")
	public static void getKitMaterials(Player reciever , Kit kit,boolean refills, boolean recraft, boolean forced){
			if(!forced && !kit.isEnabled()){
				
					return;
				
			}
		 //Kit kit = Kit.getKitByName(args[0]);
		if(kit == null)return;
		reciever.getInventory().setArmorContents(new ItemStack[]{});
		reciever.getInventory().setContents(new ItemStack[]{});
			User playerUser  =  User.getUser(reciever.getName());
			
			//Set<ItemStack> content = new HashSet<ItemStack>();
			ItemStack sword = null; 
			ItemMeta swordMeta = null;
		
			
			ItemStack head = null;
			 LeatherArmorMeta headMeta= null;
		
			
			ItemStack chest = null;
			 LeatherArmorMeta chestMeta = null;
					
			ItemStack legs = null;
			 LeatherArmorMeta legsMeta = null;
			
			
			ItemStack feet = null;
			 LeatherArmorMeta feetMeta = null;
		
			ItemStack ability;
		
			if(kit.getActivater() == null)
		
				ability = null;
			else{
				ability = new ItemStack(kit.getActivater());
			}
			ItemMeta abilityMeta = null;
		
			
			
			PlayerInventory inv = reciever.getInventory();
			switch(kit){
			 case ARCHER:  
				 
				 sword = new ItemStack(Material.STONE_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lArcher's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				
				 
				 head = new ItemStack(Material.LEATHER_HELMET);
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				 legs = new ItemStack(Material.IRON_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 
				
				 abilityMeta = ability.getItemMeta();
				 abilityMeta.addEnchant(Enchantment.ARROW_INFINITE, 1,false);
				 abilityMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3, false);
				// abilityMeta.setDisplayName(Main.colorCode("&r&lArcher's bow"));
				
				 
				 inv.setItem(inv.getSize()-1, new ItemStack(Material.ARROW,1));
				 
				 
				 break; 
			
			 case PVP: 
				 sword = new ItemStack(Material.DIAMOND_SWORD);
				 swordMeta = sword.getItemMeta();
				// swordMeta.setDisplayName("&f&lPVP's sword");
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
		
				 
				 head = new ItemStack(Material.IRON_HELMET);
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				 legs = new ItemStack(Material.IRON_LEGGINGS);
				 feet = new ItemStack(Material.IRON_BOOTS);

				 break;
			
			 case ENCHANTER: 
				 sword = new ItemStack(Material.DIAMOND_SWORD);
				 swordMeta = sword.getItemMeta();
				 swordMeta.setDisplayName("TACOOO");
				// swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				 
				 
				 head = new ItemStack(Material.CHAINMAIL_HELMET);
				 chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
				 legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
				 feet = new ItemStack(Material.CHAINMAIL_BOOTS);
				 
				 
				 
				 
				 break;
			 case KANGAROO: 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 swordMeta.setDisplayName("&f&lKanga's sword");
				// swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				
				 
				 head = new ItemStack(Material.IRON_HELMET);
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				 legs = new ItemStack(Material.IRON_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 
		
				 
				 
				 break;
				 
			 case SNIPER:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				// swordMeta.setDisplayName(Main.colorCode("&r&lSniper's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				 
				 
				 head = new ItemStack(Material.BEACON);
				 chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
				 legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 
				
				 abilityMeta = ability.getItemMeta();
				 abilityMeta.addEnchant(Enchantment.ARROW_INFINITE, 1,false);
				 abilityMeta.setDisplayName(Utils.colorCode("&r&lSniper's rifle"));
				 
				 
				 inv.setItem(inv.getSize()-1, new ItemStack(Material.ARROW,1));
				 
				 
				 break; 
				 
			 case LAUNCHER:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				
				 
				 head = new ItemStack(Material.COAL_BLOCK);
				 
				 chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				 chestMeta = (LeatherArmorMeta)chest.getItemMeta();
				 chestMeta.setColor(Color.PURPLE);
				 
				 legs = new ItemStack(Material.IRON_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 feetMeta = (LeatherArmorMeta)feet.getItemMeta();
				 feetMeta.setColor(Color.PURPLE);
				 
				 
		
				 abilityMeta = ability.getItemMeta();
				 abilityMeta.setDisplayName(Utils.colorCode("&r&lLauncher's Guide"));
				 
				 
				
				 
				 
				 break;
				 
			 case STOMPER:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				
				 
				 head = new ItemStack(Material.GOLD_HELMET);
				 
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.IRON_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 feetMeta = (LeatherArmorMeta)feet.getItemMeta();
				 feetMeta.setColor(Color.WHITE);

				 break;
				 
			 case CACTUS:  
				 
				 sword = new ItemStack(Material.WOOD_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				// swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				
				 
				 head = new ItemStack(Material.CACTUS);
			
				
				 chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				 chestMeta = (LeatherArmorMeta)chest.getItemMeta();
				 chestMeta.setColor(Color.GREEN);
				 
				 legs = new ItemStack(Material.LEATHER_LEGGINGS);
				 legsMeta = (LeatherArmorMeta)legs.getItemMeta();
				 legsMeta.setColor(Color.GREEN);
				 
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 feetMeta = (LeatherArmorMeta)feet.getItemMeta();
				 feetMeta.setColor(Color.LIME);

				 break;
				 
			 case PYRO:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
		
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				// swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				
				 
				 head = new ItemStack(Material.LEATHER_HELMET);
				 headMeta = (LeatherArmorMeta)head.getItemMeta();
				 headMeta.setColor(Color.ORANGE);
				
				 chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				 chestMeta = (LeatherArmorMeta)chest.getItemMeta();
				 chestMeta.setColor(Color.RED);
				 
				 legs = new ItemStack(Material.LEATHER_LEGGINGS);
				 legsMeta = (LeatherArmorMeta)legs.getItemMeta();
				 legsMeta.setColor(Color.ORANGE);
				 
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 feetMeta = (LeatherArmorMeta)feet.getItemMeta();
				 feetMeta.setColor(Color.RED);

				 break;	
			 case GRAPPLER:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
				 
				 
				 head = new ItemStack(Material.LEATHER_HELMET);
				 
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.IRON_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 feetMeta = (LeatherArmorMeta)feet.getItemMeta();
				 feetMeta.setColor(Color.WHITE);

				 break;
				 
			 case FISHERMAN:  
	 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
			
				 
				 head = new ItemStack(Material.LEATHER_HELMET);
				 headMeta = (LeatherArmorMeta)head.getItemMeta();
				 headMeta.setColor(Color.RED);
				
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.IRON_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 feetMeta = (LeatherArmorMeta)feet.getItemMeta();
				 feetMeta.setColor(Color.BLACK);

				 break;
				 
			 case FISH:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
			
				 
				 head = new ItemStack(Material.CHAINMAIL_HELMET);
				
				
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
				 feet = new ItemStack(Material.DIAMOND_BOOTS);
				 

				 break;
			 case SWITCHER:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 //swordMeta.setDisplayName(Main.colorCode("&r&lLauncher's sword"));
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
			
				 
				 head = new ItemStack(Material.LEATHER_HELMET);
				
				
				 chest = new ItemStack(Material.IRON_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.DIAMOND_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 

				 break;
				 
			 case BERZERK:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
			
				 
				 head = new ItemStack(Material.LEATHER_HELMET);
				
				
				 chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.LEATHER_LEGGINGS);
				 feet = new ItemStack(Material.CHAINMAIL_BOOTS);
				 

				 break;
				 
			 case VIPER:  
		 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
			
				 
				 head = new ItemStack(Material.LEATHER_HELMET);
				
				
				 chest = new ItemStack(Material.GOLD_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.GOLD_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_BOOTS);
				 

				 break;
				 
			 case WITHER:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
			
				 
				 head = new ItemStack(Material.SKULL_ITEM,1,(short)1);
				
				
				 chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.GOLD_LEGGINGS);
				 feet = new ItemStack(Material.GOLD_LEGGINGS);
				 

				 break;
				 
		 case MUCKRAKER:  
				 
				 sword = new ItemStack(Material.IRON_SWORD);
				 swordMeta = sword.getItemMeta();
				 
				 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
			
				 
				 head = new ItemStack(Material.MYCEL,1,(short)1);
				
				
				 chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				
				 
				 legs = new ItemStack(Material.GOLD_LEGGINGS);
				 feet = new ItemStack(Material.LEATHER_LEGGINGS);
				 

				 break;
				 
		 case ENDER:  
			 
			 sword = new ItemStack(Material.IRON_SWORD);
			 swordMeta = sword.getItemMeta();
			 
			 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
		
			 
			 head = new ItemStack(Material.LEATHER_HELMET);
			 headMeta = (LeatherArmorMeta) head.getItemMeta();
			 headMeta.setColor(Color.RED);
			
			
			
			 chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			 chestMeta = (LeatherArmorMeta) chest.getItemMeta();
			 chestMeta.setColor(Color.RED);
			 
			
			 
			 legs = new ItemStack(Material.GOLD_LEGGINGS);
			 feet = new ItemStack(Material.GOLD_BOOTS);
			 

			 break;
		 case THOR:  
			 
			 sword = new ItemStack(Material.IRON_SWORD);
			 swordMeta = sword.getItemMeta();
			 
			 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
		
			 
			 head = new ItemStack(Material.IRON_HELMET);
			 
			
			
			
			 chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			 chestMeta = (LeatherArmorMeta) chest.getItemMeta();
			 chestMeta.setColor(Color.BLUE);
			 
			
			 
			 legs = new ItemStack(Material.DIAMOND_LEGGINGS);
			 feet = new ItemStack(Material.GOLD_BOOTS);
			 

			 break;
			 
	 case TURTLE:  
			 
			 sword = new ItemStack(Material.STONE_SWORD);
			 swordMeta = sword.getItemMeta();
			 swordMeta.setLore(Arrays.asList(new String[]{"NINJA TURTLE POWAH"}));
			
			 chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
			 legs = new ItemStack(Material.DIAMOND_LEGGINGS);
			 ///feet = new ItemStack(Material.DIAMOND_LEGGINGS);
			 

			 break;
	 case ANCHOR:  
		 
		 sword = new ItemStack(Material.IRON_SWORD);
		 swordMeta = sword.getItemMeta();
		
		 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
		 
		 
		 head = new ItemStack(Material.ANVIL);
		 chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		 legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
	 feet = new ItemStack(Material.IRON_BOOTS);
		 
		
		 
		 break;
			 
	 case SCOUT:  
		 
		 sword = new ItemStack(Material.DIAMOND_SWORD);
		 swordMeta = sword.getItemMeta();
		
		 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, false);
		 
		
		 
		
		 
		 break;
			 
			 
			 
			
			 default: Messenger.send("This kit gives no items :/",reciever, Messenger.WARN);
			 }
			
			
			
		
			
			
			playerUser.changeKit(kit);
			
		if(kit == null)return;
			
			{//If they have no display name....  give it a name
				if(sword == null)sword = new ItemStack(Material.AIR);
				if(head == null)head = new ItemStack(Material.AIR);
				if(chest == null)chest = new ItemStack(Material.AIR);
				if(legs == null)legs = new ItemStack(Material.AIR);
				if(feet == null)feet = new ItemStack(Material.AIR);
				if(ability == null){
					
					ability = Main.GLOBAL_SOUP;
				}
				if(swordMeta == null && sword!= null){
					
					swordMeta = sword.getItemMeta();
				}
				if(abilityMeta == null){
					
					abilityMeta = ability.getItemMeta();
				}
			if(swordMeta!=  null){
				if(!swordMeta.hasDisplayName()){
					
					String itemName = sword.getType().name().toLowerCase().replace("_", " ");
					swordMeta.setDisplayName(Utils.colorCode("&r&l" + kit.getName() + "'s " + itemName));
				}
			}
				if(!abilityMeta.hasDisplayName()){
					
					String itemName = ability.getType().name().toLowerCase().replace("_", " ");
					abilityMeta.setDisplayName(Utils.colorCode("&r&l" + kit.getName() + "'s " + itemName));
				}

			}
			
			if(swordMeta!=null)sword.setItemMeta(swordMeta);
			if(headMeta!=null)head.setItemMeta(headMeta);
			if(chestMeta!=null)chest.setItemMeta(chestMeta);
			if(legsMeta!=null)legs.setItemMeta(legsMeta);
			if(feetMeta!=null)feet.setItemMeta(feetMeta);
			if(abilityMeta!=null)ability.setItemMeta(abilityMeta);
			
			inv.setHelmet(head);
			inv.setChestplate(chest);
			inv.setLeggings(legs);
			inv.setBoots(feet);
			inv.setItem(0, sword);
			inv.setItem(1, ability);
			if(kit == Kit.SNIPER ||kit == Kit.ARCHER){
				 inv.setItem(inv.getSize()-1, new ItemStack(Material.ARROW,1));
			}
			if(refills){
				while( Arrays.asList(reciever.getInventory().getContents()).contains(null)){
					reciever.getInventory().addItem(Main.GLOBAL_SOUP);
				}
				/*for(int i = 0; i<pl.getInventory().getSize(); i++){
					pl.getInventory().addItem(Main.GLOBAL_SOUP);
				}*/
			}else{
				for(int i = 0; i< 8; i++){
					reciever.getInventory().addItem(Main.GLOBAL_SOUP);
				}
			}
			if(recraft){
				reciever.getInventory().setItem(reciever.getInventory().getSize()-1, new ItemStack( Material.RED_MUSHROOM,64));
				reciever.getInventory().setItem(reciever.getInventory().getSize()-2, new ItemStack(Material.BOWL,64));
				reciever.getInventory().setItem(reciever.getInventory().getSize()-3, new ItemStack(Material.BROWN_MUSHROOM,64));
			}
			/*for(int i = 0;i<inv.getSize();i++){
				inv.addItem(Main.GLOBAL_SOUP);
			}*/
		}
	public static Kit getKitByName(String kitName){
		return getKitByName(kitName,false);
	}
	public static Kit getKitByName(String kitName,boolean all){
			
		if(all){
			for(Kit temp : Kit.values()){
				if(temp.name().toLowerCase().equalsIgnoreCase(kitName) || temp.getName().toLowerCase().equalsIgnoreCase(kitName) ){
					return temp;
				}
			}
			return null;
		}else{
			for(Kit temp : Kit.getAllKits()){
				if(temp.name().toLowerCase().equalsIgnoreCase(kitName) || temp.getName().toLowerCase().equalsIgnoreCase(kitName) ){
					return temp;
				}
			}
			return null;
		//return valueOf(kitName);
		}
	}
	
	public static void addUnusedUnrankedKits(Player holder ,Inventory inv){
		User playerUser = User.getUser(holder.getName());
		for(Kit k : Kit.getKits(Type.NONDONOR)){
			if(k != null){
			if(k.isEnabled()){
			
			if(!(playerUser.getOwnedKits(false).contains(k)) ){
				ItemStack item;
				Wool wool;
				ItemMeta itemM;
				if(playerUser.getMoney().compareTo(BigInteger.valueOf(k.getPrice()))!= -1 ){
					wool = new Wool(DyeColor.GREEN);
					 item = wool.toItemStack(1);
						 itemM = item.getItemMeta();
						itemM.setDisplayName(Utils.colorCode(k.getType().getColorCode()+k.getName()));
						
						List<String> lore = itemM.getLore();
						if(lore == null){lore = new ArrayList<String>();}
						lore.add(Utils.colorCode( "&aYou can buy this kit."));
						lore.add(Utils.colorCode("&a" +playerUser.getMoney()+"&8&l/&7"+k.getPrice() + " &2&lBits") );
						itemM.setLore(lore);
					}else{
						 wool = new Wool(DyeColor.RED);
						 item = wool.toItemStack(1);
							itemM = item.getItemMeta();
							itemM.setDisplayName(Utils.colorCode(k.getType().getColorCode()+k.getName()));
							List<String> lore = itemM.getLore();
							if(lore == null){lore = new ArrayList<String>();}
							lore.add(Utils.colorCode("&cYou can't afford this kit.") );
							lore.add(Utils.colorCode("&c"+playerUser.getMoney()+"&8&l/&7"+k.getPrice() + " &2&lBits") );
							itemM.setLore(lore);
						
					}
				item.setItemMeta(itemM);
				
				inv.addItem(item);	
				
		}
		}	
				
			/*	if(playerUser.getMoney() >= k.getPrice()){
					
				
					}else{
						
					
					}*/
				
			}
			
			
		
		}
	}
	
	public static void addUnusedRankedKits(Player holder ,Inventory inv){
		User user = User.getUser(holder.getName());
		for(Kit k : Kit.getKits(Type.DONOR)){
			if(k != null){
				Wool wool;
				ItemStack item;
				if(k.isEnabled()){
				if(!user.getOwnedKits(true).contains(k)){
					if(!user.hasPermission(k.getRequiredRank())){
				//Main.debug("added:"+k.getName());
				wool = new Wool(DyeColor.BLUE);
				 item = wool.toItemStack(1);
				ItemMeta itemM = item.getItemMeta();
				itemM.setDisplayName(Utils.colorCode(k.getType().getColorCode()+k.getName()));
				List<String> lore = itemM.getLore();
				if(lore == null){lore = new ArrayList<String>();}
				lore.add(Utils.colorCode("&bThis kit is for rank "+k.getRequiredRank().getFullName()+".") );
				
				itemM.setLore(lore);
				
				item.setItemMeta(itemM);
				inv.addItem(item);
			}else{
				if(user.getMoney().compareTo(BigInteger.valueOf(k.getPrice()))!= -1){
					wool = new Wool(DyeColor.GREEN);
					 item = wool.toItemStack(1);
					}else{
						 wool = new Wool(DyeColor.RED);
						 item = wool.toItemStack(1);
						
					}
				
				ItemMeta itemM = item.getItemMeta();
				itemM.setDisplayName(Utils.colorCode(k.getType().getColorCode()+k.getName()));
				
				if(user.getMoney().compareTo(BigInteger.valueOf(k.getPrice()))!= -1){
					
					List<String> lore = itemM.getLore();
					if(lore == null){lore = new ArrayList<String>();}
					lore.add(Utils.colorCode( "&aYou can buy this kit."));
					lore.add(Utils.colorCode("&a" +user.getMoney()+"&8&l/&7"+k.getPrice() + " &2&lBits") );
					itemM.setLore(lore);
					}else{
						
						List<String> lore = itemM.getLore();
						if(lore == null){lore = new ArrayList<String>();}
						lore.add(Utils.colorCode("&cYou can't afford this kit.") );
						lore.add(Utils.colorCode("&c" +user.getMoney()+"&8&l/&7"+k.getPrice() + " &2&lBits") );
						itemM.setLore(lore);
					}
				
				item.setItemMeta(itemM);
				
				inv.addItem(item);
			}
				}
			}
			}
		}
	}
	public static void addKits(Player holder ,Inventory inv,boolean addUsed){
		User playerUser = User.getUser(holder.getName());
		if(addUsed){
			for(Kit k : playerUser.getOwnedKits(true)){
				if(k != null){
					if(k.isEnabled()){
	
				inv.addItem(k.getItem());
					}}
			}
		}else{
			Kit.addUnusedUnrankedKits(holder, playerUser.getInv());
			Kit.addUnusedRankedKits(holder, playerUser.getInv());
		}
	}
	

	public static List<Kit> getAllKits(){
		List<Kit> list = new ArrayList<Kit>();
		for(Kit k:values()){
			if(k.isEnabled()){
				list.add(k);
			}
		}
		
		
		return list;
	}
	 
	
	public static Set<Kit> getKits(Type k){
		 Set<Kit> regKit = new HashSet<Kit>();

			  for(Kit kit : values()){
				  if(kit.getType() == k ){
					  regKit.add(kit); 
				  }
			  }
		  return  regKit;
	  }




	public Material getActivater() {
		return activater;
	}

	public String getFullName(){
		return Utils.colorCode(getType().getColorCode() + getName()); 
	}



	  
	  

	
}
