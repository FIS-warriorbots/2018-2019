package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class RemoteControl extends LinearOpMode {

    private double multiplier = 0.6f;
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor LinearSlide;
    private DcMotor PickupMotor;

    private Servo Lock;

    private CRServo PickupJoint;
    private CRServo PickupGrabber;

    private double LeftPower = 0f;
    private double RightPower = 0f;
    private double LinearSlidePower = 0f;
    private double PickupJointPower = 0f;
    private double PickupPower = 0f;

    public void runOpMode() {
        leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "RightMotor");
        LinearSlide = hardwareMap.get(DcMotor.class, "HookMotor");
        PickupMotor = hardwareMap.get(DcMotor.class, "PickupMotor");

        PickupGrabber = hardwareMap.get(CRServo.class, "PickupGrabber");
        Lock = hardwareMap.get(Servo.class, "LinearSlideLockServo");

        PickupJoint = hardwareMap.get(CRServo.class, "PickupJoint");

        telemetry.addData(">", "Press Play to start driving");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){

            LeftPower = -this.gamepad1.right_stick_y * multiplier;
            RightPower = this.gamepad1.left_stick_y * multiplier;
            LinearSlidePower = this.gamepad1.right_trigger - this.gamepad1.left_trigger;
            PickupJointPower = this.gamepad2.right_stick_y * 0.5;
            PickupPower = -this.gamepad2.left_stick_y;

            if(this.gamepad1.a){
                Lock.setPosition(1);
            }

            else if(this.gamepad1.b){
                Lock.setPosition(0);
            }

            if(this.gamepad2.a){
                PickupGrabber.setPower(1);
            }

            if(this.gamepad2.b){
                PickupGrabber.setPower(0);
            }

            if(this.gamepad2.x){
                PickupGrabber.setPower(-1);
            }

            leftMotor.setPower(LeftPower);
            rightMotor.setPower(RightPower);
            LinearSlide.setPower(LinearSlidePower);
            PickupJoint.setPower(PickupJointPower);
            PickupMotor.setPower(PickupPower);
            telemetry.addData("PickupMotor", PickupMotor.getPower());
            telemetry.update();
        }
    }
}