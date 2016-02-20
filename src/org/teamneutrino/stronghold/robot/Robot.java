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
import org.teamneutrino.stronghold.robot.subsystems.Stinger;
import org.teamneutrino.stronghold.robot.util.CurrentMonitor;

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
	private Stinger stinger;

	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		gamepad = new Joystick(Constants.GAMEPAD);
		drive = new Drive();
		intake = new Intake();
		shooter = new Shooter();
		stinger = new Stinger();
		driver = new AutoDriver(drive, shooter);

		// current monitor
		// new CurrentMonitor();

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
//		autoController.run();
		driver.aim(true);
	}

	@Override
	public void operatorControl()
	{
		boolean intakeActive = false;

		intake.setSetpoint(-10);
		shooter.setSetpoint(45);

		while (isOperatorControl() && isEnabled())
		{
			// Shooter
			// manually move shooter
			// shooter.setActuatorOverride(-gamepad.getRawAxis(5));

			if (gamepad.getRawButton(4))
			{
				shooter.setSetpoint(45);
			}
			else if (gamepad.getRawButton(1))
			{
				shooter.setSetpoint(135);
			}
			else if (gamepad.getRawButton(5) || gamepad.getRawButton(2))
			{
				shooter.setSetpoint(0);
			}

			// System.out.println(shooter.getPosition());

			// shoot
			shooter.setFlippers(gamepad.getRawButton(3));

			// start shooter
			if (gamepad.getRawButton(5))
			{
				shooter.reverse();
				intakeActive = true;
			}
			else
			{
				if (intakeActive)
				{
					intakeActive = false;
					shooter.stop();
				}

				if (gamepad.getPOV() == 0)
				{
					shooter.start();
				}
				else if (gamepad.getPOV() == 180)
				{
					shooter.stop();
				}
			}

			// manually move intake TODO
			// intake.setActuatorOverride(-.25 * gamepad.getRawAxis(1));

//			if (gamepad.getRawButton(4) || gamepad.getRawButton(1))
//			{
//				intake.setSetpoint(90);
//			} else
			if (gamepad.getRawButton(5) || gamepad.getRawButton(2))
			{
				intake.setSetpoint(-10);
			}

			// intake
			intake.set((intakeActive ? 1 : 0));

			// stingers
			stinger.setStinger(joyLeft.getRawButton(1) || joyRight.getRawButton(1));

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
