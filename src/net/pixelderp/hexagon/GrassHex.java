package net.pixelderp.hexagon;

import java.io.File;

import org.bukkit.Location;

public class GrassHex extends Hex{

	private static int[] locs;
	private static int[] mats;
	private static int[] data;
	
	private int height = 0;
	public static File file = new File("plugins\\Hex\\GrassHex");
	
	
	
	public GrassHex(Location loc){
		super(file, loc);
		
	}
	
	public static void intitalize(){
		new GrassHex(null);
	}
	
	
	@Override
	void addRandom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void finalTouches() {
		// TODO Auto-generated method stub
		
	}


	public int[] getLocs() {
		return locs;
	}


	public void setLocs(int[] locs) {
		GrassHex.locs = locs;
	}


	public int[] getMats() {
		return mats;
	}


	public void setMats(int[] mats) {
		GrassHex.mats = mats;
	}


	public int[] getData() {
		return data;
	}


	public void setData(int[] data) {
		GrassHex.data = data;
	}
	
	

}
