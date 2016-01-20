package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Talon;

public class Drive {
	Talon tal1;
	Talon tal2; 
	Talon tal3;
	Talon tal4;
	 
	public Drive()
	{
		tal1 = new Talon(Constants.DRIVE_RIGHT_1_CHANNEL.getInt());
		tal2 = new Talon(Constants.DRIVE_LEFT_1_CHANNEL.getInt());
		tal3 = new Talon(Constants.DRIVE_RIGHT_2_CHANNEL.getInt());
		tal4 = new Talon(Constants.DRIVE_LEFT_2_CHANNEL.getInt());
	}
	
	public void setRightSpeed(double speed)
	{
		tal1.set(speed);
		tal3.set(speed);
	}
	
	public void setLeftSpeed(double speed)
	{
		tal2.set(speed);
		tal4.set(speed);
	}

}
