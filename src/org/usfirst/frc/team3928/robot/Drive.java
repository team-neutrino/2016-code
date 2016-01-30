package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;

public class Drive {
	Joystick joyLeft;
	Joystick joyRight;
	SpeedController tal1;
	SpeedController tal2; 
	SpeedController tal3;
	SpeedController tal4;
	SpeedController tal5;
	SpeedController tal6;
	
	public Drive()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		
		if(Constants.RealBot)
		{
			tal1 = new TalonSRX(Constants.DRIVE_RIGHT_1_CHANNEL);
			tal2 = new TalonSRX(Constants.DRIVE_LEFT_1_CHANNEL);
			tal3 = new TalonSRX(Constants.DRIVE_RIGHT_2_CHANNEL);
			tal4 = new TalonSRX(Constants.DRIVE_LEFT_2_CHANNEL);
			tal5 = new TalonSRX(Constants.DRIVE_RIGHT_3_CHANNEL);
			tal6 = new TalonSRX(Constants.DRIVE_LEFT_3_CHANNEL);
		}
		else
		{
			tal1 = new Victor(Constants.DRIVE_RIGHT_1_CHANNEL);
			tal2 = new Victor(Constants.DRIVE_LEFT_1_CHANNEL);
			tal3 = new Victor(Constants.DRIVE_RIGHT_2_CHANNEL);
			tal4 = new Victor(Constants.DRIVE_LEFT_2_CHANNEL);
			tal5 = new Victor(Constants.DRIVE_RIGHT_3_CHANNEL);
			tal6 = new Victor(Constants.DRIVE_LEFT_3_CHANNEL);
		}
	}
	
	public void setRightSpeed(double speed)
	{
		if(joyLeft.getRawButton(1) || joyRight.getRawButton(1))
		{
			tal1.set(speed);
			tal3.set(speed);
			tal5.set(speed);
		}
		else
		{
			tal1.set(speed * .5);
			tal3.set(speed * .5);
			tal5.set(speed * .5);
		}
		
	}
	
	public void setLeftSpeed(double speed)
	{
		if(joyLeft.getRawButton(1) || joyRight.getRawButton(1))
		{
			tal2.set(speed);
			tal4.set(speed);
			tal6.set(speed);
		}
		else
		{
			tal2.set(speed * .5);
			tal4.set(speed * .5);
			tal6.set(speed * .5);
		}
		
		
	}

}
