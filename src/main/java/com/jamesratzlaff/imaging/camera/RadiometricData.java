package com.jamesratzlaff.imaging.camera;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class RadiometricData {

	private Camera camera;
	private int[] data;
	private Path dataPath;
	
	public int[] getData() {
		if(data==null) {
			data=new int[(int)camera.getAvailableResolutions().get(0).getTotalPixels()];
		}
		
		return data;
	}
	
	
	private static int[] toInts(String[] strs) {
		int[] reso = new int[strs.length];
		for(int i=0;i<strs.length;i++) {
			reso[i]=Integer.parseInt(strs[i]);
		}
		return reso;
	}
	
	public int getMin() {
		return Arrays.stream(getData()).min().orElse(Integer.MIN_VALUE);
	}
	
	public int getMax() {
		return Arrays.stream(getData()).max().orElse(Integer.MAX_VALUE);
	}
	
	
	protected void readData() {
		if(this.dataPath!=null) {
			try {
				String asStr = Files.readString(dataPath);
				String[] asStrs = asStr.trim().split("\\s+");
				setData(toInts(asStrs));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Path getDataPath() {
		return this.dataPath;
	}


	public Camera getCamera() {
		return camera;
	}


	public void setCamera(Camera camera) {
		this.camera = camera;
	}


	public void setData(int[] data) {
		this.data = data;
	}


	public void setDataPath(Path dataPath) {
		this.dataPath = dataPath;
		this.data=null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
