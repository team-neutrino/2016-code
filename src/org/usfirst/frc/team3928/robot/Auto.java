package org.usfirst.frc.team3928.robot;

public class Auto
{
	AutoMovement autoMove;

	public Auto()
	{
		autoMove = new AutoMovement();
	}

	/**
	 * Do Nothing Autonomous
	 */
	public void autoMode0()
	{

	}

	/**
	 * Move Forward Autonomous (and can be used for testing)
	 */
	public void autoMode1()
	{
		autoMove.moveDistance(5);
	}

	public void autoMode2()
	{
		// TODO: Some kind of camera-driven autonomous
		// maybe make a new class called "VisionTracking" to tell whether we are
		// aimed at the
		// goal or not, so we can re-use code for auto and drive code
	}

	public void runAutoMode()
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