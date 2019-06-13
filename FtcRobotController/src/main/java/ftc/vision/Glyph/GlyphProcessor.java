package ftc.vision.Glyph;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ftc.vision.ImageProcessor;
import ftc.vision.ImageProcessorResult;
import ftc.vision.ImageUtil;

public class GlyphProcessor implements ImageProcessor<GlyphResult> {
    private static final String TAG = "GlyphProcessor";

    @Override
    public ImageProcessorResult<GlyphResult> process(long startTime, Mat rgbaFrame, boolean saveImages) {

        //Initializing different colors
        double[] greyPixel = {100, 100, 100}, brownPixel = {99, 80, 26};
        double[] greenPixel = {0, 255, 0}, redPixel = {255, 0, 0};
        double[][] newColors = {greyPixel, brownPixel};
        double[][] oldColors = {greenPixel, redPixel};

        //Final data variables
        GlyphResult.GlyphColor color = null;
        int xPos = 1, yPos = 1;

        //convert to hsv
        Mat hsv = new Mat();
        Imgproc.cvtColor(rgbaFrame, hsv, Imgproc.COLOR_RGB2HSV);


        //h range is 0-179
        //s range is 0-255
        //v range is 0-255

        //Detting definitions for the limits of brown and gray
        List<Scalar> hsvMaxs = new ArrayList<>();
        List<Scalar> hsvMins = new ArrayList<>();

        hsvMaxs.add(new Scalar(20, 255, 100)); //brown maxs
        hsvMins.add(new Scalar(13, 80, 40)); //brown mins

        hsvMaxs.add(new Scalar(179, 50, 150)); //grey maxs
        hsvMins.add(new Scalar(0, 0, 10)); //grey mins

        List<Mat> rgbaChannels = new ArrayList<>();

        Mat maskedImage;

        //Variables for contour detection
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        List<Rect> rects = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            maskedImage = new Mat();

            //Applying limits
            ImageUtil.hsvInRange(hsv, hsvMins.get(i), hsvMaxs.get(i), maskedImage);

            //Finding contours for rectangle detection
            Mat contTemp = maskedImage.clone();
            Imgproc.findContours(contTemp, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            for (MatOfPoint currCont : contours) {
                Rect rect = Imgproc.boundingRect(currCont);

                if (i == 0) {
                    rect.setBrown(true);
                } else {
                    rect.setBrown(false);
                }

                rects.add(rect);
            }

            rgbaChannels.add(maskedImage.clone());
        }

        //Adding blank Mat
        rgbaChannels.add(Mat.zeros(hsv.size(), CvType.CV_8UC1));

        Core.merge(rgbaChannels, rgbaFrame);



        int h = rgbaFrame.height(), w = rgbaFrame.width();

        for (int n = 0; n < 2; n++) {
            for (int j = 0; j < w * h; j++) {

                double[] pixel = rgbaFrame.get((j - (j % w)) / w, j % w);

                if (Arrays.equals(pixel, oldColors[n])) {
                    rgbaFrame.put((j - (j % w)) / w, j % w, newColors[n]);
                }
            }
        }

        Log.i(TAG, "Size: " + rects.size());

        for (Rect rect : rects) {
            double porportion = rect.height / rect.width;

            if (porportion < 1) {
                porportion = 1 / porportion;
            }

            if (rect.area() > 100 && porportion < 2) {
                Imgproc.rectangle(rgbaFrame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 1);
            } else if (rect.area() > 100){
                Imgproc.rectangle(rgbaFrame, rect.tl(), rect.br(), new Scalar(255, 255, 0), 1);
            } else {
                Imgproc.rectangle(rgbaFrame, rect.tl(), rect.br(), new Scalar(255, 0, 0), 1);
            }
        }

        return new ImageProcessorResult<>(startTime, rgbaFrame, new GlyphResult(color, xPos, yPos));
    }
}
