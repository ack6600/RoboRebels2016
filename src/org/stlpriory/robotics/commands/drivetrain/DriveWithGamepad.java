package org.stlpriory.robotics.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import org.stlpriory.robotics.Robot;

/**
 *
 */
public class DriveWithGamepad extends Command {

    public DriveWithGamepad() {
        super("DriveWithGamepad");
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
//        Robot.drivetrain.controllerDrive(Robot.oi.getController());
        Robot.drivetrain.driveSingleStick(Robot.oi.getController());
    }


    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
