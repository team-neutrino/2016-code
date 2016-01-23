
package org.usfirst.frc.team3928.robot;


import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
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
    
    double time;
    
    double RPM1;
    double RPM2;
    
    double maxRPM1;
    double maxRPM2;
    
    double tgtRPM;
    
    double tgtSpd1;
    double tgtSpd2;

    Counter count2;
    Counter count1;
    
    int counter1;
    int counter2;
    
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
    	
    	maxRPM1 = 0;
    	maxRPM2 = 0;
    	tgtRPM = 3000;
    	tgtSpd1 = .5;
    	tgtSpd2 = .5;
    	count1 = new Counter(break1);
    	count2 = new Counter(break2);
    	t1.set(-tgtSpd1);
    	t2.set(tgtSpd2);
    	Timer.delay(.5);
    	
        while (isOperatorControl() && isEnabled()) 
        {
        	
        	time = System.currentTimeMillis();
        	
        	counter1 = count1.get();
            counter2 = count2.get();
            
            RPM1 = counter1*100;
            RPM2 = counter2*100;
            
            if (RPM1 > maxRPM1)
            {
            	maxRPM1 = RPM1;
            }
            if (RPM2 > maxRPM2)
            {
            	maxRPM2 = RPM2;
            }
            
            System.out.println("RPM1: " + RPM1);
            System.out.println("RPM2: " + RPM2);
            count1.reset();
            count2.reset();
        	
        	while((System.currentTimeMillis() - time < 600) && isOperatorControl() && isEnabled())
        	{
        		if (tgtRPM != RPM1)
        		{
        			if(tgtRPM > RPM1)
        			{
        				tgtSpd1 = RPM1/tgtRPM;
        			}
        			else if (tgtRPM < RPM1)
        			{
        				tgtSpd1 = (2-(RPM1/tgtRPM));
        			}
        		}
        		if (tgtRPM != RPM2)
        		{
        			if(tgtRPM > RPM2)
        			{
        				tgtSpd2 = RPM2/tgtRPM;
        			}
        			else if (tgtRPM < RPM2)
        			{
        				tgtSpd2 = (2-(RPM2/tgtRPM));
        			}
        		}
        		if (RPM1 == 0)
        		{
        			tgtSpd1 = 1;
        		}
            	if (RPM2 == 0)
            	{
            		tgtSpd2 = 1;
            	}
            	t1.set(-tgtSpd1);
            	t2.set(tgtSpd2);
        	}
            
        	Timer.delay(0.005);		// wait for a motor update time
            
        }
        
        System.out.println("Max 1: " + maxRPM1);
        System.out.println("Max 2: " + maxRPM2);
    }

    /**
     * Runs during test mode
     */
    public void test() 
    {
    	
    }
}
