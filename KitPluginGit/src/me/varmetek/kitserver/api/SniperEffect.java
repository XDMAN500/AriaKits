package me.varmetek.kitserver.api;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;



public abstract class SniperEffect {
	public LivingEntity en;
	int range;
	
	public SniperEffect(LivingEntity eplayer, int erange){
		this.en = eplayer;
		this.range = erange;
	
		
	}
	
	
	private double compareLoc(Location loc1, Location loc2){
        double dist = 0;
        dist += Math.abs(loc1.getBlockX() - loc2.getBlockX());
        dist += Math.abs(loc1.getBlockY() - loc2.getBlockY());
        dist += Math.abs(loc1.getBlockZ() - loc2.getBlockZ());
        return dist;
	}
	

	public abstract void onHitHead(LivingEntity e,Location loc);
	public abstract void onHit(LivingEntity e,Location loc);
	public abstract void onHitWall(Location loc);
	
	@SuppressWarnings("deprecation")
	public SniperEffect start(){
		//player.sendMessage("BOOM");
		int dist = 0;
		//int range = 150;
		
		World world = en.getWorld();
		
		double x = en.getLocation().getX();
		double y = en.getEyeLocation().getY();
		double z = en.getLocation().getZ();
		
		double StartX = en.getEyeLocation().getX();
		double StartY = en.getEyeLocation().getY();
		double StartZ = en.getEyeLocation().getZ();
		
		Location CurrentLoc = new Location(en.getWorld(), x, y, z);
		
		Location EndLoc = en.getTargetBlock(null, range).getLocation();
		
		double EndX = EndLoc.getX()+.5;
		double EndY = EndLoc.getY()+.5;
		double EndZ = EndLoc.getZ()+.5;
		
			
			world.playSound(CurrentLoc, Sound.CREEPER_DEATH, 7, 1);
		
		while(dist <=range){
			
			//player.sendMessage(dist+" loop "+range);
			//to help reduce lag on long ranges
			x-= (StartX-EndX)/Math.max(range/10,1);
			y-= (StartY-EndY)/Math.max(range/10,1);
			z-= (StartZ-EndZ)/Math.max(range/10,1);
		
			
			CurrentLoc.setX(x);
			CurrentLoc.setY(y);
			CurrentLoc.setZ(z);
			
			{
				Location tloc = new Location( CurrentLoc.getWorld(),Math.round(CurrentLoc.getX()),Math.round(CurrentLoc.getY()),Math.round(CurrentLoc.getZ()));
				world.playEffect(tloc, Effect.HAPPY_VILLAGER, 0);
			}
			//3 values cubed = 27
			dist += ((Math.abs(StartX-x))+(Math.abs(StartY-y))+(Math.abs(StartZ-z)))/27;
			
		
			if(!CurrentLoc.getBlock().getType().isSolid()){
				
				List<Entity> targetList = en.getNearbyEntities(range*2, range*2, range*2);
			
				for(Entity entity: targetList){
					
					if(entity instanceof LivingEntity){
				
						LivingEntity target = (LivingEntity)entity;
						
						boolean head = (compareLoc(target.getEyeLocation(),CurrentLoc) <= .3 );
						boolean body = (compareLoc(target.getLocation(),CurrentLoc) <= .7 );
					
						if(head || body){
							if(head){
								this.onHitHead(target,CurrentLoc);
							}else{
								if(body){
									this.onHit(target,CurrentLoc);
									}
								}
							return this;	
						}
					
						}
						
				}
		}else{
			
			this.onHitWall(CurrentLoc);
			break;
			
		}
	
		
		
		}
		
		return this;
	}

}