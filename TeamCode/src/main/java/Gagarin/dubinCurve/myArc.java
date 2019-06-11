package Gagarin.dubinCurve;

public class myArc {

    public final double RADIUS = 0.75;
    Node startNode;
    public double length;
    boolean right;

    public myArc(Node startNode) {
        this.startNode = startNode;
    }

    public myPoint findCenter() {
        double x = startNode.x;
        double y = startNode.y;

        if (right) {
            x += RADIUS * Math.sin(Math.toRadians(startNode.ang) + (Math.PI / 2));
            y += RADIUS * Math.sin(Math.toRadians(startNode.ang) + (Math.PI));
        } else {
            x -= RADIUS * Math.sin(Math.toRadians(startNode.ang) + (Math.PI / 2));
            y += RADIUS * Math.sin(Math.toRadians(startNode.ang));
        }

        return new myPoint(x, y);
    }

    public void setLength(double length) {
        this.length = length;
    }

    public myPoint fin() {
        double x, y;

        if (right) {
            y = findCenter().y + (Math.sin(Math.toRadians(startNode.ang) + length) * RADIUS);
            x = findCenter().x - (Math.cos(Math.toRadians(startNode.ang) + length) * RADIUS);
        } else {
            y = 0;
            x = 0;
        }

        return new myPoint(x, y);
    }

    public void setDirection(boolean right) {
        this.right = right;
    }
}
