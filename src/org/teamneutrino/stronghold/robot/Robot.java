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
		autoController.run();
	}

	@Override
	public void operatorControl()
	{
		int INTAKE_MID_POS = -3;
		
		intake.setSetpoint(INTAKE_MID_POS);
		shooter.setSetpoint(45);

		boolean shooterSpinPrev = false;

		boolean shooterOverrideEnabled = false;
		boolean intakeOverrideEnabled = false;

		while (isOperatorControl() && isEnabled())
		{
			boolean intaking = false;
			boolean outtaking = false;
			boolean shooting = false;

			double shooterPosition = shooter.getPosition();
			double intakePosition = intake.getPosition();
			
			// overrides
			if (joyRight.getRawButton(6))
			{
				intakeOverrideEnabled = true;
			}
			else if (joyRight.getRawButton(7))
			{
				intakeOverrideEnabled = false;
			}
			if (joyRight.getRawButton(11))
			{
				shooterOverrideEnabled = true;
			}
			else if (joyRight.getRawButton(10))
			{
				shooterOverrideEnabled = false;
			}

			// Shooter Spinup & Shoot
			boolean shooterSpinCurr = gamepad.getRawAxis(3) > .6 && (shooterPosition > 25 || shooterOverrideEnabled);
			if (shooterSpinCurr != shooterSpinPrev)
			{
				shooterSpinPrev = shooterSpinCurr;
				if (shooterSpinCurr)
				{
					shooter.start();
				}
				else
				{
					shooter.stop();
				}
			}
			shooting = shooterSpinCurr;

			// intake/outtake
			if (!shooting && gamepad.getRawButton(5))
			{
				intaking = true;
				outtaking = false;
				shooter.setOverrideSpeed(-1);
				if (shooterPosition < 10 || shooterOverrideEnabled)
				{
					intake.set(1);
				}
			}
			else if (!shooting && gamepad.getRawAxis(2) > .6)
			{
				outtaking = true;
				intaking = false;
				intake.set(-1);
				if (shooterPosition < 10 || shooterOverrideEnabled)
				{
					shooter.setOverrideSpeed(1);
				}
			}
			else
			{
				if (!shooting)
				{
					shooter.setOverrideSpeed(0);
				}
				intake.set(0);
				intaking = false;
				outtaking = false;
			}

			// intake position
			double gamepadPOV = gamepad.getPOV();
			if (intakeOverrideEnabled)
			{
				intake.setActuatorOverride(-.25 * gamepad.getRawAxis(1));
			}
			else if ((shooting && intakePosition > 0) || intaking || gamepadPOV == 270 || gamepad.getRawButton(2))
			{
				intake.setSetpoint(INTAKE_MID_POS);
			}
			else if (gamepadPOV == 0)
			{
				intake.setSetpoint(90);
			}
			else if (gamepadPOV == 180)
			{
				intake.setSetpoint(-40);
			}

			// shooter position
			if (shooterOverrideEnabled)
			{
				shooter.setActuatorOverride(-gamepad.getRawAxis(4));
			}
			else if (intaking || outtaking || gamepad.getRawButton(4) || gamepad.getRawButton(2))
			{
				shooter.setSetpoint(0);
			}
			else if (gamepad.getRawButton(3))
			{
				shooter.setSetpoint(45);
			}
			else if (gamepad.getRawButton(1))
			{
				shooter.setSetpoint(135);
			}
			
			if (outtaking)
			{
				shooter.startEjectThread();
			}
			else
			{
				shooter.setFlippers(shooting && gamepad.getRawButton(6) && (intakePosition < 10 || intakeOverrideEnabled));
			}

			// drive
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
