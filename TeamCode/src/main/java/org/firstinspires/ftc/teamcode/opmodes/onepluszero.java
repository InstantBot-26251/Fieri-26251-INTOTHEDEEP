package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants;
import org.firstinspires.ftc.teamcode.util.Arm2;

@Autonomous(name = "Autonomous Routines for Scoring Specimens")
public class onepluszero extends OpMode {
    DcMotorEx fl;
    DcMotorEx fr;
    DcMotorEx bl;
    DcMotorEx br;
    Arm2 arm;

    private static final double DRIVE_POWER = 0.5; // Full power to move forward/backward

    private boolean movementComplete = false;
    private boolean scoringComplete = false;

    @Override
    public void init() {
        // Initialize motors
        fl = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        bl = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        br = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);
        fr = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);

        // Motor behaviors
        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Reverse motors for correct directions
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        // Reset encoders
        resetEncoders();

        arm = new Arm2(hardwareMap);

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (!movementComplete) {
            // Set target positions only once
            fl.setTargetPosition(1152);
            fr.setTargetPosition(-1);
            bl.setTargetPosition(-992);
            br.setTargetPosition(443);

            // Enable RUN_TO_POSITION mode and start movement
            setRunToPosition();
            setMotorPowers(DRIVE_POWER);

            movementComplete = true; // Ensure this block only runs once
        }

        // Check if all motors have finished moving
        if (movementComplete && !fl.isBusy() && !fr.isBusy() && !bl.isBusy() && !br.isBusy()) {
            stopMotors();

            if (!scoringComplete) {
                // scoreSpecimen();
                scoringComplete = true;
            }
        }
        // Debugging telemetry
        telemetry.addData("FL Position", fl.getCurrentPosition());
        telemetry.addData("FR Position", fr.getCurrentPosition());
        telemetry.addData("BL Position", bl.getCurrentPosition());
        telemetry.addData("BR Position", br.getCurrentPosition());
        telemetry.addLine("Running...");
        telemetry.update();

        telemetry.update();

        arm.setSlidePowerAuto();
        arm.setPivotPowerAuto();


        //  scoreSpecimen();
        parkObvZone();
    }

    private void moveForward(int targetPosition) {
        fl.setTargetPosition(-722);
        fr.setTargetPosition(1142);
        bl.setTargetPosition(2478);
        br.setTargetPosition(36);

        // Set motor power to move forward
        setMotorPowers(DRIVE_POWER);


        // Stop the motors once the robot has reached the target position
        stopMotors();
    }
    private void setRunToPosition() {
        fl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
    }

    private void moveBackward(int targetPosition) {
        // Set target position for all motors, but move backward
        setTargetPosition(-targetPosition);

        // Set motor power to move backward
        setMotorPowers(-DRIVE_POWER);


        // Stop the motors once the robot has reached the target position
        stopMotors();
    }

    private void setTargetPosition(int position) {
        fl.setTargetPosition(fl.getCurrentPosition() + position);
        fr.setTargetPosition(fr.getCurrentPosition() + position);
        bl.setTargetPosition(bl.getCurrentPosition() + position);
        br.setTargetPosition(br.getCurrentPosition() + position);
    }

    private void setMotorPowers(double power) {
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
    }



    private void resetEncoders() {
        fl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void stopMotors() {
        setMotorPowers(0);
    }

    private void parkObvZone() {
        fl.setPower(-.5);
        br.setPower(-.5);
        fr.setPower(.5);
        bl.setPower(.5);
    }
    private void scoreSpecimen() {
        // Sequence to score specimen
        arm.setPivotPosition(-1053);

        if (!arm.isPivotBusy()) {
            arm.setSlidePosition(-1507);

            if (!arm.isSlideBusy()) {
                arm.setPivotPosition(-590);

                if (!arm.isPivotBusy()) {
                    arm.setSlidePosition(-1300);
                }
                if (!arm.isSlideBusy()) {
                    fl.setTargetPosition(1152);
                    fr.setTargetPosition(-1);
                    bl.setTargetPosition(-992);
                    br.setTargetPosition(443);
                    setRunToPosition();
                    setMotorPowers(-DRIVE_POWER);
                }
            }
        }
        telemetry.addLine("Specimen Scoring Complete");
    }
}
// Slide -1507, Pivot = -590