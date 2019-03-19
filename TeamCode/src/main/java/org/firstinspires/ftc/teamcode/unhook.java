
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class unhook extends LinearOpMode {

    private DcMotor hook;

    private double power = 0.35f;

    @Override
    public void runOpMode() {
        hook = hardwareMap.get(DcMotor.class, "HookMotor");

        hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hook.setPower(power);

        waitForStart();

        if(opModeIsActive()){
            hook.setTargetPosition(630);
        }
    }
}
