package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoMovement
{
	Encoder encLeft;
	Encoder encRight;
	Gyro gyro;

	Drive drive;

	public AutoMovement()
	{
		encLeft = new Encoder(Constants.ENCODER_LEFT_A_CHANNEL, Constants.ENCODER_LEFT_B_CHANNEL);
		encRight = new Encoder(Constants.ENCODER_RIGHT_A_CHANNEL, Constants.ENCODER_RIGHT_B_CHANNEL);
		drive = new Drive();

		encLeft.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		encRight.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
	}

	public void moveDistance(double distance)
	{
		encLeft.reset();
		encRight.reset();
		double speed = Constants.AUTON_MOVE_SPEED;
		
		if (distance > 0)
		{
			while((distance > encLeft.getDistance()) || (distance > encRight.getDistance()))
			{
				if (distance > encLeft.getDistance())
				{
					drive.setLeftSpeed(speed);
				}
				else if (distance > encRight.getDistance())
				{
					drive.setRightSpeed(-speed);
				}
			}
			drive.setLeftSpeed(0);
			drive.setRightSpeed(0);
		}
		else if (distance < 0)
		{
			while((distance < encLeft.getDistance()) || (distance < encLeft.getDistance()))
			{
				if (distance < encLeft.getDistance())
				{
					drive.setLeftSpeed(-speed);
				}
				else if (distance < encRight.getDistance())
				{
					drive.setRightSpeed(speed);
				}
			}
			drive.setLeftSpeed(0);
			drive.setRightSpeed(0);
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