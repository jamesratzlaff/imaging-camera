package com.jamesratzlaff.imaging.camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ValueToColorConverter {

	private static final Color[] DEFAULT_COLORS = new Color[] {Color.BLACK,Color.WHITE};
	
	private BufferedImage gradient;
	private int min;
	private int range;
	private Color[] colors = Arrays.copyOf(DEFAULT_COLORS, DEFAULT_COLORS.length);
	
	
	public int getColorFromValue(int value) {
		int normalized = value-min;
		if(normalized<0||normalized>getRange()) {
			return Color.TRANSLUCENT;
		}
		return getGradient().getRGB(normalized, 0);
	}
	
	
	public BufferedImage getGradient() {
		if(this.gradient==null) {
			if(this.colors==null||this.colors.length==0) {
				this.colors=Arrays.copyOf(DEFAULT_COLORS, DEFAULT_COLORS.length);
			}
			this.gradient=getGradientImage(getRange(), getColors());
		}
		return gradient;
	}
	public void setGradient(BufferedImage gradient) {
		this.gradient = gradient;
	}
	
	public Color[] getColors() {
		return this.colors;
	}
	
	public ValueToColorConverter clearColors() {
		this.colors=new Color[0];
		this.gradient=null;
		return this;
	}
	
	public void setColors(Color start, Color color, Color...colors) {
		Color[] asArr = fromColorsToArray(start, color, colors);
		if(!Arrays.equals(asArr, this.colors)) {
			this.gradient=null;
			this.colors=asArr;
		}
	}
	
	public void addColor(Color color, Color...colors) {
		List<Color> all = new ArrayList<Color>(List.of(this.colors));
		List<Color> toAdd = new ArrayList<Color>(colors.length+1);
		if(color!=null) {
			toAdd.add(color);
		}
		toAdd.addAll(Arrays.stream(colors).filter(c->c!=null).collect(Collectors.toList()));
		if(!toAdd.isEmpty()) {
			all.addAll(toAdd);
			this.gradient=null;
			this.colors=all.toArray(size->new Color[size]);
		}
	}
	
	
	private static Color[] fromColorsToArray(Color start, Color color, Color...colors) {
		List<Color> asList = fromColors(start, color, colors);
		return asList.toArray(size->new Color[size]);
	}
	
	private static List<Color> fromColors(Color start, Color color, Color...colors){
		ArrayList<Color> result = new ArrayList<Color>(colors.length+2);
		if(start!=null) {
			result.add(start);
		}
		if(color!=null) {
			result.add(color);
		}
		result.addAll(Arrays.stream(colors).filter((c)->c!=null).collect(Collectors.toList()));
		result.trimToSize();
		return result;
	}
	
	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		if(this.min!=min) {
			this.min = min;
			this.gradient=null;
		}
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		if(this.range!=range) {
			this.range = range;
			this.gradient=null;
		}
	}
	
	
	public static BufferedImage getGradientImage(int range, Color...colors) {
		BufferedImage gradientHolder = new BufferedImage(range+1,1,BufferedImage.TYPE_INT_ARGB);
		LinearGradientPaint lgp = createSimpleLinearGradient(range, colors);
		Graphics2D g2d = (Graphics2D)gradientHolder.getGraphics();
		g2d.setPaint(lgp);
		Rectangle2D r=new Rectangle(range+1, 1);
		g2d.fill(r);
		g2d.draw(r);
		return gradientHolder;
		
	}
	
	
	public static LinearGradientPaint createSimpleLinearGradient(int range, Color...colors) {
		float toAdd = 1.0f/((float)colors.length-1);
		float[] fractions = new float[colors.length];
		fractions[0]=0;
		fractions[fractions.length-1]=1.0f;
		for(int i=1;i<colors.length-1;i++) {
			float val = i*toAdd;
			fractions[i]=val;
		}
		LinearGradientPaint lgp = new LinearGradientPaint(0.0f, 0.0f, range, 0.0f, fractions, colors);
		return lgp;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(colors);
		result = prime * result + Objects.hash(min, range);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ValueToColorConverter)) {
			return false;
		}
		ValueToColorConverter other = (ValueToColorConverter) obj;
		return Arrays.equals(colors, other.colors) && min == other.min && range == other.range;
	}
	
	
	
}
