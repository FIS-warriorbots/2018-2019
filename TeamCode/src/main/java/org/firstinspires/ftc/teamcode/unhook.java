
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class unhook extends LinearOpMode {

    private DcMotor slide;
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private Servo lock;

    private double power = 0.35f;

    private double drivePower = 0.6f;

    @Override
    public void runOpMode() {
        slide = hardwareMap.get(DcMotor.class, "HookMotor");
        leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "RightMotor");

        lock = hardwareMap.get(Servo.class, "HookLockServo");

        //reset motor positions to zero
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set motor runModes so the motors will turn on until they hit the designated encoder positions
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set motorPower values
        leftMotor.setPower(drivePower);
        rightMotor.setPower(drivePower);
        slide.setPower(power);

        waitForStart();

        if(opModeIsActive()){
            lock.setPosition(0.5); //move linearSlide lock 90°
            sleep(2000);
            slide.setTargetPosition(630); //move linear slide up (robot down)
        }
    }
}
