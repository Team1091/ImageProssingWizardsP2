import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class MageProssingP1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String dir = "C:\\Users\\Team1091\\Desktop\\PhotoSamplesSpike_2017\\Good";
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

			// F = P * D * (1/W)
			// (F * W) / P = D
			// F = 35p * 60in * (1/2in)
			// F = 1050 p
			ImageIO.write(testImage, "PNG", new File(dir + "\\" + flist[i].getName()));
		}
	}
}