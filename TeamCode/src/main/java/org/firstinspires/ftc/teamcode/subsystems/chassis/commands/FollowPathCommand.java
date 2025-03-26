package org.firstinspires.ftc.teamcode.subsystems.chassis.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.chassis.Chassis;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;

public class FollowPathCommand extends CommandBase {
    private final Chassis chassis;
    private final Path path;

    public FollowPathCommand(Chassis chassis, Path path) {
        this.chassis = chassis;
        this.path = path;
        addRequirements(chassis);
    }

    @Override
    public void initialize() {
        chassis.followPath(path);
    }

    @Override
    public boolean isFinished() {
        return !chassis.isBusy();
    }

    @Override
    public void end(boolean interrupted) {
        chassis.breakFollowing();
    }
}