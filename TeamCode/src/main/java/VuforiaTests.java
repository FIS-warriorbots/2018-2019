import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class VuforiaTests extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        while(opModeIsActive()){
            telemetry.update();
            idle();
        }
    }
    public void setupVuforia(){

    }
}
