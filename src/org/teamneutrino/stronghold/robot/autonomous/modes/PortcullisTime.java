package org.teamneutrino.stronghold.robot.autonomous.modes;

import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class PortcullisTime implements AutoMode
{
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;

	public PortcullisTime(AutoDriver driver, Shooter shooter, Intake intake)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
	}

	@Override
	public String getName()
	{
		return "Portcullis (time)";
	}

	@Override
	public void run()
	{
		shooter.setTargetPosition(Shooter.Position.INTAKE);
		intake.setTargetPosition(Intake.Position.DOWN);
		driver.sleep(500);

		driver.moveTime(1500, 1);
	}
}
