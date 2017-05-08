package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.GraphicalUserInterface;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class KitCommand implements CommandExecutor,Listener{
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
		
			final Player player = (Player)sender;
			final User playerUser  =  User.getUser(player.getName());
			
			if(args.length == 0 ){
				//player.performCommand("shop kits");
				//return false;
				if(playerUser.usingKit()){
					Messenger.send("You currently are using a kit.", player, Messenger.WARN);
					return false;
				}
				
				if(playerUser.getGameMode() != User.GameMode.PLAYER){
					Messenger.send("Only in player gamemode can you select kits.", player, Messenger.WARN);
					return false;
				}
				playerUser.setInv(Bukkit.createInventory(null, 45,"Owned Kits"));
				playerUser.getInv().clear();
				new BukkitRunnable(){
					public void run(){
					Kit.addKits(player, playerUser.getInv(), true);
					}
				}.runTaskAsynchronously(Main.getPluginInstance());
				player.openInventory(	playerUser.getInv());
				playerUser.setGui(GraphicalUserInterface.KIT);
		}
			
			if(args.length == 1){
				if(playerUser.getGameMode() != User.GameMode.PLAYER){
					Messenger.send("Only in player gamemode can you select kits.", player, Messenger.WARN);
					return false;
				}
				if(playerUser.usingKit()){
					Messenger.send("You currently are using a kit.", player, Messenger.WARN);
					return false;
				}
				Kit kit = Kit.getKitByName(args[0]);
				
				if(kit != null){
					
					if(playerUser.getOwnedKits(true).contains(kit)  ){
						
					
					
						
						playerUser.changeKit(kit);
						kit.getMaterials(player);
						Messenger.send("You have chosen kit "+ kit.getType().getColorCode()+kit.getName(), player, Messenger.INFO);
						return true;
					}else
						Messenger.send("You do not own this kit", player, Messenger.WARN);
				}else{
					
					Messenger.send("No such Kit", player, Messenger.WARN);
				}

			}
			}
		return false;
		}
		
	
	
	

	
	
	@EventHandler
	public void getClicked(InventoryClickEvent ev){
		Player player =(Player)ev.getWhoClicked();
		User user = User.getUser(player.getName());
		ItemStack item = ev.getCurrentItem();
		Inventory cinv = ev.getInventory();
		if(cinv == null)return;
		//cinv.getTitle().equalsIgnoreCase("Owned Kits")
			if(user.getGui() == GraphicalUserInterface.KIT){
				if(!item.hasItemMeta()) return;
				if(!item.getItemMeta().hasDisplayName()) return;
				player.performCommand("kit "+ChatColor.stripColor(item.getItemMeta().getDisplayName()));
				player.closeInventory();
				ev.setCancelled(true);
			}
		/*if(ev.getInventory().equals(inv)){
		 Kit.getKitByName(ev.getCurrentItem().getItemMeta().getDisplayName()).getMaterials((Player)ev.getWhoClicked());
		// ev.setCancelled(true);
		 Messenger.send("You have chosen a kit", (Player)ev.getWhoClicked(), Messenger.INFO);
		 ev.getWhoClicked().closeInventory();
		}*/
	}
}
/*Set<ItemStack> content = new HashSet<ItemStack>();
ItemStack sword = null; 
ItemMeta swordMeta = null;
content.add(sword);

ItemStack head = null;
ItemMeta headMeta= null;
content.add(head);

ItemStack chest = null;
ItemMeta chesstMeta = null;
content.add(chest);

ItemStack legs = null;
ItemMeta legsMeta = null;
content.add(legs);

ItemStack feet = null;
ItemMeta feetMeta = null;
content.add(feet);

ItemStack ability = null;
ItemMeta abilityMeta = null;
content.add(ability);


PlayerInventory inv = player.getInventory();
switch(kit){
 case ARCHER:  
	 
	 sword = new ItemStack(Material.STONE_SWORD);
	 swordMeta = sword.getItemMeta();
	 swordMeta.setDisplayName("Archer's bow");
	 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
	 sword.setItemMeta(swordMeta);
	 
	 head = new ItemStack(Material.LEATHER_HELMET);
	 chest = new ItemStack(Material.IRON_CHESTPLATE);
	 legs = new ItemStack(Material.IRON_LEGGINGS);
	 feet = new ItemStack(Material.LEATHER_BOOTS);
	 
	 ability = new ItemStack(Main.soupGlobal);
	 
	 
	 break; 

 case PVP: 
	 sword = new ItemStack(Material.DIAMOND_SWORD);
	 swordMeta = sword.getItemMeta();
	 swordMeta.setDisplayName("PVP's sword");
	 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
	 sword.setItemMeta(swordMeta);
	 
	 head = new ItemStack(Material.IRON_HELMET);
	 chest = new ItemStack(Material.IRON_CHESTPLATE);
	 legs = new ItemStack(Material.IRON_LEGGINGS);
	 feet = new ItemStack(Material.IRON_BOOTS);
	 
	 ability = new ItemStack(Main.soupGlobal);
	 
	 break;

 case BUFFERINO: 
	 sword = new ItemStack(Material.DIAMOND_SWORD);
	 swordMeta = sword.getItemMeta();
	 swordMeta.setDisplayName("TACOOO");
	// swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
	 sword.setItemMeta(swordMeta);
	 
	 head = new ItemStack(Material.CHAINMAIL_HELMET);
	 chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
	 legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
	 feet = new ItemStack(Material.CHAINMAIL_BOOTS);
	 
	 ability = new ItemStack(Main.soupGlobal);
	 
	 
	 break;
 
 default: Messenger.send("No such kit.", player, Messenger.WARN);
 }

for(ItemStack l :(ItemStack[])content.toArray()){
	if (l == null) return false;
}

playerUser.changeKit(kit);

inv.setHelmet(head);
inv.setChestplate(chest);
inv.setLeggings(legs);
inv.setBoots(feet);
inv.setItem(0, sword);
inv.setItem(1, ability);

for(int i = 0;i<inv.getSize();i++){
	inv.addItem(Main.soupGlobal);
}*/



/*public void getKitMaterials(Player reciever , Kit kit){
//Kit kit = Kit.getKitByName(args[0]);
User playerUser  =  User.getUser(reciever.getName());

Set<ItemStack> content = new HashSet<ItemStack>();
ItemStack sword = null; 
ItemMeta swordMeta = null;
content.add(sword);

ItemStack head = null;
ItemMeta headMeta= null;
content.add(head);

ItemStack chest = null;
ItemMeta chesstMeta = null;
content.add(chest);

ItemStack legs = null;
ItemMeta legsMeta = null;
content.add(legs);

ItemStack feet = null;
ItemMeta feetMeta = null;
content.add(feet);

ItemStack ability = null;
ItemMeta abilityMeta = null;
content.add(ability);


PlayerInventory inv = reciever.getInventory();
switch(kit){
 case ARCHER:  
	 
	 sword = new ItemStack(Material.STONE_SWORD);
	 swordMeta = sword.getItemMeta();
	 swordMeta.setDisplayName("Archer's bow");
	 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
	 sword.setItemMeta(swordMeta);
	 
	 head = new ItemStack(Material.LEATHER_HELMET);
	 chest = new ItemStack(Material.IRON_CHESTPLATE);
	 legs = new ItemStack(Material.IRON_LEGGINGS);
	 feet = new ItemStack(Material.LEATHER_BOOTS);
	 
	 ability = new ItemStack(Main.soupGlobal);
	 
	 
	 break; 

 case PVP: 
	 sword = new ItemStack(Material.DIAMOND_SWORD);
	 swordMeta = sword.getItemMeta();
	 swordMeta.setDisplayName("PVP's sword");
	 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
	 sword.setItemMeta(swordMeta);
	 
	 head = new ItemStack(Material.IRON_HELMET);
	 chest = new ItemStack(Material.IRON_CHESTPLATE);
	 legs = new ItemStack(Material.IRON_LEGGINGS);
	 feet = new ItemStack(Material.IRON_BOOTS);
	 
	 ability = new ItemStack(Main.soupGlobal);
	 
	 break;

 case BUFFERINO: 
	 sword = new ItemStack(Material.DIAMOND_SWORD);
	 swordMeta = sword.getItemMeta();
	 swordMeta.setDisplayName("TACOOO");
	// swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
	 sword.setItemMeta(swordMeta);
	 
	 head = new ItemStack(Material.CHAINMAIL_HELMET);
	 chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
	 legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
	 feet = new ItemStack(Material.CHAINMAIL_BOOTS);
	 
	 ability = new ItemStack(Main.soupGlobal);
	 
	 
	 break;
 
 default: Messenger.send("No such kit.",reciever, Messenger.WARN);
 }

for(ItemStack l :(ItemStack[])content.toArray()){
	if (l == null) return ;
}

playerUser.changeKit(kit);

inv.setHelmet(head);
inv.setChestplate(chest);
inv.setLeggings(legs);
inv.setBoots(feet);
inv.setItem(0, sword);
inv.setItem(1, ability);

for(int i = 0;i<inv.getSize();i++){
	inv.addItem(Main.soupGlobal);
}
}*/









