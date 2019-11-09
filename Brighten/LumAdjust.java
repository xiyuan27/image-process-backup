package Brighten;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LumAdjust {
    /**
     * 图片亮度调整
     *
     * @param image
     * @param param
     * @throws IOException
     */
    public void lumAdjustment(BufferedImage image, int param) throws IOException {
        if (image == null) {
            return;
        } else {
            int rgb, R, G, B;
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    rgb = image.getRGB(i, j);
                    R = ((rgb >> 16) & 0xff) + param;
                    G = ((rgb >> 8) & 0xff) + param;
                    B = (rgb & 0xff) + param;

                    rgb = ((clamp(255) & 0xff) << 24) | ((clamp(R) & 0xff) << 16) | ((clamp(G) & 0xff) << 8)
                            | ((clamp(B) & 0xff));
                    image.setRGB(i, j, rgb);
                }
            }
        }
    }

    // 判断a,r,g,b值，大于256返回256，小于0则返回0,0到256之间则直接返回原始值
    private int clamp(int rgb) {
        if (rgb > 255)
            return 255;
        if (rgb < 0)
            return 0;
        return rgb;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\test1\\01.jpg");
        BufferedImage image = ImageIO.read(file);
        LumAdjust lumAdjust = new LumAdjust();
        lumAdjust.lumAdjustment(image, 30);
        File file2 = new File("C:\\Users\\Administrator\\Desktop\\test1\\01-1.jpg");
        file2.createNewFile();
        ImageIO.write(image, "JPG", file2);

    }
}