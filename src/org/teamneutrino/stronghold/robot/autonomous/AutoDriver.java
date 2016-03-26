package org.teamneutrino.stronghold.robot.autonomous;

import org.teamneutrino.stronghold.robot.Constants;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.exceptions.GyroUnpluggedException;
import org.teamneutrino.stronghold.robot.sensors.Camera;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

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
	private Shooter shooter;
	private Camera cam;

	private boolean shooterAimingThreadRunning;
	private boolean driveAimed;
	private boolean shooterAimed;

	private boolean leftEncoderUnplugged;
	private boolean rightEncoderUnplugged;

	private static final int TIMEOUT = 100000;

	// smaller number, more correction; bigger number, less correction
	private static final double CORRECTION_DISTANCE = .05;

	private static final double MIN_SPEED = .175;

	private static final double MIN_RAMP = .25;

	private static final double RAMP_UP_DISTANCE = 3;
	private static final double RAMP_DOWN_DISTANCE = 5;

	private static final double RAMP_UP_DEGREES = 90;
	private static final double RAMP_DOWN_DEGREES = 180;

	private static final int ENCODER_UNPLUGGED_TIMEOUT = 500;

	private static final double GYRO_UNPLUGGED_TIMEOUT = 1000;
	private static final double GYRO_UNPLUGGED_THRESHOLD = 10;

	private static final int TIMEOUT_REFRESH_RATE = 5;
	private static final int SHOOTER_AIMING_THREAD_REFRESH_RATE = 5;

	private static final double AIM_DRIVE_SPEED = .1;
	private static final double AIM_SHOOTER_ACTUATION_SPEED = .15;

	private static final double AIM_ON_TARGET_THRESHOLD = 2;

	private static final double AIM_PROPORTIONAL_OFFSET_THRESHOLD = 20;

	public AutoDriver(Drive drive, Shooter shooter, Camera cam)
	{
		this.drive = drive;
		this.shooter = shooter;
		this.cam = cam;
		encLeft = new Encoder(Constants.ENCODER_LEFT_A_CHANNEL, Constants.ENCODER_LEFT_B_CHANNEL);
		encRight = new Encoder(Constants.ENCODER_RIGHT_A_CHANNEL, Constants.ENCODER_RIGHT_B_CHANNEL);
		gyro = new AnalogGyro(Constants.GYRO_CHANNEL);

		encLeft.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		encRight.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);

		shooterAimingThreadRunning = false;
		driveAimed = false;
		shooterAimed = false;
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
	 * @throws EncoderUnpluggedException
	 *             when a an encoder is unplugged
	 */
	public void moveDistance(double distance, double speed) throws EncoderUnpluggedException
	{
		if (true)
		{
			throw new EncoderUnpluggedException("Purposely unpluged encoders");
		}
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

		double rightDistancePrev = 0;
		double leftDistancePrev = 0;

		long rightEncoderUnpluggedTime = 0;
		long leftEncoderUnpluggedTime = 0;

		while (!terminate)
		{
			count++;
			double rightDistance = Math.abs(encRight.getDistance());
			double leftDistance = Math.abs(encLeft.getDistance());
			double maxDistance = Math.max(leftDistance, rightDistance);
			double minDistance = Math.min(leftDistance, rightDistance);

			double rightCorrection;
			double leftCorrection;

			// unplugged detection
			if (Math.abs(leftDistancePrev - leftDistance) < .001)
			{
				if (leftEncoderUnpluggedTime == 0)
				{
					leftEncoderUnpluggedTime = System.currentTimeMillis();
				}
			} else
			{
				leftEncoderUnpluggedTime = 0;
			}

			if (Math.abs(rightDistancePrev - rightDistance) < .001)
			{
				if (rightEncoderUnpluggedTime == 0)
				{
					rightEncoderUnpluggedTime = System.currentTimeMillis();
				}
			} else
			{
				rightEncoderUnpluggedTime = 0;
			}

			if (leftEncoderUnpluggedTime != 0
					&& (System.currentTimeMillis() - leftEncoderUnpluggedTime) > ENCODER_UNPLUGGED_TIMEOUT)
			{
				DriverStation.reportError("left encoder unplugged", false);
				leftEncoderUnplugged = true;
			}

			if (rightEncoderUnpluggedTime != 0
					&& (System.currentTimeMillis() - rightEncoderUnpluggedTime) > ENCODER_UNPLUGGED_TIMEOUT)
			{
				DriverStation.reportError("right encoder unplugged", false);
				rightEncoderUnplugged = true;
			}

			rightDistancePrev = rightDistance;
			leftDistancePrev = leftDistance;

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
			} else if (leftEncoderUnplugged && rightEncoderUnplugged)
			{
				// encoder unplugged
				drive.setLeft(0);
				drive.setRight(0);

				DriverStation.reportError("both encoders unplugged", false);
				throw new EncoderUnpluggedException("both encoders unplugged");
			} else if (!(leftEncoderUnplugged || rightEncoderUnplugged))
			{
				if (diff > 0)
				{
					msg = "veer right";
					// veer right
					leftCorrection = 1;
					rightCorrection = Math.max(1 - (diff / CORRECTION_DISTANCE), 0);
				} else if (diff < 0)
				{
					msg = "veer left";
					// veer right
					leftCorrection = Math.max(1 - (-diff / CORRECTION_DISTANCE), 0);
					rightCorrection = 1;
				} else
				{
					msg = "going straight";
					// go straight
					leftCorrection = 1;
					rightCorrection = 1;
				}
			} else
			{
				msg = "going straight (encoder unplugged)";
				leftCorrection = 1;
				rightCorrection = 1;
			}

			double remainDistance = distance - maxDistance;

			double ramp = 1;

//			if ((minDistance < RAMP_UP_DISTANCE) && (remainDistance < RAMP_DOWN_DISTANCE))
//			{
//				// both ramp up and ramp down are in effect, pick the min
//				ramp = Math.min(minDistance / RAMP_UP_DISTANCE, remainDistance / RAMP_DOWN_DISTANCE);
//			} else if (minDistance < RAMP_UP_DISTANCE)
//			{
//				// ramp up
//				ramp = (minDistance / RAMP_UP_DISTANCE);
//			} else 
				if (remainDistance < RAMP_DOWN_DISTANCE)
			{
				// ramp down
				ramp = (remainDistance / RAMP_DOWN_DISTANCE);
			}

			// scale the ramp from between 0 and 1 to between MIN_RAMP and 1
			ramp = ramp * (1 - MIN_RAMP) + MIN_RAMP;

			double leftSpeed = speed * ramp * leftCorrection;
			double rightSpeed = speed * ramp * rightCorrection;

			// scale speed from between 0 and 1 to between MIN_SPEED and 1
			leftSpeed = negitiveMultiplier * (leftSpeed * (1 - MIN_SPEED) + MIN_SPEED);
			rightSpeed = negitiveMultiplier * (rightSpeed * (1 - MIN_SPEED) + MIN_SPEED);

			drive.setLeft(leftSpeed);
			drive.setRight(rightSpeed);

			// if (count % 10 == 0)
			// System.out.println(msg + " Right Distance: " + rightDistance +
			// "Right Speed: " + rightSpeed
			// + " Left Distance: " + leftDistance + " Left Speed: " + leftSpeed
			// + " Ramp: " + ramp);

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

		boolean timeout = false;
		long startTime = System.currentTimeMillis();

		drive.setLeft(speed);
		drive.setRight(speed);

		long currTime = startTime;

		while (System.currentTimeMillis() - startTime < time && !timeout)
		{
			try
			{
				Thread.sleep(TIMEOUT_REFRESH_RATE);
			} catch (InterruptedException e)
			{
			}

			// timeout
			if ((currTime - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
					|| DriverStation.getInstance().isDisabled())
			{
				timeout = true;
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
	 * @throws EncoderUnpluggedException
	 * @throws GyroUnpluggedException
	 */
	public void turnDegrees(double degrees, double speed) throws EncoderUnpluggedException, GyroUnpluggedException
	{
		gyro.reset();
		encLeft.reset();
		encRight.reset();

		// multiply speed to motors by -1 if going backwards
		int negitiveMultiplier = (degrees >= 0 ? 1 : -1);

		// make speed positive and less than 1
		speed = Math.min(Math.abs(speed), 1);

		// make distance positive, this class does all calculations using
		// positive numbers and makes the output negative if going backwards
		degrees = Math.abs(degrees);

		boolean terminate = false;
		double startTime = System.currentTimeMillis();

		// int count = 0;

		double rightDistancePrev = 0;
		double leftDistancePrev = 0;

		long rightEncoderUnpluggedTime = 0;
		long leftEncoderUnpluggedTime = 0;

		while (!terminate)
		{
			// count++;
			double rightDistance = Math.abs(encRight.getDistance());
			double leftDistance = Math.abs(encLeft.getDistance());
			double degreesTraveled = Math.abs(gyro.getAngle());

			double currTime = System.currentTimeMillis();

			double rightCorrection;
			double leftCorrection;

			// unplugged detection
			if (Math.abs(leftDistancePrev - leftDistance) < .001)
			{
				if (leftEncoderUnpluggedTime == 0)
				{
					leftEncoderUnpluggedTime = System.currentTimeMillis();
				}
			} else
			{
				leftEncoderUnpluggedTime = 0;
			}

			if (Math.abs(rightDistancePrev - rightDistance) < .001)
			{
				if (rightEncoderUnpluggedTime == 0)
				{
					rightEncoderUnpluggedTime = System.currentTimeMillis();
				}
			} else
			{
				rightEncoderUnpluggedTime = 0;
			}

			if (leftEncoderUnpluggedTime != 0
					&& (System.currentTimeMillis() - leftEncoderUnpluggedTime) > ENCODER_UNPLUGGED_TIMEOUT)
			{
				DriverStation.reportError("left encoder unplugged", false);
				leftEncoderUnplugged = true;
			}

			if (rightEncoderUnpluggedTime != 0
					&& (System.currentTimeMillis() - rightEncoderUnpluggedTime) > ENCODER_UNPLUGGED_TIMEOUT)
			{
				DriverStation.reportError("right encoder unplugged", false);
				rightEncoderUnplugged = true;
			}

			rightDistancePrev = rightDistance;
			leftDistancePrev = leftDistance;

			// String msg;

			double diff = rightDistance - leftDistance;

			if (degreesTraveled > degrees)
			{
				// done
				System.out.println("Left Distance Traveled: " + leftDistance);
				System.out.println("Right Distance Traveled: " + rightDistance);
				leftCorrection = 0;
				rightCorrection = 0;
				terminate = true;
				// msg = "done";
			} else if ((currTime - startTime) > GYRO_UNPLUGGED_TIMEOUT && degreesTraveled < GYRO_UNPLUGGED_THRESHOLD)
			{
				// gyro unplugged
				drive.setLeft(0);
				drive.setRight(0);

				DriverStation.reportError("Gyro is unplugged", false);
				throw new GyroUnpluggedException("Gyro is unplgged");
			} else if (leftEncoderUnplugged && rightEncoderUnplugged)
			{
				// encoder unplugged
				drive.setLeft(0);
				drive.setRight(0);

				DriverStation.reportError("both encoders unplugged", false);
				throw new EncoderUnpluggedException("both encoders unplugged");
			} else if (!(leftEncoderUnplugged || rightEncoderUnplugged))
			{
				if (diff > 0)
				{
					// msg = "veer right";
					// veer right
					leftCorrection = 1;
					rightCorrection = Math.max(1 - (diff / CORRECTION_DISTANCE), 0);
				} else if (diff < 0)
				{
					// msg = "veer left";
					// veer right
					leftCorrection = Math.max(1 - (-diff / CORRECTION_DISTANCE), 0);
					rightCorrection = 1;
				} else
				{
					// msg = "going straight";
					// go straight
					leftCorrection = 1;
					rightCorrection = 1;
				}
			} else
			{
				leftCorrection = 1;
				rightCorrection = 1;
			}

			double degreesRemain = degrees - degreesTraveled;

			double ramp = 1;

			// don't ramp up
			// if ((degreesTraveled < RAMP_UP_DEGREES) && (degreesRemain <
			// RAMP_DOWN_DEGREES))
			// {
			// // both ramp up and ramp down are in effect, pick the min
			// ramp = Math.min(degreesTraveled / RAMP_UP_DISTANCE, degreesRemain
			// / RAMP_DOWN_DISTANCE);
			// }
			// else if (degreesTraveled < RAMP_UP_DEGREES)
			// {
			// // ramp up
			// ramp = (degreesTraveled / RAMP_UP_DEGREES);
			// }
			// else
			if (degreesRemain < RAMP_DOWN_DEGREES)
			{
				// ramp down
				ramp = (degreesRemain / RAMP_DOWN_DEGREES);
			}

			// scale the ramp from between 0 and 1 to between MIN_RAMP and 1
			ramp = ramp * (1 - MIN_RAMP) + MIN_RAMP;

			double leftSpeed = negitiveMultiplier * speed * ramp * leftCorrection;
			double rightSpeed = -negitiveMultiplier * speed * ramp * rightCorrection;

			// scale speed from between 0 and 1 to between MIN_SPEED and 1
			leftSpeed = leftSpeed * (1 - MIN_SPEED) + MIN_SPEED;
			rightSpeed = rightSpeed * (1 - MIN_SPEED) + MIN_SPEED;

			drive.setLeft(leftSpeed);
			drive.setRight(rightSpeed);

			// if (count % 10 == 0)
			// System.out.println(msg + " Degrees Traveled: " + degreesTraveled
			// + " Right Distance: "
			// + rightDistance + " Right Speed: " + rightSpeed + " Left
			// Distance: " + leftDistance
			// + " Left Speed: " + leftSpeed + " Ramp: " + ramp);

			// timeout
			if ((currTime - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
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
	 * Uses the camera to rotate the robot and shooter so that it is pointed
	 * toward the goal.
	 */
	public void aim()
	{
		System.out.println("Starting aim");
		shooterAimed = false;
		driveAimed = false;

		if (!shooterAimingThreadRunning)
		{
			shooterAimingThreadRunning = true;
			new Thread(new ShooterAimingThread()).start();
			new Thread(new DriveAimingThread()).start();
		}
	}

	public void stopAim()
	{
		shooterAimingThreadRunning = false;
	}

	public boolean isAiming()
	{
		return shooterAimingThreadRunning;
	}

	public boolean isAimed()
	{
		return isAiming() && shooterAimed && driveAimed;
	}

	// TODO tell the shooter to go to a specific position instead of using time
	private class ShooterAimingThread implements Runnable
	{
		@Override
		public void run()
		{
			int prevFrame = 0;
			int currFrame = 0;
			while (shooterAimingThreadRunning)
			{
				try
				{
					Thread.sleep(SHOOTER_AIMING_THREAD_REFRESH_RATE);
				} catch (InterruptedException e)
				{
				}

				// check for new frame
				currFrame = cam.getCurrentFrame();
				if (currFrame != prevFrame && cam.targetInFrame())
				{
					shooterAimed = aimShooter();
				}
			}

			shooterAimingThreadRunning = false;
		}
	}

	// TODO use gyro
	private class DriveAimingThread implements Runnable
	{
		@Override
		public void run()
		{
			int prevFrame = 0;
			int currFrame = 0;
			while (shooterAimingThreadRunning)
			{
				try
				{
					Thread.sleep(SHOOTER_AIMING_THREAD_REFRESH_RATE);
				} catch (InterruptedException e)
				{
				}

				// check for new frame
				currFrame = cam.getCurrentFrame();
				if (currFrame != prevFrame && cam.targetInFrame())
				{
					prevFrame = currFrame;
					driveAimed = aimDrive();
				}
			}

			shooterAimingThreadRunning = false;
		}
	}

	private boolean aimDrive()
	{
		double targetX = cam.getTargetX();
		double error = targetX - (Constants.CAMERA_TARGET_X + Constants.CAMERA_TARGET_X_OFFSET);
		double speed = error / AIM_PROPORTIONAL_OFFSET_THRESHOLD;
		boolean onTarget = Math.abs(error) < AIM_ON_TARGET_THRESHOLD;

		// bound speed between -1 and 1
		speed = AIM_DRIVE_SPEED * Math.max(-1, Math.min(1, speed));

		if (!onTarget)
		{
			drive.setLeft(speed);
			drive.setRight(-speed);

			int time;
			if (error > 10)
			{
				time = (int) ((error - 10) * 2 + 100);
			} else
			{
				time = 100;
			}

			try
			{
				Thread.sleep(time);
			} catch (InterruptedException e)
			{
			}
		}

		drive.setLeft(0);
		drive.setRight(0);

		return onTarget;
	}

	private boolean aimShooter()
	{
		double targetY;
		double error;
		double speed;
		double distToShootRatio = Constants.DISTANCE_TO_SHOOTER_ANGLE_RATIO;
		
		if (!Constants.USE_TIME_FOR_SHOOTER_AIMING)
		{
			targetY = cam.getTargetY();
			System.out.println(targetY);
			error = targetY - (Constants.CAMERA_TARGET_Y + Constants.CAMERA_TARGET_Y_OFFSET);
			speed = error / AIM_PROPORTIONAL_OFFSET_THRESHOLD;
			boolean onTarget = Math.abs(error) < AIM_ON_TARGET_THRESHOLD;;

			if (!onTarget)
			{
				// bound speed between -1 and 1
				speed = AIM_SHOOTER_ACTUATION_SPEED * Math.max(-1, Math.min(1, speed));
				
				shooter.setActuatorOverride(speed);

				int time;
				if (error > 10)
				{
					time = (int) ((error - 10) * 2 + 100);
				} else
				{
					time = 100;
				}

				try
				{
					Thread.sleep(time);
				} catch (InterruptedException e)
				{

				}
			}

			shooter.setActuatorOverride(0);
			return onTarget;
		} else
		{
			
			double targetYPos = cam.getTargetY();
			targetY = Constants.CAMERA_TARGET_Y * distToShootRatio;
			double pixelsOff = targetYPos - targetY;
			boolean onTarget = (Math.abs(pixelsOff)< AIM_PROPORTIONAL_OFFSET_THRESHOLD) || cam.getOffsetDegrees() == shooter.getSetpoint();
			
			if (!onTarget)
			{
				shooter.setSetpoint(shooter.getPosition() + cam.getOffsetDegrees());
			}
			
			return onTarget;
		}

	}
}