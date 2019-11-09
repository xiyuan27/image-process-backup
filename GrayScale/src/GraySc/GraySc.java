package GraySc;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import static javax.imageio.ImageIO.read;


public class GraySc {
    private BufferedImage image;
    private BufferedImage gray;

    public GraySc(BufferedImage image) {
        this.image = image;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage input = null;

        String path = "D:\\Process\\GrayScale\\src\\GraySc\\01.png";
        input = read(new File(path));

        GraySc gs = new GraySc(input);
        gs.grayImage();

    }

    public void grayImage() throws IOException {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }

        JFrame frame = new JFrame("GraySc ale");
        frame.getContentPane().add(new JLabel(new ImageIcon(grayImage)));
        frame.setVisible(true);
        File newFile = new File(System.getProperty("D:\\Process\\GrayScale\\src\\GraySc")+"02.png");
        ImageIO.write(grayImage, "png", newFile );
    }
}






