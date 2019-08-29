package Gagarin.dubinCurve;

import java.util.ArrayList;

import Gagarin.Subsystems.DriveSubsystem;

/**
 * Version 1.0 of the Dubin's Curve Algorithm
 * Created by Core on 20/08/19
 */

public class curveProcessor2 {

    private myArc firstArc;
    private myArc secondArc;
    private myStraight straight;

    private double targetSlope;

    private double xDiff;
    private double yDiff;

    private DriveSubsystem drive;

    private final int TIICKSPERTILE = 1075;

    private double lastTrackDist;

    public curveProcessor2(DriveSubsystem drive) {
        this.drive = drive;
    }

    public void move(Node start, Node end) {
        findCurves(start, end);

        double currAng = drive.gyro.getYaw();
        if (currAng < 0) {
            currAng += 360;
        }

        if (firstArc.right) {
            drive.swing_turn_PID(currAng + firstArc.length, true);
        } else {
            drive.swing_turn_PID(currAng - firstArc.length, false);
        }

        drive.move_straight_raw(straight.length * TIICKSPERTILE);

        if (secondArc.right) {
            drive.swing_turn_PID(end.rawAng, true);
        } else {
            drive.swing_turn_PID(end.rawAng, false);
        }

        drive.set_Pows(0, 0, 0, 0);
    }

    public void findCurves(Node start, Node end) {

        double xOffset = start.x;
        double yOffset = start.y;

        start.x -= xOffset;
        start.y -= yOffset;
        end.x   -= xOffset;
        end.y   -= yOffset;

        double angOffset = start.rawAng;
        start.calcAng -= angOffset;
        end.calcAng -= angOffset;
        if (end.calcAng < 0) {
            end.calcAng += 360;
        }

        xDiff = end.x;
        yDiff = end.y;

        double startAng = slopeFinder(xDiff, yDiff);
        double relAng = startAng - start.rawAng;
        if (relAng < 0) {
            relAng += 360;
        }

        double distFromCent = xDiff / Math.sin(Math.toRadians(startAng));

        end.x = Math.sin(Math.toRadians(relAng)) * distFromCent;
        end.y = Math.cos(Math.toRadians(relAng)) * distFromCent;







        firstArc = new myArc(start);
        secondArc = new myArc(end);

        ArrayList<Double> distances = new ArrayList<>();

        RSR(start, end);
        distances.add(lastTrackDist);
        RSL(start, end);
        distances.add(lastTrackDist);
        LSL(start, end);
        distances.add(lastTrackDist);
        LSR(start, end);
        distances.add(lastTrackDist);

        double minDist = Double.MAX_VALUE;
        int minIndex = -1;
        int ticker = 0;
        String[] paths = {"RSR", "RSL", "LSL", "LSR"};
        for (double distance : distances) {
            System.out.println("Distance:" + distance);
            if (distance < minDist) {
                minIndex = ticker;
                minDist = distance;
            }
            ticker++;
        }

        if (paths[minIndex].equals("RSR")) {
            System.out.println("RIGHT STRAIGHT RIGHT");
            RSR(start, end);
        }
        if (paths[minIndex].equals("RSL")) {
            System.out.println("RIGHT STRAIGHT LEFT");
            RSL(start, end);
        }
        if (paths[minIndex].equals("LSL")) {
            System.out.println("LEFT STRAIGHT LEFT");
            LSL(start, end);
        }
        if (paths[minIndex].equals("LSR")) {
            System.out.println("LEFT STRAIGHT RIGHT");
            LSR(start, end);
        }

        telemtry(start, end);
    }

    private double findDist(myPoint point1, myPoint point2) {
        return Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    }

    private void RSR(Node start, Node end) {

        firstArc = new myArc(start);
        secondArc = new myArc(end);

        firstArc.setDirection(true);
        secondArc.setDirection(true);

        //NEW STUFF
        xDiff = secondArc.findCenter().x - firstArc.findCenter().x;
        yDiff = secondArc.findCenter().y - firstArc.findCenter().y;
        targetSlope = slopeFinder(xDiff, yDiff);

        firstArc.setLength(targetSlope - start.calcAng);

        double temp = end.calcAng - targetSlope;
        if (temp < 0) {
            temp += 360;
        }
        secondArc.setLength(temp);

        double dist = findDist(firstArc.findCenter(), secondArc.findCenter());

        straight = new myStraight(dist);

        totalDist();
    }

    private void RSL(Node start, Node end) {

        firstArc = new myArc(start);
        secondArc = new myArc(end);

        firstArc.setDirection(true);
        secondArc.setDirection(false);

        xDiff = secondArc.findCenter().x - firstArc.findCenter().x;
        yDiff = secondArc.findCenter().y - firstArc.findCenter().y;
        double centerSlope = slopeFinder(xDiff, yDiff);

        double hyp = findDist(firstArc.findCenter(), secondArc.findCenter()) / 2;
        double oppSide = myArc.RADIUS;
        double diffSlope = Math.toDegrees(Math.asin(oppSide/hyp));

        targetSlope = simplifyAng(centerSlope + diffSlope);

        firstArc.setLength(targetSlope - start.calcAng);


        double temp = targetSlope - end.calcAng;
        if (temp < 0) {
            temp += 360;
        }
        secondArc.setLength(temp);

        double dist = 2 * hyp * Math.cos(Math.toRadians(diffSlope));

        straight = new myStraight(dist);

        totalDist();
    }

    private void LSL(Node start, Node end) {

        firstArc = new myArc(start);
        secondArc = new myArc(end);

        firstArc.setDirection(false);
        secondArc.setDirection(false);



        //NEW STUFF
        xDiff = secondArc.findCenter().x - firstArc.findCenter().x;
        yDiff = secondArc.findCenter().y - firstArc.findCenter().y;
        targetSlope = slopeFinder(xDiff, yDiff);

        firstArc.setLength(360 - targetSlope);

        double temp = targetSlope - end.calcAng;
        if (temp < 0) {
            temp += 360;
        }
        secondArc.setLength(temp);

        double dist = findDist(firstArc.findCenter(), secondArc.findCenter());

        straight = new myStraight(dist);

        totalDist();
    }

    private void LSR(Node start, Node end) {

        firstArc = new myArc(start);
        secondArc = new myArc(end);

        firstArc.setDirection(false);
        secondArc.setDirection(true);

        xDiff = secondArc.findCenter().x - firstArc.findCenter().x;
        yDiff = secondArc.findCenter().y - firstArc.findCenter().y;
        double centerSlope = slopeFinder(xDiff, yDiff);

        double hyp = findDist(firstArc.findCenter(), secondArc.findCenter()) / 2;
        double oppSide = myArc.RADIUS;
        double diffSlope = Math.toDegrees(Math.asin(oppSide/hyp));

        targetSlope = centerSlope - diffSlope;

        firstArc.setLength(360 - targetSlope);


        double temp = end.calcAng - targetSlope;
        if (temp < 0) {
            temp += 360;
        }
        secondArc.setLength(temp);

        double dist = 2 * hyp * Math.cos(Math.toRadians(diffSlope));

        straight = new myStraight(dist);

        totalDist();
    }

    public void telemtry(Node start, Node end) {
        System.out.println();
        System.out.println("////Data////");
        System.out.println("Slope: " + targetSlope);
        System.out.println("Line Length: " + straight.length + " Tiles.");
        System.out.println("Line Length: " + (straight.length * TIICKSPERTILE) + " Ticks.");
        System.out.println();
        System.out.println("////First Arc////");
        System.out.println("Length: " + firstArc.length);
        System.out.println("Start Point: (" + start.x + ", " + start.y + ")");
        System.out.println("End Point: (" + firstArc.fin().x + ", " + firstArc.fin().y + ")");
        System.out.println("Direction: " + firstArc.right);
        System.out.println("Center: (" + firstArc.findCenter().x + ", " + firstArc.findCenter().y + ")");
        System.out.println();
        System.out.println("////Second Arc////");
        System.out.println("Length: " + secondArc.length);
        System.out.println("Direction: " + secondArc.right);
        System.out.println("Center: (" + secondArc.findCenter().x + ", " + secondArc.findCenter().y + ")");
        System.out.println();
        start.print("START");
        end.print("END");
        System.out.println("Total Distance Travelled: " + lastTrackDist);
    }

    private void totalDist() {
        lastTrackDist = firstArc.findDistance() + straight.length + secondArc.findDistance();
    }

    private double slopeFinder(double xDiff, double yDiff) {
        double raw = Math.atan(yDiff/xDiff);
        if (xDiff >= 0 && yDiff >= 0) {
            raw *= -1;
            raw += Math.PI / 2;
            return Math.toDegrees(raw);
        } else if (xDiff >= 0 && yDiff <= 0) {
            raw *= -1;
            raw += Math.PI / 2;
            return Math.toDegrees(raw);
        } else if (xDiff <= 0 && yDiff <= 0) {
            raw *= -1;
            raw += 3 * (Math.PI / 2);
            return Math.toDegrees(raw);
        } else if (xDiff <= 0 && yDiff >= 0) {
            raw *= -1;
            raw += 3 * (Math.PI / 2);
            return Math.toDegrees(raw);
        }
        return 42;
    }

    private double simplifyAng(double input) {
        if (input > 360) {
            return input - 360;
        } else {
            return input;
        }
    }
}