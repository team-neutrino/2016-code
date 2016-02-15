package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.exceptions.GyroUnpluggedException;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class TestMode implements AutoMode
{

	private AutoDriver driver;
	private Shooter shoot;

	public TestMode(AutoDriver driver, Shooter shoot)
	{
		this.driver = driver;
		this.shoot = shoot;
	}
	
	@Override
	public String getName()
	{
		return "Test Mode";
	}

	@Override
	public void run()
	{
		try
		{
			driver.moveDistance(2, 1);
			driver.turnDegrees(90, .5);
			driver.turnDegrees(-90, .5);
			driver.moveDistance(-2, 1);
		}
		catch (EncoderUnpluggedException | GyroUnpluggedException e)
		{
		}
	}

}
