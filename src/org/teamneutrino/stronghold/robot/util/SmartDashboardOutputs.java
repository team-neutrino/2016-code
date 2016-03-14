package org.teamneutrino.stronghold.robot.util;

import org.teamneutrino.stronghold.robot.Constants;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardOutputs implements Runnable
{
	CurrentMonitor currMon;
	Shooter shooter;
	Intake intake;
	
	public SmartDashboardOutputs(Shooter shooter, Intake intake)
	{
		currMon = new CurrentMonitor();
		this.shooter = shooter;
		this.intake = intake;

		new Thread(this).start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(Constants.DRIVER_STATION_REFRESH_RATE);
			}
			catch (InterruptedException e)
			{
			}
			
			SmartDashboard.putNumber("Shooter Position", shooter.getPosition());
			SmartDashboard.putNumber("Intake Position", intake.getPosition());
			
			currMon.send();
		}
	}
}
