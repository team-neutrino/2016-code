package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;

public class Intake
{
	private SpeedController intakeFrontToBackMotor;
	private SpeedController intakeSideToSideMotor;
	private SpeedController actuatorMotor;
	private AnalogPotentiometer encoder;

	private PIDController actuationPID;

	public Intake()
	{
		if (Constants.REAL_BOT)
		{
			intakeFrontToBackMotor = new TalonSRX(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new TalonSRX(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
			actuatorMotor = new TalonSRX(Constants.INTAKE_ACUATOR_MOTOR_CHANNEL);
		}
		else
		{
			intakeFrontToBackMotor = new Victor(Constants.INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL);
			intakeSideToSideMotor = new Victor(Constants.INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL);
			actuatorMotor = new Victor(Constants.INTAKE_ACUATOR_MOTOR_CHANNEL);
		}
		intakeFrontToBackMotor.setInverted(true);
		intakeSideToSideMotor.setInverted(true);
		actuatorMotor.setInverted(true);

		encoder = new AnalogPotentiometer(Constants.INTAKE_ENCODER_CHANNEL, Constants.INTAKE_ENCODER_SCALE,
				Constants.INTAKE_ENCODER_OFFSET);

		actuationPID = new PIDController(Constants.INTAKE_ACTUATION_K_P, Constants.INTAKE_ACTUATION_K_I,
				Constants.SHOOTER_ACTUATION_K_D, encoder, actuatorMotor);
		actuationPID.setContinuous(true);
		
		// TODO remove
		actuationPID.setOutputRange(-.25, .25);
	}
	
	public double getPosition()
	{
		return encoder.get();
	}

	public void setSetpoint(double angle)
	{
		actuationPID.setSetpoint(angle);
		actuationPID.enable();
	}

	public void setActuatorOverride(double speed)
	{
		actuationPID.disable();
		actuatorMotor.set(speed);
	}

	public void set(double speed)
	{
		intakeFrontToBackMotor.set(speed);
		intakeSideToSideMotor.set(speed);
	}

	public void intakeAngleOverride(double speed)
	{
		actuationPID.disable();
		actuatorMotor.set(speed);
	}
}
