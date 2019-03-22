
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class unhook extends LinearOpMode {

    ElapsedTime time = new ElapsedTime();

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

        lock = hardwareMap.get(Servo.class, "LinearSlideLockServo");

        //reset motor positions to zero
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set motor runModes so the motors will turn on until they hit the designated encoder positions
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set motorPower values
        slide.setPower(power);

        waitForStart();

        if(opModeIsActive()){
            lock.setPosition(0);
            time.reset();
            while(time.milliseconds() <= 2000){}
            slide.setTargetPosition(607); //move linear slide up (robot down)

            while(slide.isBusy()){} //wait for slide to move all the way up


            leftMotor.setPower(-drivePower);
            rightMotor.setPower(-drivePower);
            rightMotor.setTargetPosition(-60);
            leftMotor.setTargetPosition(-125);

            while(leftMotor.isBusy()){}

            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            slide.setPower(-power);
            slide.setTargetPosition(-400);

            time.reset();
            while(time.milliseconds() <= 2000){}

            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftMotor.setPower(-drivePower);
            rightMotor.setPower(drivePower);

            leftMotor.setTargetPosition(-120);
            rightMotor.setTargetPosition(120);

            while(leftMotor.isBusy()){}

            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftMotor.setPower(-drivePower);
            rightMotor.setPower(-drivePower);

            leftMotor.setTargetPosition(-30);
            rightMotor.setTargetPosition(-30);

            while(leftMotor.isBusy()){}
        }
    }
}
