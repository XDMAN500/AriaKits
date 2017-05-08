package me.varmetek.kitserver.api;

import java.util.Random;

import me.varmetek.kitserver.main.Main;

public class BattleRequest{
	private DuelManager.FightStyle fs;
	private boolean recrafts ;
	private boolean refills;
	public BattleRequest( DuelManager.FightStyle fs, boolean refills,boolean recrafts){
		this.fs = fs;
		this.recrafts= recrafts;
		this.refills = refills;
	}
	public DuelManager.FightStyle  getFightStyle(){
		return  fs;
	}
	public boolean isRecrafts(){
		return recrafts;
	}
	public boolean isRefills(){
		return refills;
	}
	
	public  static BattleRequest getRandomBattleRequest(){
		Random r = new Random();
		int rf = r.nextInt(2);
		int rc = r.nextInt(2);
		Main.debug(rf+":"+rc);
		return  new BattleRequest(DuelManager.getRandomFightStyle(),
				Main.NumberToBoolean(rf), 
				Main.NumberToBoolean(rc)
				);
		
		
	}
}
