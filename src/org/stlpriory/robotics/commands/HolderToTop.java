package org.stlpriory.robotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.stlpriory.robotics.Robot;

public class HolderToTop extends Command {

    public HolderToTop() {
        requires(Robot.ballHolder);
    }

    @Override
    protected void initialize() {
        // TODO Auto-generated method stub
//    	new BallHolderUp();
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }

}
