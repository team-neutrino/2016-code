package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Drive
{
	private SpeedController right1;
	private SpeedController left1;
	private SpeedController right2;
	private SpeedController left2;
	private SpeedController right3;
	private SpeedController left3;
	
	private boolean fastMode;
	private int leftSpeed;
	private double rightSpeed;

	public Drive()
	{
		if (Constants.REAL_BOT)
		{
			right1 = new CANTalon(Constants.DRIVE_RIGHT_1_CHANNEL);
			left1 = new CANTalon(Constants.DRIVE_LEFT_1_CHANNEL);
			right2 = new CANTalon(Constants.DRIVE_RIGHT_2_CHANNEL);
			left2 = new CANTalon(Constants.DRIVE_LEFT_2_CHANNEL);
			right3 = new CANTalon(Constants.DRIVE_RIGHT_3_CHANNEL);
			left3 = new CANTalon(Constants.DRIVE_LEFT_3_CHANNEL);
			
			setBreakMode(false, left1);
			setBreakMode(false, right1);
			setBreakMode(true, left2);
			setBreakMode(true, right2);
			setBreakMode(true, left3);
			setBreakMode(true, right3);
		}
		else
		{
			right1 = new Victor(Constants.DRIVE_RIGHT_1_CHANNEL);
			left1 = new Victor(Constants.DRIVE_LEFT_1_CHANNEL);
			right2 = new Victor(Constants.DRIVE_RIGHT_2_CHANNEL);
			left2 = new Victor(Constants.DRIVE_LEFT_2_CHANNEL);
			right3 = new Victor(Constants.DRIVE_RIGHT_3_CHANNEL);
			left3 = new Victor(Constants.DRIVE_LEFT_3_CHANNEL);
		}
		
		fastMode = false;
		
		right1.setInverted(true);
		right2.setInverted(true);
		right3.setInverted(true);
	}

	public void setRight(double speed)
	{
		rightSpeed = speed;
		if (fastMode)
		{
			right1.set(speed);
		}
		right2.set(speed);
		right3.set(speed);
	}

	public void setLeft(double speed)
	{
		leftSpeed = (int) speed;
		if (fastMode)
		{
			left1.set(speed);
		}
		left2.set(speed);
		left3.set(speed);
	}
	
	public void setFastMode(boolean enabled)
	{
		fastMode = enabled;
		setBreakMode(enabled, left1);
		setBreakMode(enabled, right1);
		
		if (enabled)
		{
			left1.set(leftSpeed);
			right1.set(rightSpeed);
		}
		else
		{
			left1.set(0);
			right1.set(0);
		}
	}
	
	private void setBreakMode(boolean enabled, SpeedController sp)
	{
		if (sp instanceof CANTalon)
		{
			((CANTalon) sp).enableBrakeMode(enabled);
		}
	}

}
