package me.varmetek.kitserver.commands;

import me.varmetek.kitserver.api.DataManager;
import me.varmetek.kitserver.api.Kit;
import me.varmetek.kitserver.api.Messenger;
import me.varmetek.kitserver.api.Rank;
import me.varmetek.kitserver.api.User;
import me.varmetek.kitserver.main.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitSettingsCommand implements CommandExecutor {

	
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(sender instanceof Player){
			if(!User.getUser(sender.getName()).hasPermission(Rank.ModPlus)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.WARN);
				return false;
			}
		}
		int len = args.length;
	
		if(len == 0){
			Messenger.send("/kitsettings <kit>", sender, Messenger.WARN);
			return false;
		}else{
			Kit kit = Kit.getKitByName(args[0],true);
			if(kit == null){
				Messenger.send("No such kit", sender, Messenger.WARN);
				return false;
			}
			if(len == 1){
				Messenger.send("&9===&2&lKit&8- "+kit.getFullName() + " &9===", sender, Messenger.WARN);
				Messenger.send("&2Price&8: &a"+kit.getPrice(), sender,Messenger.INFO);
				Messenger.send("&2Rank&8: &a"+kit.getRequiredRank().name(), sender,Messenger.INFO);
				Messenger.send("&2Type&8: &a"+kit.getType().getColorCode()+kit.getType().name(), sender,Messenger.INFO);
				if(DataManager.isKitEnabled(kit)){
					Messenger.send("&2Enabled&8: &aon", sender,Messenger.INFO);
				}else{
					Messenger.send("&2Enabled&8: &coff", sender,Messenger.INFO);
				}
			}
			if(len >1){
				if(len == 2){
					if(args[1].equalsIgnoreCase("rank")){
						Messenger.send("/kitsettings <kit> rank <rank>", sender,Messenger.WARN);
						return false;
					}
					if(args[1].equalsIgnoreCase("price")){
						Messenger.send("/kitsettings <kit> price <num>", sender,Messenger.WARN);
						return false;
					}
					if(args[1].equalsIgnoreCase("enable")){
						Messenger.send("/kitsettings <kit> enable <on?>", sender,Messenger.WARN);
						return false;
					}
				}
				if(len >2){
					if(len == 3){
						if(args[1].equalsIgnoreCase("rank")){
							Rank ran = Rank.getRankByName(args[2]);
							if(ran == null){
								Messenger.send("No such rank.", sender,Messenger.WARN);
								return false;
							}
							DataManager.setKitRank(kit, ran);
							String msg = "&2Kit&a&o "+kit.getName()+" &a's rank has been set to&e&o "+ran.name()+" &2.";
							//msg.replaceAll("{k}", );
							//msg.replaceAll("{r}", );
							
							Messenger.send(msg, sender,Messenger.INFO);
						}
						if(args[1].equalsIgnoreCase("price")){
							try{
							int price = Integer.parseInt(args[2]);
							
							DataManager.setKitPrice(kit, price);
							String msg = "&2Kit&a&o "+kit.getName()+" &a's rank has been set to&e&o "+price+" &2.";
							//msg.replaceAll("{k}", kit.getName());
							//msg.replaceAll("{r}", price+"");
							
							Messenger.send(msg, sender,Messenger.INFO);
							}catch(NumberFormatException e){
								Messenger.send("No a valid number.", sender,Messenger.WARN);
								return false;
							}
						}
						if(args[1].equalsIgnoreCase("enable")){
						boolean on = args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true");
							DataManager.setKitEnabled(kit, on);
							
							String msg = on? "&2Kit&a&o "+kit.name()+"&a's is enabled.": "&2Kit&a&o "+kit.name()+" &a's is disnabled.";
							//msg.replaceAll("{k}", kit.getName());
							//msg.replaceAll("{r}", on+"");
							
							Messenger.send(msg, sender,Messenger.INFO);
						}
					}
				}
			}
		}
		
	
		return false;
	}

}
