package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.Solenoid;

public class Stinger
{
	private Solenoid piston;
	
	private boolean isOn;
	
	public Stinger()
	{
		piston = new Solenoid(Constants.STINGER_CYLINDER_CHANNEL);
		
		setStinger(false);
	}

	public boolean isOn()
	{
		return isOn;
	}

	public void setStinger(boolean on)
	{
		this.isOn = on;
		piston.set(isOn);
	}
}
