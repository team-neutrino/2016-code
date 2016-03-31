package org.teamneutrino.stronghold.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;

public class DriveEncoders implements Runnable
{
	Drive drive;
	Encoder encoderLeft;
	Encoder encoderRight;

	public DriveEncoders(Drive drive, Encoder left, Encoder right)
	{
		this.drive = drive;
		this.encoderLeft = left;
		this.encoderRight = right;
	}

	public void setTargetSpeed(double leftSpeed, double rightSpeed)
	{
		double encoderLeftSpeed = encoderLeft.getRate();
		double encoderRightSpeed = encoderRight.getRate();
		
		
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}
