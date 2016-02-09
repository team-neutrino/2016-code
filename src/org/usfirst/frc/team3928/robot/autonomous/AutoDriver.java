package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.Constants;
import org.usfirst.frc.team3928.robot.exceptions.EncoderUnpluggedException;
import org.usfirst.frc.team3928.robot.sensors.Camera;
import org.usfirst.frc.team3928.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;

/**
 * This class autonomously drives and operates the robot using feed back from
 * sensors.
 */
public class AutoDriver
{
	private Encoder encLeft;
	private Encoder encRight;
	private AnalogGyro gyro;

	private Drive drive;
	private Camera cam;

	private static final int TIMEOUT = 100000;

	// smaller number, more correction; bigger number, less correction
	private static final double CORRECTION_DISTANCE = .25;

	private static final double MIN_SPEED = .175;
	
	private static final double MIN_RAMP = .25;

	private static final double RAMP_UP_DISTANCE = 1;
	private static final double RAMP_DOWN_DISTANCE = 2;
	
	private static final double ENCODER_UNPLUGGED_THRESHOLD = .5;

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

	/**
	 * Uses encoders to drive straight the given distance at the given speed.
	 * 
	 * @param distance
	 *            the distance to travel in feet. A positive number will go
	 *            forwards. A negative number will go backwards.
	 * 
	 * @param speed
	 *            speed to travel. This number should be between 0.0 and 1.0.
	 *            1.0 is full speed. The closer to 0.0, the slower the robot
	 *            will move.
	 * @throws EncoderUnpluggedException  when a an encoder is unplugged
	 */
	public void moveDistance(double distance, double speed) throws EncoderUnpluggedException
	{
		encLeft.reset();
		encRight.reset();

		// multiply speed to motors by -1 if going backwards
		int negitiveMultiplier = (distance >= 0 ? 1 : -1);

		// make speed positive and less than 1
		speed = Math.min(Math.abs(speed), 1);

		// make distance positive, this class does all calculations using
		// positive numbers and makes the output negative if going backwards
		distance = Math.abs(distance);

		boolean terminate = false;
		double startTime = System.currentTimeMillis();

		int count = 0;

		while (!terminate)
		{
			count++;
			double rightDistance = Math.abs(encRight.getDistance());
			double leftDistance = Math.abs(encLeft.getDistance());
			double maxDistance = Math.max(leftDistance, rightDistance);
			double minDistance = Math.min(leftDistance, rightDistance);

			double rightCorrection;
			double leftCorrection;
			String msg;

			double diff = rightDistance - leftDistance;

			if (rightDistance > distance || leftDistance > distance)
			{
				// done
				System.out.println("Left Distance Traveled: " + leftDistance);
				System.out.println("Right Distance Traveled: " + rightDistance);
				leftCorrection = 0;
				rightCorrection = 0;
				terminate = true;
				msg = "done";
			}
			else if (diff >= ENCODER_UNPLUGGED_THRESHOLD)
			{
				// encoder unplugged
				drive.setLeft(0);
				drive.setRight(0);
				
				if (diff >= 0)
				{
					DriverStation.reportError("Right is ahead of left (left encoder unplugged)", false);
					throw new EncoderUnpluggedException("Right is ahead of left (left encoder unplugged)");
				} else
				{
					DriverStation.reportError("Left is ahead of Right (left encoder unplugged)", false);
					throw new EncoderUnpluggedException("Left is ahead of Right (left encoder unplugged)");
				}
			}
			else if (diff > 0)
			{
				msg = "veer right";
				// veer right
				leftCorrection = 1;
				rightCorrection = Math.max(1 - (diff / CORRECTION_DISTANCE), 0);
			}
			else if (diff < 0)
			{
				msg = "veer left";
				// veer right
				leftCorrection = Math.max(1 - (-diff / CORRECTION_DISTANCE), 0);
				rightCorrection = 1;
			}
			else
			{
				msg = "going straight";
				// go straight
				leftCorrection = 1;
				rightCorrection = 1;
			}

			double remainDistance = distance - maxDistance;

			double ramp = 1;

			if (minDistance < RAMP_UP_DISTANCE)
			{
				// ramp up
				ramp = (minDistance / RAMP_UP_DISTANCE);
			}
			else if (remainDistance < RAMP_DOWN_DISTANCE)
			{
				// ramp down
				ramp = (remainDistance / RAMP_DOWN_DISTANCE);
			}
			
			// scale the ramp from between 0 and 1 to between MIN_RAMP and 1
			ramp = ramp * (1 - MIN_RAMP) + MIN_RAMP;

			double leftSpeed = speed * ramp * leftCorrection;
			double rightSpeed = speed * ramp * rightCorrection;

			// scale speed from between 0 and 1 to between MIN_SPEED and 1
			leftSpeed = leftSpeed * (1 - MIN_SPEED) + MIN_SPEED;
			rightSpeed = rightSpeed * (1 - MIN_SPEED) + MIN_SPEED;

			drive.setLeft(negitiveMultiplier * leftCorrection);
			drive.setRight(negitiveMultiplier * rightCorrection);

			if (count % 10 == 0)
				System.out.println(msg + "    Right Distance: " + rightDistance + "    Right Speed: " + rightSpeed
						+ "    Left Distance: " + leftDistance + "    Left Speed: " + leftSpeed + "    Ramp: " + ramp);

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

	/**
	 * Drives the robot in a straight line (without using encoders) for the
	 * given time.
	 * 
	 * @param time
	 *            The time to travel in milliseconds. The value should be
	 *            positive.
	 * @param speed
	 *            The speed to drive. Value should be between -1.0 and 1.0. -1.0
	 *            is full speed backward. 1.0 is full speed forward.
	 */
	public void moveTime(int time, double speed)
	{
		// if time is negative make it 0
		time = Math.max(0, time);

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

	/**
	 * Rotates the robot by the given amount using the gyro.
	 * 
	 * @param degrees
	 *            rotational distance to rotate the robot in degrees. A positive
	 *            number will got clockwise. A negative number will go counter
	 *            clockwise.
	 * @param speed
	 */
	public void turnDegrees(double degrees, double speed)
	{
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

	/**
	 * Uses the camera to rotate the robot so that it is pointed toward the
	 * goal.
	 */
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
