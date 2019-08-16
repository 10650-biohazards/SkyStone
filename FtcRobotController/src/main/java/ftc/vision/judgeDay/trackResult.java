package ftc.vision.judgeDay;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

import ftc.vision.ImageUtil;

public class trackResult {

    public Point screenLoc;

    public trackResult(Point screenLoc) {
        this.screenLoc = screenLoc;
    }

    @Override
    public String toString() {
        if (screenLoc != null) {
            return screenLoc.toString();
        } else {
            return "Sorry, can't help you!";
        }
    }
}
