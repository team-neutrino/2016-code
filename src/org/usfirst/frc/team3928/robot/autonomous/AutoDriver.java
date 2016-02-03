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
	private Encoder encLeft;
	private Encoder encRight;
	private Gyro gyro;

	private Drive drive;
	private Camera cam;

	public AutoDriver(Drive drive, Camera cam, Encoder encLeft, Encoder encRight)
	{
		this.drive = drive;
		this.cam = cam;
		this.encLeft = encLeft;
		this.encRight = encRight;

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

	public void rotateTowardGoal()
	{
		double temp;
		double temp1;

		temp = cam.getLargestArea();
		this.turnDegrees(1, false);
		temp1 = cam.getLargestArea();
		if(temp1 > temp)
		{
			while(temp1 > temp)
			{
				temp = cam.getLargestArea();
				this.turnDegrees(1, false);
				temp1 = cam.getLargestArea();
			}
		}
		else
		{
			while(temp1 > temp)
			{
				temp = cam.getLargestArea();
				this.turnDegrees(-1, false);
				temp1 = cam.getLargestArea();
			}
		}
		
	}
}
