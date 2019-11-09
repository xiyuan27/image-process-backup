package GraySc1;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.IOException;
import static javax.imageio.ImageIO.read;


public class GraySc1 {
    private BufferedImage image;
    private BufferedImage gray;

    public GraySc1(BufferedImage image) {
        this.image = image;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage input = null;

        String path = "D:\\Process\\GrayScale\\src\\GraySc\\test.jpg";
        input = read(new File(path));

        GraySc1 gs = new GraySc1(input);
        gs.avg(input);

    }

    // The average grayscale method
    private static BufferedImage avg(BufferedImage original) throws IOException {

        int alpha, red, green, blue;
        int newPixel;

        BufferedImage avg_gray = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] avgLUT = new int[766];
        for (int i = 0; i < avgLUT.length; i++) avgLUT[i] = (int) (i / 3);

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                newPixel = red + green + blue;
                newPixel = avgLUT[newPixel];
                // Return back to original format
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);

                // Write pixels into image
                avg_gray.setRGB(i, j, newPixel);

            }
        }

        JFrame frame = new JFrame("GrayScale1");
        frame.getContentPane().add(new JLabel(new ImageIcon(avg_gray)));
        frame.setVisible(true);
        File newFile = new File(System.getProperty("D:\\Process\\GrayScale\\src\\GraySc")+"test1.jpg");
        ImageIO.write(avg_gray, "jpg", newFile );

        return avg_gray;

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
