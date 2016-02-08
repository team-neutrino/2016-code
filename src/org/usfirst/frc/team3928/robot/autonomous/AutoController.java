package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.sensors.ThumbwheelSwitch;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoController
{
	private AutoMode[] modes;

	private ThumbwheelSwitch thumbwheelSwitch;

	private boolean running;
	private int runningMode;

	private static final int MAX_MODES = 16;
	private static final int DRIVER_STATION_REFRESH_RATE = 1000;

	public AutoController()
	{
		modes = new AutoMode[MAX_MODES];

		thumbwheelSwitch = new ThumbwheelSwitch();

		running = false;
		runningMode = 0;

		SmartDashboard.putBoolean("Auto Switch Override", false);
		SmartDashboard.putNumber("Auto Switch Override Number", 0);

		Thread smartDashboardThread = new Thread(new SmartDashboardThread());
		smartDashboardThread.start();
		
		Thread autoMonitorThread = new Thread(new AutoMonitorThread());
		autoMonitorThread.start();
	}

	/**
	 * Assign an autonomous mode to a particular number
	 * 
	 * @param num
	 *            number to assign mode to
	 * @param mode
	 *            mode to assign
	 */
	public void assignMode(int num, AutoMode mode)
	{
		if (num < 0 || num > MAX_MODES)
		{
			DriverStation.reportError("Can not assign [" + mode.getName() + "] to [" + num + "], out of bounds", false);
		}
		else
		{
			modes[num] = mode;
		}
	}

	/**
	 * Run autonomous
	 */
	public void run()
	{
		int modeNum = getModeNum();

		AutoMode mode;
		if (modeNum < 0 || modeNum > MAX_MODES)
		{
			DriverStation.reportError("Can not run mode [" + modeNum + "], out of bounds", false);
			mode = null;
		}
		else
		{
			mode = modes[modeNum];
		}

		if (mode != null)
		{
			System.out.println("Running auto [" + mode.getName() + "] assigned to [" + modeNum + "]");
			running = true;
			runningMode = modeNum;
			mode.run();
			running = false;
			System.out.println("Auto [" + mode.getName() + "] Done!");
		}
		else
		{
			DriverStation.reportError("No auto mode assigned to [" + modeNum + "]", false);
		}
	}

	private int getModeNum()
	{
		int modeNum;

		if (SmartDashboard.getBoolean("Auto Switch Override", false))
		{
			modeNum = (int) SmartDashboard.getNumber("Auto Switch Override Number", 0);
		}
		else
		{
			modeNum = thumbwheelSwitch.get();
		}

		return modeNum;
	}

	private class SmartDashboardThread implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(DRIVER_STATION_REFRESH_RATE);
				}
				catch (InterruptedException e)
				{
				}

				int modeNum = getModeNum();
				AutoMode mode;
				if (modeNum < 0 || modeNum > MAX_MODES)
				{
					mode = null;
				}
				else
				{
					mode = modes[modeNum];
				}

				SmartDashboard.putNumber("Autonomous Mode", modeNum);

				if (mode != null)
				{
					SmartDashboard.putString("Autonomous Mode Description", mode.getName());
				}
				else
				{
					SmartDashboard.putString("Autonomous Mode Description", "Auto [" + modeNum + "] not implemented");
				}
			}
		}
	}

	private class AutoMonitorThread implements Runnable
	{

		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(DRIVER_STATION_REFRESH_RATE);
				}
				catch (InterruptedException e)
				{
				}

				if (running && (DriverStation.getInstance().isDisabled())
						|| !DriverStation.getInstance().isAutonomous())
				{
					try
					{
						Thread.sleep(DRIVER_STATION_REFRESH_RATE);
					}
					catch (InterruptedException e)
					{
					}

					if (running)
					{
						DriverStation
								.reportError(
										"Auto Mode " + modes[runningMode].getName() + "] assigned to [" + runningMode
												+ "] did not terminate at the end of autonomous! (restart robot code)",
										false);
					}
				}
			}
		}
	}
}
