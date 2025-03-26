package org.firstinspires.ftc.teamcode.subsystems.chassis;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;

public class Chassis extends SubsystemBase {
    private final Follower follower;
    private final Telemetry telemetry;
    private boolean isRobotCentric = false;
    private double speedModifier = 1.0;

    public Chassis(Follower follower, Telemetry telemetry, HardwareMap hardwareMap) {
        this.follower = follower;

        this.telemetry = telemetry;
    }

    public void setPosition(Pose pose) {
        follower.setPose(pose);
    }

    public Pose getPoseEstimate() {
        return follower.getPose();
    }

    public void setDrivePowers(double forward, double strafe, double rotate) {
        follower.setTeleOpMovementVectors(forward * speedModifier, strafe * speedModifier, rotate * speedModifier, isRobotCentric);
    }

    public void followPath(Path path) {
        follower.followPath(path);
    }

    public void breakFollowing() {
        follower.breakFollowing();
    }

    public boolean isBusy() {
        return follower.isBusy();
    }

    public void toggleRobotCentric() {
        isRobotCentric = !isRobotCentric;
    }

    public void enableSlowMode() {
        speedModifier = 0.3;
    }

    public void disableSlowMode() {
        speedModifier = 1.0;
    }

    // Response curve function for finer joystick control
    public static double AvyuktResponseCurve(double input) {
        double exponent = 1.5;
        return Math.signum(input) * Math.pow(Math.abs(input), exponent);
    }

    // Response curve function for finer arm joystick control
    public double IshaanResponseCurve(double input) {
        double exponent = 1.111111;
        return Math.signum(input) * Math.pow(Math.abs(input), exponent);
    }


    @Override
    public void periodic() {
        follower.update();
        telemetry.addData("Robot Centric", isRobotCentric);
        telemetry.addData("Current Pose", getPoseEstimate());
        telemetry.update();
    }

    public void startTeleopDrive() {
        follower.startTeleopDrive();
        telemetry.addData("TeleOpDrive", "Started");
    }
}
