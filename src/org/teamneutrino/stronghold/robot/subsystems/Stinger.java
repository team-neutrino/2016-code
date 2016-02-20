package org.teamneutrino.stronghold.robot.subsystems;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.Solenoid;

public class Stinger
{
	private Solenoid cylinderExtend;
	private Solenoid cylinderRetract;
	
	public Stinger()
	{
		cylinderExtend = new Solenoid(Constants.STINGER_CYLINDER_EXTEND_CHANNEL);
		cylinderRetract = new Solenoid(Constants.STINGER_CYLINDER_RETRACT_CHANNEL);
		
		setStinger(false);
	}

	public void setStinger(boolean on)
	{
		cylinderExtend.set(on);
		cylinderRetract.set(!on);
	}
}
