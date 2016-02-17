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

	private boolean isInPosition;
	private boolean on;
	private int positionNumber;
	private double currDegrees;
	private double desiredDegrees;

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

	public void run()
	{

	}

	public void setPosition(int posNumber)
	{
		intakeThread.start();
		on = true;
		positionNumber = posNumber;
		currDegrees = anPo.get();
		isInPosition = false;
		while (on)
		{
			if (positionNumber == 0)
			{
				desiredDegrees = 0;
				goToPosition();
			}
			if (limUp.get() || limDown.get() || DriverStation.getInstance().isDisabled())
			{
				on = false;
			}
		}
	}

	public void goToPosition()
	{
		if (Math.abs(desiredDegrees - currDegrees) < 10)
		{
			isInPosition = true;
		}
	}

	public boolean isInPosition()
	{
		return isInPosition;
	}

	public void set(double speed)
	{
		intakeFrontToBackMotor.set(speed);
		intakeSideToSideMotor.set(speed);
	}
}
