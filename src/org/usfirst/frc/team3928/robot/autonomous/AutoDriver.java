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

	private static final int TIMEOUT = 100000;

	// smaller number, more correction; bigger number, less correction
	private static final double CORRECTION = .25;

	private static final double MIN_SPEED_MULTIPLIER = .25;
	private static final double MIN_SPEED = .125;

	private static final double RAMP_UP_DISTANCE = .25;
	private static final double RAMP_DOWN_DISTANCE = 1;
	private static final double MIN_RAMP_SPEED = .25;

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

		boolean terminate = false;
		double startTime = System.currentTimeMillis();

		while (!terminate)
		{
			double rightDistance = encRight.getDistance();
			double leftDistance = encLeft.getDistance();
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

			drive.setLeftSpeed(leftSpeed);
			drive.setRightSpeed(rightSpeed);

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

		drive.setLeftSpeed(0);
		drive.setRightSpeed(0);
	}

	public void turnDegrees(double degrees, double speed)
	{
		/*
		 * COUNTERCLOCKWISE = NEGATIVE CLOCKWISE = POSITIVE
		 */

		if (Constants.AUTO_USE_GYRO)
		{
			
			boolean terminate = false;
			double startTime = System.currentTimeMillis();
			/*
			 * In order to negate the effects of gyro drift, the gyro will be
			 * reset every time the code is run.
			 */
			if (degrees > 0)
			{
				while (gyro.getAngle() < degrees && !terminate)
				{
					drive.setLeftSpeed(speed);
					drive.setRightSpeed(-speed);
					Timer.delay(0.005); // wait for a motor update time
					if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
							|| DriverStation.getInstance().isDisabled())
					{
						terminate = true;
					}
					
					Thread.yield();
				}
				System.out.println("Final Angle: " + gyro.getAngle());
				drive.setLeftSpeed(0);
				drive.setRightSpeed(0);
				Timer.delay(.5);
				System.out.println("Real Final Angle: " + gyro.getAngle());
			}
			else if (degrees < 0)
			{
				while (gyro.getAngle() > degrees && !terminate)
				{
					drive.setLeftSpeed(-speed);
					drive.setRightSpeed(speed);
					Timer.delay(0.005); // wait for a motor update time
					if ((System.currentTimeMillis() - startTime) > TIMEOUT || !DriverStation.getInstance().isAutonomous()
							|| DriverStation.getInstance().isDisabled())
					{
						terminate = true;
					}
					
					Thread.yield();
				}
				System.out.println("Final Angle: " + gyro.getAngle());
				drive.setLeftSpeed(0);
				drive.setRightSpeed(0);
				Timer.delay(.5);
				System.out.println("Real Final Angle: " + gyro.getAngle());
			}
		}
		else
		{
			double turnCirc = Math.PI * 1.145;

			double degreePercent = degrees / 360;
			double dist = degreePercent * turnCirc;
			if (degrees > 0)
			{
			}
			else if (degrees < 0)
			{
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
		this.turnDegrees(1, Constants.AUTO_MOVE_SPEED);
		temp1 = cam.getLargestArea();
		if (temp1 > temp)
		{
			while (temp1 > temp)
			{
				temp = cam.getLargestArea();
				this.turnDegrees(1, Constants.AUTO_MOVE_SPEED);
				temp1 = cam.getLargestArea();
				
				Thread.yield();
			}
		}
		else
		{
			while (temp1 > temp)
			{
				temp = cam.getLargestArea();
				this.turnDegrees(-1, Constants.AUTO_MOVE_SPEED);
				temp1 = cam.getLargestArea();
				
				Thread.yield();
			}
		}

	}
}
