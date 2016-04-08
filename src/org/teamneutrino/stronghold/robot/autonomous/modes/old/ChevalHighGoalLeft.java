package org.teamneutrino.stronghold.robot.autonomous.modes.old;


import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.AutoMode;
import org.teamneutrino.stronghold.robot.exceptions.EncoderUnpluggedException;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

public class ChevalHighGoalLeft implements AutoMode

{	
	private AutoDriver driver;
	private Shooter shooter;
	private Intake intake;
	private Drive drive;
	
	public ChevalHighGoalLeft(AutoDriver driver, Shooter shooter, Intake intake, Drive drive)
	{
		this.driver = driver;
		this.shooter = shooter;
		this.intake = intake;
		this.drive = drive;
	}

	@Override
	public String getName()
	{
		return "Cheval de Frise with High Goal Rotate Left";
	}

	@Override
	public void run()
	{
		try
		{
			intake.setTargetPosition(Intake.Position.UP);
			driver.moveDistance(7, .75);
			intake.setTargetPosition(Intake.Position.DOWN);
			driver.moveDistance(6, 1);
			shooter.setTargetPosition(Shooter.Position.FRONT);
			drive.setLeft(0);
			drive.setRight(0);
			driver.autonomousAim(4000, -.2);

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
		catch (EncoderUnpluggedException e1)
		{
			drive.setLeft(0);
			drive.setRight(0);
		}
		
	}

}

