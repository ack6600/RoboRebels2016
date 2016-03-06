package org.stlpriory.robotics.commands.drivetrain;

import org.stlpriory.robotics.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Rotate extends Command {
    public static final double DEFAULT_ROTATION_SPEED = -.5;
    public enum RotationDirection {CLOCKWISE, COUNTERCLOCKWISE};

    double inAngle;
    double startAngle;
    double speed;
    RotationDirection direction;

    public Rotate(final double inAngle, final double speed, final RotationDirection direction) {
        super("DriveWithGamepad");
        // if direction is true, turns left
        // else turns right
        requires(Robot.drivetrain);
        this.speed = speed;
        if(direction == RotationDirection.COUNTERCLOCKWISE)
            speed *= -1;
        this.direction = direction;
        this.inAngle = inAngle;
    }

    public Rotate(final double inAngle, final double speed) {
        this(Math.abs(inAngle), speed, inAngle >= 0 ? RotationDirection.CLOCKWISE : RotationDirection.COUNTERCLOCKWISE);
    }

    public Rotate(final double inAngle) {
        this(Math.abs(inAngle), DEFAULT_ROTATION_SPEED, inAngle >= 0 ? RotationDirection.CLOCKWISE : RotationDirection.COUNTERCLOCKWISE);
    }

    public Rotate(final double inAngle, final RotationDirection direction) {
        this(inAngle, DEFAULT_ROTATION_SPEED, direction);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        startAngle = Robot.drivetrain.getAngle();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (direction == RotationDirection.CLOCKWISE) {
            Robot.drivetrain.tankDrive(this.speed, 0);
        } else {
            Robot.drivetrain.tankDrive(0, this.speed);
        }
        SmartDashboard.putNumber("Robot Speed", Robot.drivetrain.getSpeedInRPM());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if(inAngle == 0.0)
            return true;
        else
            return Math.abs(Robot.drivetrain.getAngle - startAngle) > Math.abs(inAngle);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.stop();

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.drivetrain.stop();

    }
}
