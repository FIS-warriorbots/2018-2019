package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class RemoteControl extends LinearOpMode {

    double multiplier = 0.5f;
    private DcMotor leftMotor;
    private  DcMotor rightMotor;


    private double LeftPower = 0f;
    private double RightPower = 0f;

    public void runOpMode() {
        leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "RightMotor");

        telemetry.addData(">", "Press Play to start driving");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()){

            LeftPower = -this.gamepad1.left_stick_y;
            RightPower = this.gamepad1.right_stick_y;

            leftMotor.setPower(LeftPower);
            rightMotor.setPower(RightPower);

            telemetry.addData("multiplier",  multiplier);
            telemetry.update();
        }

    }
}