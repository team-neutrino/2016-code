package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class DriveForwardIntakePosition implements AutoMode
{	
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;
	
	public DriveForwardIntakePosition(AutoDriver driver, Shooter shooter, Intake intake)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
	}

	@Override
	public String getName()
	{
		return "Drive Forward Intake Position";
	}

	@Override
	public void run()
	{
		shooter.setTargetPosition(Shooter.Position.INTAKE);
		intake.setTargetPosition(Intake.Position.INTAKE);
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		
		try
		{
			driver.moveDistance(15, .5);
		}
		catch (EncoderUnpluggedException e)
		{
			driver.moveTime(3500, .5);
		}
	}

}
