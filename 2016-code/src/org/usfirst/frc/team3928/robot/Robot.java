
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
    
    long time;

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
    	
    	time = System.currentTimeMillis();
    	count1 = new Counter(break1);
    	count2 = new Counter(break2);
    	t1.set(-1);
    	t2.set(1);
    	Timer.delay(.005);
    	
        while (isOperatorControl() && isEnabled()) 
        {
        	
        	while(!(System.currentTimeMillis() - time < 60000))
        		if(count1.get() > count2.get())
            	{
            		 t1.set(-(count2.get()/count1.get()));
                     t2.set(1);
            	}
            	if(count2.get() > count1.get())
            	{
            		 t2.set((count1.get()/count2.get()));
                     t1.set(-1);
            	}
            Timer.delay(0.005);		// wait for a motor update time
        }
        
        counter1 = count1.get();
        counter2 = count2.get();
//        if(counter1 > counter2)
//    	{
//    		 t1.set(counter2/counter1);
//             t2.set(1);
//    	}
//    	if(counter2 > counter1)
//    	{
//    		 t2.set(counter1/counter2);
//             t1.set(1);
//    	}
        count1.reset();
        count2.reset();
        
     
    }

    /**
     * Runs during test mode
     */
    public void test() 
    {
    	
    }
}
