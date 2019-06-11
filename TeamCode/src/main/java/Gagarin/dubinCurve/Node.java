package Gagarin.dubinCurve;

public class Node {

    public double x, y, ang;
    public double rawAng;
    public double calcAng;

    public Node(double x, double y, double ang) {
        this.x = x;
        this.y = y;
        this.ang = ang;
        this.rawAng = ang;
        this.calcAng = ang;

        if (this.ang < 0) {
            this.ang += 360;
        }
    }
}
