package org.teamneutrino.stronghold.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;

public class DriveEncoders
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

	public void setRight(double leftSpeed, double rightSpeed)
	{
		double encoderLeftSpeed = encoderLeft.getRate();
		double encoderRightSpeed = encoderRight.getRate();
		
		
	}
}
