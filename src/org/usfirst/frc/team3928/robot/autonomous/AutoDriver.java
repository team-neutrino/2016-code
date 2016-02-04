package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.Constants;
import org.usfirst.frc.team3928.robot.sensors.Camera;
import org.usfirst.frc.team3928.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoDriver
{
	private Encoder encLeft;
	private Encoder encRight;
	private Gyro gyro;

	private Drive drive;
	private Camera cam;

	public AutoDriver(Drive drive, Camera cam, Encoder encLeft, Encoder encRight, Gyro gyro)
	{
		this.drive = drive;
		this.cam = cam;
		this.encLeft = encLeft;
		this.encRight = encRight;
		this.gyro = gyro;
		
		
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
					drive.setRightSpeed(speed);
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
					drive.setRightSpeed(-speed);
				}
				Timer.delay(0.005);
			}
			drive.setLeftSpeed(0);
			drive.setRightSpeed(0);
		}
	}

	public void turnDegrees(double degrees, boolean useGyro)
	{
		double speed = Constants.AUTO_MOVE_SPEED;
		
		if (useGyro)
		{
			gyro.reset();
			
			/*
			 * In order to negate the effects of gyro drift, the gyro will be reset every time
			 * the code is run.
			 * This code is also based off of a 360 degree circle, so left is between 180 and 360,
			 * while right is between 0 and 180.
			 */
			if (degrees < 180)
			{
				drive.setLeftSpeed(speed);
				drive.setRightSpeed(speed);
			}
			else if ((degrees > 180) && (degrees < 360))
			{
				drive.setLeftSpeed(speed);
				drive.setRightSpeed(speed);
			}
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
