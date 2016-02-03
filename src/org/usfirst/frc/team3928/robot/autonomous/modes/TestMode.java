package org.usfirst.frc.team3928.robot.autonomous.modes;

import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.autonomous.AutoMode;
import org.usfirst.frc.team3928.robot.subsystems.Shooter;

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
		
	}

}
