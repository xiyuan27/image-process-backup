package GraySC3;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.IOException;
import static javax.imageio.ImageIO.read;


public class GraySc3 {
    private BufferedImage image;
    private BufferedImage gray;

    public GraySc3(BufferedImage image) {
        this.image = image;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage input = null;

        String path = "D:\\Process\\GrayScale\\src\\GraySc\\01_1.jpg";
        input = read(new File(path));

        GraySc3 gs = new GraySc3(input);
        gs.desaturation(input);

    }

    // The desaturation method
    private static BufferedImage desaturation(BufferedImage original) throws IOException {

        int alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage des = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] desLUT = new int[511];
        for(int i=0; i<desLUT.length; i++) desLUT[i] = (int) (i / 2);

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

                int newval = (int) (findMax(pixel) + findMin(pixel));
                newval = desLUT[newval];

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                des.setRGB(i, j, newPixel);

            }
        }

        JFrame frame = new JFrame("GrayScale3");
        frame.getContentPane().add(new JLabel(new ImageIcon(des)));
        frame.setVisible(true);
        File newFile = new File(System.getProperty("D:\\Process\\GrayScale\\src\\GraySc")+"01_11.jpg");
        ImageIO.write(des, "jpg", newFile );
        return des;

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

        for(int i=0; i<pixel.length; i++) {
            if(pixel[i] > max)
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

