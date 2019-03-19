
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class unhook extends LinearOpMode {

    private DcMotor hook;

    private Servo lock;

    private double power = 0.35f;

    @Override
    public void runOpMode() {
        hook = hardwareMap.get(DcMotor.class, "HookMotor");
        lock = hardwareMap.get(Servo.class, "HookLockServo");

        hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hook.setPower(power);

        waitForStart();

        if(opModeIsActive()){
            lock.setPosition(0.5);
            sleep(2000);
            hook.setTargetPosition(630);
        }
    }
}
