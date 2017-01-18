import com.team1091.vision.ImageProcessingP1;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageCenterTests {

    @Test
    public void testBlackImage() throws IOException {
        // TODO: Test that the a pure black image does not cause the program to crash
        BufferedImage bi = generateColoredImage(10, 10, Color.BLACK);

        BufferedImage output = ImageProcessingP1.process(bi);
        assert output != null;

    }


    @Test
    public void testGreenImage() throws IOException {
        // TODO: Test that the a pure green image does not cause the program to crash
        BufferedImage bi = generateColoredImage(10, 10, Color.GREEN);

        BufferedImage output = ImageProcessingP1.process(bi);
        assert output != null;
    }


    @Test
    public void testCenter() {
        // TODO: test that a valid image returns a reasonable center
        assert true;
    }


    private BufferedImage generateColoredImage(int xSize, int ySize, Color color) {
        BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        graphics.setPaint(color);
        graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        return bi;
    }
}
