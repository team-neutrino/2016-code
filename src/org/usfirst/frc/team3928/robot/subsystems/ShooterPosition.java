package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;

public class ShooterPosition 
{
	private SpeedController right1;
	private SpeedController left1;
	private AnalogPotentiometer potent;
	private Thread shooterPositionThread;
	private boolean resetPosition;
	
	ShooterPosition()
	{
		shooterPositionThread.start();
		potent = new AnalogPotentiometer(Constants.POTENTIOMETER_CHANNEL);
		if (Constants.REAL_BOT == true)
		{
			right1 = new TalonSRX(Constants.SHOOTER_POSITION_MOTOR_RIGHT);
			left1 = new TalonSRX(Constants.SHOOTER_POSITION_MOTOR_LEFT);
		}
		if (Constants.REAL_BOT == false)
		{
			right1 = new Victor(Constants.SHOOTER_POSITION_MOTOR_RIGHT);
			left1 = new Victor(Constants.SHOOTER_POSITION_MOTOR_RIGHT);
		}
	}
	
	public double getPosition()
	{
		return potent.get();
	}
	
	public void resetPosition()
	{
		resetPosition = true;
	}
	
	public void setPosition(double angle)
	{
		double startTime = System.currentTimeMillis();
		double pos = potent.get();
		while (System.currentTimeMillis() - startTime < 0.2) // ONLY FOR TESTING
		{
			pos = potent.get();
			if (pos < angle)
			{
				right1.set(0.01);
				left1.set(-0.01);
			}
			if (pos > angle)
			{
				right1.set(-0.01);
				left1.set(0.01);
			}
			if (angle == 0 && pos == 0)
			{
				right1.set(0);
				left1.set(0);
				break;
			}
			if (resetPosition == true)
			{
				setPosition(0);
				break;
			}
		}
	resetPosition = false;
	}
}
