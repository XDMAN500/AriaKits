package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;


public class InvseeCommand implements CommandExecutor, Listener{
	Player plTarg ;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			final Player pl = (Player)sender;
			if(args.length == 1){
				 plTarg = Bukkit.getPlayer(args[0]);
				
				 
				 
				 if(plTarg == null){
					Messenger.send("That player is not online.", sender, Messenger.WARN);
					return false;
				}
				 User.getUser(pl.getName()).setInvSee(true);
				 pl.openInventory(plTarg.getInventory());
			}
		}
	return false;
	}
	
	
	@EventHandler
	public void click(InventoryClickEvent ev){
	
		Player pl = (Player)ev.getWhoClicked();
		User user = User.getUser(pl.getName());
		if(!user.isInvSee())return;
		if(ev.getInventory() == null)return;
		
		if(ev.getInventory() instanceof PlayerInventory){
			PlayerInventory modifiedInv =  (PlayerInventory) ev.getClickedInventory();
			if(pl.getInventory().equals(modifiedInv)) return;
			
			plTarg =(Player) modifiedInv.getHolder();
			if( !(user.hasPermission(Rank.Admin)) ){
				ev.setCancelled(true);
			}
			
		}	
			
	

			
	
	}
				

}
/*Player plTarg ;

final Map<Integer,Integer> translateToCustomInv = new HashMap<Integer,Integer>();
final Map<Integer,Integer> translateFromCustomInv = new HashMap<Integer,Integer>();	
ItemStack[] invMiddleMan = new ItemStack[36];
 ItemStack[] armorMiddleMan = new ItemStack[4];
 boolean invUpdate = true;

public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	 for(int i = 9; i<36; i++){
			translateToCustomInv.put(i, i-9);
		}
		
		for(int i = 0; i<9; i++){
			translateToCustomInv.put(i, i+27);
		}
		
		for(int i = 0; i<27; i++){
				translateFromCustomInv.put(i, i+9);
				Main.debug(translateFromCustomInv.get(i)+"");
			}
			
		for(int i = 27; i<36; i++){
				translateFromCustomInv.put(i, i-27);
				Main.debug(translateFromCustomInv.get(i)+"");
			}
	///final Map<Integer,Integer> translate = t;
	if(sender instanceof Player){
		final Player pl = (Player)sender;
		final User plUser = User.getUser(pl.getName());
		if(args.length == 1){
			 plTarg = Bukkit.getPlayer(args[0]);
			
			 
			 
			 if(plTarg == null){
				Messenger.send("That player is not online.", sender, Messenger.WARN);
				return false;
			}
			
		
		
			final User u = User.getUser(((Player)sender).getName());
			u.setInv(Bukkit.createInventory(null, 54, plTarg.getName()+"'s Inventory"));
			((Player)sender).openInventory(u.getInv());
			
			Wool wool; 
			ItemStack item;
			
			if(plUser.hasPermission(Rank.Admin)){
				wool = new Wool(DyeColor.GREEN);
				 item = wool.toItemStack(1);
				 item.getItemMeta().setDisplayName(Main.replaceColorCodes("&cYou can edit this inventory."));
			}else{
				wool = new Wool(DyeColor.RED);
				 item = wool.toItemStack(1);
				 item.getItemMeta().setDisplayName(Main.replaceColorCodes("&cYou can't edit this inventory."));
				 //item.getItemMeta().setDisplayName("You can't edit this inventory.");
			}
			
			for(int i = u.getInv().getSize()-1; i>u.getInv().getSize()-10;i--){
				
				u.getInv().setItem(i, item);
			}
			PlayerInventory invP = plTarg.getInventory();
			invMiddleMan=invP.getContents();
			armorMiddleMan=invP.getArmorContents();
			//invP.setContents(invMiddleMan);
			//invP.setArmorContents(armorMiddleMan);
			/*for(int i = 0; i< invP.getSize();i++){
				 u.getInv().setItem(translateToCustomInv.get(i), invP.getItem(i));
			}
			
			 u.getInv().setItem(36, invP.getHelmet());
			 u.getInv().setItem(37, invP.getHelmet());
			 u.getInv().setItem(38, invP.getHelmet());
			 u.getInv().setItem(39, invP.getHelmet());
			
			Bukkit.getScheduler().runTaskTimer(Main.getPluginInstance(), new Runnable(){
			final User user = u;
		
			public void run(){
				if(!invUpdate)return;
				if(u.getInv()== null){return;}
				PlayerInventory invP = plTarg.getInventory();
				
				invP.setContents(invMiddleMan);
				invP.setArmorContents(armorMiddleMan);
				for(int i = 0; i< invMiddleMan.length;i++){
					 u.getInv().setItem(translateToCustomInv.get(i), invMiddleMan[i]);
				}
				
				 u.getInv().setItem(36, armorMiddleMan[0]);
				 u.getInv().setItem(37, armorMiddleMan[1]);
				 u.getInv().setItem(38, armorMiddleMan[2]);
				 u.getInv().setItem(39, armorMiddleMan[3]);
				
				 invUpdate =false;
				
			}
		}, 2L, 20L);
			
		
			
			
		}
	}
	return false;
}
@EventHandler
public void click(InventoryClickEvent ev){
	Main.debug("YAy");
	final Player pl = (Player)ev.getWhoClicked();

	for(int i = 0; i<27; i++){
		translateFromCustomInv.put(i, i+9);
		//Main.debug(translateFromCustomInv.get(i)+"");
	}
	
	for(int i = 27; i<36; i++){
		translateFromCustomInv.put(i, i-27);
		//Main.debug(translateFromCustomInv.get(i)+"");
	}
	for(int i = 9; i<36; i++){
	translateToCustomInv.put(i, i-9);
	}

	for(int i = 0; i<9; i++){
		translateToCustomInv.put(i, i+27);
	}
	

	final Inventory modifiedInv =  ev.getClickedInventory();
	String[] all =ev.getInventory().getTitle().split("'");
	plTarg = Bukkit.getPlayer(all[0]);
	
	if(pl.getInventory().equals(modifiedInv)){
		invMiddleMan = modifiedInv.getContents();
		armorMiddleMan = ((PlayerInventory)modifiedInv).getArmorContents();
		Main.debug("Players own");
		invUpdate = true;
	}else{
		Main.debug("Players outside");
		if(ev.getInventory().getTitle().equalsIgnoreCase( plTarg.getName()+"'s Inventory")){
			Main.debug("Players outside ready");
			
			//Main.debug(translateFromCustomInv.get(1)+"");
			Main.debug(modifiedInv.getItem(translateFromCustomInv.get(1)).getType()+"");
		
			for(int i = 0; i< 36; i++){
			invMiddleMan[i]= modifiedInv.getItem(translateFromCustomInv.get(i));
		}
		
	Bukkit.getScheduler().runTask(Main.getPluginInstance(),new Runnable(){
		public void run(){
			armorMiddleMan[0] = modifiedInv.getItem(36)	;	
			armorMiddleMan[1] = modifiedInv.getItem(37)	;		
			armorMiddleMan[2] = modifiedInv.getItem(38)	;		
			armorMiddleMan[3] = modifiedInv.getItem(39)	;
		
			Main.debug(Arrays.asList(armorMiddleMan)+"");
			invUpdate = true;
		}
		
	});

		}
	}*/
	/*final Player player = (Player)ev.getWhoClicked();
	//User user = User.getUser(plTarg.getName());
	ItemStack item = ev.getCurrentItem();
	int slot = ev.getSlot();
	String[] all =ev.getInventory().getTitle().split("'");
	plTarg = Bukkit.getPlayer(all[0]);
	Main.debug(ev.getInventory().getTitle()+"|"+plTarg.getName()+"'s Inventory");

	
	if(ev.getInventory().getTitle().equalsIgnoreCase( plTarg.getName()+"'s Inventory")){
		Main.debug("YAy");
		
		
		if(slot <36){
			plTarg.getInventory().setItem(slot,ev.getInventory().getItem(slot));
			plTarg.updateInventory();
		}
	}
}*/