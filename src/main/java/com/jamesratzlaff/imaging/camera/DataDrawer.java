package com.jamesratzlaff.imaging.camera;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class DataDrawer {

	private RadiometricData data;
	private ValueToColorConverter vtcc;
	
	public DataDrawer() {
		vtcc=new ValueToColorConverter();
	}

	public ValueToColorConverter getVtcc() {
		return vtcc;
	}
	
	public RadiometricData getData() {
		return data;
	}

	public void setData(RadiometricData data) {
		this.data = data;
		if(this.data!=null) {
			vtcc.setMin(data.getMin());
			vtcc.setRange(data.getRange());
		}
	}
	
	public BufferedImage getAsBufferedImage() {
		BufferedImage bi = new BufferedImage(getData().getWidth(), getData().getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int y=0;y<getData().getHeight();y++) {
			for(int x=0;x<getData().getWidth();x++) {
				int rawVal = getData().getRawValueAt(x, y);
				
				int colorVal = vtcc.getColorFromValue(rawVal);
				bi.setRGB(x, y, colorVal);
			}
		}
		
		
		
		return bi;
	}
	
	
	public void setGradientColors(Color start, Color color, Color...colors) {
		this.vtcc.setColors(start, color, colors);
	}
	
	
	
	
	
	
}
