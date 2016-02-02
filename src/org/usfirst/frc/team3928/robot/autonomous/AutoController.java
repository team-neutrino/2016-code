package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoController
{
	private AutoMode[] modes;

	private static final int MAX_MODES = 16;

	public AutoController()
	{
		modes = new AutoMode[MAX_MODES];
	}

	// TODO: Some kind of camera-driven autonomous
	// maybe make a new class called "VisionTracking" to tell whether we are
	// aimed at the
	// goal or not, so we can re-use code for auto and drive code

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
			DriverStation.reportError("Can not assign [" + mode.getName() + "] to [" + num + "], out of bounds",
					false);
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
		// TODO get mode from limit switch
		int modeNum = Constants.AUTO_MODE;

		AutoMode mode = modes[modeNum];

		if (mode != null)
		{
			System.out.println("Running auto [" + mode.getName() + "] assigned to [" + modeNum + "]");
			mode.run();
			System.out.println("Auto [" + mode.getName() + "] Done!");
		}
		else
		{
			DriverStation.reportError("No auto mode assigned to [" + modeNum + "]", false);
		}
	}
	
	// TODO add smartdashboard override
}
