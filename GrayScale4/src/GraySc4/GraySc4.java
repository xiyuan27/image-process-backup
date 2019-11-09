package GraySc4;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static javax.imageio.ImageIO.read;

public class GraySc4 {
    private BufferedImage image;
    private BufferedImage gray;

    public GraySc4(BufferedImage image) {
        this.image = image;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage input = null;

        String path = "D:\\Process\\GrayScale\\src\\GraySc\\test.jpg";
        input = read(new File(path));

        GraySc4 gs = new GraySc4(input);
        gs.decompMin(input);

    }

    // The minimal decomposition method
    private static BufferedImage decompMin(BufferedImage original) throws IOException {

        int alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage decomp = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;

                int newval = findMin(pixel);

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                decomp.setRGB(i, j, newPixel);

            }
        }
        JFrame frame = new JFrame("GrayScale4");
        frame.getContentPane().add(new JLabel(new ImageIcon(decomp)));
        frame.setVisible(true);
        File newFile = new File(System.getProperty("D:\\Process\\GrayScale4\\src\\GraySc")+"test4.jpg");
        ImageIO.write(decomp, "jpg", newFile );
        return decomp;

    }

    // The maximum decomposition method
    private static BufferedImage decompMax(BufferedImage original) throws IOException {

        int alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage decomp = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;

                int newval = findMax(pixel);

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                decomp.setRGB(i, j, newPixel);

            }

        }

        JFrame frame = new JFrame("GrayScale4");
        frame.getContentPane().add(new JLabel(new ImageIcon(decomp)));
        frame.setVisible(true);
        File newFile = new File(System.getProperty("D:\\Process\\GrayScale4\\src\\GraySc")+"02.png");
        ImageIO.write(decomp, "png", newFile );
        return decomp;

    }

    private static int findMin(int[] pixel) {

        int min = pixel[0];

        for(int i=0; i<pixel.length; i++) {
            if(pixel[i] < min)
                min = pixel[i];
        }

        return min;

    }

    private static int findMax(int[] pixel) {

        int max = pixel[0];

        for (int i = 0; i < pixel.length; i++) {
            if (pixel[i] > max)
                max = pixel[i];
        }

        return max;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }
}

