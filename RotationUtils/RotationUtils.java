package RotationUtils;

import old.utils.HandleImgUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import static ContoursUtils.ContoursUtils.findMaxRect;
import static GeneralUtils.GeneralUtils.canny;
import static old.reconsitution.ImgUtils.cutRect;

/**
 * 旋转矩形工具类
 */
public class RotationUtils {

    /**
     * 旋转矩形
     * 返回旋转后的Mat
     * @param mat
     *            mat矩阵
     * @param rect
     *            矩形
     * @return
     */
    public static Mat rotation(Mat mat, RotatedRect rect) {
        // 获取矩形的四个顶点
        Point[] rectPoint = new Point[4];
        rect.points(rectPoint);

        double angle = rect.angle + 90;

        Point center = rect.center;

        Mat CorrectImg = new Mat(mat.size(), mat.type());

        mat.copyTo(CorrectImg);

        // 得到旋转矩阵算子
        Mat matrix = Imgproc.getRotationMatrix2D(center, angle, 0.8);

        Imgproc.warpAffine(CorrectImg, CorrectImg, matrix, CorrectImg.size(), 1, 0, new Scalar(0, 0, 0));

        return CorrectImg;
    }

    public static void correct(Mat src) {
        // Canny
        Mat cannyMat = canny(src);

        // 获取最大矩形
        RotatedRect rect = findMaxRect(cannyMat);

        // 旋转矩形
        Mat CorrectImg = rotation(cannyMat , rect);
        Mat NativeCorrectImg = rotation(src , rect);


        //裁剪矩形
        cutRect(CorrectImg , NativeCorrectImg);

        HandleImgUtils.saveImg(src, "C:\\Users\\Administrator\\Desktop\\test\\1.jpg");

        HandleImgUtils.saveImg(CorrectImg, "C:\\Users\\Administrator\\Desktop\\test\\2.jpg");
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = HandleImgUtils.matFactory("C:\\Users\\Administrator\\Desktop\\1123.jpg");
        HandleImgUtils.correct(src);
    }

}

