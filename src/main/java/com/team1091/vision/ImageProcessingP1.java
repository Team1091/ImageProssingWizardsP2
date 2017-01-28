package com.team1091.vision;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

public class ImageProcessingP1 {

    public static final DecimalFormat df = new DecimalFormat("#.0");

    public static void main(String[] args) throws IOException {

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);
        panel.setPainter(new WebcamPanel.Painter() {
            @Override
            public void paintPanel(WebcamPanel panel, Graphics2D g2) {

            }

            @Override
            public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {
                try {
                    BufferedImage out = process(image);
                    g2.drawImage(out, 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        JFrame window = new JFrame("Test webcam panel");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);

    }

    public static BufferedImage process(BufferedImage inputImage) throws IOException {

        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        long xsum = 0;
        long ysum = 0;
        int totalcount = 0;

        for (int x = 0; x < inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {
                int green = new Color(inputImage.getRGB(x, y)).getGreen();
                if (green > 251) {
                    outputImage.setRGB(x, y, 0x00FF00);
                    xsum += x;
                    ysum += y;
                    totalcount++;
                } else {
                    outputImage.setRGB(x, y, 0x000000);
                }
            }
        }

        int xcenter;
        int ycenter;

        if (totalcount == 0) {
            xcenter = inputImage.getWidth() / 2;
            ycenter = inputImage.getHeight() / 2;
        } else {
            xcenter = (int) (xsum / totalcount);
            ycenter = (int) (ysum / totalcount);
        }

        Graphics2D g = outputImage.createGraphics();
        g.setColor(Color.RED);

        //Distance
        int counter = 0;
        int rightWidth = 0;
        int leftWidth = 0;
        for (int x = xcenter; x < outputImage.getWidth(); x++) {
            if (getAvg(outputImage, x, ycenter)) {
                counter++;
            } else {
                counter = 0;
                continue;
            }
            if (counter >= 5) {
                rightWidth = x - xcenter - 5;
                break;
            }
        }
        counter = 0;
        for (int x = xcenter; x > 0; x--) {
            if (getAvg(outputImage, x, ycenter)) {
                counter++;
            } else {
                counter = 0;
                continue;
            }
            if (counter >= 5) {
                leftWidth = xcenter - (x + 5);
                break;
            }
        }
        int width = leftWidth + rightWidth;

        double distance = (4120 / (1.059984 * width - 3.89)) + 0.808314;

        int rightX = xcenter + rightWidth;
        int leftX = xcenter - leftWidth;

        g.drawLine(xcenter, ycenter - 10, xcenter, ycenter + 10);
        g.drawLine(xcenter, ycenter, rightX, ycenter);
        g.drawLine(xcenter, ycenter, leftX, ycenter);
        g.drawLine(rightX, ycenter - 8, rightX, ycenter + 8);
        g.drawLine(leftX, ycenter - 8, leftX, ycenter + 8);

        g.setColor(Color.cyan);
        int calcXCenter = ((rightX) + (leftX)) / 2;
        g.drawLine(calcXCenter, ycenter + 15, calcXCenter, ycenter - 15);

        // width labels, px and % screen width
        g.drawString(width + " px", xcenter, ycenter - 25);
        g.drawString(df.format(distance) + " in", xcenter, ycenter + 35);

        return outputImage;
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