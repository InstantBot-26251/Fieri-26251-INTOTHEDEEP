package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * This is the Arm Functions class. It contains all the arm functions.
 *
 * @author Lakshya Khandelwal - 26251 Instantbots
 * @version 1.3, 10/4/2024
 */
public class ArmAndIntakeFunctions {
    private final Arm arm;
    private final Intake intake;
    private final double ARM_POSITION_TOLERANCE = 2.0; // Adjust as needed for your setup (in degrees)
    private final double LIFT_POSITION_TOLERANCE = 1.0; // Tolerance for lift position (adjust as needed)

    public ArmAndIntakeFunctions(Arm arm, Intake intake) {
        this.arm = arm;
        this.intake = intake;
    }

    // Method to check if the lift is at the target position within a tolerance
    public boolean isLiftAtTarget(double targetPosition) {
        double currentLiftPosition = arm.getEncoderValue(); // Get the current lift position
        return Math.abs(currentLiftPosition - targetPosition) <= LIFT_POSITION_TOLERANCE;
    }

    // Method to check if the arm is at the target position within a tolerance
    public boolean isArmAtTarget(double targetPosition) {
        double currentArmPosition = arm.getRotatedArmPosition(); // Assuming this method exists to get the current position
        return Math.abs(currentArmPosition - targetPosition) <= ARM_POSITION_TOLERANCE;
    }

    // Method to rotate the arm to a straight-up position and reset encoder
    public void armTo90Degrees() {
        double currentArmPosition = arm.getRotatedArmPosition(); // Get current arm position in degrees

        // Check if the arm is not straight up (e.g., 90°), then rotate to straight up
        if (currentArmPosition < 90) {
            arm.rotateArm(90 - currentArmPosition); // Rotate arm to 90° position (straight up)
        } else if (currentArmPosition > 90) {
            arm.rotateArm(90 - currentArmPosition); // Rotate arm back to 90° position
        }

        // Stop arm rotation and reset encoder
        arm.rotateArm(0); // Stop arm rotation
        arm.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset motor encoder
        arm.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Resume normal operation
    }

    // Method to rotate the arm down and reset encoder
    public void armToDownPosition() {
        double currentArmPosition = arm.getRotatedArmPosition(); // Get current arm position in degrees

        // Check if the arm is not down, then rotate down
        if (currentArmPosition != 180) {
            arm.rotateArm(180 - currentArmPosition); // Rotate arm down
        }

        // Stop arm rotation and reset encoder
        arm.rotateArm(0); // Stop arm rotation
        arm.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset motor encoder
        arm.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Resume normal operation
    }

    public void collectSample() {
        armToDownPosition();
        if (isArmAtTarget(180)) {
            intake.setIntakePower(1.0);
        }
    }

    public void scoreHighBasket() {
        armTo90Degrees(); // Move arm to 90 degrees for scoring
        if (isArmAtTarget(90)) { // Check if the arm is at 90 degrees
            arm.toPoint(-5000);
            intake.setIntakePower(-1.0);
        }
    }

    public void scoreLowBasket() {
        arm.toPoint(-1000);
        arm.rotateArm(-0.5);
        intake.setIntakePower(-1);
    }

    public void scoreSpecimen() {
        arm.rotateArm(-0.5);
        intake.setPivotPosition(0.5);
    }

    public void levelTwoAscent() {
        arm.rotateArm(-0.5);
        arm.toPoint(-1250);
        arm.toPoint(0);
    }

    // Method to check if scoring in the high basket is finished
    public boolean isFinished() {
        // Check if the arm has reached the target position for scoring
        double targetPosition = -5000; // The position for high basket scoring
        double currentArmPosition = arm.getRotatedArmPosition();

        // Check if the arm is close enough to the target position
        boolean isArmAtPosition = Math.abs(currentArmPosition - targetPosition) < 50; // Adjust tolerance as necessary

        // Check if the intake servo is in the closed position (e.g., 0.0)
        boolean isIntakeClosed = Math.abs(intake.getIntakePosition() - 0.0) < 0.1; // Assuming a closed position of 0.0

        return isArmAtPosition && isIntakeClosed; // Return true if both conditions are met
    }
}
