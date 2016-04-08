package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class LowBarTime implements AutoMode
{
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;

	public LowBarTime(AutoDriver driver, Shooter shooter, Intake intake)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
	}

	@Override
	public String getName()
	{
		return "Low Bar (time)";
	}

	@Override
	public void run()
	{
		shooter.setTargetPosition(Shooter.Position.INTAKE);
		intake.setTargetPosition(Intake.Position.INTAKE);
		driver.sleep(500);

		driver.moveTime(3200, .5);
	}
}
