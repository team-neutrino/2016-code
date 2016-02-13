package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;

/**
 * Move Forward Autonomous (and can be used for testing)
 */
public class MoveForward implements AutoMode
{

	private AutoDriver driver;

	public MoveForward(AutoDriver driver)
	{
		this.driver = driver;
	}

	@Override
	public String getName()
	{
		return "Move Forward";
	}

	@Override
	public void run()
	{
		driver.moveTime(1000, 1);
	}

}
