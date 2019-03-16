package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

public class VuforiaTests extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        VuforiaLocalizer vuforia;

        //gets and sets the id of the Android view that will be used to display camera output, in vuforia

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        //set license
        parameters.vuforiaLicenseKey = "AXV87A7/////AAABmSK8TBqSnE8BoA67IBB11xOKDV9hMC6t7E4bul+/SiHvBcaqpFqe8ePgOqiSzS/hzuDlwONwvVgbkb38AnXW7mWPK8u/mDdvLThoFKRML5pZdg1gKqXVXBpXXm5jDTich8FFpsV9RMV95FzON4EIzsmQ3Kdx1AqhSm+dk/5UxJvdKC2/Iydev2eowWxGA4bTgfSQOAlKRsmGJDHYqTcl+hbuMOm8NjzRdzoLyXVgPdhoxFYkcVurSEjfZfWk/Jknhyh4lGxspISlHxmhnTFZbfi8Q62Cr+l2HjM4ljoE748OmDa3uKXk9AMS0MaPWDguNjD0Ks1isBSRt+7kEPNRzVzIYcXeMt78FLwVdXMkSqhG";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        //Get trackables from file
        VuforiaTrackables targetsRoverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        //add all trackables to list
        List<VuforiaTrackable> Trackables = new ArrayList<VuforiaTrackable>();
        Trackables.addAll(targetsRoverRuckus);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        targetsRoverRuckus.activate();

        waitForStart();

        while(opModeIsActive()){

        }
    }
}
