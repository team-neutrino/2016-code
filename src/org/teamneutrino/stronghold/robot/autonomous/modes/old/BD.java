package org.teamneutrino.stronghold.robot.autonomous.modes.old;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class BD implements AutoMode
{	
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;
	
	public BD(AutoDriver driver, Shooter shooter, Intake intake)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
	}

	@Override
	public String getName()
	{
		return "B and D Defenses";
	}

	@Override
	public void run()
	{
		shooter.setTargetPosition(Shooter.Position.INTAKE);
		intake.setTargetPosition(Intake.Position.UP);
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		
		try
		{
			driver.moveDistance(15, 1);
		}
		catch (EncoderUnpluggedException e)
		{
			driver.moveTime(4000, 1);
		}
	}

}
