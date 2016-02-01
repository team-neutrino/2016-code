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
		if (distance > 0)
		{
			while(distance > encLeft.getDistance())
			{
					drive.setLeftSpeed(Constants.AUTON_MOVE_SPEED);
			}
			drive.setLeftSpeed(0);
		}
		else if (distance < 0)
		{
			while(distance < encLeft.getDistance())
			{
					drive.setLeftSpeed(-Constants.AUTON_MOVE_SPEED);
			}
			drive.setLeftSpeed(0);
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