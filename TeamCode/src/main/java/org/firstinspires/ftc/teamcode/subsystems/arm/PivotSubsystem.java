package org.firstinspires.ftc.teamcode.subsystems.arm;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.trajectory.TrapezoidProfile;
import com.arcrobotics.ftclib.trajectory.TrapezoidProfile.State;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PivotSubsystem extends SubsystemBase {
    public final DcMotorEx pivotMotor;
    private final PIDFController pivotPid;
    private TrapezoidProfile pivotMotionProfile;
    private State pivotGoalState;
    private State pivotCurrentState;

    public PivotSubsystem(HardwareMap hardwareMap) {
        pivotMotor = hardwareMap.get(DcMotorEx.class, "rotationMotor");
        pivotMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        pivotMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        pivotPid = new PIDFController(
                ArmConstants.PIVOT_KP,
                ArmConstants.PIVOT_KI,
                ArmConstants.PIVOT_KD,
                ArmConstants.PIVOT_KF
        );

        pivotGoalState = new State(0, 0);
        pivotCurrentState = new State(0, 0);
    }

    public void toPoint(double position) {
        pivotGoalState = new State(position, 0);
        pivotMotionProfile = new TrapezoidProfile(
                new TrapezoidProfile.Constraints(ArmConstants.PIVOT_MAX_VELOCITY, ArmConstants.PIVOT_MAX_ACCELERATION),
                pivotGoalState,
                pivotCurrentState
        );
    }

    public void update() {
        double dt = 0.02;
        pivotCurrentState = pivotMotionProfile.calculate(dt);

        double feedforward = ArmConstants.PIVOT_KF * pivotCurrentState.velocity;
        double feedback = pivotPid.calculate(pivotMotor.getCurrentPosition(), pivotCurrentState.position);

        pivotMotor.setPower(feedforward + feedback);
    }

    public void stop() {
        pivotMotor.setPower(0);
    }

    public double getEncoderValue() {
        return pivotMotor.getCurrentPosition();
    }
}
