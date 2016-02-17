package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.Solenoid;

public class Stinger
{
	private Solenoid sting;
	
	private boolean isStingerOn;
	
	public Stinger()
	{
		sting = new Solenoid(Constants.STINGER_SOLENOID_CHANNEL);
		
		setStinger(false);
	}

	public boolean isStingerOn()
	{
		return isStingerOn;
	}

	public void setStinger(boolean on)
	{
		this.isStingerOn = on;
		sting.set(isStingerOn);
	}
}
