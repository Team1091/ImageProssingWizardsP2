package com.team1091.vision;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessingP1 {

    public static void main(String[] args) throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage input = webcam.getImage();
        BufferedImage out = process(input);
        ImageIO.write(out , "PNG", new File("hashTagYolo.png"));


        //String dir = "C:\\Users\\Team1091\\Desktop\\PhotoSamplesSpike_2017\\Good";
//        String dir = "C:\\Users\\Team1091\\Desktop\\PhotoSamplesSpike_2017\\TestWidth";
//        File directory = new File(dir);
//        File[] flist = directory.listFiles();
//        dir += "_PROCESSED";
//        directory = new File(dir);
//        directory.mkdir();
//        for (int i = 0; i < flist.length; i++) {
//            BufferedImage image = ImageIO.read(flist[i]);
//            BufferedImage out = process(image);
//            ImageIO.write(out, "PNG", new File(dir + "\\" + flist[i].getName()));
//        }

    }

    public static BufferedImage process(BufferedImage inputImage) throws IOException {

        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        long xsum = 0;
        long ysum = 0;
        int totalcount = 0;

        for (int x = 0; x < inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {

                if (new Color(inputImage.getRGB(x, y)).getGreen() > 250) {
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

//        outputImage.setRGB(xcenter, ycenter, 0xff);
        Graphics2D g = outputImage.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(xcenter - 5, ycenter - 5, 10, 10);

        //Distance
        int counter = 0;
        int width = 0;
        for (int x = xcenter; x < outputImage.getWidth(); x++) {
            if (getAvg(outputImage, x, ycenter)) {
                counter++;
            } else {
                counter = 0;
                continue;
            }
            if (counter >= 5) {
                width += x - xcenter - 5;
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
                width += xcenter - (x + 5);
                break;
            }
        }
        System.out.println("Width = " + width);
        // F = P * D * (1/W)
        // (F * W) / P = D
        // F = 35p * 60in * (1/2in)
        // F = 1050 p

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