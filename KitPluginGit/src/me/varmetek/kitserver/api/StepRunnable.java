package me.varmetek.kitserver.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;

public class StepRunnable {
	public class CleanUserSet implements Runnable
	{

		@Override
		public void run() {
			Collection<? extends User> values = User.users.values();
			
			for(User user:values){
				if(!user.getPlayer().isOnline()){
					user.remove();
				}
			}
			
		}
		

	}
	
	public class UpdateAllScoreboards implements Runnable
	{
		Collection<? extends User> values = User.users.values();
		@Override
		public void run() {
			for(User user:values){
				if(user.getPlayer().isOnline()){
					user.updateScoreBoard();
				}
			}
			
		}
		

	}
	
	public class UpdateEXPBar implements Runnable{
		Collection<? extends User> values = User.users.values();
		@Override
		public void run() {
			for(User user:values){
				if(user.getPlayer().isOnline()){
					Player p = (Player)user.getPlayer();
					p.setLevel(user.getLevel());
					p.setExp(user.getProgress());
				}
			}
			
		}
	}
	
	public class Tps implements Runnable {
		private final  List<Long> timeList = new ArrayList<Long>();
		private double medium; 
		private  double mod;
		@Override
		public void  run() {
			
			int size = timeList.size();
			if(size < 21){
				timeList.add(System.currentTimeMillis());
			}else{
				timeList.remove(0);
				timeList.add(System.currentTimeMillis());
			}
		
			//Main.broadcast(String.format("%.3f",(getTps()) ));
		}
		
		public double getTps(){
			double tps;
			long allValues = 0;

			for(int i = 0; i< timeList.size() ;i++){
				allValues += timeList.get(i);
				
			}
			medium =  allValues/20;
			 mod = medium % 20;
			//Main.broadcast(mod+"/"+medium);
			if(mod == 0){
				tps = 20D;
			}else{
				tps = (double)(20D*(mod/medium))	 ;
			}
			
		
			return tps;
		}

	}
}
