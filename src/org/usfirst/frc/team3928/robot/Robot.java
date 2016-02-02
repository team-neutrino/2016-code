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
	AutoMovement autoMove;
	
	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		game = new Joystick(Constants.GAMEPAD);
		driver = new Drive();
		autoMove = new AutoMovement();
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
		auto.runAutoMode();
	}

	public void operatorControl()
	{

		while (isOperatorControl() && isEnabled())
		{
			double leftSpeed = joyLeft.getY();
			double rightSpeed = joyRight.getY();
			driver.setLeftSpeed(leftSpeed * Math.abs(leftSpeed));
			driver.setRightSpeed(-rightSpeed * Math.abs(rightSpeed));
			Timer.delay(0.005); // wait for a motor update time
		}
	}

	/**
	 * Runs during test mode
	 */
	public void test()
	{
		while (isTest())
		{
			double leftDist = autoMove.encLeft.getDistance();
			double rightDist = autoMove.encRight.getDistance();
			
			double leftSpeed = joyLeft.getY();
			double rightSpeed = joyRight.getY();
			driver.setLeftSpeed(leftSpeed * Math.abs(leftSpeed));
			driver.setRightSpeed(-rightSpeed * Math.abs(rightSpeed));
			
			
			
			Timer.delay(0.005); // wait for a motor update time
			
		}
	}
}
