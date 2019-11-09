package GraySc5;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static javax.imageio.ImageIO.read;

public class GraySc5 {
    private BufferedImage image;
    private BufferedImage gray;

    public GraySc5(BufferedImage image) {
        this.image = image;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage input = null;

        String path = "D:\\Process\\GrayScale\\src\\GraySc\\test.jpg";
        input = read(new File(path));

        GraySc5 gs = new GraySc5(input);
        gs.rgb(input,2);

    }

    // The "pick the color" method
    private static BufferedImage rgb(BufferedImage original, int color) throws IOException {

        int alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage rgb = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

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

                int newval = pixel[color];

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                rgb.setRGB(i, j, newPixel);

            }

        }
        JFrame frame = new JFrame("GrayScale4");
        frame.getContentPane().add(new JLabel(new ImageIcon(rgb)));
        frame.setVisible(true);
        File newFile = new File(System.getProperty("D:\\Process\\GrayScale4\\src\\GraySc")+"test5.jpg");
        ImageIO.write(rgb, "jpg", newFile );
        return rgb;

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


