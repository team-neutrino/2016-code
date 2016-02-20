package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class TurnTowardGoal implements AutoMode
{

	private AutoDriver driver;
	private Shooter shoot;

	public TurnTowardGoal(AutoDriver driver, Shooter shoot)
	{
		this.driver = driver;
		this.shoot = shoot;
	}

	@Override
	public String getName()
	{
		return "TurnTowardGoal";
	}

	@Override
	public void run()
	{
		driver.aim();
	}

}
