package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.exceptions.GyroUnpluggedException;

public class Testing implements AutoMode
{	
	private AutoDriver driver;
	
	public Testing(AutoDriver driver)
	{
		this.driver = driver;
	}

	@Override
	public String getName()
	{
		return "testing";
	}

	@Override
	public void run()
	{
		try
		{
			driver.turnDegrees(90, 1);
		}
		catch (EncoderUnpluggedException | GyroUnpluggedException e)
		{
		}

	}

}
