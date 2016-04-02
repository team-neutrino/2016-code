package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.sensors.Camera;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class BDHighGoal implements AutoMode
{
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;
	private Drive drive;
	private Camera camera;

	public BDHighGoal(AutoDriver driver, Shooter shooter, Intake intake, Drive drive, Camera camera)
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
		return "B and D Defenses High Goal";
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
			driver.moveDistance(13, 1);
		}
		catch (EncoderUnpluggedException e)
		{
			driver.moveTime(4000, 1);
		}

		intake.setTargetPosition(Intake.Position.INTAKE);
		shooter.setTargetPosition(Shooter.Position.FRONT);

		try
		{
			Thread.sleep(1000);
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

		long startTime = System.currentTimeMillis();
		boolean aiming = true;
		
		while (System.currentTimeMillis() - startTime < 3000)
		{
			aiming = aim(aiming);
		}

		while (!driver.isAimed())
		{
			aiming = aim(aiming);
		}

		driver.stopAim();
		
		drive.setLeft(0);
		drive.setRight(0);

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

	private boolean aim(boolean aiming)
	{
		if (camera.targetInFrame())
		{
			if (!aiming)
			{
				drive.setLeft(0);
				drive.setRight(0);
			}
			driver.aim();
			aiming = true;
		}
		else
		{
			driver.stopAim();
			drive.setLeft(.3);
			drive.setRight(-.3);
			aiming = false;
		}
		
		try
		{
			Thread.sleep(5);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return aiming;
	}
}
