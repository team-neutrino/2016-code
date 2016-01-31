package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutonMovement
{
	Encoder encLeft;
	Encoder encRight;
	Gyro gyro;

	Drive drive;

	public AutonMovement()
	{
		encLeft = new Encoder(Constants.ENCODER_LEFT_A_CHANNEL, Constants.ENCODER_LEFT_B_CHANNEL);
		encRight = new Encoder(Constants.ENCODER_RIGHT_A_CHANNEL, Constants.ENCODER_RIGHT_B_CHANNEL, true);
		drive = new Drive();

		encLeft.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		encRight.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
	}

	public void moveDistance(double distance)
	{
		encLeft.reset();
		encRight.reset();
		if (distance > 0)
		{
			if (distance > encLeft.get())
			{
				drive.setLeftSpeed(Constants.AUTON_MOVE_SPEED, false);
			}
			else
			{
				drive.setLeftSpeed(0, false);
			}
			if (distance > encRight.get())
			{
				drive.setRightSpeed(Constants.AUTON_MOVE_SPEED, false);
			}
			else
			{
				drive.setRightSpeed(0, false);
			}
		}
		else if (distance < 0)
		{
			if (distance < encLeft.get())
			{
				drive.setLeftSpeed(-Constants.AUTON_MOVE_SPEED, false);
			}
			else
			{
				drive.setLeftSpeed(0, false);
			}
			if (distance < encRight.get())
			{
				drive.setRightSpeed(-Constants.AUTON_MOVE_SPEED, false);
			}
			else
			{
				drive.setRightSpeed(0, false);
			}
		}

	}

	public void turnDegrees(double degrees, boolean useGyro)
	{
		if (useGyro)
		{
			
		}
		else
		{
			
		}
	}
}