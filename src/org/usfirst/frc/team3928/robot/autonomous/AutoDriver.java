package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.Constants;
import org.usfirst.frc.team3928.robot.sensors.Camera;
import org.usfirst.frc.team3928.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoDriver
{
	// TODO change this to private after access is no longer needed in
	// Robot.java
	public Encoder encLeft;
	public Encoder encRight;
	private Gyro gyro;

	private Drive drive;
	private Camera cam;

	public AutoDriver(Drive drive, Camera cam)
	{
		encLeft = new Encoder(Constants.ENCODER_LEFT_A_CHANNEL, Constants.ENCODER_LEFT_B_CHANNEL);
		encRight = new Encoder(Constants.ENCODER_RIGHT_A_CHANNEL, Constants.ENCODER_RIGHT_B_CHANNEL);
		this.drive = drive;
		this.cam = cam;

		encLeft.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		encRight.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
	}

	public void moveDistance(double distance)
	{
		encLeft.reset();
		encRight.reset();
		double speed = Constants.AUTO_MOVE_SPEED;

		if (distance > 0)
		{
			while ((distance > encLeft.getDistance()) || (distance > encRight.getDistance()))
			{
				if (distance > encLeft.getDistance())
				{
					drive.setLeftSpeed(speed);
				} else if (distance > encRight.getDistance())
				{
					drive.setRightSpeed(-speed);
				}
				Timer.delay(0.005);
			}
			drive.setLeftSpeed(0);
			drive.setRightSpeed(0);
		} else if (distance < 0)
		{
			while ((distance < encLeft.getDistance()) || (distance < encLeft.getDistance()))
			{
				if (distance < encLeft.getDistance())
				{
					drive.setLeftSpeed(-speed);
				} else if (distance < encRight.getDistance())
				{
					drive.setRightSpeed(speed);
				}
				Timer.delay(0.005);
			}
			drive.setLeftSpeed(0);
			drive.setRightSpeed(0);
		}
	}

	public void turnDegrees(double degrees, boolean useGyro)
	{
		if (useGyro)
		{

		} else
		{

		}
	}

	public void turnToGoal()
	{

	}

	public void moveToGoal()
	{

	}
}
