package org.teamneutrino.stronghold.robot.util;

import org.teamneutrino.stronghold.robot.Constants;
import org.teamneutrino.stronghold.robot.sensors.PressureSensor;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardOutputs implements Runnable
{
	CurrentMonitor currMon;
	Shooter shooter;
	Intake intake;
	PressureSensor pressure;
	
	public SmartDashboardOutputs(CurrentMonitor currMon, Shooter shooter, Intake intake)
	{
		this.currMon = currMon;
		this.shooter = shooter;
		this.intake = intake;
		this.pressure = new PressureSensor();
		
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
			SmartDashboard.putNumber("Shooter Offset", shooter.getOffset());
			SmartDashboard.putNumber("Intake Offset", intake.getOffset());
			SmartDashboard.putNumber("Pnumatic Pressure", pressure.getPressure());
			
			currMon.send();
		}
	}
}
