package com.jamesratzlaff.imaging.camera;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

public class RadiometricData {

	private Camera camera;
	private int[] data;
	private Path dataPath;
	
	public int[] getData() {
		if(data==null) {
			data=new int[(int)camera.getAvailableResolutions().get(0).getTotalPixels()];
			readData();
		}
		
		return data;
	}
	
	public int getRawValueAt(int x, int y) {
		int offset = getOffset(x, y);
		if(offset<0) {
			return Integer.MIN_VALUE;
		}
		return data[offset];
		
	}
	
	private int getOffset(int x, int y){
		int offset = Integer.MIN_VALUE;
		if((x>-1&&x<getWidth())&&(y>-1&&y<getHeight())) {
			offset=(y*getWidth())+x;
		}
		return offset;
		
	}
	
	
	public int[] getLine(int lineNo){
		if(lineNo<getHeight()) {
			int[] line = new int[getWidth()];
			int index = lineNo*getWidth();
			for(int i=0;i<getWidth();i++) {
				line[i]=getData()[index+i];
			}
			return line;
		}
		return null;
		
	}
	
	public int[][] getLines(){
		int[][] result=new int[getHeight()][getWidth()];
		for(int line=0;line<getHeight();line++) {
			result[line]=getLine(line);
		}
		return result;
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
	
	public double getAvg() {
		return Arrays.stream(getData()).average().orElse(Integer.MIN_VALUE);
	}
	public IntSummaryStatistics getStats() {
		return Arrays.stream(getData()).summaryStatistics();
	}
	
	public float getMedian() {
		int middle = getData().length>>1;
		if((getData().length&1)==0) {
			return getData()[middle]+getData()[middle+1];
		}
		return getData()[middle];
	}
	
	public int[][] getCoordinates(List<Integer> offsets){
		int[][] result = new int[offsets.size()][];
		for(int i=0;i<offsets.size();i++) {
			result[i]=getCoordinate(offsets.get(i));
		}
		return result;
	}
	
	public int[] getCoordinate(int offset) {
		int y = offset/getWidth();
		int x = offset%getWidth();
		return new int[] {x,y};
	}
	
	public int[][] getCoordinatesOfValue(int value){
		List<Integer> offsets = getOffsetsOfValue(value);
		int[][] coords = getCoordinates(offsets);
		return coords;
	}
	
	public List<Integer> getOffsetsOfValue(int value){
		ArrayList<Integer> offsets = new ArrayList<Integer>();
		for(int i=0;i<getData().length;i++) {
			int val = getData()[i];
			if(val==value) {
				offsets.add(i);
			}
		}
		offsets.trimToSize();
		return offsets;
	}
	
	public int getMode() {
		int mostFrequentKey=Integer.MIN_VALUE;
		int highestValue = Integer.MIN_VALUE;
		Map<Integer,AtomicInteger> histo = getHistogram();
		for(Integer key : histo.keySet()) {
			int currValue = histo.get(key).get();
			if(currValue>highestValue) {
				mostFrequentKey=key.intValue();
				highestValue=currValue;
			}
		}
		return mostFrequentKey;
	}
	
	public Map<Integer,AtomicInteger> getHistogram(){
		int[] distinctValues = Arrays.stream(getData()).sorted().distinct().toArray();
		Map<Integer,AtomicInteger> map = createCountMap(distinctValues);
		for(int i=0;i<getData().length;i++) {
			int currentValue = getData()[i];
			map.get(currentValue).getAndIncrement();
		}
		return map;
	}
	
	private static Map<Integer,AtomicInteger> createCountMap(int[] distinctValues){
		Map<Integer,AtomicInteger> map = new LinkedHashMap<Integer, AtomicInteger>(distinctValues.length);
		for(int i=0;i<distinctValues.length;i++) {
			map.put(distinctValues[i], new AtomicInteger(0));
		}
		return map;
	}
	
	
	public int getMax() {
		return Arrays.stream(getData()).max().orElse(Integer.MAX_VALUE);
	}
	
	public int getRange() {
		return getMax()-getMin();
	}
	
	public int getWidth() {
		return getCamera().getSelectedResolution().getWidth();
	}
	
	public int getHeight() {
		return getCamera().getSelectedResolution().getHeight();
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
	
	
	private static int[] getPolar(ToIntFunction<IntStream> func, int[]...dataArrays) {
		int[] polars = new int[dataArrays.length];
		for(int i=0;i<dataArrays.length;i++) {
			polars[i]=func.applyAsInt(Arrays.stream(dataArrays[i]));
		}
		return polars;
	}
	
	public static int[] nonMutatingDelta(int[] values, int delt) {
		int[] result = new int[values.length];
		for(int i=0;i<values.length;i++) {
			result[i]=values[i]+delt;
		}
		return result;
	}
	
	public static void delta(int[] values, int delt) {
		for(int i=0;i<values.length;i++) {
			values[i]+=delt;
		}
	}
	
	
	
	public static int getMin(int[]...dataArrays) {
		return Arrays.stream(getPolar((p)->p.min().getAsInt(), dataArrays)).min().getAsInt();
	}
	public static int getMax(int[]...dataArrays) {
		return Arrays.stream(getPolar((p)->p.max().getAsInt(), dataArrays)).max().getAsInt();
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
