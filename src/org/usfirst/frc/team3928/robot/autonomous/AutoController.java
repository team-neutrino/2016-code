package org.usfirst.frc.team3928.robot.autonomous;

import org.usfirst.frc.team3928.robot.Constants;

public class AutoController
{
	private AutoDriver driver;

	public AutoController(AutoDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * Do Nothing Autonomous
	 */
	private void autoMode0()
	{

	}

	/**
	 * Move Forward Autonomous (and can be used for testing)
	 */
	private void autoMode1()
	{
		driver.moveDistance(5);
	}

	private void autoMode2()
	{
		// TODO: Some kind of camera-driven autonomous
		// maybe make a new class called "VisionTracking" to tell whether we are
		// aimed at the
		// goal or not, so we can re-use code for auto and drive code
	}

	public void run()
	{
		int mode = Constants.AUTO_MODE;
		switch (mode)
		{
		case 0: autoMode0(); break;
		case 1: autoMode1(); break;
		default: throw new IllegalArgumentException("Invalid auto mode.");
		}

	}

}