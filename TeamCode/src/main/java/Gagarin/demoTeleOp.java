package Gagarin;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import FtcExplosivesPackage.ExplosiveTele;
import Gagarin.Commands.ArmCommand;
import Gagarin.Commands.DriveCommand;
import Gagarin.Commands.IntakeCommand;
import Gagarin.Commands.LiftCommand;
import Gagarin.Commands.MarkerCommand;
import Utilities.PID;

@TeleOp(name = "Gagarin Demo")
public class demoTeleOp extends ExplosiveTele {

    public GagarinRobot robot;

    private double startTime;

    private PID testPID = new PID();

    @Override
    public void initHardware() {
        robot = new GagarinRobot(this, hardwareMap);

        ArmCommand arm = new ArmCommand(this, robot.slideMotor, robot.rackMotor, robot.potent);
        DriveCommand drive = new DriveCommand(this, robot.fleft, robot.fright, robot.bleft, robot.bright, robot.gyro);
        IntakeCommand intake = new IntakeCommand(this, robot.door, robot.intakeMotor, robot.lRotator, robot.rRotator,
                                                 robot.intakeGyro, robot.potent);
        LiftCommand lift = new LiftCommand(this, robot.liftMotor);
        MarkerCommand mark = new MarkerCommand(this, robot.markServo);


        arm.enable();
        drive.enable();
        intake.enable();
        lift.enable();
        mark.enable();
    }

    @Override
    public void initAction() {
        robot.markServo.setPosition(0);
    }

    @Override
    public void firstLoop() {
        startTime = System.currentTimeMillis();
        testPID.setup(0,0,-3,0,1,10000);
    }

    @Override
    public void bodyLoop() {

    }

    @Override
    public void exit() {

    }
}