package org.usfirst.frc.team3928.robot;

public class Auto
{
	AutoMovement autonMove;

	public Auto()
	{
		autonMove = new AutoMovement();
	}

	/**
	 * Do Nothing Autonomous
	 */
	public void autonMode0()
	{

	}

	/**
	 * Move Forward Autonomous (and can be used for testing)
	 */
	public void autonMode1()
	{
		autonMove.moveDistance(5);
	}

	public void autonMode2()
	{
		// TODO: Some kind of camera-driven autonomous
		// maybe make a new class called "VisionTracking" to tell whether we are
		// aimed at the
		// goal or not, so we can re-use code for auton and drive code
	}

	public void runAutonMode()
	{
		int mode = Constants.AUTON_MODE;
		switch (mode)
		{
		case 0:
			autonMode0();
			break;
		case 1:
			autonMode1();
			break;

		}

	}

}