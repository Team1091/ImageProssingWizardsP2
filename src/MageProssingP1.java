import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

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

			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {

					if (new Color(image.getRGB(x, y)).getGreen() > 250) {
						testImage.setRGB(x, y, 0x00FF00);
					} else {
						testImage.setRGB(x, y, 0x000000);
					}
				}
			}

			ImageIO.write(testImage, "PNG", new File(dir + "\\" + flist[i].getName()));
		}
	}
}