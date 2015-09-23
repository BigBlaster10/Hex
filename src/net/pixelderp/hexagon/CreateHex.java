package net.pixelderp.hexagon;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class CreateHex implements Listener{

	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(!player.getItemInHand().getType().equals(Material.WOOD_HOE)) return;
		if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			Location loc = event.getClickedBlock().getLocation();
			PlayerSelection ps = PlayerSelection.getPlayerSelection(player);
			ps.setLoc1(loc);
			player.sendMessage(ChatColor.GREEN + "Set Loc1 to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");	
			event.setCancelled(true);
		}else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Location loc = event.getClickedBlock().getLocation();
			PlayerSelection ps = PlayerSelection.getPlayerSelection(player);
			ps.setLoc2(loc);
			player.sendMessage(ChatColor.GREEN + "Set Loc2 to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");	
			event.setCancelled(true);
		}
	}
	
	public static class PlayerSelection{
		
		private Location loc1;
		private Location loc2;
		private Player player;
		
		public static ArrayList<PlayerSelection> players = new ArrayList<PlayerSelection>();
		
		PlayerSelection(Player player){
			this.player = player;
			players.add(this);
		}


		public Location getLoc1() {
			return loc1;
		}


		public void setLoc1(Location loc1) {
			this.loc1 = loc1;
		}


		public Location getLoc2() {
			return loc2;
		}


		public void setLoc2(Location loc2) {
			this.loc2 = loc2;
		}


		public Player getPlayer() {
			return player;
		}		
		
		public static PlayerSelection getPlayerSelection(Player player){
			for(PlayerSelection p : players){
				if(p.getPlayer().equals(player)) return p;
			}
			return new PlayerSelection(player);
		}
		
		
	}
	
	
	public static void onCommand(Player player, String[] args){
		
		if(args.length >= 1 && args[0].equals("save")){
			if(args.length < 2 || args[1] == ""){
				player.sendMessage("Error: /hex save <fileName>");
				return;
			}
			
			if(saveSelection(player, args[1]) == false){
				player.sendMessage("Error: Please select both points");			
				return;
			}
			
			player.sendMessage("File created");
			
			return;
		}else if(args.length >= 1 && args[0].equals("test")){
			//hex.create(new Location(player.getWorld(), 123, 91, 77));
			GrassHex hex = new GrassHex(player.getLocation());
			hex.create();

			
			player.sendMessage(ChatColor.GREEN + "Created");
			return;
		}else if(args.length >= 1 && args[0].equals("grid")){

			Location start = new Location(player.getWorld(), 144, 86, 91);
			
			Location min = start.clone().add(-8,-5,-8);
			Location max = start.clone().add(100,10,100);
			
			World world = start.getWorld();
			
			for(int x = min.getBlockX(); x <= max.getBlockX(); x++){
				for(int y = min.getBlockY(); y <= max.getBlockY(); y++){
					for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++){
						Block block = new Location(world, x, y, z).getBlock();
						if(block.getType().equals(Material.AIR)) continue;
						block.setType(Material.AIR);
					}
				}
			}
			
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(MainHexagon.plugin, new Runnable() {
	            @Override
	            public void run() {
	            	int t = 0;
	    			for(int x = 0; x <= 2; x++){
	    				for(int z = 0; z <= 2; z++){
	    					
	    					
	    					int i = 0;
	    					if(z % 2 == 1) i = 1;
	    					GrassHex hex = new GrassHex(new Location(start.getWorld(), start.getBlockX() + (x * 8) + 4*i, 86 + z, start.getBlockZ() + (z * 7)));
	    					hex.create();
	    					
	    					//delayTask(x,z,start, t);
	    					t++;
	    					
	    				}
	    			}
	            }
	        }, 20);
			
			
			
			
			player.sendMessage(ChatColor.GREEN + "Created");
			return;
		}
		player.sendMessage("Error: /hex save <fileName>");
		player.sendMessage("Error: /hex test");

	}
	
	public static void delayTask(int x, int z, Location start, long time){
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(MainHexagon.plugin, new Runnable() {
            @Override
            public void run() {
				//hex.create(new Location(start.getWorld(), start.getBlockX() + (x * 8), 86, start.getBlockZ() + (z * 8)));

            }
        }, time);
	}
	
	
	private static boolean saveSelection(Player player, String file){
		if(PlayerSelection.getPlayerSelection(player) == null) return false;
		PlayerSelection ps = PlayerSelection.getPlayerSelection(player);
		if(ps.getLoc1() == null) return false;
		if(ps.getLoc2() == null) return false;
		
		Location loc1 = ps.getLoc1();
		Location loc2 = ps.getLoc2();
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("plugins\\Hex\\" + file, "UTF-8");
			
			Location max = new Location(player.getWorld(), Math.max(loc1.getBlockX(), loc2.getBlockX()), Math.max(loc1.getBlockY(), loc2.getBlockY()), Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
			Location min = new Location(player.getWorld(), Math.min(loc1.getBlockX(), loc2.getBlockX()), Math.min(loc1.getBlockY(), loc2.getBlockY()), Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

			Location center = player.getLocation();
			int cX = center.getBlockX();
			int cY = center.getBlockY();
			int cZ = center.getBlockZ();
			
			
			String locs = "";
			String mats = "";
			String data = "";
			
			World world = player.getWorld();
			int total = 0;
			
			for(int x = min.getBlockX(); x <= max.getBlockX(); x++){
				for(int y = min.getBlockY(); y <= max.getBlockY(); y++){
					for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++){
						Block block = new Location(world, x, y, z).getBlock();
						if(block.getType().equals(Material.AIR)) continue;
						locs += (x - cX) + "," + (y - cY) + "," + (z - cZ) + ",";
						mats += block.getType().getId() + ",";
						data += block.getData() + ",";		
						total++;
					}
				}
			}
			
			writer.write("" + total + "\n");
			writer.write(locs + "\n");
			writer.write(mats + "\n");
			writer.write(data);
			
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return true;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
