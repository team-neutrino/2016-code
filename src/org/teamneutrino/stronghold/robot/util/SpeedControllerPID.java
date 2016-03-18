package org.teamneutrino.stronghold.robot.util;

import edu.wpi.first.wpilibj.SpeedController;

public class SpeedControllerPID implements SpeedController
{
	SpeedController controller;
	double oldDeadbandMin;
	double oldDeadbandMax;
	double newDeadbandMin;
	double newDeadbandMax;
	
	boolean pidEnable;
	
	public SpeedControllerPID(SpeedController controller, double oldDeadbandMin, double oldDeadbandMax, 
			double newDeadbandMin, double newDeadbandMax)
	{
		this.controller = controller;
		this.oldDeadbandMin = oldDeadbandMin;
		this.oldDeadbandMax = oldDeadbandMax;
		this.newDeadbandMin = newDeadbandMin;
		this.newDeadbandMax = newDeadbandMax;
		
		pidEnable = true;
	}
	
	@Override
	public void pidWrite(double output)
	{
		if (pidEnable)
		{
			set(scale(output));
		}
	}

	@Override
	public double get()
	{
		return controller.get();
	}

	@Override
	public void set(double speed, byte syncGroup)
	{
		controller.set(speed, syncGroup);
	}

	@Override
	public void set(double speed)
	{
		controller.set(speed);
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
	
	public void enablePID()
	{
		pidEnable = true;
	}
	
	public void disablePID()
	{
		pidEnable = false;
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
}
