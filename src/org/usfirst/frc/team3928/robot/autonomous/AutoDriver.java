package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.Constants;
import org.usfirst.frc.team3928.robot.sensors.Camera;
import org.usfirst.frc.team3928.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;

public class AutoDriver
{
	private Encoder encLeft;
	private Encoder encRight;
	private AnalogGyro gyro;

	private Drive drive;
	private Camera cam;

	private static final int TIMEOUT = 100000;

	// smaller number, more correction; bigger number, less correction
	private static final double CORRECTION = .25;

	private static final double MIN_SPEED_MULTIPLIER = .25;
	private static final double MIN_SPEED = .125;

	private static final double RAMP_UP_DISTANCE = .25;
	private static final double RAMP_DOWN_DISTANCE = 1;
	private static final double MIN_RAMP_SPEED = .25;
	
	private static final long TIMEOUT_REFRESH_RATE = 250;

	public AutoDriver(Drive drive)
	{
		this.drive = drive;
		cam = new Camera();
		encLeft = new Encoder(Constants.ENCODER_LEFT_A_CHANNEL, Constants.ENCODER_LEFT_B_CHANNEL);
		encRight = new Encoder(Constants.ENCODER_RIGHT_A_CHANNEL, Constants.ENCODER_RIGHT_B_CHANNEL);
		gyro = new AnalogGyro(Constants.GYRO_CHANNEL);

		encLeft.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		encRight.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
	}

	public void moveDistance(double distance, double speed)
	{
		encLeft.reset();
		encRight.reset();

		int negitiveMultiplier = (speed >= 0 ? 1 : -1);
		speed = Math.abs(speed);

		boolean terminate = false;
		double startTime = System.currentTimeMillis();

		while (!terminate)
		{
			double rightDistance = Math.abs(encRight.getDistance());
			double leftDistance = Math.abs(encLeft.getDistance());
			double maxDistance = Math.max(leftDistance, rightDistance);
			double minDistance = Math.min(leftDistance, rightDistance);

			double rightSpeed;
			double leftSpeed;
			String msg;

			double diff = rightDistance - leftDistance;

			double rampSpeed = speed;
			double remainDistance = distance - maxDistance;
			if (remainDistance < RAMP_UP_DISTANCE)
			{
				// ramp down
				rampSpeed = speed * ((remainDistance / RAMP_UP_DISTANCE) * (1 - MIN_RAMP_SPEED) + MIN_RAMP_SPEED);
			}
			else if (minDistance < RAMP_DOWN_DISTANCE)
			{
				rampSpeed = speed * ((minDistance / RAMP_DOWN_DISTANCE) * (1 - MIN_RAMP_SPEED) + MIN_RAMP_SPEED);
			}

			if (rightDistance > distance || leftDistance > distance)
			{
				System.out.println("Left Distance Traveled: " + leftDistance);
				System.out.println("Right Distance Traveled: " + rightDistance);
				leftSpeed = 0;
				rightSpeed = 0;
				terminate = true;
				msg = "done";
			}
			else if (diff > 0)
			{
				msg = "veer right";
				// veer right
				leftSpeed = rampSpeed;
				rightSpeed = Math.max(rampSpeed - rampSpeed * Math.min((diff / CORRECTION), 1 - MIN_SPEED_MULTIPLIER),
						MIN_SPEED);
			}
			else if (diff < 0)
			{
				msg = "veer left";
				// veer right
				leftSpeed = Math.max(rampSpeed - rampSpeed * Math.min((-diff / CORRECTION), 1 - MIN_SPEED_MULTIPLIER),
						MIN_SPEED);
				rightSpeed = rampSpeed;
			}
			else
			{
				msg = "going straight";
				// go straight
				leftSpeed = rampSpeed;
				rightSpeed = rampSpeed;
			}

			drive.setLeft(negitiveMultiplier * leftSpeed);
			drive.setRight(negitiveMultiplier * rightSpeed);

			System.out.println(msg + " Right Distance: " + rightDistance + " Right Speed: " + rightSpeed
					+ " Left Distance: " + leftDistance + " Left Speed: " + leftSpeed);

			// timeout
			if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
					|| DriverStation.getInstance().isDisabled())
			{
				terminate = true;
				System.out.println("drive timeout");
			}

			Thread.yield();
		}

		drive.setLeft(0);
		drive.setRight(0);
	}

	public void moveTime(int time, double speed)
	{
		boolean terminate = false;
		long startTime = System.currentTimeMillis();

		drive.setLeft(speed);
		drive.setRight(speed);
		
		long currTime = startTime;
		long timeRemain = time;

		while (!terminate)
		{
			try
			{
				Thread.sleep(Math.min(timeRemain, TIMEOUT_REFRESH_RATE));
			}
			catch (InterruptedException e)
			{
			}
			
			currTime = System.currentTimeMillis();
			timeRemain = time - (currTime - startTime);
			
			// done
			if (timeRemain <= 0)
			{
				terminate = true;
			}
			
			// timeout
			if ((currTime - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
					|| DriverStation.getInstance().isDisabled())
			{
				terminate = true;
				System.out.println("drive timeout");
			}
		}
		
		drive.setLeft(0);
		drive.setRight(0);
	}

	public void turnDegrees(double degrees, double speed)
	{
		/*
		 * COUNTERCLOCKWISE = NEGATIVE CLOCKWISE = POSITIVE
		 */
		gyro.reset();
		boolean terminate = false;
		double startTime = System.currentTimeMillis();
		double degreesTurned = gyro.getAngle();
		double slowDegrees = degrees / 8;
		double slowSpeed;
		while (Math.abs(degreesTurned) < Math.abs(slowDegrees) && !terminate)
		{
			degreesTurned = gyro.getAngle();
			if (degrees < 0)
			{
				speed = -speed;
			}
			drive.setLeft(speed);
			drive.setRight(-speed);
			if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
					|| DriverStation.getInstance().isDisabled())
			{
				terminate = true;
			}

			Thread.yield();
		}
		if (degrees > 0)
		{
			while (degreesTurned < degrees && !terminate)
			{
				slowSpeed = speed * (degreesTurned / degrees);
				drive.setLeft(slowSpeed);
				drive.setRight(-slowSpeed);
				if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
						|| DriverStation.getInstance().isDisabled())
				{
					terminate = true;
				}

				Thread.yield();
			}
		}
		if (degrees < 0)
		{
			while (degreesTurned > degrees && !terminate)
			{
				slowSpeed = speed * (degreesTurned / degrees);
				drive.setLeft(slowSpeed);
				drive.setRight(-slowSpeed);
				if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
						|| DriverStation.getInstance().isDisabled())
				{
					terminate = true;
				}

				Thread.yield();
			}
		}
	}

	public void turnToGoal()
	{

	}

	public void rotateTowardGoal()
	{
		double prevArea;
		double postArea;
		double startTime = System.currentTimeMillis();
		boolean terminate = false;

		prevArea = cam.getLargestArea();
		this.turnDegrees(1, Constants.AUTO_MOVE_SPEED);
		postArea = cam.getLargestArea();
		if (postArea > prevArea)
		{
			while (postArea > prevArea && !terminate)
			{
				prevArea = cam.getLargestArea();
				this.turnDegrees(1, Constants.AUTO_MOVE_SPEED);
				postArea = cam.getLargestArea();

				if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
						|| DriverStation.getInstance().isDisabled())
				{
					terminate = true;
					System.out.println("drive timeout");
				}

				Thread.yield();
			}
		}
		else
		{
			while (postArea > prevArea && !terminate)
			{
				prevArea = cam.getLargestArea();
				this.turnDegrees(-1, Constants.AUTO_MOVE_SPEED);
				postArea = cam.getLargestArea();

				if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
						|| DriverStation.getInstance().isDisabled())
				{
					terminate = true;
					System.out.println("drive timeout");
				}

				Thread.yield();
			}
		}

	}
}
