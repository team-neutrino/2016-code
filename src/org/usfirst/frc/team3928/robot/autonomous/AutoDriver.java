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

	public void moveLeftDistance(double distance)
	{
		encLeft.reset();
		double speed = Constants.AUTO_MOVE_SPEED;
		if (distance > 0)
		{
			while (distance > encLeft.getDistance())
			{
				drive.setLeftSpeed(speed);
				Timer.delay(0.005); // wait for a motor update time
			}
			drive.setLeftSpeed(0);
		} else if (distance < 0)
		{
			while (distance < encLeft.getDistance())
			{
				drive.setLeftSpeed(-speed);
				Timer.delay(0.005); // wait for a motor update time
			}
			drive.setLeftSpeed(0);
		}
	}

	public void moveRightDistance(double distance)
	{
		encRight.reset();
		double speed = Constants.AUTO_MOVE_SPEED;
		if (distance > 0)
		{
			while (distance > encRight.getDistance())
			{
				drive.setRightSpeed(speed);
				Timer.delay(0.005); // wait for a motor update time
			}
			drive.setRightSpeed(0);
		} else if (distance < 0)
		{
			while (distance < encRight.getDistance())
			{
				drive.setRightSpeed(-speed);
				Timer.delay(0.005); // wait for a motor update time
			}
			drive.setRightSpeed(0);
		}
	}

	public void moveDistance(double distance)
	{
		moveLeftDistance(distance);
		moveRightDistance(distance);
	}

	public void turnDegrees(double degrees)
	{
		/*
		 * COUNTERCLOCKWISE = NEGATIVE
		 * CLOCKWISE = POSITIVE
		 */
		double speed = Constants.AUTO_MOVE_SPEED;

		if (Constants.AUTO_USE_GYRO)
		{
			gyro.reset();

			/*
			 * In order to negate the effects of gyro drift, the gyro will be
			 * reset every time the code is run.
			 */
			if (degrees > 0)
			{
				while (gyro.getAngle() < degrees)
				{
					drive.setLeftSpeed(speed);
					drive.setRightSpeed(-speed);
					Timer.delay(0.005); // wait for a motor update time
				}
				drive.setLeftSpeed(0);
				drive.setRightSpeed(0);
			} else if (degrees < 0)
			{
				while (gyro.getAngle() > degrees)
				{
					drive.setLeftSpeed(-speed);
					drive.setRightSpeed(speed);
					Timer.delay(0.005); // wait for a motor update time
				}
				drive.setLeftSpeed(0);
				drive.setRightSpeed(0);
			}
		} else
		{
			double turnCirc = Math.PI * 1.145;
			
			double degreePercent = degrees/360;
			double dist = degreePercent*turnCirc;
			
			moveLeftDistance(dist);
			moveRightDistance(-dist);
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
		this.turnDegrees(1);
		temp1 = cam.getLargestArea();
		if (temp1 > temp)
		{
			while (temp1 > temp)
			{
				temp = cam.getLargestArea();
				this.turnDegrees(1);
				temp1 = cam.getLargestArea();
			}
		} else
		{
			while (temp1 > temp)
			{
				temp = cam.getLargestArea();
				this.turnDegrees(-1);
				temp1 = cam.getLargestArea();
			}
		}

	}
}
