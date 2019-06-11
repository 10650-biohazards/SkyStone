package Gagarin;

import Gagarin.Subsystems.DriveSubsystem;

public class driveTracker {

    DriveSubsystem drive;

    public double x = 0, y = 0;

    private double oldEnc = 0;

    public driveTracker(DriveSubsystem drive) {
        this.drive = drive;
    }

    private double avgEnc() {
        double sum = 0;
        sum += drive.bright.getCurrentPosition();
        sum += drive.bleft.getCurrentPosition();
        sum += -drive.fright.getCurrentPosition();
        sum += -drive.fleft.getCurrentPosition();

        return sum / 4;
    }

    public void refresh() {
        double currAng = drive.gyro.getYaw();
        double currEnc = avgEnc();

        double deltaEnc = currEnc - oldEnc;

        y += Math.cos(Math.toRadians(currAng)) * deltaEnc;
        x += Math.sin(Math.toRadians(currAng)) * deltaEnc;

        oldEnc = currEnc;
    }
}
