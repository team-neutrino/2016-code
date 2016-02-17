package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Intake implements Runnable
{
	private SpeedController positionMotor;
	private SpeedController intakeFrontToBackMotor;
	private SpeedController intakeSideToSideMotor;
	private AnalogPotentiometer anPo;
	private DigitalInput limUp;
	private DigitalInput limDown;
	private Thread intakeThread;

	private boolean on;
	private int positionNumber;

	public Intake()
	{
		if (Constants.REAL_BOT)
		{
			positionMotor = new CANTalon(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeFrontToBackMotor = new CANTalon(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new CANTalon(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
		}
		else
		{
			positionMotor = new Victor(Constants.INTAKE_POSITION_MOTOR_CHANNEL);
			intakeFrontToBackMotor = new Victor(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new Victor(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
		}

		anPo = new AnalogPotentiometer(Constants.INTAKE_POTENTIOMETER_CHANNEL, Constants.INTAKE_POTENTIOMETER_FULLRANGE,
				Constants.INTAKE_POTENTIOMETER_OFFSET);

		limUp = new DigitalInput(Constants.INTAKE_LIMIT_UP_CHANNEL);
		limDown = new DigitalInput(Constants.INTAKE_LIMIT_DOWN_CHANNEL);

		intakeThread = new Thread(this);
	}

	public void setPosition(int posNumber)
	{
		
	}

	public void setAngle()
	{
		
	}

	public void set(double speed)
	{
		intakeFrontToBackMotor.set(speed);
		intakeSideToSideMotor.set(speed);
	}
	
	public void run()
	{
		// TODO
	}
}
