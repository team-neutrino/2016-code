package org.usfirst.frc.team3928.robot.autonomous.modes;

import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.autonomous.AutoMode;
import org.usfirst.frc.team3928.robot.excetions.EncoderUnpluggedException;

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
		try
		{
			driver.moveDistance(-5, .25);
		}
		catch (EncoderUnpluggedException e)
		{
		}
	}

}
