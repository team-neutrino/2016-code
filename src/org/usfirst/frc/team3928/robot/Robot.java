package org.usfirst.frc.team3928.robot;

import org.usfirst.frc.team3928.robot.autonomous.AutoController;
import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot
{
	private Joystick joyLeft;
	private Joystick joyRight;
	private Joystick gamepad;
	private Drive drive;
	private AutoController auto;
	private AutoDriver driver;

	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		gamepad = new Joystick(Constants.GAMEPAD);
		drive = new Drive();
		driver = new AutoDriver(drive);
		auto = new AutoController(driver);
	}

	@Override
	public void robotInit()
	{

	}

	@Override
	public void disabled()
	{

	}

	@Override
	public void autonomous()
	{
		auto.run();
	}

	@Override
	public void operatorControl()
	{

		while (isOperatorControl() && isEnabled())
		{
			double leftSpeed = joyLeft.getY();
			double rightSpeed = joyRight.getY();
			drive.setLeftSpeed(leftSpeed * Math.abs(leftSpeed));
			drive.setRightSpeed(-rightSpeed * Math.abs(rightSpeed));
			Timer.delay(0.005); // wait for a motor update time
		}
	}

	@Override
	public void test()
	{
		while (isTest())
		{
			double leftDist = driver.encLeft.getDistance();
			double rightDist = driver.encRight.getDistance();

			double leftSpeed = joyLeft.getY();
			double rightSpeed = joyRight.getY();
			drive.setLeftSpeed(leftSpeed * Math.abs(leftSpeed));
			drive.setRightSpeed(-rightSpeed * Math.abs(rightSpeed));

			Timer.delay(0.005); // wait for a motor update time
		}
	}
}
