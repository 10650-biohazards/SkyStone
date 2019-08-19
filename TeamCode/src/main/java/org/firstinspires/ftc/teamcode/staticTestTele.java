package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "Static Test")
public class staticTestTele extends OpMode {

    static double testVariable;

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("Value", testVariable);
        telemetry.update();
    }
}
