package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Intake
{
	SpeedController positionMotor;
	SpeedController intakeFrontToBackMotor;
	SpeedController intakeSideToSideMotor;
	AnalogPotentiometer anPo;

	public Intake()
	{
		if (Constants.REAL_BOT)
		{
			positionMotor = new CANTalon(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeFrontToBackMotor = new CANTalon(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new CANTalon(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
		} else
		{
			positionMotor = new Victor(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeFrontToBackMotor = new Victor(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new Victor(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
		}
		anPo = new AnalogPotentiometer(Constants.INTAKE_POTENTIOMETER_CHANNEL, Constants.INTAKE_POTENTIOMETER_FULLRANGE,
				Constants.INTAKE_POTENTIOMETER_OFFSET);
	}

	public void setPosition(double position)
	{
		// TODO
	}

	public void set(double speed)
	{
		intakeFrontToBackMotor.set(speed);
		intakeSideToSideMotor.set(speed);
	}
}
