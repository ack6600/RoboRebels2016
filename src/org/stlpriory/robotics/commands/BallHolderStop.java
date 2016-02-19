package org.stlpriory.robotics.commands;

import org.stlpriory.robotics.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BallHolderStop extends Command {

    public BallHolderStop() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.ballHolder);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.ballHolder.set(0);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
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