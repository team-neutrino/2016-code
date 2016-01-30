package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.SafePWM;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;

public class Drive {
	SpeedController tal1;
	SpeedController tal2; 
	SpeedController tal3;
	SpeedController tal4;
	SpeedController tal5;
	SpeedController tal6;
	
	public Drive()
	{
		if(Constants.RealBot.getBoolean())
		{
			tal1 = new TalonSRX(Constants.DRIVE_RIGHT_1_CHANNEL.getInt());
			tal2 = new TalonSRX(Constants.DRIVE_LEFT_1_CHANNEL.getInt());
			tal3 = new TalonSRX(Constants.DRIVE_RIGHT_2_CHANNEL.getInt());
			tal4 = new TalonSRX(Constants.DRIVE_LEFT_2_CHANNEL.getInt());
			tal5 = new TalonSRX(Constants.DRIVE_RIGHT_3_CHANNEL.getInt());
			tal6 = new TalonSRX(Constants.DRIVE_LEFT_3_CHANNEL.getInt());
		}
		else
		{
			tal1 = new Victor(Constants.DRIVE_RIGHT_1_CHANNEL.getInt());
			tal2 = new Victor(Constants.DRIVE_LEFT_1_CHANNEL.getInt());
			tal3 = new Victor(Constants.DRIVE_RIGHT_2_CHANNEL.getInt());
			tal4 = new Victor(Constants.DRIVE_LEFT_2_CHANNEL.getInt());
			tal5 = new Victor(Constants.DRIVE_RIGHT_3_CHANNEL.getInt());
			tal6 = new Victor(Constants.DRIVE_LEFT_3_CHANNEL.getInt());
		}
	}
	
	public void setRightSpeed(double speed)
	{
		tal1.set(speed);
		tal3.set(speed);
		tal5.set(speed);
	}
	
	public void setLeftSpeed(double speed)
	{
		tal2.set(speed);
		tal4.set(speed);
		tal6.set(speed);
	}

}
