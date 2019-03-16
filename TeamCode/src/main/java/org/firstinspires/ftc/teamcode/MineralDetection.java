package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous
public class MineralDetection extends LinearOpMode {

    private static final String TensorFlowAssetFile = "RoverRuckus.tflite";
    private static final String GoldMineralLabel = "Gold Mineral";
    private static final String SilverMineralLabel = "Silver Mineral";

    private static final String VUFORIA_KEY = "AXV87A7/////AAABmSK8TBqSnE8BoA67IBB11xOKDV9hMC6t7E4bul+/SiHvBcaqpFqe8ePgOqiSzS/hzuDlwONwvVgbkb38AnXW7mWPK8u/mDdvLThoFKRML5pZdg1gKqXVXBpXXm5jDTich8FFpsV9RMV95FzON4EIzsmQ3Kdx1AqhSm+dk/5UxJvdKC2/Iydev2eowWxGA4bTgfSQOAlKRsmGJDHYqTcl+hbuMOm8NjzRdzoLyXVgPdhoxFYkcVurSEjfZfWk/Jknhyh4lGxspISlHxmhnTFZbfi8Q62Cr+l2HjM4ljoE748OmDa3uKXk9AMS0MaPWDguNjD0Ks1isBSRt+7kEPNRzVzIYcXeMt78FLwVdXMkSqhG";


    private VuforiaLocalizer vuforia;

    private TFObjectDetector tensorFlow;

    @Override
    public void runOpMode() {
        initVuforia();

        initTensorFlow();

        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        if(opModeIsActive()){
            if(tensorFlow != null){
                tensorFlow.activate();
            }
        }

        while(opModeIsActive()){
            List<Recognition> recognitions = tensorFlow.getUpdatedRecognitions();
            if(recognitions != null){
                if(recognitions.size() == 3){
                    int GoldMineralX =-1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;

                    for(Recognition recognition : recognitions){
                        if(recognition.getLabel().equals(GoldMineralLabel)){
                            GoldMineralX = (int) recognition.getLeft();
                        }
                        else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }

                    }

                    if (GoldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (GoldMineralX < silverMineral1X && GoldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (GoldMineralX > silverMineral1X && GoldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                        }
                    }
                }

            }
            telemetry.update();
        }
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
