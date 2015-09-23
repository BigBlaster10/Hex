package net.pixelderp.hexagon;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MainHexagon extends JavaPlugin{

	World world;
	Location startLoc;
	public static Plugin plugin;
	
	public void onEnable(){
		plugin = this;
		world = Bukkit.getWorld("world");
		startLoc = new Location(world, 123, 91, 69);
		Bukkit.getPluginManager().registerEvents(new CreateHex(), this);
		
		GrassHex.intitalize();
		
		
		 Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			 @Override
			 public void run() {
	             for(Player player : Bukkit.getOnlinePlayers()){
	            	 if(isOnPlatform(player) != null){
	            		 Hex hex = isOnPlatform(player);
	            		 if(!(hex.getNeighbors().size() < 6)) continue;
	            		 Location loc = hex.getLocation();
	            		 if(!(Hex.isHex(loc.clone().add(8,0,0)))){
	            			 new GrassHex(loc.clone().add(8, random1Int(0),0)).create();	            			
	            		 }else if(!(Hex.isHex(loc.clone().add(-8,0,0)))){
	            			 new GrassHex(loc.clone().add(-8,-random1Int(0),0)).create();
	            		 }else if(!(Hex.isHex(loc.clone().add(4,0,-7)))){
	            			 new GrassHex(loc.clone().add(4,-random1Int(0),-7)).create();
	            		 }else if(!(Hex.isHex(loc.clone().add(4,0,7)))){
	            			 new GrassHex(loc.clone().add(4,-random1Int(0),7)).create();
	            		 }else if(!(Hex.isHex(loc.clone().add(-4,0,-7)))){
	            			 new GrassHex(loc.clone().add(-4,-random1Int(0),-7)).create();
	            		 }else if(!(Hex.isHex(loc.clone().add(-4,0,7)))){
	            			 new GrassHex(loc.clone().add(-4,-random1Int(0),7)).create();
	            		 }else{
	            			 Bukkit.broadcastMessage("False");
	            		 }
	            		 
	            		 
	            		 
	            	 }
	             }
				 
				 
	         }
	        }, 0L, 1L);
	}
	
	public static int random1Int(int i){
		Random rand = new Random();
		return rand.nextInt((i+1) - (i-1) + 1) + i-1;
	}
	
	public static void main(String[] args) {
		System.out.println("" + random1Int(10));
	}
	
	
	public Hex isOnPlatform(Player player){
		Location loc = player.getLocation();
		for(Hex hex : Hex.hexes){
			if(hex.getMinLoc() == null || hex.getMaxLoc() == null) continue;
			Location minLoc = hex.getMinLoc();
			Location maxLoc = hex.getMaxLoc();
			if(loc.getBlockX() >= minLoc.getBlockX() && loc.getBlockZ() >= minLoc.getBlockZ()
					&& loc.getBlockX() <= maxLoc.getBlockX() && loc.getBlockZ() <= maxLoc.getBlockZ()) return hex;			
		}
		return null;	
	}
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hex")) { 
			CreateHex.onCommand((Player) sender, args);			
			return true;
		} 
		return false; 
	}
	
	
	
}
