package org.teamneutrino.stronghold.robot;

import org.teamneutrino.stronghold.robot.autonomous.AutoController;
import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.modes.DoNothing;
import org.teamneutrino.stronghold.robot.autonomous.modes.MoveForward;
import org.teamneutrino.stronghold.robot.autonomous.modes.TestMode;
import org.teamneutrino.stronghold.robot.autonomous.modes.TurnTowardGoal;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;
import org.teamneutrino.stronghold.robot.util.CurrentMonitor;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;

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
	private Solenoid shooterSensorPower1;
	private Solenoid shooterSensorPower2;
	private Solenoid shooterSensorPower3;

	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		gamepad = new Joystick(Constants.GAMEPAD);
		drive = new Drive();
		intake = new Intake();
		driver = new AutoDriver(drive, shooter);
		shooter = new Shooter();
		shooterSensorPower1 = new Solenoid(1);
		shooterSensorPower2 = new Solenoid(6);
		shooterSensorPower3 = new Solenoid(7);
		
		// current monitor
		new CurrentMonitor();

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
		boolean isShooterButtonPressed = false;
		boolean wasShooterButtonPressed = false;
		boolean isShooterActive = false;
		while (isOperatorControl() && isEnabled())
		{
			// TODO move

			// Shooter
			if (isShooterButtonPressed != wasShooterButtonPressed)
			{
				wasShooterButtonPressed = isShooterButtonPressed;
			}
			isShooterButtonPressed = (joyLeft.getRawButton(1) || joyRight.getRawButton(1));
			if (wasShooterButtonPressed)
			{
				if (isShooterActive)
				{
					shooter.stop();
				}
				else
				{
					shooter.start();
				}
			}

			if (joyLeft.getRawButton(3) || joyRight.getRawButton(3))
			{
				shooter.setFlippers(true);
			}
			else
			{
				shooter.setFlippers(false);
			}

			if (joyLeft.getRawButton(2) || joyRight.getRawButton(2))
			{
				shooter.reverse();
			}
			else
			{
				shooter.stop();
			}

			if (gamepad.getRawButton(4))
			{
				shooter.setActuatorOverride(1);
			}
			else if (gamepad.getRawButton(1))
			{
				shooter.setActuatorOverride(-1);
			}
			else
			{
				shooter.setActuatorOverride(0);
			}
			
			double intakeSpeed = gamepad.getY();
			
			intake.intakeAngleOverride(intakeSpeed);

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
