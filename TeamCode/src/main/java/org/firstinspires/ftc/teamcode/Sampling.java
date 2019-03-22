package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous
public class Sampling extends LinearOpMode {

    ElapsedTime time = new ElapsedTime();

    boolean delStat = false;
    double delay = 500f;

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private int distance = 1000; //1000 =
    private double motorpower = 0.4f;

    private boolean Sampled = false;

    private static final String TensorFlowAssetFile = "RoverRuckus.tflite";
    private static final String GoldMineralLabel = "Gold Mineral";
    private static final String SilverMineralLabel = "Silver Mineral";

    private static final String VUFORIA_KEY = "AXV87A7/////AAABmSK8TBqSnE8BoA67IBB11xOKDV9hMC6t7E4bul+/SiHvBcaqpFqe8ePgOqiSzS/hzuDlwONwvVgbkb38AnXW7mWPK8u/mDdvLThoFKRML5pZdg1gKqXVXBpXXm5jDTich8FFpsV9RMV95FzON4EIzsmQ3Kdx1AqhSm+dk/5UxJvdKC2/Iydev2eowWxGA4bTgfSQOAlKRsmGJDHYqTcl+hbuMOm8NjzRdzoLyXVgPdhoxFYkcVurSEjfZfWk/Jknhyh4lGxspISlHxmhnTFZbfi8Q62Cr+l2HjM4ljoE748OmDa3uKXk9AMS0MaPWDguNjD0Ks1isBSRt+7kEPNRzVzIYcXeMt78FLwVdXMkSqhG";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tensorFlow;

    private int GoldMineralX = -1;
    private int SilverMineral1X = -1;
    private int SilverMineral2X = -1;


    @Override
    public void runOpMode() {

        leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "RightMotor");


        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        initVuforia();

        initTensorFlow();

        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        if(opModeIsActive()){
            if(tensorFlow != null){
                tensorFlow.activate();
            }

            rightMotor.setPower(motorpower);
            leftMotor.setPower(motorpower);

            time.reset();
        }

        while(opModeIsActive()){

            if(time.milliseconds() >= delay){
                delStat = !delStat;
                time.reset();
            }

            List<Recognition> recognitions = tensorFlow.getUpdatedRecognitions();

            GoldMineralX = -1;
            SilverMineral1X = -1;
            SilverMineral2X = -1;

            if(recognitions != null) {
                for (Recognition recognition : recognitions) {
                    if (recognition.getLabel().equals(GoldMineralLabel)) {
                        GoldMineralX = (int) recognition.getLeft();
                    } else if (SilverMineral1X == -1) {
                        SilverMineral1X = (int) recognition.getLeft();
                    } else {
                        SilverMineral2X = (int) recognition.getLeft();
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

                    leftMotor.setTargetPosition(-distance);
                    rightMotor.setTargetPosition(distance);

                    leftMotor.setPower(-motorpower);
                    rightMotor.setPower(motorpower);

                    Sampled = true;
                }

                else{
                    if(delStat){
                        rightMotor.setPower(motorpower);
                        leftMotor.setPower(motorpower);
                    }

                    else{
                        rightMotor.setPower(0);
                        leftMotor.setPower(0);
                    }
                }
            }

            telemetry.addData("Xpos", GoldMineralX);
            telemetry.addData("sampled", Sampled);

            telemetry.update();
        }

        tensorFlow.shutdown();
    }

    private void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        parameters.cameraDirection = CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTensorFlow() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        tensorFlow = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tensorFlow.loadModelFromAsset(TensorFlowAssetFile, GoldMineralLabel, SilverMineralLabel);
    }
}
