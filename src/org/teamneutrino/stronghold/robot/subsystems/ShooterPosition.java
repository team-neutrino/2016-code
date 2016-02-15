package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;

public class ShooterPosition 
{
	private SpeedController motor;
	private AnalogPotentiometer potent;
	private Thread shooterPositionThread;
	private boolean resetPosition;
	
	ShooterPosition()
	{
		shooterPositionThread.start();
		potent = new AnalogPotentiometer(Constants.POTENTIOMETER_CHANNEL);
		if (Constants.REAL_BOT)
		{
			motor = new TalonSRX(Constants.SHOOTER_POSITION_MOTOR);
		}
		else
		{
			motor = new Victor(Constants.SHOOTER_POSITION_MOTOR);
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
				motor.set(0.01);
			}
			if (pos > angle)
			{
				motor.set(-0.01);
			}
			if (angle == 0 && pos == 0)
			{
				motor.set(0);
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
