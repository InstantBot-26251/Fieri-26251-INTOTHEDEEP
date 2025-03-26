package org.firstinspires.ftc.teamcode.subsystems.arm;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.trajectory.TrapezoidProfile;
import com.arcrobotics.ftclib.trajectory.TrapezoidProfile.State;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ArmSubsystem extends SubsystemBase {
    private final DcMotorEx armMotor;
    private final PIDFController armPid;
    private TrapezoidProfile PivotMotionProfile;
    private State goalPivotState;
    private State currentPivotState;
    private final DcMotorEx pivotMotor;
    private final ElapsedTime elapsedTime;



    public ArmSubsystem(HardwareMap hardwareMap, ElapsedTime elapsedTime) {
        armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");
        this.elapsedTime = elapsedTime;
        armMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        pivotMotor = hardwareMap.get(DcMotorEx.class, "pivotMotor");
        pivotMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        pivotMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);


        armPid = new PIDFController(
                ArmConstants.ARM_KP,
                ArmConstants.ARM_KI,
                ArmConstants.ARM_KD,
                ArmConstants.ARM_KF
        );

        goalPivotState = new State(0, 0);
        currentPivotState = new State(0, 0);
    }

    public void setPower(double input)   {
        armMotor.setPower(input);
    }

    public void toPoint(double position) {
        goalPivotState = new State(position, 0);
        PivotMotionProfile = new TrapezoidProfile(
                new TrapezoidProfile.Constraints(ArmConstants.MAX_VELOCITY, ArmConstants.MAX_ACCELERATION),
                goalPivotState,
                currentPivotState
        );
    }

    public void update() {
        double dt = 0.02; // Loop time
        currentPivotState = PivotMotionProfile.calculate(dt);

        double feedforward = ArmConstants.ARM_KF * currentPivotState.velocity;
        double feedback = armPid.calculate(armMotor.getCurrentPosition(), currentPivotState.position);

        armMotor.setPower(feedforward + feedback);
    }

    public void limitCheck(Gamepad gamepad2) {
        if (armMotor.getCurrentPosition() == -2764 && pivotMotor.getCurrentPosition() == 0 && Math.abs(gamepad2.left_stick_y) > 0.1) {
            armMotor.setPower(0);
        }
    }

    public void stop() {
        armMotor.setPower(0);
    }

    public double getEncoderValue() {
        return armMotor.getCurrentPosition();
    }
}
