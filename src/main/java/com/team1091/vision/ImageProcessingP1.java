package com.team1091.vision;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

import static spark.Spark.get;
import static spark.Spark.port;

public class ImageProcessingP1 {

    public static final String hostName = "roborio-1091-frc.local";
    public static final int portNumber = 5805;

    public static final DecimalFormat df = new DecimalFormat("#.0");

    private static float center = 0;

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            Webcam.setDriver(new IpCamDriver());
            IpCamDeviceRegistry.register("RoboRioCam", "http://roborio-1091-frc.local:1181/stream.mjpg", IpCamMode.PUSH);
        }

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);

        panel.setPainter(new WebcamPanel.Painter() {
            @Override
            public void paintPanel(WebcamPanel panel, Graphics2D g2) {

            }

            @Override
            public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {
                try {
                    TargetingOutput targetingOutput = process(image);

                    center = targetingOutput.getInstructions();

                    g2.drawImage(targetingOutput.drawOntoImage(targetingOutput.processedImage), 0, 0, null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        JFrame window = new JFrame("Test webcam panel");
        window.add(panel);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);

        port(5805);
        get("/", (req, res) -> center); //have webserver that displays center value
    }


    public static TargetingOutput process(BufferedImage inputImage) throws IOException {

        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        long xsum = 0;
        long ysum = 0;
        int totalcount = 0;

        for (int x = 0; x < inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {
                Color color = new Color(inputImage.getRGB(x, y));
                int green = color.getGreen();
                int red = color.getRed();
                int blue = color.getBlue();
                int brightness = (green + red + blue) / 3;
                if (green > blue + 30 && green > red + 30 ) {
                    outputImage.setRGB(x, y, 0x00FF00);
                    xsum += x;
                    ysum += y;
                    totalcount++;
                } else {
                    outputImage.setRGB(x, y, color.getRGB());
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

        TargetingOutput targetingOutput = new TargetingOutput();
        targetingOutput.imageWidth = inputImage.getWidth();
        targetingOutput.imageHeight = inputImage.getHeight();

        targetingOutput.rightX = xcenter + rightWidth;
        targetingOutput.leftX = xcenter - leftWidth;
        targetingOutput.calcXCenter = (targetingOutput.rightX + targetingOutput.leftX) / 2;

        targetingOutput.xcenter = xcenter;
        targetingOutput.ycenter = ycenter;
        targetingOutput.rightWidth = rightWidth;
        targetingOutput.leftWidth = leftWidth;
        targetingOutput.width = width;
        targetingOutput.distance = distance;
        targetingOutput.processedImage = outputImage;
        return targetingOutput;
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