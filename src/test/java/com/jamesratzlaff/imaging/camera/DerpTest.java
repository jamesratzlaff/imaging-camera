package com.jamesratzlaff.imaging.camera;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Test;

public class DerpTest {

	
	@Test
	public void testStuff() throws Exception{
		Camera cam = new Camera();
		cam.addResolution(384, 288);
		cam.setFieldOfView(32.8f, 24.6f);
		cam.setFocalDistanceMm(13);
		
		RadiometricData rd=new RadiometricData();
		rd.setCamera(cam);
		rd.setDataPath(Paths.get(System.getProperty("user.home"), "Desktop","thermals","20200906_172502","20200906_172502_temps.txt"));
		
		
		System.out.println(rd.getData());
		System.out.println(rd.getStats());
		System.out.println(rd.getMedian());
		System.out.println(rd.getMode());
		System.out.println(Arrays.deepToString(rd.getCoordinatesOfValue(-2491)));
//		histo.entrySet().forEach(System.out::println);
		System.out.println(rd.getMin());
		System.out.println(rd.getMax());
		DataDrawer dd = new DataDrawer();
		dd.setData(rd);
		dd.setGradientColors(Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW,Color.GREEN,Color.CYAN, Color.BLUE, Color.MAGENTA, Color.WHITE);
		BufferedImage grad = dd.getVtcc().getGradient();
		ImageIO.write(grad, "png", Paths.get(System.getProperty("user.home"),"Desktop","grad_2020.png").toFile());
		BufferedImage bi = dd.getAsBufferedImage();
		ImageIO.write(bi, "png", Paths.get(System.getProperty("user.home"),"Desktop","thermal_img_2020.png").toFile());
		
	}
	
}
