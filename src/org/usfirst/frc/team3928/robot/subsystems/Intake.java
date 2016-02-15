package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Intake
{
	SpeedController positionMotor;
	SpeedController intakeFrontBackMotor;
	SpeedController intakeSideSideMotor;

	public Intake()
	{
		if (Constants.REAL_BOT)
		{
			positionMotor = new CANTalon(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeFrontBackMotor = new CANTalon(Constants.INTAKE_FRONT_BACK_MOTOR_CHANNEL);
			intakeSideSideMotor = new CANTalon(Constants.INTAKE_SIDE_SIDE_MOTOR_CHANNEL);
		}
		else
		{
			positionMotor = new Victor(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeFrontBackMotor = new Victor(Constants.INTAKE_FRONT_BACK_MOTOR_CHANNEL);
			intakeSideSideMotor = new Victor(Constants.INTAKE_SIDE_SIDE_MOTOR_CHANNEL);
		}
	}

	public void setPosition(double position)
	{
		// TODO
	}

	public void set(double speed)
	{
		intakeFrontBackMotor.set(speed);
		intakeSideSideMotor.set(speed);
	}
}
