package org.firstinspires.ftc.teamcode.opmodes;

import androidx.loader.content.Loader;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants;

@Autonomous(name = "test for encoder ticks")
public class testforencoderticks extends OpMode {
    private boolean isMovingForward = true;
    private boolean isScoring = false;
    private boolean isMovingBackward = false;

    DcMotorEx fl;
    DcMotorEx fr;
    DcMotorEx bl;
    DcMotorEx br;

    @Override
    public void init() {
        fl = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        bl = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        br = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);
        fr = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);

        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    // time for driving to scoring:
    @Override
    public void loop() {

        telemetry.addData("Front Left Motor Encoder:", fl.getCurrentPosition());
        telemetry.addData("Front Right Motor Encoder:", fr.getCurrentPosition());
        telemetry.addData("Back Left Motor Encoder:", bl.getCurrentPosition());
        telemetry.addData("Back Right Motor Encoder:", br.getCurrentPosition());
        telemetry.addData("Average Encoder Position:", (Math.abs(fl.getCurrentPosition()) + Math.abs(fr.getCurrentPosition()) +
                Math.abs(bl.getCurrentPosition()) + Math.abs(br.getCurrentPosition())) / 4);
        telemetry.addData("Is Moving Forward:", isMovingForward);
        telemetry.addData("Is Scoring:", isScoring);
        telemetry.addData("Is Moving Backward:", isMovingBackward);
        telemetry.update();  // This should be at the end of loop()
    }

}
