package Gagarin.dubinCurve;

public class spookyPathing {

    static Node start = new Node(2, 3, 0);
    static Node end = new Node(0,0, 180);


    static curveProcessor curve = new curveProcessor(null);

    public static void main(String args[]) {
        curve.findCurves(start, end);
        curve.telemtry(start, end);
    }
}