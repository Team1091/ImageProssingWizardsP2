import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;

public class MageProssingP1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage image = ImageIO.read(new File("C:\\Users\\Team1091\\Desktop\\Cam_Stamples\\Good\\WIN_20170111_18_48_39_Pro.jpg"));
		BufferedImage testImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		int[][][] screen = new int[image.getWidth()][image.getHeight()][3];

		for (int x = 0 ; x<image.getWidth(); x++){
			for (int y = 0 ; y<image.getHeight(); y++){
				Color color = new Color(image.getRGB(x, y));
				screen[x][y][0] = color.getRed();
				screen[x][y][1] = color.getGreen();
				screen[x][y][2] = color.getBlue();
			}	
		}

		for (int x = 0 ; x<image.getWidth(); x++){
			for (int y = 0 ; y<image.getHeight(); y++){
				Color color = new Color(0, screen[x][y][1], 0);
				testImage.setRGB(x, y, color.getRGB());
				
			}	
		}
		
		
		ImageIO.write(testImage, "PNG", new File("C:\\Users\\Team1091\\Desktop\\Cam_Stamples\\temp.png"));
	}

}
