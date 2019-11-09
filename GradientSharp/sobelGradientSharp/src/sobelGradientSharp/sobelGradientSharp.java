package sobelGradientSharp;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class sobelGradientSharp {
    int[] pixelArray;
    double[] tanArray;
    int width;
    int height;
    int maxGray;
    int minGray;
    int left;
    int right;
    public sobelGradientSharp(int[] pixelArray, int width, int height){
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }
    //转化为灰度图像
    public void toGray(){
        int[] grayArray = new int[width*height];
        for (int i=0; i<width*height; i++){
            int alpha = pixelArray[i] & 0xff000000;
            int red = pixelArray[i] & 0x00ff0000;
            int green = pixelArray[i] & 0x0000ff00;
            int blue  = pixelArray[i] & 0x000000ff;
            int gray = (int)((red>>16)*0.30 + (green>>8)*0.59 + (blue)*0.11 +0.5);
            grayArray[i] =  alpha | (gray<<16) | (gray<<8) | gray;
        }
        pixelArray = grayArray;
    }
    //高斯滤波
    void gaussianFilter(){
        //double[] gaussianKernel = generateGaussianKernel(3,1);
        int[] grayArray = new int[width*height];
        int[] elementMatrix = {1, 2, 1, 2, 4, 2, 1, 2, 1};//高斯滤波模板
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(0==i || 0==j || width-1==j || height-1==i){
                    grayArray[i*width+j] = pixelArray[i*width+j];
                }else{
                    int pixel = pixelArray[i*width+j];
                    int alpha = pixel & 0xff000000;
                    int[] sourceMatrix = new int[9];
                    sourceMatrix[0] = pixelArray[(i-1)*width+j-1] & 0X000000FF;
                    sourceMatrix[1] = pixelArray[(i-1)*width+j] & 0X000000FF;
                    sourceMatrix[2] = pixelArray[(i-1)*width+j+1] & 0X000000FF;
                    sourceMatrix[3] = pixelArray[i*width+j-1] & 0X000000FF;
                    sourceMatrix[4] = pixelArray[i*width+j] & 0X000000FF;
                    sourceMatrix[5] = pixelArray[i*width+j+1] & 0X000000FF;
                    sourceMatrix[6] = pixelArray[(i+1)*width+j-1] & 0X000000FF;
                    sourceMatrix[7] = pixelArray[(i+1)*width+j] & 0X000000FF;
                    sourceMatrix[8] = pixelArray[(i+1)*width+j+1] & 0X000000FF;
                    int res = 0;
                    for(int m=0; m<9; m++){
                        res = res + (sourceMatrix[m] * elementMatrix[m]);
                    }
                    res = (int)(res*1.0/16+0.5);
                    grayArray[i*width+j] = alpha | (res<<16) | (res<<8) | res;
                }

            }
        }
        pixelArray = grayArray;
    }
    //最大类间方差阈值
    public int thresholdOstu(){
        int[] numPerGray = new int[256];
        float[] ostus = new float[256];
        for(int i=0; i<256; i++)
            numPerGray[i] = 0;
        //求出每个灰度级有多少个像素点
        for (int i=0; i<width*height; i++){
            int gray = pixelArray[i] & 0x000000ff;
            numPerGray[gray]++;
        }

        int sumGray = 0;
        for (int i=0; i<width*height; i++){
            sumGray += (pixelArray[i] & 0x000000ff);
        }
        float averageGray = sumGray/(width*height);//图像的平均灰度
        float wTarget = 0.0f;//目标占图像比例
        float averageTarget = 0.0f;//目标平均灰度
        // float wBackground = 0.0f;//背景所占图像比例
        float averageBackground = 0.0f;//背景平均灰度
        for(int i=0; i<256; i++){
            int sum1 = 0;
            int gray1 = 0;
            for(int j=0; j<=i; j++){
                wTarget += numPerGray[j];
                gray1 += numPerGray[j]*j;
                sum1 += numPerGray[j];
            }
            wTarget = wTarget/(height*width);
            averageTarget = (0 != sum1) ? gray1/sum1 : 0;
            wTarget = (wTarget >1) ? 1 : wTarget;
            int sum2 = 0;
            int gray2 = 0;
            for(int j=i+1; j<256; j++){
                // wBackground += numPerGray[j]/(height*width);
                gray2 += numPerGray[j]*j;
                sum2 += numPerGray[j];
            }
            averageBackground = (0 != sum2) ? gray2/sum2 : 0;
            ostus[i] = wTarget*(averageTarget-averageGray)*(averageTarget-averageGray) + (1-wTarget)*(averageBackground-averageGray)*(averageBackground-averageGray);
        }
        int max = 0;
        for(int i=1; i<256; i++){
            if(ostus[max] < ostus[i]){
                max = i;
            }
        }
        System.out.println(max);
        return max;
    }
    //二值化
    public void threshold(){
        int threshold = thresholdOstu();
        int[] grayArray = new int[width*height];
        for (int i=0; i<width*height; i++){
            int alpha = pixelArray[i] & 0xff000000;
            int gray = ((pixelArray[i] & 0x000000ff) < threshold) ? 0 : 255;
            grayArray[i] =  alpha | (gray<<16) | (gray<<8) | gray;
        }
        pixelArray = grayArray;
    }
    //灰度拉伸
    public void brightnessExpand(){

        float d;
        int[] grayArray = new int[width*height];
        for (int i=0; i<width*height; i++){
            int gray = pixelArray[i] & 0x000000ff;
            System.out.println("font:"+gray);
            d = 255*(gray - minGray)/(maxGray - minGray);
//            d = 255/((maxGray-minGray)*(pixelArray[i] & 0x000000ff));
            if(d > 255)
                gray = 255;
            else if(d < 0){
                gray = 0;
            }else{
                gray = (int)d;
            }

            System.out.println("back:"+gray);
            grayArray[i] =  (pixelArray[i] & 0xff000000) | (gray<<16) | (gray<<8) | gray;
        }
        pixelArray = grayArray;
    }

    //中值滤波
    void medianFilter(){
        int[] grayArray = new int[width*height];
        //int[] elementMatrix = {1, 2, 1, 2, 4, 2, 1, 2, 1};//高斯滤波模板
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(0==i || 0==j || width-1==j || height-1==i){
                    grayArray[i*width+j] = pixelArray[i*width+j];
                }else{
                    int pixel = pixelArray[i*width+j];
                    int alpha = pixel & 0xff000000;
                    int[] sourceMatrix = new int[9];
                    sourceMatrix[0] = pixelArray[(i-1)*width+j-1] & 0X000000FF;
                    sourceMatrix[1] = pixelArray[(i-1)*width+j] & 0X000000FF;
                    sourceMatrix[2] = pixelArray[(i-1)*width+j+1] & 0X000000FF;
                    sourceMatrix[3] = pixelArray[i*width+j-1] & 0X000000FF;
                    sourceMatrix[4] = pixelArray[i*width+j] & 0X000000FF;
                    sourceMatrix[5] = pixelArray[i*width+j+1] & 0X000000FF;
                    sourceMatrix[6] = pixelArray[(i+1)*width+j-1] & 0X000000FF;
                    sourceMatrix[7] = pixelArray[(i+1)*width+j] & 0X000000FF;
                    sourceMatrix[8] = pixelArray[(i+1)*width+j+1] & 0X000000FF;
                    for(int m=0; m<9; m++){
                        for(int n=1; n<9-m-1; n++){
                            if(sourceMatrix[n] > sourceMatrix[n+1]){
                                int temp = sourceMatrix[n+1];
                                sourceMatrix[n+1] = sourceMatrix[n];
                                sourceMatrix[n] = temp;
                            }
                        }
                    }
                    int res = sourceMatrix[4];
                    grayArray[i*width+j] = alpha | (res<<16) | (res<<8) | res;
                }
            }
        }
        pixelArray = grayArray;
    }
    //sobel算子进行梯度锐化
    void sobelGradientSharp(){
        int[] grayArray = new int[width*height];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(0==i || 0==j || width-1==j || height-1==i){
                    grayArray[i*width+j] = pixelArray[i*width+j];
                }else{
                    int pixel = pixelArray[i*width+j];
                    int alpha = pixel & 0xff000000;
                    int[] sourceMatrix = new int[9];
                    sourceMatrix[0] = pixelArray[(i-1)*width+j-1] & 0X000000FF;
                    sourceMatrix[1] = pixelArray[(i-1)*width+j] & 0X000000FF;
                    sourceMatrix[2] = pixelArray[(i-1)*width+j+1] & 0X000000FF;
                    sourceMatrix[3] = pixelArray[i*width+j-1] & 0X000000FF;
                    sourceMatrix[4] = pixelArray[i*width+j] & 0X000000FF;
                    sourceMatrix[5] = pixelArray[i*width+j+1] & 0X000000FF;
                    sourceMatrix[6] = pixelArray[(i+1)*width+j-1] & 0X000000FF;
                    sourceMatrix[7] = pixelArray[(i+1)*width+j] & 0X000000FF;
                    sourceMatrix[8] = pixelArray[(i+1)*width+j+1] & 0X000000FF;
                    int gx = sourceMatrix[2]+2*sourceMatrix[3]+sourceMatrix[4]-(sourceMatrix[0]+2*sourceMatrix[7]+sourceMatrix[8]);
                    int gy = sourceMatrix[0]+2*sourceMatrix[1]+sourceMatrix[2]-(sourceMatrix[6]+2*sourceMatrix[5]+sourceMatrix[4]);
                    int temp = Math.abs(gx) + Math.abs(gy);
                    int gray = 0;
                    if(temp > 255){
                        gray = 255;
                    } else if(gray < 0){
                        gray = 0;
                    }else{
                        gray = temp;
                    }
                    grayArray[i*width+j] = alpha | (gray<<16) | (gray<<8) | gray;
                }
            }
        }
        pixelArray = grayArray;
    }

    //生成图片
    public void generateImage(){
        BufferedImage grayImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        grayImage.setRGB(0,0,width,height,pixelArray,0,width);
        File outFile = new File("C:\\Users\\Administrator\\Desktop\\1113.jpg");
        try {
            ImageIO.write(grayImage, "jpg", outFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        File fileOne = new File("C:\\Users\\Administrator\\Desktop\\01_1.jpg");
        BufferedImage sourceImage = ImageIO.read(fileOne);
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int[] pixelArray = new int[width * height];
        pixelArray = sourceImage.getRGB(0, 0, width, height, pixelArray, 0, width);
        sobelGradientSharp process = new sobelGradientSharp(pixelArray, width, height);
        //预处理
        process.toGray();//转化为灰度图像
        process.threshold();//二值化
        //  process.brightnessExpand();
        process.sobelGradientSharp();//基于sobel的梯度锐化
        process.medianFilter();//中值滤波
        process.generateImage();
    }
}



