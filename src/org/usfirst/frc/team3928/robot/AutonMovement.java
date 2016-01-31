package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;


public class AutonMovement {
	Encoder encLeft;
	Encoder encRight;
	Gyro gyro;
	public AutonMovement()
	{
		encLeft = new Encoder(Constants.ENCODER_LEFT_A_CHANNEL, Constants.ENCODER_LEFT_B_CHANNEL);
		encRight = new Encoder(Constants.ENCODER_RIGHT_A_CHANNEL, Constants.ENCODER_RIGHT_B_CHANNEL, true);
		
		
		double moveSpd = Constants.AUTON_MOVE_SPEED;
		encLeft.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		encRight.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
	}
	public void moveDistance(double distance)
	{
		encLeft.reset();
		encRight.reset();
		
		
	}
	public void turnDegrees(double degrees)
	{
		
	}
}