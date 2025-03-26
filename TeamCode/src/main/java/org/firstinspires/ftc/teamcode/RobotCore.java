package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.subsystems.arm.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.arm.ArmSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.arm.PivotSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.chassis.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.chassis.commands.TeleopDriveCommand;
import org.firstinspires.ftc.teamcode.util.RobotGlobal;

public class RobotCore extends Robot {
    private static RobotCore INSTANCE;
    public Follower follower;
    // Subsystems
    private ClawSubsystem clawSubsystem;
    private ArmSubsystem armSubsystem;
    private PivotSubsystem pivotSubsystem;
    private Chassis chassis;

    // Controllers
    private GamepadEx driveController;
    private GamepadEx manipController;

    // Commands
    private TeleopDriveCommand driveCommand;

    // Enum for OpMode types
    public enum OpModeType {
        TELEOP,
        AUTO,
        EMPTY
    }

    /**
     * Singleton pattern to access the RobotCore instance.
     */
    public static RobotCore getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("RobotCore is not initialized!");
        }
        return INSTANCE;
    }

    /**
     * RobotCore constructor
     *
     * @param telemetry The telemetry object for logging data.
     * @param gamepad1  The primary driver gamepad.
     * @param gamepad2  The manipulator gamepad.
     * @param type      The type of OpMode (TELEOP, AUTO, EMPTY).
     */
    public RobotCore(Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2, OpModeType type) {
        INSTANCE = this;

        // Initialize controllers
        this.driveController = new GamepadEx(gamepad1);
        this.manipController = new GamepadEx(gamepad2);

        // Initialize subsystems and set up the OpMode
        initSubsystems();
        setupOpMode(type);
    }

    /**
     * Initialize the subsystems of the robot.
     */
    private void initSubsystems() {
        // Initialize subsystems
        chassis = new Chassis(follower, telemetry, hardwareMap);
        // armSubsystem = new ArmSubsystem(hardwareMap, ElapsedTime);
        pivotSubsystem = new PivotSubsystem(hardwareMap);
        clawSubsystem = new ClawSubsystem(hardwareMap);

        // Register subsystems for command scheduling (ftclib handles this automatically)
        register(chassis, armSubsystem, pivotSubsystem, clawSubsystem);

        // Set initial values for RobotGlobal (e.g., alliance color)
        RobotGlobal.setAlliance(RobotGlobal.Alliance.NONE);

        telemetry.addData("Status", "Subsystems initialized");
        telemetry.update();
    }

    /**
     * Configure drive controls for teleop.
     */
    private void setDriveControls() {
        // Using DoubleSupplier to pass lambdas for gamepad inputs
        driveCommand = new TeleopDriveCommand(
                chassis,
                (Chassis.AvyuktResponseCurve(driveController.gamepad.left_stick_x)),
                (Chassis.AvyuktResponseCurve(-driveController.gamepad.left_stick_y)),
                (Chassis.AvyuktResponseCurve(-driveController.gamepad.right_stick_x))
        );

        // Set default command for chassis
        chassis.setDefaultCommand(driveCommand);
    }

    /**
     * Setup the robot for different OpMode types.
     *
     * @param type The type of OpMode (TELEOP, AUTO, EMPTY).
     */
    private void setupOpMode(OpModeType type) {
        switch (type) {
            case TELEOP:
                RobotGlobal.isTeleOp = true;
                RobotGlobal.isAutonomous = false;
                setDriveControls();
                chassis.startTeleopDrive();
                break;

            case AUTO:
                RobotGlobal.isAutonomous = true;
                RobotGlobal.isTeleOp = false;
                RobotGlobal.startMatchTimer();
                break;

            case EMPTY:
                RobotGlobal.isAutonomous = false;
                RobotGlobal.isTeleOp = false;
                break;
        }
    }

    // Accessor methods for subsystems and controllers
    public Chassis getChassis() {
        return chassis;
    }

    public ArmSubsystem getArmSubsystem() {
        return armSubsystem;
    }

    public PivotSubsystem getPivotSubsystem() {
        return pivotSubsystem;
    }

    public ClawSubsystem getClawSubsystem(){return clawSubsystem;}

    public GamepadEx getDriveController() {
        return driveController;
    }

    public GamepadEx getManipController() {
        return manipController;
    }
}
