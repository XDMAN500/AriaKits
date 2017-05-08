package me.varmetek.kitserver.api;

import java.util.List;

import me.varmetek.kitserver.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;



public abstract class BeamEffect {
	
	Player player;
	Material material;
	boolean hit;
	int range;
	Vector dir;
	public BeamEffect(Player player, Material material,int range){
		this.material = material;
		this.player = player;
		this.range = range;
	}
	
	public abstract void onHit(LivingEntity living, Location loc);
	public abstract void onHitHead(LivingEntity living, Location loc);
	public abstract void onHitWall(Location loc);
	
	Location loc;
	public BeamEffect start(){
		 dir = player.getLocation().getDirection().multiply(2);
		//player.getWorld().playSound(player.getLocation(), Sound.CREEPER_DEATH, 2f, 0f);
		//player.playSound(player.getLocation(), Sound.CREEPER_DEATH, 2f, 0f);
		
	
			
			@SuppressWarnings("deprecation")
			final
			List<Block> b = player.getLineOfSight(null, range);
			
		/*	new BukkitRunnable(){

				public void run(){
					if (b.iterator().hasNext()){
						Block block = b.iterator().next();
						loop(block);
						b.remove(block);
					}else this.cancel();
				}
		}.runTaskTimer(Main.getPluginInstance(), 0, 0);*/
				while(b.iterator().hasNext()){
					Block block = b.iterator().next();
					loc = block.getLocation();
					loop(block);
					b.remove(block);
					
				}
				
					//onHitWall(b.get(b.size()-1).getLocation());
				
			
	
		return this;
	
	}
	

	
	public void loop(Block block){
		final Location loc = block.getLocation();
		loc.getWorld().playEffect(loc, Effect.STEP_SOUND,material);
		//Player sn = loc.getWorld().spawn(loc, Player.class);
		//sn.setVelocity(dir);
		final Material bb = loc.getBlock().getType();
		 //loc.getBlock().setType(Material.LAPIS_BLOCK);
		for (Entity entity : player.getNearbyEntities(range*2, range*2, range*2)){
			if (entity instanceof LivingEntity){
				LivingEntity le = (LivingEntity) entity;
				boolean head = le.getEyeLocation().distance(loc) < .5;
				boolean body = le.getLocation().distance(loc) < 2;
				
				
				if (head){
					onHitHead(le,le.getLocation());	
				}else{
					if (body){
						onHit(le,le.getLocation());
					}
				}
			
			
			}
		}
	Bukkit.getScheduler().runTaskLater(Main.getPluginInstance(), new Runnable(){
			
			public void run(){
				loc.getBlock().setType(bb);
			}
		},2L);
			
		
	}
}
