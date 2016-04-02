package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.sensors.Camera;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class LowBarHighGoal implements AutoMode
{	
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;
	private Drive drive;
	private Camera camera;
	
	public LowBarHighGoal(AutoDriver driver, Shooter shooter, Intake intake, Drive drive, Camera camera)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
		this.drive = drive;
		this.camera = camera;
	}

	@Override
	public String getName()
	{
		return "Low Bar High Goal";
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
			driver.moveDistance(18.5, .5);
		}
		catch (EncoderUnpluggedException e)
		{
			driver.moveTime(3500, .5);
		}
		
		shooter.setTargetPosition(Shooter.Position.FRONT);
		
		try
		{
			Thread.sleep(200);
		}
		catch (InterruptedException e)
		{
		}
		
		drive.setLeft(.3);
		drive.setRight(-.3);
		
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		
		while (!camera.targetInFrame())
		{
			drive.setLeft(.3);
			drive.setRight(-.3);
		}
		
		drive.setLeft(0);
		drive.setRight(0);
		
		driver.aim();
		
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
		}
		
		while (!driver.isAimed())
		{
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		driver.stopAim();
		
		shooter.start();
		
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		
		while (!shooter.isAtTargetSpeed())
		{
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
			}
		}
		
		shooter.setFlippers(true);
		
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		
		shooter.setFlippers(false);
		shooter.stop();
	}
}
