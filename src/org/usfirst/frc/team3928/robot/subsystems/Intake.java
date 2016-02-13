package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Intake
{
	SpeedController positionMotor;
	SpeedController intakeCenterMotor;
	SpeedController intakeSideMotor;

	public Intake()
	{
		if (Constants.REAL_BOT)
		{
			positionMotor = new CANTalon(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeCenterMotor = new CANTalon(Constants.INTAKE_CENTER_MOTOR_CHANNEL);
			intakeSideMotor = new CANTalon(Constants.INTAKE_SIDE_MOTOR_CHANNEL);
		}
		else
		{
			positionMotor = new Victor(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeCenterMotor = new Victor(Constants.INTAKE_CENTER_MOTOR_CHANNEL);
			intakeSideMotor = new Victor(Constants.INTAKE_SIDE_MOTOR_CHANNEL);
		}
	}

	public void setPosition(double position)
	{
		// TODO
	}

	public void set(double speed)
	{
		intakeCenterMotor.set(speed);
		intakeSideMotor.set(speed);
	}
}
