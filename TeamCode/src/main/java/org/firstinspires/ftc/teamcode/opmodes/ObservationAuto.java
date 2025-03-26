package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants;

@Autonomous(name = "Obv Auto")
public class ObservationAuto extends OpMode {
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

        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        strafeRight();
        telemetry.addData("OpMode", "stop the OpMode when observation zone reached");

    }
    public void moveForward() {
        fl.setPower(1);
        fr.setPower(1);
        br.setPower(1);
        bl.setPower(1);
    }
    public void strafeRight() {
        fl.setPower(-.5);
        br.setPower(-.5);
        fr.setPower(.5);
        bl.setPower(.5);
    }
    public void strafeLeft() {
        fl.setPower(1);
        br.setPower(1);
        fr.setPower(-1);
        bl.setPower(-1);
    }
    public void moveBackward() {
        fl.setPower(-1);
        fr.setPower(-1);
        br.setPower(-1);
        bl.setPower(-1);
    }
}
