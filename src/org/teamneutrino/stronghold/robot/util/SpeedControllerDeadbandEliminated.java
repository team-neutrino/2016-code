package org.teamneutrino.stronghold.robot.util;

import edu.wpi.first.wpilibj.SpeedController;

public class SpeedControllerDeadbandEliminated implements SpeedController
{
	SpeedController controller;
	double oldDeadbandMin;
	double oldDeadbandMax;
	double newDeadbandMin;
	double newDeadbandMax;
	
	public SpeedControllerDeadbandEliminated(SpeedController controller, double oldDeadbandMin, double oldDeadbandMax, 
			double newDeadbandMin, double newDeadbandMax)
	{
		this.controller = controller;
		this.oldDeadbandMin = oldDeadbandMin;
		this.oldDeadbandMax = oldDeadbandMax;
		this.newDeadbandMin = newDeadbandMin;
		this.newDeadbandMax = newDeadbandMax;
	}
	
	@Override
	public void pidWrite(double output)
	{
		set(output);
		System.out.println("Setting " + output);
	}

	@Override
	public double get()
	{
		return reverseScale(controller.get());
	}

	@Override
	public void set(double speed, byte syncGroup)
	{
		controller.set(scale(speed), syncGroup);
	}

	@Override
	public void set(double speed)
	{
		controller.set(scale(speed));

	}

	@Override
	public void setInverted(boolean isInverted)
	{
		controller.setInverted(isInverted);

	}

	@Override
	public boolean getInverted()
	{
		return controller.getInverted();
	}

	@Override
	public void disable()
	{
		controller.disable();
	}

	@Override
	public void stopMotor()
	{
		controller.stopMotor();
	}
	
	private double scale(double speed)
	{
		if (speed <= newDeadbandMin)
		{
			// speed is below the deadband
			return Util.scale(speed, -1, newDeadbandMin, -1, oldDeadbandMin);
		}
		else if (speed > newDeadbandMin && speed < newDeadbandMax)
		{
			// speed is within the deadband
			return Util.scale(speed, newDeadbandMin, newDeadbandMax, oldDeadbandMin, oldDeadbandMax);
		}
		else
		{
			// speed is above the deadband
			return Util.scale(speed, newDeadbandMax, 1, oldDeadbandMax, 1);
		}
	}
	
	private double reverseScale(double speed)
	{
		if (speed <= oldDeadbandMin)
		{
			// speed is below the deadband
			return Util.scale(speed, -1, oldDeadbandMin, -1, newDeadbandMin);
		}
		else if (speed > oldDeadbandMin && speed < oldDeadbandMax)
		{
			// speed is within the deadband
			return Util.scale(speed, oldDeadbandMin, oldDeadbandMax, newDeadbandMin, newDeadbandMax);
		}
		else
		{
			// speed is above the deadband
			return Util.scale(speed, oldDeadbandMax, 1, newDeadbandMax, 1);
		}
	}
}
