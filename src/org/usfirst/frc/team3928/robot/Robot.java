package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot
{

	Joystick joyLeft;
	Joystick joyRight;
	Joystick game;
	Drive driver;
	Auto auto;

	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		game = new Joystick(Constants.GAMEPAD);
		driver = new Drive();
		auto = new Auto();
	}

	public void robotInit()
	{

	}

	public void disabled()
	{

	}

	public void autonomous()
	{
		auto.runAutonMode();
	}

	public void operatorControl()
	{

		while (isOperatorControl() && isEnabled())
		{
			double leftSpeed = joyLeft.getY();
			double rightSpeed = joyRight.getY();
			driver.setLeftSpeed(leftSpeed, isOperatorControl());
			driver.setRightSpeed(-rightSpeed, isOperatorControl());

			Timer.delay(0.005); // wait for a motor update time
		}
	}

	/**
	 * Runs during test mode
	 */
	public void test()
	{

	}
}
