package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class LowBarHighGoal implements AutoMode
{
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;
	private Drive drive;

	public LowBarHighGoal(AutoDriver driver, Shooter shooter, Intake intake, Drive drive)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
		this.drive = drive;
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
		driver.sleep(500);

		try
		{
			driver.moveDistance(18.5, .5);
		}
		catch (EncoderUnpluggedException e)
		{
			driver.moveTime(3500, .5);
		}

		shooter.setTargetPosition(Shooter.Position.FRONT);

		driver.sleep(200);

		drive.setLeft(.3);
		drive.setRight(-.3);

		driver.sleep(300);

		drive.setLeft(0);
		drive.setRight(0);

		driver.autonomousAim(3000, 0);

		shooter.start();

		driver.sleep(1500);

		while (!shooter.isAtTargetSpeed() && driver.isAutoEnabled())
		{
			driver.sleep(5);
		}

		shooter.setFlippers(true);

		driver.sleep(500);

		shooter.setFlippers(false);
		shooter.stop();
	}
}
