package me.varmetek.kitserver.api;


import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;

public class BanEntry {
	
	private UUID id;
	private Date expire;
	private String reason;
	
	public BanEntry(UUID uuid,Date expireDate, String whyBan){
		id = uuid;
		expire = expireDate;
		reason = whyBan;
	}
	
	public UUID getUUID(){
		return id;
	}
	public Date getExpireDate(){
		return expire;
		
	}
	public String getReason(){
		return reason;
	}
	public String toString(){
		String date;
		if(expire == null){
			date = "E";
			
		}else{
			date = expire.toString();	
			
		}
		return id.toString() + "|" + date + "|" + reason.replaceAll("|", ":");
	}
	
	
	
	@SuppressWarnings("deprecation")
	public static BanEntry getBanEntry(String name){
		return getBanEntry(Bukkit.getOfflinePlayer(name).getUniqueId());
	}
	public static BanEntry getBanEntry(UUID id){
		for(BanEntry be : DataManager.getBanEntryList()){
			if(be.id.equals(id)){
				
				return be;
			}
		}
	
		return null;
		
	}
	@SuppressWarnings("deprecation")
	public static Date convertDate(String el){
		try{
			
			Calendar cal = Calendar.getInstance();
			
			cal.setTimeInMillis(Date.parse(el));
			return cal.getTime();
		}catch(IllegalArgumentException e){
			return null;
		}
		
	}
	
}
