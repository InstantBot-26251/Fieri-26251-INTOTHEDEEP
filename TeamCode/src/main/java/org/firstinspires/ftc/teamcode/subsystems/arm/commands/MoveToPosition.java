package org.firstinspires.ftc.teamcode.subsystems.arm.commands;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.util.PIDFController;

public class MoveToPosition {
    DcMotorEx slide;

    public MoveToPosition(HardwareMap hardwareMap, Integer Point, PIDFController PIDF) {
        slide = hardwareMap.get(DcMotorEx.class, "armMotor");
        slide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


}
