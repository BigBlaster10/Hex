package net.pixelderp.hexagon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public abstract class Hex {


	private int height = 0;
	
	private Location loc;
	private Location maxLoc;
	private Location minLoc;
	
	private File file;
	
	public static ArrayList<Hex> hexes = new ArrayList<Hex>();
	
	public Hex(File file, Location loc){
		hexes.add(this);
		this.file = file;
		this.loc = loc;
		if(getLocs() == null){
			loadFile();
		}
	}
	
	public void create(){
		addRandom();
		finalTouches();	
		
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();		
		
		int maxX = getLocs()[0];
		int maxY = getLocs()[1];
		int maxZ = getLocs()[2];
		
		int minX = getLocs()[0];
		int minY = getLocs()[1];
		int minZ = getLocs()[2];
		
		World world = loc.getWorld();
		
		int ls = 0;
		for(int i = 0; i < getMats().length; i++){
			
			if(getLocs()[i + ls] > maxX) maxX = getLocs()[i + ls];
			if(getLocs()[i + ls + 1] > maxY) maxY = getLocs()[i + ls + 1];
			if(getLocs()[i + ls + 2] > maxZ) maxZ = getLocs()[i + ls + 2];

			if(getLocs()[i + ls] < minX) minX = getLocs()[i + ls];
			if(getLocs()[i + ls + 1] < minY) minY = getLocs()[i + ls + 1];
			if(getLocs()[i + ls + 2] < minZ) minZ = getLocs()[i + ls + 2];
			
			Location l = new Location(world, getLocs()[i + ls] + x, getLocs()[i + ls +1] + y, getLocs()[i + ls +2] + z);
			//Bukkit.broadcastMessage("X: " + (getLocs()[i]) + "Y: " + (getLocs()[i+1]) + "Y: " + (getLocs()[i+2]));
			Material mat = Material.getMaterial(getMats()[i]);
			byte d = (byte) getData()[i];
			Block block = world.getBlockAt(l);
			block.setType(mat);
			block.setData(d);			
			ls += 2;
		}
		
		maxLoc = new Location(world, maxX + loc.getBlockX(), maxY + loc.getBlockY(), maxZ + loc.getBlockZ());
		minLoc = new Location(world, minX + loc.getBlockX(), minY + loc.getBlockY(), minZ + loc.getBlockZ());	
		
	}
	
	public Location getMinLoc(){
		return minLoc;
	}
	
	public Location getMaxLoc(){
		return maxLoc;
	}
	
	abstract void addRandom();
	abstract void finalTouches();
	
	public ArrayList<Hex> getNeighbors(){
		ArrayList<Hex> neigh = new ArrayList<Hex>();
		
		checkNeighbor(loc.clone().add(8,0,0), neigh);
		checkNeighbor(loc.clone().add(-8,0,0), neigh);
		checkNeighbor(loc.clone().add(4,0,-7), neigh);
		checkNeighbor(loc.clone().add(4,0,7), neigh);
		checkNeighbor(loc.clone().add(-4,0,-7), neigh);
		checkNeighbor(loc.clone().add(-4,0,7), neigh);
		
		return neigh;		
	}
	
	private void checkNeighbor(Location loc, ArrayList<Hex> neigh){
		if(isHex(loc)) neigh.add(getHex(loc));
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public static Hex getHex(Location loc){		
		for(Hex hex : hexes){
			if(hex.loc == null) continue;
			if(hex.loc.equals(loc)) return hex;
			if(hex.getLocation().getBlockX() == loc.getBlockX() && hex.getLocation().getBlockZ() == loc.getBlockZ()) return hex;
		}
		return null;		
	}
	
	public static boolean isHex(Location loc){
		if(getHex(loc) == null){
			for(Hex hex : hexes){
				if(hex.getLocation() == null) continue;
				if(hex.getLocation().getBlockX() == loc.getBlockX() && hex.getLocation().getBlockZ() == loc.getBlockZ()) return true;
			}		
			
			return false;
		}
		return true;
	}
	
	abstract int[] getLocs();
	abstract int[] getMats();
	abstract int[] getData();

	abstract void setLocs(int[] locs);
	abstract void setMats(int[] mats);
	abstract void setData(int[] data);

	private void loadFile(){
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			int total = -1;
			String sLocs = "";
			String sMats = "";
			String sData = "";
			
			String s = null;
			while((s = reader.readLine()) != null){			    
				if(total == -1){
					total = Integer.parseInt(s);
				}else if(sLocs.equals("")){
					sLocs = s;
				}else if(sMats.equals("")){
					sMats = s;
				}else if(sData.equals("")){
					sData = s;
				}				
			}
			
			setLocs(new int[total * 3]);
			setMats(new int[total]); 
			setData(new int[total]);
			
			
			
			
			s = "";
			int tot = 0;
			for(int i = 0; i < sLocs.length(); i++){
				char c = sLocs.charAt(i);
				if(c == ','){
					getLocs()[tot] = Integer.parseInt(s);
					s = "";
					tot++;
				}else{
					s += c;
				}				
			}
			
			s = "";
			tot = 0;
			for(int i = 0; i < sMats.length(); i++){
				char c = sMats.charAt(i);
				if(c == ','){
					getMats()[tot] = Integer.parseInt(s);
					s = "";
					tot++;
				}else{
					s += c;
				}				
			}
			
			s = "";
			tot = 0;
			for(int i = 0; i < sData.length(); i++){
				char c = sData.charAt(i);
				if(c == ','){
					getData()[tot] = Integer.parseInt(s);
					s = "";
					tot++;
				}else{
					s += c;
				}				
			}
			
			
			
			
			
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	
	
	
	
}
