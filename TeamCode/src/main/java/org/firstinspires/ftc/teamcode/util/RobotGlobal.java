package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;

public class RobotGlobal {

    // Enum for alliance colors
    public enum Alliance {
        NONE,  // No alliance set
        RED,   // Red alliance
        BLUE   // Blue alliance
    }

    // Robot pose (shared between subsystems, updated during localization)
    public static Pose robotPose = new Pose(0, 0, 0);

    // Configuration flags
    public static boolean isAutonomous = false; // Indicates if the robot is in autonomous mode
    public static boolean isTeleOp = false;    // Indicates if the robot is in teleop mode
    public static boolean debugMode = false;   // Enable or disable debug telemetry

    // Game-specific values
    public static Alliance alliance = Alliance.NONE;  // Use the Alliance enum
    public static int startPosition = 0; // Starting position indicator (e.g., 0: None, 1-3)

    // Match state
    public static long matchStartTime = 0; // Time when the match started (milliseconds)

    // Utility methods
    public static void startMatchTimer() {
        matchStartTime = System.currentTimeMillis();
    }

    public static long getElapsedMatchTime() {
        return System.currentTimeMillis() - matchStartTime;
    }

    public static String getMatchTimeString() {
        long elapsedSeconds = getElapsedMatchTime() / 1000;
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    // Method to set the alliance color
    public static void setAlliance(Alliance allianceColor) {
        alliance = allianceColor;
    }

    // Method to get the current alliance color
    public static String getAllianceColor() {
        switch (alliance) {
            case RED:
                return "Red";
            case BLUE:
                return "Blue";
            case NONE:
            default:
                return "None";
        }
    }

    // Method to check and adjust pose based on the alliance color
    public static Pose checkAlliance(Pose pose) {
        if (alliance == Alliance.RED) {
            return toRed(pose);  // Adjust pose for red alliance if needed
        } else {
            return pose;  // No change for blue or no alliance
        }
    }

    // Method to modify the pose for red alliance (example implementation)
    private static Pose toRed(Pose pose) {
        // Modify the pose for the red alliance (e.g., reverse direction or offset)
        // For example, you could flip the x-axis for the red alliance to swap sides
        return new Pose(-pose.getX(), pose.getY(), pose.getHeading());
    }
}
