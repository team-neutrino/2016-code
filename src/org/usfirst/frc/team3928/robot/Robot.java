package org.usfirst.frc.team3928.robot;

import org.usfirst.frc.team3928.robot.autonomous.AutoController;
import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.autonomous.modes.DoNothing;
import org.usfirst.frc.team3928.robot.autonomous.modes.MoveForward;
import org.usfirst.frc.team3928.robot.sensors.Camera;
import org.usfirst.frc.team3928.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot
{
	private Joystick joyLeft;
	private Joystick joyRight;
	private Joystick gamepad;
	private Drive drive;
	private AutoController autoController;
	private AutoDriver driver;
	private Camera cam;

	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		gamepad = new Joystick(Constants.GAMEPAD);
		drive = new Drive();
		driver = new AutoDriver(drive, cam);
		cam = new Camera();
		
		// set up auto modes
		autoController = new AutoController();
		autoController.assignMode(0, new DoNothing());
		autoController.assignMode(1, new MoveForward(driver));
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
		autoController.run();
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
		double ctr = 0;
		boolean count = false;
		while (isTest())
		{
			ctr++;

			if (ctr == 200)
			{
				ctr = 0;
				count = true;
			}

			double leftSpeed = joyLeft.getY();
			double rightSpeed = joyRight.getY();
			drive.setLeftSpeed(leftSpeed * Math.abs(leftSpeed));
			drive.setRightSpeed(-rightSpeed * Math.abs(rightSpeed));

			DriverStation.reportError("Left Distance:" + driver.encLeft.getDistance(), count);
			DriverStation.reportError("Right Distance:" + driver.encRight.getDistance(), count);

			Timer.delay(0.005); // wait for a motor update time
		}
	}
}
