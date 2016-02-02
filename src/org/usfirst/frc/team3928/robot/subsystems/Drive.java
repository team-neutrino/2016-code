package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

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
	}

	public void setRightSpeed(double speed)
	{
		right1.set(speed);
		right2.set(speed);
		right3.set(speed);
	}

	public void setLeftSpeed(double speed)
	{
		left1.set(speed);
		left2.set(speed);
		left3.set(speed);
	}

}
