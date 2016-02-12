package org.usfirst.frc.team3928.robot;

import org.usfirst.frc.team3928.robot.autonomous.AutoController;
import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.autonomous.modes.DoNothing;
import org.usfirst.frc.team3928.robot.autonomous.modes.MoveForward;
import org.usfirst.frc.team3928.robot.autonomous.modes.TestMode;
import org.usfirst.frc.team3928.robot.autonomous.modes.TurnTowardGoal;
import org.usfirst.frc.team3928.robot.subsystems.Drive;
import org.usfirst.frc.team3928.robot.subsystems.Intake;
import org.usfirst.frc.team3928.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot
{
	private Joystick joyLeft;
	private Joystick joyRight;
	private Joystick gamepad;
	private Drive drive;
	private AutoController autoController;
	private AutoDriver driver;
	private Intake intake;
	private Shooter shooter;

	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		gamepad = new Joystick(Constants.GAMEPAD);
		drive = new Drive();
		intake = new Intake();
		driver = new AutoDriver(drive);

		// set up auto modes
		autoController = new AutoController();
		autoController.assignMode(0, new DoNothing());
		autoController.assignMode(1, new MoveForward(driver));
		autoController.assignMode(2, new TestMode(driver, shooter));
		autoController.assignMode(3, new TurnTowardGoal(driver, shooter));
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
//			if (gamepad.getRawButton(4))
//			{
//				shooter.on = true;
//			}
//			else
//			{
//				shooter.on = false;
//			}
			double leftSpeed = -joyLeft.getY();
			double rightSpeed = -joyRight.getY();
			drive.setLeft(leftSpeed * Math.abs(leftSpeed));
			drive.setRight(rightSpeed * Math.abs(rightSpeed));
			Thread.yield();
		}
	}

	@Override
	public void test()
	{
		
	}
}
