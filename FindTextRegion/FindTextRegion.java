package FindTextRegion;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;
public class FindTextRegion {

    private static boolean isLibraryLoaded = true;

    static {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            isLibraryLoaded = false;
        }
    }

    public  static Mat findTextRegion(Mat img) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(img, contours, hierarchy, RETR_CCOMP, CHAIN_APPROX_SIMPLE, new Point(0, 0));
        System.out.println("轮廓数量：" + contours.size());
        System.out.println(hierarchy);
        Mat contourImg = new Mat(img.size(), img.type());
        for (int i = 0; i < contours.size(); i++) {
            drawContours(contourImg, contours, i, new Scalar(255, 0, 0), 1);
/**         boundingRect(contours.get(i));
            int x = boundingRect(contours.get(i)).x;
            int y = boundingRect(contours.get(i)).y;
            int w = boundingRect(contours.get(i)).width;
            int h = boundingRect(contours.get(i)).height;
            double area = boundingRect(contours.get(0)).area();
            int index = 0;
            // 找出匹配到的最大轮廓
            double tempArea = boundingRect(contours.get(i)).area();
            if (tempArea > area) {
                    area = tempArea;
                    index = i;
                }
            System.out.println(tempArea);
 **/
            double area = contourArea(contourImg);
            System.out.println(area);

        }
        return contourImg;
    }

    public static void main(String[] args) {
        Mat textImageSrc = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\Hough\\01.jpg",0);
        Mat img =  findTextRegion(textImageSrc);
        Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\Hough\\112.jpg",img);
    }
}

