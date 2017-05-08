package me.varmetek.kitserver.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import me.varmetek.kitserver.api.GraphicalUserInterface;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Perk;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.api.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class GambleCommand implements CommandExecutor,Listener
{
	 private static HashMap<UUID,String> enchTree = new HashMap<UUID,String>();
	 private static HashMap<UUID,Page> pageTree = new HashMap<UUID,Page>();
	 private static HashMap<UUID,Integer> tierTree = new HashMap<UUID,Integer>();

	
	private enum Page{
		MAIN,ENCHANTMENTS,
		GAMBLE_ENCH,PERK_SEL,
		GAMBLE_PERK,PERK_TAKE;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if(!(sender instanceof Player))return false;
		Player pl = (Player)sender;
		User user = User.getUser(pl.getName());
		int len = args.length;
		Map<String,String> argMap = new HashMap<String,String>();
		argMap.put("bet enchant <enchant name>","Shows the gamble options for that enchant.");
		argMap.put("bet enchant <id","Shows the gamble options for that enchant.");
		Enchantment ench;
		List<String> validArgs;		
		if(len == 0){
			pageTree.put(pl.getUniqueId(), Page.MAIN);
			user.setInv(openGambleMainMenu(pl));
			user.setGui(GraphicalUserInterface.GAMBLE);
			pl.openInventory(user.getInv());
			/*Messenger.send("&1-------&4Teleport&7-&aHelp&1-------", sender, Messenger.INFO);
			for(String arg: argMap.keySet()){
				String desc = argMap.get(arg);
				Messenger.send("&4/&6" + arg + " &8:&3 " + desc,sender, Messenger.INFO);
			}*/
		}else{
			if(len ==1){
				validArgs = Arrays.asList(new String[]{"enchant","perks"});
			/*if(!validArgs.contains(args[0].toLowerCase())){
				Messenger.send("&4Invalid Param. &7Try "+Utils.listToString(validArgs), sender);
			}*/
				if(!Utils.validateParam(sender, validArgs, args[0]))return false;
			
				if(args[0].equalsIgnoreCase("enchant")){
				//Messenger.send("&4Needs param <enchant name> or <enchant id>.", pl);
				//return false;
					pageTree.put(pl.getUniqueId(), Page.ENCHANTMENTS);
					user.setInv(openGambleEnchSel(pl));
					user.setGui(GraphicalUserInterface.GAMBLE);
					pl.openInventory(user.getInv());
				}
				if(args[0].equalsIgnoreCase("perks")){
					pageTree.put(pl.getUniqueId(), Page.PERK_SEL);
					user.setInv(openGamblePerkSel(pl));
					user.setGui(GraphicalUserInterface.GAMBLE);
					pl.openInventory(user.getInv());
				}
			}
			if(len>1){
				if(args[0].equalsIgnoreCase("enchant")){
					
					if(Utils.isInt(args[1])){
						ench = Enchantment.getById(Integer.parseInt(args[1]));
					}else{
						ench = Enchantment.getByName(args[1].toUpperCase());
					}
					if(ench == null){
						Messenger.send("&4Invalid enchantment.", pl);
						return false;
					}
					enchTree.put(pl.getUniqueId(), ench.getName());
					pageTree.put(pl.getUniqueId(), Page.GAMBLE_ENCH);
					user.setInv(openGambleEnchGUI(pl,ench.getMaxLevel()));
					user.setGui(GraphicalUserInterface.GAMBLE);
				
					
					pl.openInventory(user.getInv());
					
				}
				if(args[0].equalsIgnoreCase("perks")){
					if(Utils.isInt(args[1])){
						int level = Integer.parseInt(args[1]);
			
						if(user.getMoney().compareTo(BigInteger.valueOf(Perk.getPrices()[level-1]))!= -1){
							user.subtractMoney(Perk.getPrices()[level-1]);
							tierTree.put(pl.getUniqueId(), Math.min(level,6));
							pageTree.put(pl.getUniqueId(), Page.GAMBLE_PERK);
						
							user.setGui(GraphicalUserInterface.GAMBLE);
							//pl.openInventory(user.getInv());
							//	user.setInv(openGamblePerkSel(pl));
							openGamblePerkSlider(pl);
						}else{
							Messenger.send("&c Not enough Funds", pl);
							return false;
						}
					}
				}
			}
		}
		return false;
	}
	private static Inventory openGambleEnchGUI(Player pl,final int tier){
		final Inventory inv = Bukkit.createInventory(null, 9,Utils.colorCode("&cGamble "));
		final User user = User.getUser(pl.getName());
		//user.setInv(inv);
		final double[] per = Utils.getPercentTier(tier);
		
		
		for(int i = 1; i<=tier; i ++){
			int price = (int) Math.pow(10, i/2D);
			int percent = (int) per[i-1];
			
			ItemStack item;
			if(user.getMoney().compareTo(BigInteger.valueOf(price))!= -1){
				item = new ItemStack(Material.EMERALD_BLOCK);
			}else{
				item = new ItemStack(Material.REDSTONE_BLOCK);
			}
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(Utils.colorCode("&aTier "+ i));
			List<String> lore = im.getLore();
			int bonus = 0;
			Perk p = user.getCurrentPerk();
			if(p != null ){
				if(p.getType() == Perk.Type.LUCK){
					bonus = Perk.VALUES.get(p.getType())[p.getLevel()-1];
				}
			}
			if(lore == null)lore = new ArrayList<String>();
			
			lore.add(bonus == 0? Utils.colorCode("&7"+percent+"% chance of winning."): Utils.colorCode("&7"+percent+"% "+" &a+"+bonus+"% &7"+"chance of winning."));
			lore.add(Utils.colorCode("&7cost "+price+ "&5&oBits &7 to run."));
			if(user.getMoney().compareTo(BigInteger.valueOf(price))== -1){
				lore.add(Utils.colorCode("&4&l-=CANT AFFORD=-"));
			}
			im.setLore(lore);
			item.setItemMeta(im);
			inv.addItem(item);
		}
	
		return inv;
	}
		
	private static Inventory openGambleMainMenu(Player pl){
		final Inventory inv = Bukkit.createInventory(null, 9,Utils.colorCode("&cGamble "));
		ItemStack item ;
		ItemMeta im;
		item = new ItemStack(Material.BOOK);
		im = item.getItemMeta();
		im.setDisplayName(Utils.colorCode("&7Enchant"));
		item.setItemMeta(im);
		inv.addItem(item);
		
		 item = new ItemStack(Material.BOOK);
		 im = item.getItemMeta();
		im.setDisplayName(Utils.colorCode("&7Perks"));
		item.setItemMeta(im);
		inv.addItem(item);
		return inv;
	}
	private static Inventory openGambleEnchSel(Player pl){
		final Inventory inv = Bukkit.createInventory(null, 9,Utils.colorCode("&cGamble "));
		
		Enchantment[] enchz = new Enchantment[]{
				Enchantment.PROTECTION_ENVIRONMENTAL,
				Enchantment.DAMAGE_ALL,
				Enchantment.KNOCKBACK,
				Enchantment.THORNS,
				
		};
		
	for(Enchantment e: enchz){
		ItemStack item = new ItemStack(Material.LAPIS_BLOCK);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(Utils.colorCode("&6"+e.getName()));
		item.setItemMeta(im);
		inv.addItem(item);
		
	}
	return inv;
	}
	
	
	private static Inventory openGamblePerkSel(Player pl){
		final Inventory inv = Bukkit.createInventory(null, 9,Utils.colorCode("&cGamble "));
		

		User user = User.getUser(pl.getName());
	for(int i = 1; i<7; i++){
		ItemStack item = new ItemStack(Material.DETECTOR_RAIL);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(Utils.colorCode("&6Tier &4"+i));
		List<String> lore = im.getLore();
		if(lore == null)lore= new ArrayList<String>();
		double[] per = Perk.getPercentTier(i);
		int bonus = 0;
		Perk p = user.getCurrentPerk();
		if(p != null ){
			if(p.getType() == Perk.Type.LUCK){
				bonus = Perk.VALUES.get(p.getType())[p.getLevel()-1];
			}
		}
		
		lore.add(Utils.colorCode("&7T1: "+ (int)per[0]+"%"+" &a+"+bonus+"%"));
		lore.add(Utils.colorCode("&7T2: "+ (int)per[1]+"%"+" &a+"+bonus+"%"));
		lore.add(Utils.colorCode("&7T3: "+ (int)per[2]+"%"+" &a+"+bonus+"%"));
		lore.add(Utils.colorCode("&aPrice: &7"+ Perk.getPrices()[i-1]));
		im.setLore(lore);
		item.setItemMeta(im);
		
		inv.addItem(item);
		
	}
	return inv;
	}
	
	private static void openGamblePerkSlider(final Player pl){
		final Inventory inv = Bukkit.createInventory(null, 18,Utils.colorCode("&cGamble "));
		final int level = tierTree.get(pl.getUniqueId());
		final List<Perk> possible = new ArrayList<Perk>();
	
	//	final int[] alt =  new int[]{0,1,2,4,6,7,8};
		inv.setItem(13,new ItemStack(Material.FIREWORK));
		//inv.setItem(5, new ItemStack(Material.LADDER));
	
		
		final User user = User.getUser(pl.getName());
		  int b= 0;
		Perk p = user.getCurrentPerk();
		if(p != null ){
			if(p.getType() == Perk.Type.LUCK){
				b = Perk.VALUES.get(p.getType())[p.getLevel()-1];
			}
		}
		final int bonus = b;
		for(int i = 9; i>0 ; i--){
				possible.add(Perk.getRandom(level,bonus));
		}
		pageTree.put(pl.getUniqueId(), Page.GAMBLE_PERK);
		user.setInv(inv);
		user.setGui(GraphicalUserInterface.GAMBLE);
		pl.openInventory(inv);
		new BukkitRunnable(){
			 int multiplier = 1;
			 double value = 0;
			
			 public void run(){
				if(multiplier >= 30 && value >= 30 ){
						this.cancel();
						
						//pageTree.put(pl.getUniqueId(), Page.PERK_TAKE);
						//user.setGui(GraphicalUserInterface.GAMBLE);
						//user.setInv(inv);
						//pl.openInventory(inv);
						
						Perk p = possible.get(5);
						for(int i = 0; i< 9; i++){
							inv.setItem(i, new ItemStack(Material.DETECTOR_RAIL));
						}
					if(p == null){
						Messenger.send("&aYou lost the gamble.", pl);
						pl.playSound(pl.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
						inv.setItem(4,null);
					}else{
						inv.setItem(4,p.getIcon());
						Messenger.send("&aYou won the perk "+ p.getType().getName(), pl);
						pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1f, 1f);
						user.addOwnedPerk(p);
						
					}
				pl.updateInventory();
					
					return;
				}
				if(!pl.isOnline()){
					this.cancel();
					return;
				}
				//pl.openInventory(inv);
				if(value >= multiplier){multiplier++; 
				value = 0;
				possible.remove(0);
				int b = bonus;
				possible.add(Perk.getRandom(level,b));
				pl.playSound(pl.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
				}
				
				for(int i = 0; i<9;i++){
					Perk p = possible.get(i);
					if(p != null){
						inv.setItem(i, p.getIcon());
					}else{
						inv.setItem(i, new ItemStack(Material.AIR));
					}
					
				}
				pl.updateInventory();
				
				value += 3;
				
			}
		}.runTaskTimer(Utils.PLUGIN, 0L, 1L);
		
	}
	@EventHandler
	public void getClicked(InventoryClickEvent ev){
		Player player =(Player)ev.getWhoClicked();
		User user = User.getUser(player.getName());
		ItemStack item = ev.getCurrentItem();
		Inventory cinv = ev.getInventory();
		Random ran = new Random();
		if(cinv == null){return;}
		if(!cinv.equals(user.getInv()))return;
			if(user.getGui() == GraphicalUserInterface.GAMBLE){
				ev.setCancelled(true);
				if(!pageTree.containsKey(player.getUniqueId()))return;
				
				switch(pageTree.get(player.getUniqueId()) ){
				case ENCHANTMENTS:
					if(item.getType() != Material.LAPIS_BLOCK){ev.setCancelled(true);return;}
					//	Utils.debug("Is a valid block");
						if(!item.hasItemMeta()){ev.setCancelled(true);return;}
					//	Utils.debug("Has meta");
						if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
						player.performCommand("bet enchant "+ ChatColor.stripColor(item.getItemMeta().getDisplayName()));
					break;
				case GAMBLE_ENCH:
					if(item.getType() != Material.EMERALD_BLOCK){ev.setCancelled(true);return;}
					if(!item.hasItemMeta()){ev.setCancelled(true);return;}
					if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
					int num = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" ")[1]);
					Enchantment enche = Enchantment.getByName(enchTree.get(player.getUniqueId()));
					if(enche == null){ev.setCancelled(true);return;}
					 double[] per = Utils.getPercentTier(enche.getMaxLevel());
					
					
					
						int price = (int) Math.pow(10, num/2);
						int bonus = 0;
						Perk p = user.getCurrentPerk();
						if(p != null ){
							if(p.getType() == Perk.Type.LUCK){
								bonus = Perk.VALUES.get(p.getType())[p.getLevel()-1];
							}
						}
						
						int percent = (int) per[num-1];
						
						user.subtractMoney(price);
						boolean winner = ran.nextInt(100) <= percent+bonus;
						player.closeInventory();
						if(winner){
							player.getItemInHand().addEnchantment(enche, num);
							Messenger.send("&aYou won the gamble.", player);
						//	Utils.debug("Winner");
						}else{
							Messenger.send("&cYou lost the gamble.", player);
						//	Utils.debug("Loser");
						}
						
					break;
				case GAMBLE_PERK:
					ev.setCancelled(true);
					//player.closeInventory();
					
					break;
				case MAIN:
					if(item.getType() != Material.BOOK){ev.setCancelled(true);return;}
					
					if(!item.hasItemMeta()){ev.setCancelled(true);return;}
			
					if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
					player.performCommand("bet "+ ChatColor.stripColor(item.getItemMeta().getDisplayName()));
					break;
				case PERK_SEL:
					if(!item.hasItemMeta()){ev.setCancelled(true);return;}
					
					if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
					String ding = ChatColor.stripColor(item.getItemMeta().getDisplayName());
					Utils.debug(ding.substring(ding.length()-1, ding.length()));
					player.performCommand("bet perks "+ ding.substring(ding.length()-1, ding.length()));
					break;
				case PERK_TAKE:
					ev.setCancelled(true);
					player.closeInventory();
					break;
				default:
					break;
					
				}
				/*if(pageTree.get(player.getUniqueId()) == Page.GAMBLE_ENCH){
				//Utils.debug("In Gamble GUI");
				if(item.getType() != Material.EMERALD_BLOCK){ev.setCancelled(true);return;}
			//	Utils.debug("Is a valid block");
				if(!item.hasItemMeta()){ev.setCancelled(true);return;}
			//	Utils.debug("Has meta");
				if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
			//	Utils.debug("Has a display name");
				int num = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" ")[1]);
				Enchantment enche = Enchantment.getByName(enchTree.get(player.getUniqueId()));
				//Utils.debug("Request."+enchTree.get(player.getUniqueId()));
				//Utils.debug("UUID: "+ player.getUniqueId());
				if(enche == null){ev.setCancelled(true);return;}
				 double[] per = Utils.getPercentTier(enche.getMaxLevel());
				
				
				
					int price = (int) Math.pow(10, num/2);
					int percent = (int) per[num-1];
					
					user.subtractMoney(price);
					boolean winner = ran.nextInt(100) <= percent;
					player.closeInventory();
					if(winner){
						player.getItemInHand().addEnchantment(enche, num);
						Messenger.send("&aYou won the gamble.", player);
					//	Utils.debug("Winner");
					}else{
						Messenger.send("&cYou lost the gamble.", player);
					//	Utils.debug("Loser");
					}
					
				
				}else{
					if(pageTree.get(player.getUniqueId()) == Page.MAIN){
						if(item.getType() != Material.BOOK){ev.setCancelled(true);return;}
				
							if(!item.hasItemMeta()){ev.setCancelled(true);return;}
					
							if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
							player.performCommand("bet "+ ChatColor.stripColor(item.getItemMeta().getDisplayName()));
					}else{
						if(pageTree.get(player.getUniqueId()) == Page.ENCHANTMENTS){
							if(item.getType() != Material.LAPIS_BLOCK){ev.setCancelled(true);return;}
							//	Utils.debug("Is a valid block");
								if(!item.hasItemMeta()){ev.setCancelled(true);return;}
							//	Utils.debug("Has meta");
								if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
								player.performCommand("bet enchant "+ ChatColor.stripColor(item.getItemMeta().getDisplayName()));
						}else{
							if(pageTree.get(player.getUniqueId()) == Page.GAMBLE_PERK){
								ev.setCancelled(true);
								
								return;
							}else{
								if(pageTree.get(player.getUniqueId()) == Page.PERK_SEL){
									if(!item.hasItemMeta()){ev.setCancelled(true);return;}
									
									if(!item.getItemMeta().hasDisplayName()){ev.setCancelled(true);return;}
									String ding = ChatColor.stripColor(item.getItemMeta().getDisplayName());
									Utils.debug(ding.substring(ding.length()-1, ding.length()));
									player.performCommand("bet perks "+ ding.substring(ding.length()-1, ding.length()));
								}else{
									if(pageTree.get(player.getUniqueId()) == Page.GAMBLE_PERK){
										ev.setCancelled(true);
										player.closeInventory();
										return;
									}
								}
							}
						}
					}
				}*/
			}
		/*if(ev.getInventory().equals(inv)){
		 Kit.getKitByName(ev.getCurrentItem().getItemMeta().getDisplayName()).getMaterials((Player)ev.getWhoClicked());
		// ev.setCancelled(true);
		 Messenger.send("You have chosen a kit", (Player)ev.getWhoClicked(), Messenger.INFO);
		 ev.getWhoClicked().closeInventory();
		}*/
	}
		
	
	
}
