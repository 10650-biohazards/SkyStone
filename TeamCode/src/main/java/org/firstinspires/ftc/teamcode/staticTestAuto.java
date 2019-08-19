package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "Static test")
public class staticTestAuto extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {
        staticTestTele.testVariable = 42;
    }
}
