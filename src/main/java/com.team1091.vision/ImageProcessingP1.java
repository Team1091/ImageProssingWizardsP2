package com.team1091.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class ImageProcessingP1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//String dir = "C:\\Users\\Team1091\\Desktop\\PhotoSamplesSpike_2017\\Good";
		String dir = "C:\\Users\\Team1091\\Desktop\\PhotoSamplesSpike_2017\\TestWidth";
		File directory = new File(dir);
		File[] flist = directory.listFiles();
		dir += "_PROCESSED";
		directory = new File(dir);
		directory.mkdir();
		for (int i = 0; i < flist.length; i++) {
			BufferedImage image = ImageIO.read(flist[i]);
			BufferedImage testImage = new BufferedImage(image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			long xsum = 0;
			long ysum = 0;
			int totalcount = 0;

			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {

					if (new Color(image.getRGB(x, y)).getGreen() > 250) {
						testImage.setRGB(x, y, 0x00FF00);
						xsum += x;
						ysum += y;
						totalcount++;
					} else {
						testImage.setRGB(x, y, 0x000000);
					}
				}
			}
			int xcenter = (int) (xsum / totalcount);
			int ycenter = (int) (ysum / totalcount);

			testImage.setRGB(xcenter, ycenter, 0xff);
			Graphics2D g = testImage.createGraphics();
			g.setColor(Color.BLUE);
			g.fillRect(xcenter - 5, ycenter - 5, 10, 10);

			int counter = 0;
			int width = 0;
			for(int x = xcenter; x < testImage.getWidth(); x++)
			{
				if(getAvg(testImage, x, ycenter))
				{
					counter++;
				}
				else
				{
					counter = 0;
					continue;
				}
				if(counter >= 5)
				{
					width += x - xcenter - 5;
					break;
				}
			}
			counter = 0;
			for(int x = xcenter; x > 0; x--)
			{
				if(getAvg(testImage, x, ycenter))
				{
					counter++;
				}
				else
				{
					counter = 0;
					continue;
				}
				if(counter >= 5)
				{
					width += xcenter - (x + 5);
					break;
				}
			}
			System.out.println("Width = " + width);
			// F = P * D * (1/W)
			// (F * W) / P = D
			// F = 35p * 60in * (1/2in)
			// F = 1050 p
			ImageIO.write(testImage, "PNG", new File(dir + "\\" + flist[i].getName()));
		}
	}

	private static boolean getAvg(BufferedImage image, int x, int y) {
		boolean given = false;
		int green = 0;
		int black = 0;
		if ((image.getRGB(x, y) & 0x00FF00) == 0x00FF00) {
			green++;
			given = true;
		} else {
			black++;
		}
		if (x + 1 < image.getWidth()) {
			if ((image.getRGB(x + 1, y) & 0x00FF00) == 0x00FF00) {
				green++;
			} else {
				black++;
			}
		}
		if (x - 1 > 0) {
			if ((image.getRGB(x - 1, y) & 0x00FF00) == 0x00FF00) {
				green++;
			} else {
				black++;
			}
		}
		if (y + 1 < image.getHeight()) {
			if ((image.getRGB(x, y + 1) & 0x00FF00) == 0x00FF00) {
				green++;
			} else {
				black++;
			}
		}
		if (y - 1 > 0) {
			if ((image.getRGB(x, y - 1) & 0x00FF00) == 0x00FF00) {
				green++;
			} else {
				black++;
			}
		}
		return green == black ? given : green > black;
	}
}