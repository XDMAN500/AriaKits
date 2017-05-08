package me.varmetek.kitserver.api;

import java.util.Random;

public class DuelRequest{
	private DuelManager.FightStyle fs;
	private boolean recrafts ;
	private boolean refills;
	public DuelRequest( DuelManager.FightStyle fs, boolean refills,boolean recrafts){
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
	public DuelRequest setRecrafts(boolean rec){
		recrafts = rec;
			return this;
	}
	public DuelRequest setRefills(boolean rec){
		refills = rec;
		return this;
	}
	public  static DuelRequest getRandomDuelRequest(){
		Random ran = new Random();
		int rf = ran.nextInt(2);
		int rc = ran.nextInt(2);
	//	Main.debug(rf+":"+rc);
		return  new DuelRequest(DuelManager.getRandomFightStyle(),
				Utils.NumberToBoolean(rf), 
				Utils.NumberToBoolean(rc)
				);

	}
}
