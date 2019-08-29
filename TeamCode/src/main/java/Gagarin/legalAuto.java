package Gagarin;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import FtcExplosivesPackage.ExplosiveAuto;
import Gagarin.Subsystems.DriveSubsystem;
import Gagarin.dubinCurve.Node;
import Gagarin.dubinCurve.curveProcessor2;
import Utilities.Utility;

@Autonomous(name = "Big Boy")
public class legalAuto extends ExplosiveAuto {

    private GagarinRobot robot;
    private curveProcessor2 curve;

    private Node currentNode;

    private driveTracker track;
    DriveSubsystem drive;

    Utility u = new Utility(this);

    @Override
    public void initHardware() {
        robot = new GagarinRobot(this);
        robot.initSubsystems(this);
        track = new driveTracker(robot.drive);
        robot.drive.setTracker(track);
        curve = new curveProcessor2(robot.drive);
        drive = robot.drive;
    }

    @Override
    public void initAction() {
        currentNode = new Node(0, 0,90);
    }

    @Override
    public void body() throws InterruptedException {

        drive.setTracker(track);


        robot.gyro.startAng = 90;

        curve.move(currentNode, new Node(3, -3, 180));

        /*
        curve.move(currentNode, new Node(2, -3, 180));

        double currAng = robot.gyro.getYaw();
        if (currAng < 0) {
            currAng += 360;
        }

        tel();
        sleep(5000);


        currentNode = new Node(track.x, track.y, currAng);
        curve.move(currentNode, new Node(-0.5, -5, 270));
        tel();
        sleep(5000);

        currAng = robot.gyro.getYaw();
        if (currAng < 0) {
            currAng += 360;
        }
        currentNode = new Node(track.x, track.y, currAng);
        curve.move(currentNode, new Node(-1, -1.5, 270));
        tel();
        sleep(5000);

        currAng = robot.gyro.getYaw();
        if (currAng < 0) {
            currAng += 360;
        }
        currentNode = new Node(track.x, track.y, currAng);
        curve.move(currentNode, new Node(0, 0, 90));*/
        tel();


        //robot.gyro.startAng = 315;
        //currentNode = new Node(-0.5, -5, 315);
        //curve.move(currentNode, new Node(-1, -1.5, 270));
        //tel();


        sleep(10000);
    }

    public void tel() {
        telemetry.addData("Front left", drive.fleft.getCurrentPosition());
        telemetry.addData("Front right", drive.fright.getCurrentPosition());
        telemetry.addData("Back left", drive.bleft.getCurrentPosition());
        telemetry.addData("Back right", drive.bright.getCurrentPosition());
        telemetry.addData("Location X", track.x / 1075);
        telemetry.addData("Location Y", track.y / 1075);
        telemetry.update();
    }

    @Override
    public void exit() throws InterruptedException {

    }
}
