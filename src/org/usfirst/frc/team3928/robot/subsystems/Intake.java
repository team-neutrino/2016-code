package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Intake
{
	SpeedController updwn;
	SpeedController intk0;
	SpeedController intk1;
	
	public Intake()
	{
		if(Constants.REAL_BOT)
		{
			updwn = new CANTalon(Constants.INTAKE_UP_DOWN);
			intk0 = new CANTalon(Constants.INTAKE0);
			intk1 = new CANTalon(Constants.INTAKE1);
		}
		else
		{
			updwn = new Victor(Constants.INTAKE_UP_DOWN);
			intk0 = new Victor(Constants.INTAKE0);
			intk1 = new Victor(Constants.INTAKE1);
		}
	}
	
	public void raiseIntake(double speed)
	{
		updwn.set(speed);
	}
	
	public void intake(double speed)
	{
		intk0.set(speed);
		intk1.set(speed);
	}
}
