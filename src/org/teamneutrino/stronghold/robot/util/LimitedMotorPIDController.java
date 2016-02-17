package org.teamneutrino.stronghold.robot.util;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * A PID Controller with support for limit switches
 */
public class LimitedMotorPIDController extends PIDController implements Runnable
{
	private DigitalInput lowerLimitSwitch;
	private DigitalInput upperLimitSwitch;
	private SpeedController outputMotor;

	private static final int REFRESH_RATE = 50;

	private double motorOverrideSpeed;

	public LimitedMotorPIDController(double Kp, double Ki, double Kd, PIDSource source, SpeedController output,
			DigitalInput lowerLimitSwitch, DigitalInput upperLimitSwitch)
	{
		super(Kp, Ki, Kd, source, output);

		this.lowerLimitSwitch = lowerLimitSwitch;
		this.upperLimitSwitch = upperLimitSwitch;
		this.outputMotor = output;

		motorOverrideSpeed = 0;

		new Thread(this).run();
	}

	public synchronized void setMotorSpeedOverride(double speed)
	{
		disable();

		// limit switch is blocking
		boolean lowerLimit = !lowerLimitSwitch.get() && speed < 0;
		boolean upperLimit = !upperLimitSwitch.get() && speed > 0;

		if (lowerLimit || upperLimit)
		{
			disable();
		}
		else
		{
			outputMotor.set(speed);
			motorOverrideSpeed = speed;
		}
	}

	public synchronized double getPosition()
	{
		return getSetpoint() - getError();
	}

	@Override
	public synchronized void enable()
	{
		double position = getPosition();
		double setpoint = getSetpoint();

		// limit switch is blocking
		boolean lowerLimit = !lowerLimitSwitch.get() && setpoint < position;
		boolean upperLimit = !upperLimitSwitch.get() && setpoint > position;

		if (lowerLimit || upperLimit)
		{
			disable();
		}
		else
		{
			super.enable();
		}
	}

	@Override
	public synchronized void disable()
	{
		motorOverrideSpeed = 0;
		super.disable();
	};

	// Thread to monitor limit switches while PID is running
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(REFRESH_RATE);
			}
			catch (InterruptedException e)
			{
			}

			if (isEnabled() || motorOverrideSpeed != 0)
			{
				double position = getPosition();
				double setpoint = getSetpoint();

				// limit switch is blocking
				boolean lowerLimit;
				boolean upperLimit;
				if (isEnabled())
				{
					lowerLimit = !lowerLimitSwitch.get() && setpoint < position;
					upperLimit = !upperLimitSwitch.get() && setpoint > position;
				}
				else
				{
					lowerLimit = !lowerLimitSwitch.get() && motorOverrideSpeed < 0;
					upperLimit = !upperLimitSwitch.get() && motorOverrideSpeed > 0;
				}

				if (lowerLimit || upperLimit)
				{
					disable();
				}
			}
		}
	}
}
