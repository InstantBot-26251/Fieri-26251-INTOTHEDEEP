package org.firstinspires.ftc.teamcode.subsystems.arm;

public class ClawConstants {
    public static final double CLAW_OPEN_POWER = 1.0;   // Power for opening the claw
    public static final double CLAW_CLOSE_POWER = -1.0; // Power for closing the claw
    public static final double CLAW_REST_POWER = 0.0;  // Power for resting the claw

    // Default state
    public static final ClawSubsystem.ClawState REST_STATE = ClawSubsystem.ClawState.REST;
}
