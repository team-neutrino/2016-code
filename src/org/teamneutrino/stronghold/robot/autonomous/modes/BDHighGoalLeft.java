package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class BDHighGoalLeft implements AutoMode
{
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;
	private Drive drive;

	public BDHighGoalLeft(AutoDriver driver, Shooter shooter, Intake intake, Drive drive)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
		this.drive = drive;
	}

	@Override
	public String getName()
	{
		return "B and D Defenses High Goal Rotate Left";
	}

	@Override
	public void run()
	{
		shooter.setTargetPosition(Shooter.Position.INTAKE);
		intake.setTargetPosition(Intake.Position.UP);
		driver.wait(500);

		try
		{
			driver.moveDistance(13, 1);
		}
		catch (EncoderUnpluggedException e)
		{
			driver.moveTime(4000, 1);

			intake.setTargetPosition(Intake.Position.INTAKE);
			shooter.setTargetPosition(Shooter.Position.FRONT);

			driver.wait(1000);

			drive.setLeft(0);
			drive.setRight(0);

			driver.autonomousAim(4000, -.2);

			shooter.start();

			driver.wait(500);

			while (!shooter.isAtTargetSpeed() && driver.isAutoEnabled())
			{
				driver.wait(5);
			}

			shooter.setFlippers(true);

			driver.wait(500);

			shooter.setFlippers(false);
			shooter.stop();
		}
	}
}
