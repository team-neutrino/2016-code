package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Intake
{
	private SpeedController intakeFrontToBackMotor;
	private SpeedController intakeSideToSideMotor;
	private SpeedController actuatorMotor;
	private AnalogPotentiometer encoder;
	private DigitalInput limitSwitchUp;
	private DigitalInput limitSwitchDown;
	
	private PIDController actuationPID;

	public Intake()
	{
		if (Constants.REAL_BOT)
		{
			intakeFrontToBackMotor = new CANTalon(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new CANTalon(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
			actuatorMotor = new CANTalon(Constants.INTAKE_ACUATOR_MOTOR_CHANNEL);
		}
		else
		{
			intakeFrontToBackMotor = new Victor(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new Victor(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
			actuatorMotor = new Victor(Constants.INTAKE_ACUATOR_MOTOR_CHANNEL);
		}

		encoder = new AnalogPotentiometer(Constants.INTAKE_ENCODER_CHANNEL, Constants.INTAKE_ENCODER_SCALE,
				Constants.INTAKE_ENCODER_OFFSET);
		
		actuationPID = new PIDController(Constants.INTAKE_ACTUATION_K_P, Constants.INTAKE_ACTUATION_K_I,
				Constants.SHOOTER_ACTUATION_K_D, encoder, actuatorMotor);

		limitSwitchUp = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_UP_CHANNEL);
		limitSwitchDown = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_DOWN_CHANNEL);
	}

	public void setAngle(double angle)
	{
		actuationPID.setSetpoint(angle);
		actuationPID.enable();
		
		// TODO put limit switch in thread
	}

	public void set(double speed)
	{
		intakeFrontToBackMotor.set(speed);
		intakeSideToSideMotor.set(speed);
	}
}
