package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class TestOPmode extends LinearOpMode {

    boolean dpadDown = false;

    private double multiplier = 0.5f;
    private double multiplierChange = 0f;

    //motor declaration
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor hook;
    private double expectedLeft = 0f;
    private double expectedRight = 0f;
    private double expectedHook = 0f;

    @Override
    public void runOpMode() {
        //motor initialization
        leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "RightMotor");
        hook = hardwareMap.get(DcMotor.class, "HookMotor");

        waitForStart();

        /*
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setPower(1f);
        leftMotor.setTargetPosition(2000);
        */

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(opModeIsActive()){

            //getting input from controller for setting motor power
            expectedLeft = -this.gamepad1.left_stick_y * multiplier;
            expectedRight = this.gamepad1.right_stick_y * multiplier;
            expectedHook = this.gamepad1.right_trigger * multiplier;

            //set motor power
            leftMotor.setPower(expectedLeft);
            rightMotor.setPower(expectedRight);
            hook.setPower(expectedHook);


            if(!this.gamepad1.dpad_up && !this.gamepad1.dpad_down){
                dpadDown = false;
            }

            if (!dpadDown) {
                if (this.gamepad1.dpad_up) {
                    multiplierChange = 0.05f;
                    dpadDown = true;
                } else if (this.gamepad1.dpad_down) {
                    multiplierChange = -0.05f;
                    dpadDown = true;
                }
            }

            multiplier += multiplierChange;
            multiplierChange = 0f;

            //print expected and actual values
            telemetry.addData("Left motor expected", expectedLeft);
            telemetry.addData("Left motor actual", leftMotor.getPower());
            telemetry.addData("Right motor expected", expectedRight);
            telemetry.addData("Right motor actual", rightMotor.getPower());

            telemetry.addData("multiplier", multiplier);

            telemetry.addData("left", leftMotor.getCurrentPosition());
            telemetry.addData("right", rightMotor.getCurrentPosition()); //450 = 45 cm
            telemetry.addData("hook", hook.getCurrentPosition());

            telemetry.update(); //sent out telemetry values to driver station
        }
    }
}
