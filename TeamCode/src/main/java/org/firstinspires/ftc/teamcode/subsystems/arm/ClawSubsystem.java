package org.firstinspires.ftc.teamcode.subsystems.arm;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ClawSubsystem extends SubsystemBase {

    private final CRServo clawServo;

    // Define states for the claw
    public enum ClawState {
        OPEN, CLOSED, REST
    }

    private ClawState currentState;

    public ClawSubsystem(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(CRServo.class, "clawServo");
        setState(ClawConstants.REST_STATE); // Initialize in the rest state
    }

    // Set claw power based on the action
    private void setPower(double power) {
        clawServo.setPower(power);
    }

    // Open the claw
    public void openClaw() {
        setPower(ClawConstants.CLAW_OPEN_POWER);
        setState(ClawState.OPEN);
    }

    // Close the claw
    public void closeClaw() {
        setPower(ClawConstants.CLAW_CLOSE_POWER);
        setState(ClawState.CLOSED);
    }

    // Stop the claw
    public void restClaw() {
        setPower(ClawConstants.CLAW_REST_POWER);
        setState(ClawState.REST);
    }

    // Update the state of the claw
    public void setState(ClawState state) {
        this.currentState = state;
    }

    public ClawState getState() {
        return currentState;
    }
}
