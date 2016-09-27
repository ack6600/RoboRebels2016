package org.stlpriory.robotics;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.stlpriory.robotics.commands.ZeroGyro;
import org.stlpriory.robotics.commands.ZeroPotHigh;
import org.stlpriory.robotics.commands.ZeroPotLow;
import org.stlpriory.robotics.commands.autonomous.AutonomousCommand;
import org.stlpriory.robotics.subsystems.BallHolderSubsystem;
import org.stlpriory.robotics.subsystems.DrivetrainSubsystem;
import org.stlpriory.robotics.subsystems.ShooterSubsystem;
import org.stlpriory.robotics.utils.AutonomousInfo;
import org.stlpriory.robotics.utils.Debug;
import org.stlpriory.robotics.utils.PropertiesUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.Properties;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding
 * to each mode, as described in the IterativeRobot documentation. If you change the name of this
 * class or the package after creating this project, you must also update the manifest file in the
 * resource directory.
 */
public class Robot extends IterativeRobot {
    public static final String POT_HIGH_VALUE = "pot-zero-value";
    public static final String POT_LOW_VALUE = "pot-low-value";

    // Initialize robot subsystems
    public static DrivetrainSubsystem drivetrain = new DrivetrainSubsystem();
    public static BallHolderSubsystem ballHolder = new BallHolderSubsystem();
    public static ShooterSubsystem shooter = new ShooterSubsystem();

    // Human operator interface
    public static final OI oi = new OI();

    // Robot configuration file and properties
    public static final File CONFIG_FILE = new File("/home/lvuser/config.txt");
    public static final Properties ROBOT_PROPS = new Properties();
    public static final File AUTONOMOUS_CONFIG_FILE = new File("/usr/autonomousProperties.txt");
    public static final Properties AUTONOMOUS_PROPS = new Properties();


    private Command autonomousCommand = new AutonomousCommand();
    public static AutonomousInfo autonomousInfo = new AutonomousInfo();
    private Timer timer = new Timer();

    // ==================================================================================
    //                            ROBOT INIT SECTION
    // ==================================================================================

    @Override
    public void robotInit() {
        Debug.println("[Robot.robotInit()] Initializing...");
        timer.start();
        // Load robot configuration file
        loadRobotConfigFile();


        oi.vibrate(false);

        timer.stop();
        SmartDashboard.putData("Zero Pot High", new ZeroPotHigh());
        SmartDashboard.putData("Zero pot low", new ZeroPotLow());
        SmartDashboard.putData("Zero gyro", new ZeroGyro());
        Debug.println("[RoboRebels.robotInit()] Done in " + timer.get() * 1e6 + " ms");
        Debug.println("------------------------------------------");
        Debug.println("           Robot ready!");
        Debug.println("------------------------------------------");
    }

    @Override
    public void disabledInit() {
        setProperties();
        drivetrain.zeroGyro();
    }

    // ==================================================================================
    //                          AUTONOMOUS MODE SECTION
//     ==================================================================================

    @Override
    public void autonomousInit() {
        loadRobotConfigFile();
        setProperties();
//        drivetrain.zeroGyro();
        if (autonomousCommand != null)
            autonomousCommand.start();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    // ==================================================================================
    //                            TELEOP MODE SECTION
    // ==================================================================================

    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        setProperties();
        drivetrain.zeroGyro();
        reset();
        // Record initial status values
        updateStatus();
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        // Record updated status values
        updateStatus();
    }

    @Override
    public void disabledPeriodic() {
        oi.vibrate(false);
    }

    // ==================================================================================
    //                      P U B L I C   M E T H O D S
    // ==================================================================================

    public void loadRobotConfigFile() {
        try {
            // Clear any robot properties before reloading
            ROBOT_PROPS.clear();

            if (Files.exists(CONFIG_FILE.toPath())) {
                // Read the existing properties file
                Properties props = PropertiesUtils.load(CONFIG_FILE);
                // Put all the loaded values into the robot properties
                ROBOT_PROPS.putAll(props);
                System.out.println("read file");
            } else {
                // Create a new empty properties file
                CONFIG_FILE.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            AUTONOMOUS_PROPS.clear();
            if (Files.exists(AUTONOMOUS_CONFIG_FILE.toPath())) {
                // Read the existing properties file
                Properties props = PropertiesUtils.load(AUTONOMOUS_CONFIG_FILE);
                // Put all the loaded values into the robot properties
                AUTONOMOUS_PROPS.putAll(props);
                System.out.println("read autonomous file");
            } else {
                // Create a new empty properties file
                AUTONOMOUS_CONFIG_FILE.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveRobotConfigFile() {
        try {
            if (CONFIG_FILE.exists()) {
                // Write the properties file overwriting any existing file
                PropertiesUtils.save(ROBOT_PROPS, CONFIG_FILE);
                System.out.println("writing props");
            } else {
                // Create a new empty properties file
                CONFIG_FILE.createNewFile();
                saveRobotConfigFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProperties() {
        double highValue = Double.parseDouble(ROBOT_PROPS.getProperty(POT_HIGH_VALUE, "-1"));
        double lowValue = Double.parseDouble(ROBOT_PROPS.getProperty(POT_LOW_VALUE, "-1"));
        ballHolder.setHighValue(highValue);
        ballHolder.setLowValue(lowValue);
        if (highValue == -1)
            System.out.println("Low value is -1!");
        if (lowValue == -1)
            System.out.println("High value is -1!");
    }

    /**
     * Call the updateStatus methods on all subsystems
     */
    public void updateStatus() {
        drivetrain.updateStatus();
        ballHolder.updateStatus();
        shooter.updateStatus();
    }

    public void reset() {
        drivetrain.stop();
        ballHolder.stop();
        shooter.stop();
    }

    @Override
    public void testInit() {
        LiveWindow.run();
    }
}
