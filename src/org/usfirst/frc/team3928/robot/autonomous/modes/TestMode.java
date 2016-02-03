package org.usfirst.frc.team3928.robot.autonomous.modes;

import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.autonomous.AutoMode;

public class TestMode implements AutoMode
{

	private AutoDriver driver;

	public TestMode(AutoDriver driver)
	{
		this.driver = driver;
	}
	
	@Override
	public String getName()
	{
		return "Test Mode";
	}

	@Override
	public void run()
	{
		

	}

}
