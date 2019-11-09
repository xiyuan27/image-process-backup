package GraySc2;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.IOException;
import static javax.imageio.ImageIO.read;


public class GraySc2 {
    private BufferedImage image;
    private BufferedImage gray;

    public GraySc2(BufferedImage image) {
        this.image = image;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage input = null;

        String path = "D:\\Process\\GrayScale\\src\\GraySc\\01_1.jpg";
        input = read(new File(path));

        GraySc2 gs = new GraySc2(input);
        gs.luminosity(input);

    }

    // The luminance method
    private static BufferedImage luminosity(BufferedImage original) throws IOException {

        int alpha, red, green, blue;
        int newPixel;

        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);

                // Write pixels into image
                lum.setRGB(i, j, newPixel);

            }
        }

        JFrame frame = new JFrame("GrayScale2");
        frame.getContentPane().add(new JLabel(new ImageIcon(lum)));
        frame.setVisible(true);
        File newFile = new File(System.getProperty("D:\\Process\\GrayScale\\src\\GraySc")+"01_1-2.jpg");
        ImageIO.write(lum, "jpg", newFile );
        return lum;

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

