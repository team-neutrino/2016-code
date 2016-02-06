package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.Constants;
import org.usfirst.frc.team3928.robot.sensors.Camera;
import org.usfirst.frc.team3928.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class AutoDriver
{
	private Encoder encLeft;
	private Encoder encRight;
	private AnalogGyro gyro;

	private Drive drive;
	private Camera cam;

	public AutoDriver(Drive drive, Camera cam, Encoder encLeft, Encoder encRight, AnalogGyro gyro)
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
		double speedL = .1;
		double speedR = .1;
		double time = System.currentTimeMillis();
		boolean shouldBreak = false;
		double slowDistance = Constants.AUTON_SLOW_DISTANCE;
		if (distance > 0)
		{
			while ((distance > encLeft.getDistance() || distance > encRight.getDistance()) && !shouldBreak)
			{
				if (encRight.getDistance() > encLeft.getDistance())
				{
					speedR -= .01;
				}
				if (encRight.getDistance() < encLeft.getDistance())
				{
					speedR += .01;
				}
				if (distance - encLeft.getDistance() < slowDistance || distance - encRight.getDistance() < slowDistance)
				{
					if (distance - encLeft.getDistance() < slowDistance)
					{
						speedL = distance / encLeft.getDistance();
					}
					if (distance - encRight.getDistance() < slowDistance)
					{
						speedR = distance / encRight.getDistance();
					}
				}

				if (distance > encLeft.getDistance())
				{
					System.out.println(speedL);
					drive.setLeftSpeed(speedL);
				}
				if (distance > encRight.getDistance())
				{
					System.out.println(speedR);
					drive.setRightSpeed(speedR);
				}
				shouldBreak = System.currentTimeMillis() - time > 5000 || !DriverStation.getInstance().isAutonomous()
						|| DriverStation.getInstance().isDisabled();
			}
		} else
		{
			while (((distance < encLeft.getDistance() || distance < encRight.getDistance())) && !shouldBreak)
			{
				if (encRight.getDistance() / (time - System.currentTimeMillis()) > encLeft.getDistance()
						/ (time - System.currentTimeMillis()))
				{
					speedL += .01;
				}
				if (encRight.getDistance() / (time - System.currentTimeMillis()) < encLeft.getDistance()
						/ (time - System.currentTimeMillis()))
				{
					speedR += .01;
				}
				if (Math.abs(distance - encLeft.getDistance()) < slowDistance)
				{
					speedL = -distance / encLeft.getDistance();
				}
				if (Math.abs(distance - encRight.getDistance()) < slowDistance)
				{
					speedR = -distance / encRight.getDistance();
				}
				if (distance < encLeft.getDistance())
				{
					drive.setLeftSpeed(-speedL);
				}
				if (distance < encRight.getDistance())
				{
					drive.setRightSpeed(-speedR);
				}
				shouldBreak = System.currentTimeMillis() - time > 5000 || !DriverStation.getInstance().isAutonomous()
						|| DriverStation.getInstance().isDisabled();
			}

		}
		drive.setRightSpeed(0);
		drive.setLeftSpeed(0);

	}

	/*
	 * moveSeparateDistance doesn't work!
	 */
	public void moveSeparateDistance(double distanceL, double distanceR)
	{
		encLeft.reset();
		encRight.reset();

		double speedL = Constants.AUTO_MOVE_SPEED;
		double speedR = Constants.AUTO_MOVE_SPEED;
		double time = System.currentTimeMillis();
		boolean shouldBreak = false;
		double slowDistance = Constants.AUTON_SLOW_DISTANCE;
		boolean isLeftMoved = false;
		boolean isRightMoved = false;
		double speedLeft = 0;
		double speedRight = 0;
		
		while ((!isLeftMoved || !isRightMoved) && !shouldBreak)
		{
			if ((encRight.getDistance() / (time - System.currentTimeMillis()) > encLeft.getDistance()
					/ (time - System.currentTimeMillis())) && (!isLeftMoved && !isRightMoved))
			{
				speedL -= .01;
			}
			if ((encRight.getDistance() / (time - System.currentTimeMillis()) < encLeft.getDistance()
					/ (time - System.currentTimeMillis()) && (!isLeftMoved && !isRightMoved)))
			{
				speedR -= .01;
			}
			/*
			 * Finds whether or not the robot should be slowing down
			 */
			if (Math.abs(distanceL - encLeft.getDistance()) < slowDistance)
			{
				speedL = slowDistance / encLeft.getDistance();
			}
			if (Math.abs(distanceR - encRight.getDistance()) < slowDistance)
			{
				speedR = slowDistance / encRight.getDistance();
			}

			if (distanceL > 0)
			{
				if (distanceL > encLeft.getDistance())
				{
					speedLeft = speedL;
				} else
				{
					isLeftMoved = true;
					drive.setLeftSpeed(0);
				}

			}
			if (distanceL < 0)
			{
				if (distanceL < encLeft.getDistance())
				{
					speedLeft = -speedL;
				} else
				{
					isLeftMoved = true;
					drive.setLeftSpeed(0);
				}

			}
			if (distanceR > 0)
			{
				if (distanceR > encRight.getDistance())
				{
					speedRight = speedR;
				} else
				{
					isRightMoved = true;
					drive.setRightSpeed(0);
				}

			}
			if (distanceR < 0)
			{
				if (distanceR < encRight.getDistance())
				{
					speedRight = -speedR;
				} else
				{
					isRightMoved = true;
					drive.setRightSpeed(0);
				}

			}
			drive.setLeftSpeed(speedLeft);
			drive.setRightSpeed(speedRight);
			shouldBreak = (System.currentTimeMillis() - time > 5000) || !DriverStation.getInstance().isAutonomous()
					|| DriverStation.getInstance().isDisabled();
		}
		drive.setLeftSpeed(0);
		drive.setRightSpeed(0);
	}

	public void turnDegrees(double degrees)
	{
		/*
		 * COUNTERCLOCKWISE = NEGATIVE CLOCKWISE = POSITIVE
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

			double degreePercent = degrees / 360;
			double dist = degreePercent * turnCirc;
			if (degrees > 0)
			{
				moveSeparateDistance(dist, -dist);
			} else if (degrees < 0)
			{
				moveSeparateDistance(-dist, dist);
			}

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
