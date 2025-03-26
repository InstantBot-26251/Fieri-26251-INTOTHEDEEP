package org.firstinspires.ftc.teamcode.subsystems.chassis.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.chassis.Chassis;

public class TeleopDriveCommand extends CommandBase {
    private final Chassis chassis;
    private final double forward;
    private final double strafe;
    private final double rotate;

    public TeleopDriveCommand(Chassis chassis, double forward, double strafe, double rotate) {
        this.chassis = chassis;
        this.forward = forward;
        this.strafe = strafe;
        this.rotate = rotate;
        addRequirements(chassis);
    }

    @Override
    public void execute() {
        chassis.setDrivePowers(forward, strafe, rotate);
    }

    @Override
    public void end(boolean interrupted) {
        chassis.setDrivePowers(0, 0, 0);
    }
}
