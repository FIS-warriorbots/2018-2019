import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous
public class autonomous extends LinearOpMode {

    //Motor variables (wheels)
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    //Motor variables (Linear Slide)
    private DcMotor LinearSlide;
    private Servo Lock;

    private ElapsedTime time = new ElapsedTime(); //timer

    //global power constants
    private final double LinearSlidePower = 0.35f;
    private final double drivePower = 0.6f;



    boolean delStat = true;

    private double samplePower = 0.4f;

    private double afterSamplePower = 1;

    private boolean Sampled = false;

    private static final String TensorFlowAssetFile = "RoverRuckus.tflite";
    private static final String GoldMineralLabel = "Gold Mineral";
    private static final String SilverMineralLabel = "Silver Mineral";

    private static final String VUFORIA_KEY = "AXV87A7/////AAABmSK8TBqSnE8BoA67IBB11xOKDV9hMC6t7E4bul+/SiHvBcaqpFqe8ePgOqiSzS/hzuDlwONwvVgbkb38AnXW7mWPK8u/mDdvLThoFKRML5pZdg1gKqXVXBpXXm5jDTich8FFpsV9RMV95FzON4EIzsmQ3Kdx1AqhSm+dk/5UxJvdKC2/Iydev2eowWxGA4bTgfSQOAlKRsmGJDHYqTcl+hbuMOm8NjzRdzoLyXVgPdhoxFYkcVurSEjfZfWk/Jknhyh4lGxspISlHxmhnTFZbfi8Q62Cr+l2HjM4ljoE748OmDa3uKXk9AMS0MaPWDguNjD0Ks1isBSRt+7kEPNRzVzIYcXeMt78FLwVdXMkSqhG";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tensorFlow;

    private int GoldMineralX = -1;


    @Override
    public void runOpMode() throws InterruptedException {
        LinearSlide = hardwareMap.get(DcMotor.class, "HookMotor");
        leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "RightMotor");

        Lock = hardwareMap.get(Servo.class, "LinearSlideLockServo");

        //reset motor positions to zero
        LinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set motor runModes so the motors will turn on until they hit the designated encoder positions
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set motorPower values
        LinearSlide.setPower(LinearSlidePower);

        initVuforia();

        initTensorFlow();

        waitForStart();

        if(opModeIsActive()) {
            unHook();
        }

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        tensorFlow.activate();

        time.reset();

        while(opModeIsActive() && !Sampled){
            Sample();
        }

        tensorFlow.shutdown();

        while(leftMotor.isBusy());
    }

    void Sample(){

        if(time.milliseconds() >= 500){
            delStat = !delStat;
            time.reset();
        }

        List<Recognition> recognitions = tensorFlow.getUpdatedRecognitions();

        GoldMineralX = -1;

        if(recognitions != null) {
            for (Recognition recognition : recognitions) {
                if (recognition.getLabel().equals(GoldMineralLabel)) {
                    GoldMineralX = (int) recognition.getLeft();
                }
            }
            telemetry.addData("Xpos", GoldMineralX);
        }

        if(!Sampled) {
            if ((GoldMineralX >= 500) && (GoldMineralX <= 700)) {

                leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                leftMotor.setTargetPosition(-1000);
                rightMotor.setTargetPosition(1000);

                leftMotor.setPower(-afterSamplePower);
                rightMotor.setPower(afterSamplePower);

                Sampled = true;
            }

            else{
                if(delStat){
                    rightMotor.setPower(samplePower);
                    leftMotor.setPower(samplePower);
                }

                else{
                    rightMotor.setPower(0);
                    leftMotor.setPower(0);
                }
            }
        }
    }

    void unHook(){
        Lock.setPosition(0);
        time.reset();
        while(time.milliseconds() <= 2000){}
        LinearSlide.setTargetPosition(607); //move linear slide up (robot down)

        while(LinearSlide.isBusy()){} //wait for slide to move all the way up

        leftMotor.setPower(-drivePower);
        rightMotor.setPower(-drivePower);

        //move robot away from lander
        rightMotor.setTargetPosition(-60); //may change
        leftMotor.setTargetPosition(-125); //may change

        while(leftMotor.isBusy()){}

        LinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//reset Linear slide motor positions
        LinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        LinearSlide.setPower(-LinearSlidePower);
        //move Linear slide back down
        LinearSlide.setTargetPosition(-400); //may change

        //reset wheel motor positions
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(-drivePower);
        rightMotor.setPower(drivePower);

        //move robot forward
        leftMotor.setTargetPosition(-120); //may change
        rightMotor.setTargetPosition(120); //may change

        while(leftMotor.isBusy()){} //wait for left motor to be done moving

        //reset wheel motor positions
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(-drivePower);
        rightMotor.setPower(drivePower);

        //move robot forward
        leftMotor.setTargetPosition(-50); //may change
        rightMotor.setTargetPosition(50); //may change

        while(leftMotor.isBusy()){} //wait for left motor to be done moving

    }

    private void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTensorFlow() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        tensorFlow = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tensorFlow.loadModelFromAsset(TensorFlowAssetFile, GoldMineralLabel, SilverMineralLabel);
    }
}