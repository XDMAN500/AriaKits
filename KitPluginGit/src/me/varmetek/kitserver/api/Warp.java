package me.varmetek.kitserver.api;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Warp {
	private String id;
	private Material icon;
	private boolean kitChange;
	private Kit warpKit;
	public Warp(String ID, Material icon, boolean kitChange, Kit kit){
		id= ID;
		this.icon = icon;
		this.kitChange = kitChange;
		warpKit = kit;
		
	}
	
	public String getID(){
		return id;
	}
	public Location getLocation(){
		return DataManager.loadLocation(DataManager.WARP_LIST_FILE, id);
	}
	public boolean allowKitChange(){
		return kitChange;
	}
	public Kit getWarpKit(){
		return warpKit;
	}
	public Material getIcon(){
		if(icon == null){
			icon = Material.AIR;
		}
		return icon;
	
	}
	
	public void save(){
		DataManager.setWarpChangeKit(id, kitChange);
		DataManager.setWarpIcon(id, icon);
		DataManager.setWarpKit(id, warpKit);
		
	}

	public Warp setKitChange(boolean k){
		kitChange = k;
		return this;
	}
	public Warp setWarpKit(Kit k){
		warpKit = k;
		return this;
	}
	public Warp setIcon(Material ic){
		icon = ic;
		return this;
		
	}
	public Warp setLocation(Location loc){
		DataManager.saveLocation(DataManager.WARP_LIST_FILE,id, loc);
		return this;
	}
	public boolean exists(){
		return getLocation() != null;
	}
	
	
	public static Warp getWarp(String s){
		return DataManager.getWarp(s);
	}
	public static boolean isWarp(String s){
		return DataManager.doesWarpExist(s);
	}
	public static void openWarpGUI(Player pl){
		if(DataManager.getShopWarps().isEmpty())return;
		final User user = User.getUser(pl.getName());


		Set<ItemStack> itemList = new HashSet<ItemStack>();
		for(Warp w:DataManager.getShopWarps()){
			if(w.getIcon() != Material.AIR){
			ItemStack item = new ItemStack(w.getIcon());
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(
					Utils.colorCode("&6&l"+w.getID()));
			item.setItemMeta(im);
			itemList.add(item);
			}
		}
	
		
		user.setInv(Bukkit.createInventory(null, 36,Utils.colorCode("&4&lWarps&7...")));
		for(ItemStack i : itemList){
			user.getInv().addItem(i);
		}
		
		
		pl.openInventory(user.getInv());
		
	}
}
