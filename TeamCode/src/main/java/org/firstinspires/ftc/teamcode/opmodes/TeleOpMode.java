package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.pedroPathing.examples.TeleOpEnhancements;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.MathFunctions;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Vector;
import org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants;

@TeleOp(name = "TeleOp")
public class TeleOpMode extends OpMode {
    private Follower follower;
    Arm arm;
    Intake intake;

    @Override
    public void init() {
        follower = new Follower(hardwareMap); // Initialize follower
        arm = new Arm(hardwareMap);  // Initialize arm system
        intake = new Intake(hardwareMap);  // Initialize intake system

        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        DcMotorEx leftRear = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        DcMotorEx rightRear = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        follower.setTeleOpMovementVectors(applyResponseCurve(gamepad1.left_stick_y), -applyResponseCurve(gamepad1.left_stick_x), -applyResponseCurve(gamepad1.right_stick_x));
        follower.update();

        // Arm control using gamepad2
        if (gamepad2.dpad_up) {
            arm.drive(1000);  // target position
        } else if (gamepad2.dpad_down) {
            arm.drive(0);  // Lower position
        }

        // Rotation control using gamepad2
        if (gamepad2.left_bumper) {
            arm.rotateArm(0.5);  // Rotate arm clockwise
        } else if (gamepad2.right_bumper) {
            arm.rotateArm(-0.5);  // Rotate arm counterclockwise
        } else {
            arm.rotateArm(0);  // Stop rotation
        }

        // Intake control using gamepad2
        if (gamepad2.right_trigger > 0.1) {
            intake.setIntakePower(1.0);  // Activate intake
        } else if (gamepad2.left_trigger > 0.1) {
            intake.setIntakePower(-1.0);  // Reverse intake
        } else {
            intake.setIntakePower(0);  // Stop intake
        }

        // Pivot control for intake
        if (gamepad2.dpad_left) {
            intake.setPivotPosition(0.0);  // Pivot intake to a certain position
        } else if (gamepad2.dpad_right) {
            intake.setPivotPosition(1.0);  // Pivot intake to another position
        }

        // Telemetry for diagnostics
        telemetry.addData("Left stick y", gamepad1.left_stick_y);
        telemetry.addData("Right stick y", gamepad1.right_stick_y);
        telemetry.addData("Left stick x", gamepad1.left_stick_x);
        telemetry.addData("Right stick x", gamepad1.right_stick_x);
        telemetry.addData("Arm Position", arm.atTargetPosition() ? "At Target" : "Moving");
        telemetry.addData("Servo Position", gamepad2.a ? "Open" : gamepad2.b ? "Closed" : "Neutral");
        telemetry.update();
    }

    // Response curve function for finer joystick control
    public double applyResponseCurve(double input) {
        double exponent = 2;
        return Math.signum(input) * Math.pow(Math.abs(input), exponent);
    }
}