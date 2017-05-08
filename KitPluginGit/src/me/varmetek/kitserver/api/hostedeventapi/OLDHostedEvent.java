package me.varmetek.kitserver.api.hostedeventapi;



public abstract class OLDHostedEvent{
	/*public static OLDHostedEvent currentHostedEvent = null;
	public static final int kothMaxScore = 60;
	public enum EventType{
		BRACKETS(2),
		CLANWAR(4),
		KOTH(1,EventTypeKoth.class),
		LMS(2);
		
		
		int minPlayers;
		Class<?> type;
		private EventType(int minp, Class<?> a){
			minPlayers = minp;
			type = a;
		}
	
		public static EventType getEventTypeByName(String eType){
			
			for(EventType temp : EventType.values()){
				if(temp.name().toLowerCase().equalsIgnoreCase(eType)){
					return temp;
				}
			}
			return null;
		}
	}
	
	public enum FightType{
		NORMAL,STONE,BOW;
		public static FightType getEventTypeByName(String fType){
			
			for(FightType temp : FightType.values()){
				if(temp.name().toLowerCase().equalsIgnoreCase(fType)){
					return temp;
				}
			}
			return null;
		}
	}

	public final List<UUID> team1 = new ArrayList<UUID>();
	public final List<UUID> team2 = new ArrayList<UUID>();
	public final List<UUID> fighting = new ArrayList<UUID>();
	public final Map<UUID,Integer> points = new HashMap<UUID,Integer>();
	public EventType eType;
	public FightType fType;
	boolean allowRefills;
	boolean allowRecrafts;	
	short secondsLeft = 60;
	protected boolean started = false;
	protected Set<UUID> enrolledPlayers = new HashSet<UUID>();
	
	
	public static OLDHostedEvent getCurrent(){
		return currentHostedEvent;
	}
	
	public int getMinimumPlayers(){
		return getCurrent().getEventType().minPlayers;
		
	}
	
	public EventType getEventType(){
		return getCurrent().eType;
	}
	
	public FightType getFightType(){
		return getCurrent().fType;
	}
	
	public boolean doesAllowRefills(){
		return getCurrent().allowRefills;
	}
	
	public boolean doesAllowRecafts(){
		return getCurrent().allowRecrafts;
	}
	
	public boolean isStarted(){
		return getCurrent().started;
	}
	
	public Set<UUID> getEnrolledPlayers(){
		return getCurrent().enrolledPlayers;
	}
	public static boolean startEvent(EventType e, FightType f, boolean r, boolean rc){
		if(currentHostedEvent != null){
			return false;
		}
		try {
			currentHostedEvent = (OLDHostedEvent) e.type.newInstance();
			currentHostedEvent.eType = e;
			currentHostedEvent.fType = f;
			currentHostedEvent.allowRefills = r;
			currentHostedEvent.allowRecrafts = rc;
			currentHostedEvent.secondsLeft = 60;

			countdown();
			return true;
		} catch (InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	
		
	}
	
	public void stop(){
		
		if(started){
			Main.broadcast("&e"+eType.name()+" is finished.");
			for(UUID e: getEnrolledPlayers()){
				Player pl =Bukkit.getPlayer(e);
				User user= User.getUser(pl.getName());
				user.setGameMode(User.GameMode.SPECTATOR);
				user.setGameMode(User.GameMode.PLAYER);
				//pl.getInventory().setContents(null);
				//pl.getInventory().setArmorContents(null);
				//user.changeKit(null);
				//pl.teleport(DataManager.getWarp("spawn"));
				
			}
		}else{
			Main.broadcast(eType.name()+" has been canceled.");
		}
	
		currentHostedEvent = null;
	}
	
	public static void countdown(){
	
		new BukkitRunnable(){
			public void run(){
				
				if(getCurrent() == null){
					this.cancel();
				}
				
				if(Main.isIntInList( getCurrent().secondsLeft, 60,30,10,5))
					Main.broadcast("&e" + getCurrent().getEventType().name() + " will begin in " + getCurrent().secondsLeft + " seconds. /ev j to join.");
				
				if(getCurrent().secondsLeft== 0 ){
					if(getCurrent().getEnrolledPlayers().size() <getCurrent().getMinimumPlayers()){
						Main.broadcast(getCurrent().getEventType().name() + " didn't have enough players to start.");
						currentHostedEvent = null;
						this.cancel();
					
					}else{
						Main.broadcast(getCurrent().getEventType().name()+" has begun.");
						getCurrent().start();
						this.cancel();
					}
					this.cancel();
				}
				if(getCurrent() !=null){
					getCurrent().secondsLeft -=1;
				}
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 20);
		
	}
	protected abstract void update() ;
	protected void start(){
		started = true;
		for(UUID e: enrolledPlayers){
		
			Player pl = Bukkit.getPlayer(e);
			if(DuelManager.getMatchedPlayers().contains(e)){
				Messenger.send("You have been kicked from the event for being in a 1v1 battle.",pl, Messenger.WARN);
				removePlayer(pl);
				return;
			}
			if(DuelManager.getQueuedPlayers().contains(e)){
				Messenger.send("You have been kicked from the event for being in the 1v1 queue.",pl, Messenger.WARN);
				removePlayer(pl);
				return;
			}
			User user = User.getUser(pl.getName());
			user.changeKit(null);
			user.setGameMode(User.GameMode.PLAYER);
			pl.getInventory().setArmorContents(Main.getEmptyInv(4));
			pl.getInventory().setContents(Main.getEmptyInv(36));
			
			giveInventory(pl);
			
		}
		switch(eType){
		case BRACKETS:
			for(UUID e: enrolledPlayers){
				if(Bukkit.getPlayer(e) == null){
					enrolledPlayers.remove(e);
				}else{
					Bukkit.getPlayer(e).teleport(DataManager.getLoc("brack-start"));
		
				}
			}
			Iterator<UUID> i = enrolledPlayers.iterator();
			UUID eid =i.next();
			fighting.add(eid);
			Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point1"));
			
			eid =i.next();
			fighting.add(eid);
			Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point2"));
			break;
		case CLANWAR:
			for(UUID e: enrolledPlayers){
				if(Bukkit.getPlayer(e) == null){
					enrolledPlayers.remove(e);
				}else{
					if(enrolledPlayers.size() % 2 !=  0){
						enrolledPlayers.remove(e);
					}
					//Bukkit.getPlayer(e).teleport(DataManager.getLoc("brack-start"));
				}
			}
			
			List<UUID> l = new ArrayList<UUID>(new HashSet<UUID>(enrolledPlayers));
			
			for(int f = 0 ; f< l.size(); f++){
				if(f+1 <=  l.size()/2){
					team1.add(l.get(f));
				}else{
					team2.add(l.get(f));
				}
				
				for(UUID e: team1){
					Bukkit.getPlayer(e).teleport(DataManager.getLoc("cw-point1"));
				}
				for(UUID e: team2){
					Bukkit.getPlayer(e).teleport(DataManager.getLoc("cw-point2"));
				}
			}
			break;
		case KOTH:
			/*for(UUID e: enrolledPlayers){
				if(Bukkit.getPlayer(e) == null){
					enrolledPlayers.remove(e);
				}else{
					points.put(e, 0);
					Bukkit.getPlayer(e).teleport(DataManager.getLoc("koth-start"));
				}
			}
			checkKOTH();*/
/*			break;
		case LMS:
			for(UUID e: enrolledPlayers){
				if(Bukkit.getPlayer(e) == null){
					enrolledPlayers.remove(e);
				}else{
					Bukkit.getPlayer(e).teleport(DataManager.getLoc("lms-start"));
				}
			}
			break;
		
		}
		
	}


	public void removePlayer(Player pl){
	getCurrent().getEnrolledPlayers().remove(pl.getUniqueId());
	getCurrent().team1.remove(pl.getUniqueId());
	getCurrent().team2.remove(pl.getUniqueId());
	getCurrent().points.remove(pl.getUniqueId());
	
	getCurrent().fighting.remove(pl.getUniqueId());
	checkBrackets();
	checkClanWar();
	checkLMS();
}

	public void addPlayer(Player pl){
	getCurrent().getEnrolledPlayers().add(pl.getUniqueId());
	
}

	public static void  checkBrackets(){
	if(getCurrent() == null)return;
	Main.debug("Event is on");
	if(!getCurrent().isStarted())return;
	Main.debug("Event is started");
	if(getCurrent().getEventType() != EventType.BRACKETS)return;
	Main.debug("Event is brackets");
//	Iterator<UUID> i = getCurrent().getEnrolledPlayers().iterator();
	
	if(getCurrent().getEnrolledPlayers().size() <2 ){
		Player pl =Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0));
		 //pl =Bukkit.getPlayer(currentHostedEvent.fighting.get(0));
		Main.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
		//pl.performCommand("spawn");
		getCurrent().stop();
		
		return;
		
	}
	Main.debug("not over yet");
	if(getCurrent().fighting.size() < 2){
		if(!getCurrent().fighting.isEmpty()){
			UUID id = getCurrent().fighting.get(0);
			Player winner = Bukkit.getPlayer(id);
			
			getCurrent().fighting.remove(id);
			winner.teleport(DataManager.getLoc("brack-start"));
			Main.broadcast(winner.getName() + " has advanced to the next round." );

			winner.getInventory().setArmorContents(null);
			winner.getInventory().setContents(null);
		}
		
		
		//Iterator<UUID> i = currentHostedEvent.enrolledPlayers.iterator();
		UUID eid ;
		eid =new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0);
		Player p1 = Bukkit.getPlayer(eid);
		getCurrent().fighting.add(eid);
	
		p1.teleport(DataManager.getLoc("brack-point1"));
		getCurrent().giveInventory(p1);
	
		
		eid =new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(1);
		Player p2 = Bukkit.getPlayer(eid);
		getCurrent().fighting.add(eid);
		p2.teleport(DataManager.getLoc("brack-point2"));
		getCurrent().giveInventory(p2);
		
		Main.broadcast(p1.getName() + " vs " + p2.getName());
	}
}
	public static void  checkKOTH(){
		
		new BukkitRunnable(){
			public void run(){
				if(getCurrent() == null){this.cancel(); return;}
				if(!getCurrent().isStarted()){this.cancel(); return;}
			
				if(getCurrent().getEnrolledPlayers().size() < getCurrent().getMinimumPlayers() ){
					Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().getEnrolledPlayers()).get(0)).performCommand("spawn");
					getCurrent().stop();
					return;
				}
			
				for(UUID u: getCurrent().getEnrolledPlayers()){
					Player pl = Bukkit.getPlayer(u);
					if(!pl.isDead()){
						Block b = pl.getLocation().add(0, -0.1, 0).getBlock();
				
						if(b.getType() == Material.DIAMOND_BLOCK){
							getCurrent().points.put(u,getCurrent().points.get(u)+1);
							pl.getWorld().playEffect(pl.getEyeLocation(), Effect.HAPPY_VILLAGER,0);
							pl.getWorld().playSound(pl.getLocation(), Sound.ORB_PICKUP, 1, 1);
						}
				
						int score =  getCurrent().points.get(u);
					
						if(score % 10 == 0 && score != 0 && b.getType() == Material.DIAMOND_BLOCK){
							Messenger.sendGroupU("&5&l&o" + getCurrent().getEventType().name()+"&7&l>> " + "&6"+pl.getName() + " &bhas reached a score of " + "&a"+score + "&b/" + kothMaxScore + "." ,
									new ArrayList<UUID>(getCurrent().getEnrolledPlayers()), Messenger.INFO);
						}
				
						if(score >= kothMaxScore){
							Main.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
							getCurrent().stop();
							this.cancel();
							break;
						}
					}
				}
			}
		}.runTaskTimer(Main.getPluginInstance(), 0, 20);
	}
	
	public static void checkClanWar(){
		if(getCurrent() == null)return;
		if(!getCurrent().isStarted())return;
		if(getCurrent().getEventType() != EventType.CLANWAR)return;
		
		if(getCurrent().team1.isEmpty()){
			
			for(UUID u:getCurrent().team2){
				Player pl = Bukkit.getPlayer(u);
				pl.performCommand("spawn");
				Messenger.send("You have won " + getCurrent().getEventType().name(), pl, Messenger.INFO);
			}
			getCurrent().stop();
		}
		
		if(getCurrent().team2.isEmpty()){
			
			for(UUID u : getCurrent().team1){
				Player pl = Bukkit.getPlayer(u);
				pl.performCommand("spawn");
				Messenger.send("You have won " + getCurrent().getEventType().name(), pl, Messenger.INFO);
			}
			
			getCurrent().stop();
		}
	}
	
	public static void checkLMS(){
		if(getCurrent() == null)return;
		if(!getCurrent().isStarted())return;
		if(getCurrent().getEventType() != EventType.LMS)return;
	
		if(getCurrent().getEnrolledPlayers().size() < getCurrent().getMinimumPlayers() ){
			Player pl =Bukkit.getPlayer(new ArrayList<UUID>(getCurrent().enrolledPlayers).get(0));
			pl.performCommand("spawn");
			Main.broadcast(pl.getName() + " has won " + getCurrent().getEventType().name() + "." );
			getCurrent().stop();
			return;
			
		}
	}
	
	


	@SuppressWarnings("deprecation")
	protected void giveInventory(Player pl){
		///Main.clearInv(pl);
		pl.getInventory().setArmorContents(Main.getEmptyInv(4));
		pl.getInventory().setContents(Main.getEmptyInv(36));
		/*User user  = User.getUser(pl.getName());
		user.changeKit(Kit.PVP);
		Color c = Color.GRAY;
			ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);	
			LeatherArmorMeta lm = (LeatherArmorMeta) armor.getItemMeta();
			lm.setColor(c);
		switch (getCurrent().getFightType()){
		case BOW:
			pl.getInventory().setItem(0, new ItemStack(Material.BOW));	
			pl.getInventory().setItem(9, new ItemStack(Material.ARROW,64));	
			
			break;
		case NORMAL:
			pl.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));	
		
			
	
			break;
		case STONE:
			pl.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
			break;
	
		}
		ItemStack wool = new ItemStack(Material.WOOL,1,DyeColor.GREEN.getData());
	{
		if(HostedEvent.getCurrent().getEventType() == HostedEvent.EventType.CLANWAR){
		
			if(getCurrent().getEventType() == EventType.CLANWAR){
				c = Color.RED;
				if(team1.contains(pl.getUniqueId())){
					wool= new ItemStack(Material.WOOL,1,DyeColor.RED.getData());
				}else{
					c = Color.BLUE;
					if(team2.contains(pl.getUniqueId()))
					wool= new ItemStack(Material.WOOL,1,DyeColor.BLUE.getData());
				}
			}
		
		
		
		}
	}
	if(HostedEvent.getCurrent().getFightType() == HostedEvent.FightType.STONE){
		pl.getInventory().setArmorContents(new ItemStack[] {});
		if(HostedEvent.getCurrent().getEventType() == HostedEvent.EventType.CLANWAR )
			pl.getInventory().setHelmet(wool);
	}else{
		lm.setColor(c);
		armor.setItemMeta(lm);
		pl.getInventory().setHelmet(armor);
		pl.getInventory().setChestplate(armor);	
		pl.getInventory().setLeggings(armor);	
		pl.getInventory().setBoots(armor);
	}
		if(getCurrent().doesAllowRefills()){
			while( Arrays.asList(pl.getInventory().getContents()).contains(null)){
				pl.getInventory().addItem(Main.GLOBAL_SOUP);
			}
		}else{
			for(int i = 0; i< 8; i++){
				pl.getInventory().addItem(Main.GLOBAL_SOUP);
			}
		}
		if(getCurrent().doesAllowRecafts()){
			pl.getInventory().setItem(pl.getInventory().getSize()-1, new ItemStack( Material.RED_MUSHROOM,64));
			pl.getInventory().setItem(pl.getInventory().getSize()-2, new ItemStack(Material.BOWL,64));
			pl.getInventory().setItem(pl.getInventory().getSize()-3, new ItemStack(Material.BROWN_MUSHROOM,64));
		}*/
	/*}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent ev){
		UUID id = ev.getEntity().getUniqueId();
		
		if(getCurrent() == null)return;
		if(!getCurrent().isStarted())return;
		
		if(getCurrent().getEventType() != EventType.KOTH){
			getCurrent().removePlayer(Bukkit.getPlayer(id));
			
		}
	}
	
	
	
	@EventHandler
	public void onEventRespawn(final PlayerRespawnEvent ev){
		
		final Player pl = ev.getPlayer();
		pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20,1));
		User user= User.getUser(pl.getName());
		/*if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId())){
			ev.setRespawnLocation(pl.getEyeLocation());
			user.setGameMode(User.GameMode.SPECTATOR);
			//Main.clearInv(pl);
			pl.setVelocity(new Vector(0,.2,0));
	//	pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,10,1));
			//Main.updateInv(pl);
			return;
		}*/
/*		ev.setRespawnLocation(DataManager.getWarp("spawn"));
		user.setGameMode(User.GameMode.PLAYER);
		Main.giveStartItems(pl);
		pl.setVelocity(new Vector(0,0,0));
		if(currentHostedEvent == null)return;
	
		if(!getCurrent().isStarted())return;
		
		if(!getCurrent().getEnrolledPlayers().contains(pl.getUniqueId())) return;
		
		if(getCurrent().getEventType() == EventType.KOTH){
			//user.setGameMode(User.GameMode.PLAYER);
			
			ev.setRespawnLocation(DataManager.getLoc("koth-start"));
		
			new BukkitRunnable(){
				public void run(){
					pl.teleport(DataManager.getLoc("koth-start"));
					giveInventory(pl);
				}
			}.runTask(Main.getPluginInstance());
			
			/*new BukkitRunnable(){
				public void run(){
					
				}
			}.runTask(Main.getPluginInstance());*/
/*		}
		
	}

	@EventHandler
	public void onDamageEV(EntityDamageByEntityEvent ev){
		if(getCurrent() == null)return;
		if(!getCurrent().isStarted())return;
		
		if(ev.getEntity() instanceof Player && ev.getEntity() instanceof Player){
			Player damagee = (Player)ev.getEntity();
			Player damager = (Player)ev.getDamager();
			UUID deUUID = damagee.getUniqueId();
			UUID drUUID = damager.getUniqueId();
			
			if( (getCurrent().getEnrolledPlayers().contains(deUUID) && !getCurrent().getEnrolledPlayers().contains(drUUID)) ||
					(getCurrent().getEnrolledPlayers().contains(drUUID) && !getCurrent().getEnrolledPlayers().contains(deUUID))	){
				ev.setCancelled(true);
				return;
				
			}
			
			if(getCurrent().getEventType() == EventType.CLANWAR){
				if( (getCurrent().team1.contains(deUUID) && getCurrent().team1.contains(drUUID)) ||
						(getCurrent().team2.contains(deUUID) && getCurrent().team2.contains(drUUID))){
					ev.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent ev){
		if(getCurrent() == null)return;
		if(!getCurrent().isStarted())return;
		
		if(getCurrent().getEnrolledPlayers().contains(ev.getPlayer().getUniqueId())){
			getCurrent().removePlayer(ev.getPlayer());
			ev.getPlayer().setHealth(0.0);
		}
		
	}
	
	*/
	
}
/*if(currentHostedEvent.enrolledPlayers.size() <2){
Bukkit.getPlayer(l.get(0)).performCommand("spawn");
currentHostedEvent.cancelEvent();

}else{
currentHostedEvent.fighting.remove(id);
currentHostedEvent.fighting.remove(l.get(0));
Bukkit.getPlayer(l.get(0)).teleport(DataManager.getLoc("brack-start"));
Iterator<UUID> i = currentHostedEvent.enrolledPlayers.iterator();
UUID eid =i.next();
currentHostedEvent.fighting.add(eid);
Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point1"));

eid =i.next();
currentHostedEvent.fighting.add(eid);
Bukkit.getPlayer(eid).teleport(DataManager.getLoc("brack-point2"));
}*/