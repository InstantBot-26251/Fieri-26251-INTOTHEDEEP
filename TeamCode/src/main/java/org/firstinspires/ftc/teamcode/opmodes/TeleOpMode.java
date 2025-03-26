package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.leftFrontMotorName;
import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.leftRearMotorName;
import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.rightFrontMotorName;
import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.rightRearMotorName;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotCore;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.subsystems.chassis.Chassis2;
import org.firstinspires.ftc.teamcode.util.Arm2;
import org.firstinspires.ftc.teamcode.util.ClawState;
import org.firstinspires.ftc.teamcode.util.Intake;
import org.firstinspires.ftc.teamcode.util.RobotGlobal;

@Config
@TeleOp(name = "Period of Manual Control")
public class TeleOpMode extends OpMode {
    private Chassis2 chassis;
    Arm2 arm;
    Intake claw;
    ClawState clawState;
    Follower follower;
    private DcMotorEx leftFront;
    private DcMotorEx leftRear;
    private DcMotorEx rightFront;
    private DcMotorEx rightRear;


    @Override
    public void init() {
        // Initialize arm and intake systems first
        arm = new Arm2(hardwareMap);  // Initialize arm system
        claw = new Intake(hardwareMap, clawState);  // Initialize intake system

//        // Initialize chassis
//        chassis = new Chassis2(hardwareMap);

        leftFront = hardwareMap.get(DcMotorEx.class, leftFrontMotorName);
        leftRear = hardwareMap.get(DcMotorEx.class, leftRearMotorName);
        rightRear = hardwareMap.get(DcMotorEx.class, rightRearMotorName);
        rightFront = hardwareMap.get(DcMotorEx.class, rightFrontMotorName);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize follower
        follower = new Follower(hardwareMap);
        follower.startTeleopDrive();

    }

    @Override
    public void loop() {
        // follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        arm.setPivotPowerAuto();
        arm.setSlidePowerAuto();
        // Read inputs for controlling chassis
        double y = AvyuktResponseCurve(gamepad1.left_stick_y);
        double x = -AvyuktResponseCurve(gamepad1.left_stick_x);
        double rx = -AvyuktResponseCurve(gamepad1.right_stick_x);
        double y2 = IshaanResponseCurve(gamepad2.left_stick_y);
        double y3 = IshaanResponseCurve(gamepad2.right_stick_y);

//        Drive control
        chassis.drive(x, y, rx);
     if (gamepad1.options) {
            chassis.resetYaw();
        }

        // Arm control with limit checks
        if (arm.getSlidePosition() <= -2191 && y2 < 0 || arm.getSlidePosition() >= 0 && y2 > 0) {
            arm.setSlidePower(0); // Stop downward/upward movement
        }
        else {
            arm.setSlidePower(y2);
        }
        arm.setPivotPower(y3); // Set the pivot power

        // Claw controls
        if (gamepad2.right_trigger > 0) {
            claw.openClaw();
        }

        if (gamepad2.left_trigger > 0) {
            claw.closeClaw();
        }
        // Wrist controls
        if (gamepad2.right_bumper) {
            claw.setWristPower(-1); // Rotate right
        } else if (gamepad2.left_bumper) {
            claw.setWristPower(1); // Rotate left
        } else {
            claw.setWristPower(0); // Stop rotating when no button is pressed
        }

        // Telemetry for Debugging
        telemetry.addData("Slide Input (Left Stick Y):", gamepad2.left_stick_y);
        telemetry.addData("Pivot Input (Right Stick Y):", gamepad2.right_stick_y);
        telemetry.addData("Slide Encoder:", arm.getSlidePosition());
        telemetry.addData("Pivot Encoder:", arm.getPivotPosition());
        telemetry.addData("Claw Position:", claw.getClawPosition());
        telemetry.addData("Claw State:", clawState);
        telemetry.addData("Pivot Busy", arm.isPivotBusy());
        telemetry.addData("Slide Busy", arm.isSlideBusy());
        telemetry.update();  // This should be at the end of loop()
    }

    // Response curve function for finer joystick control
    public double AvyuktResponseCurve(double input) {
        double exponent = 1.5;
        return Math.signum(input) * Math.pow(Math.abs(input), exponent);
    }

    // Response curve function for finer arm joystick control
    public double IshaanResponseCurve(double input) {
        double exponent = 1.111111;
        return Math.signum(input) * Math.pow(Math.abs(input), exponent);
    }
}