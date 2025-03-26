package org.firstinspires.ftc.teamcode.util;


import com.qualcomm.robotcore.hardware.Gamepad;

public class Gamepad2InputHandler {
    private final Gamepad gamepad2;
    private final Arm2 arm;
    private final Intake intake;

    public Gamepad2InputHandler(Gamepad gamepad2, Arm2 arm, Intake intake) {
        this.arm = arm;
        this.intake = intake;
        this.gamepad2 = gamepad2;
    }
}