package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.Encoder;

public class DriveEncoders
{
	private Drive drive;
	private Encoder encoderLeft;
	private Encoder encoderRight;

	private boolean useEncoders;

	private double errorIntegrated;
	private long lastUpdate;

	private int MAX_TIME_BETWEEN_UPDATES = 50;

	public DriveEncoders(Drive drive, Encoder left, Encoder right)
	{
		this.drive = drive;
		this.encoderLeft = left;
		this.encoderRight = right;

		useEncoders = false;
		errorIntegrated = 0;
	}

	public void setTargetSpeed(double leftSpeed, double rightSpeed)
	{
		if (useEncoders && Math.abs(leftSpeed) > .1 && Math.abs(rightSpeed) > .1)
		{
			long currTime = System.currentTimeMillis();

			double leftRate = Math.abs(encoderLeft.getRate());
			double rightRate = Math.abs(encoderRight.getRate());

			double leftRateScaled = leftRate / Math.abs(leftSpeed);
			double rightRateScaled = rightRate / Math.abs(rightSpeed);

			double error = rightRateScaled - leftRateScaled;

			int time = Math.min(MAX_TIME_BETWEEN_UPDATES, (int) (currTime - lastUpdate));

			errorIntegrated += error * (double) time;

			if (error >= 0)
			{
				drive.setLeft(leftSpeed);
				drive.setRight((rightSpeed >= 0 ? 1 : -1)
						* (Math.abs(rightSpeed) - errorIntegrated * Constants.DRIVE_ENCODER_P));
			}
			else
			{
				drive.setLeft((leftSpeed >= 0 ? 1 : -1)
						* (Math.abs(leftSpeed) + errorIntegrated * Constants.DRIVE_ENCODER_P));
				drive.setRight(rightSpeed);
			}
		}
		else
		{
			drive.setLeft(leftSpeed);
			drive.setRight(rightSpeed);
		}
	}

	public void useEncoders(boolean useEncoders)
	{
		if (!this.useEncoders && useEncoders)
		{
			errorIntegrated = 0;
		}

		this.useEncoders = useEncoders;
	}
}
