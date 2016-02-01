package org.usfirst.frc.team3928.robot;

public class Auto
{
	AutoMovement autonMove;

	public Auto()
	{
		autonMove = new AutoMovement();
	}

	public void autonMode0()
	{
		// Do Nothing Autonomous
	}

	public void autonMode1()
	{
		// Move Forward Autonomous (and can be used for testing)
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
		if (mode == 1)
		{
			autonMode1();
		}
		else if (mode == 2)
		{
			autonMode2();
		}
		else
		{
			autonMode0();
		}
	}

}