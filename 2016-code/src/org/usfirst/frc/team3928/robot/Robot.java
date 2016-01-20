
package org.usfirst.frc.team3928.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot 
{
    Talon t1;
    Talon t2;
    int sum1;
    int sum2;
    int total;
    Joystick joy1;
    Joystick joy2;
    
    DigitalInput break1;
    DigitalInput break2;
    

    public Robot() 
    {
        t1 = new Talon(0);
        t2 = new Talon(1);
        joy1 = new Joystick(0);
        joy2 = new Joystick(1);
        break1 = new DigitalInput(1);
        break2 = new DigitalInput(2);
    }
    
    public void robotInit() 
    {
        
        
    }

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the if-else structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomous() 
    {
    	
    	
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() 
    {
    	boolean beamBreak1Val = false;
    	boolean beamBreak2Val = false;
    	
    	boolean beamBreak1Prev = false;
    	boolean beamBreak2Prev = false;
    	
    	int ctr1 = 0;
    	int ctr2 = 0;
    	
    	long currTime = System.currentTimeMillis();
    	long startTime = System.currentTimeMillis();
    	
        while (isOperatorControl() && isEnabled()) 
        {
//            t1.set(joy1.getRawAxis(2));
//            t2.set(-(joy2.getRawAxis(2)));
        	beamBreak1Prev = beamBreak1Val;
//        	System.out.println(beamBreak1Prev);
        	beamBreak2Prev = beamBreak2Val;
        	beamBreak1Val = break1.get();
        	
        	beamBreak2Val = break2.get();
        	if ((beamBreak1Val == true) && (beamBreak1Prev == false))
        	{
        		ctr1++;
        		
        		
        	}
        	if ((beamBreak2Val == true) && (beamBreak2Prev == false))
        	{
        		ctr2++;
        	}
        	
            t1.set(.5);
            t2.set(-.5);
            
            Timer.delay(0.005);		// wait for a motor update time
            currTime = System.currentTimeMillis();
            long timePassed = currTime - startTime;
            if (timePassed >= 1000)
            {
            	long rPS1 = (ctr1 / (timePassed/1000));
            	long rPS2 = (ctr2 / (timePassed/1000));
            	
            	total++;
            	sum1 += rPS1;
            	sum2 += rPS2;
            	ctr1 = 0; 
            	ctr2 = 0;
            	startTime = currTime;
            }
            
            
        }
        System.out.println("Average for 1 " + sum1/total);
        System.out.println("Average for 2 " + sum2/total);
    }

    /**
     * Runs during test mode
     */
    public void test() 
    {
    	
    }
}
