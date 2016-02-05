package org.usfirst.frc.team3928.robot.autonomous.modes;

import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.autonomous.AutoMode;

import edu.wpi.first.wpilibj.Encoder;

/**
 * Move Forward Autonomous (and can be used for testing)
 */
public class MoveForward implements AutoMode
{

	private AutoDriver driver;
	private Encoder encLeft;
	private Encoder encRight;

	public MoveForward(AutoDriver driver, Encoder encLeft, Encoder encRight)
	{
		this.driver = driver;
		this.encLeft = encLeft;
		this.encRight = encRight;
	}
	
	@Override
	public String getName()
	{
		return "Move Forward";
	}

	@Override
	public void run()
	{
		driver.moveDistance(5);
		System.out.println("Left: " + encLeft.getDistance());
		System.out.println("Right: " + encRight.getDistance());
	}

}
