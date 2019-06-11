package Gagarin;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import FtcExplosivesPackage.ExplosiveAuto;
import Gagarin.dubinCurve.Node;
import Gagarin.dubinCurve.curveProcessor;
import Utilities.Utility;

@Autonomous(name = "Big Boy")
public class legalAuto extends ExplosiveAuto {

    private GagarinRobot robot;
    private curveProcessor curve;

    private Node currentNode;

    private driveTracker track;

    Utility u = new Utility(this);

    @Override
    public void initHardware() {
        robot = new GagarinRobot(this);
        track = new driveTracker(robot.drive);
        robot.drive.setTracker(track);
        curve = new curveProcessor(robot.drive);
    }

    @Override
    public void initAction() {
        currentNode = new Node(0, 0,0);
    }

    @Override
    public void body() throws InterruptedException {
        curve.move(currentNode, new Node(2, 3, 0));
        currentNode = new Node(track.x, track.y, robot.gyro.getYaw());
        curve.move(currentNode, new Node(0, 0, 180));

        telemetry.addData("X", track.x);
        telemetry.addData("Y", track.y);
        telemetry.update();
        u.waitMS(3000);
    }

    @Override
    public void exit() throws InterruptedException {

    }
}
