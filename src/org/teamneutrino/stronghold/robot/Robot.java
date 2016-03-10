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
import org.teamneutrino.stronghold.robot.util.JoystickButtonManager;

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

	private JoystickButtonManager joyLeftMan;
	private JoystickButtonManager joyRightMan;
	private JoystickButtonManager gamepadMan;

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
		joyLeftMan = new JoystickButtonManager(joyLeft);
		joyLeftMan.updateButtons();
		joyRightMan = new JoystickButtonManager(joyRight);
		joyRightMan.updateButtons();
		gamepadMan = new JoystickButtonManager(gamepad);
		gamepadMan.updateButtons();

		int INTAKE_MID_POS = -3;

		intake.setSetpoint(INTAKE_MID_POS);
		shooter.setSetpoint(45);

		boolean shooterSpinPrev = false;

		boolean shooterOverrideEnabled = false;
		boolean intakeOverrideEnabled = false;
		
		boolean outtakingPrev = false;
		boolean outtaking = false;

		while (isOperatorControl() && isEnabled())
		{
			joyLeftMan.updateButtons();
			joyRightMan.updateButtons();
			gamepadMan.updateButtons();

			boolean intaking = false;
			outtakingPrev = outtaking;
			outtaking = false;
			boolean shooting = false;

			double shooterPosition = shooter.getPosition();
			double intakePosition = intake.getPosition();

			// overrides
			if (joyRightMan.getButtonState(6))
			{
				// enable intake override
				intakeOverrideEnabled = true;
			}
			else if (joyRightMan.getButtonState(7))
			{
				// disable intake override
				intakeOverrideEnabled = false;
			}
			if (joyRightMan.getButtonState(11))
			{
				// enable shooter override
				shooterOverrideEnabled = true;
			}
			else if (joyRightMan.getButtonState(10))
			{
				// disable shooter override
				shooterOverrideEnabled = false;
			}

			// auto aiming
			boolean isAiming = driver.isAiming();
			if ((joyLeftMan.getButtonState(3) || joyRightMan.getButtonState(3)) && !isAiming)
			{
				driver.aim(false);
				isAiming = true;
			}
			else if (!(joyLeftMan.getButtonState(3) || joyRightMan.getButtonState(3)) && isAiming)
			{
				driver.stopAim();
				isAiming = true;
			}

			// Shooter Spinup & Shoot
			boolean shooterSpinCurr = gamepad.getRawAxis(3) > .6 && (shooterPosition > 25 || shooterOverrideEnabled);
			if (shooterSpinCurr != shooterSpinPrev)
			{
				// spin up the shooter
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
			if (!shooting && gamepadMan.getButtonState(5))
			{
				// intake
				intaking = true;
				outtaking = false;
				shooter.setFutter(true);
				intake.setFutter(true);
				shooter.setOverrideSpeed(-1);
				if (shooterPosition < 15 || shooterOverrideEnabled)
				{
					intake.set(1);
				}
			}
			else if (!shooting && gamepad.getRawAxis(2) > .6)
			{
				// outtake
				outtaking = true;
				intaking = false;
				shooter.setFutter(false);
				intake.setFutter(false);
				intake.set(-1);
				if (shooterPosition < 15 || shooterOverrideEnabled)
				{
					shooter.setOverrideSpeed(1);
				}
			}
			else
			{
				// no intake
				if (!shooting)
				{
					shooter.setOverrideSpeed(0);
				}
				intake.set(0);
				intaking = false;
				outtaking = false;
				shooter.setFutter(false);
				intake.setFutter(false);
			}

			// intake position
			double gamepadPOV = gamepad.getPOV();
			if (intakeOverrideEnabled)
			{
				// override mode
				intake.setActuatorOverride(-.6 * gamepad.getRawAxis(1));
			}
			else if ((shooting && intakePosition > 0) || intaking || gamepadPOV == 270 || gamepadMan.getButtonState(2))
			{
				// mid
				intake.setSetpoint(INTAKE_MID_POS);
			}
			else if (gamepadPOV == 0)
			{
				// up
				intake.setSetpoint(90);
			}
			else if (gamepadPOV == 180)
			{
				// down
				intake.setSetpoint(-19);
			}

			// shooter position
			if (isAiming)
			{
				// don't move shooter position when aiming
			}
			if (shooterOverrideEnabled)
			{
				shooter.setActuatorOverride(-gamepad.getRawAxis(5));
			}
			else if (intaking || outtaking || gamepadMan.getButtonState(4) || gamepadMan.getButtonState(2))
			{
				shooter.setSetpoint(0);
			}
			else if (gamepadMan.getButtonState(3))
			{
				shooter.setSetpoint(30);
			}
			else if (gamepadMan.getButtonState(1))
			{
				shooter.setSetpoint(120);
			}

			if (outtaking)
			{
				if (!outtakingPrev)
				{
					shooter.startEjectThread();
				}
			}
			else
			{
				shooter.setFlippers(
						shooting && gamepadMan.getButtonState(6) && (intakePosition < 10 || intakeOverrideEnabled));
			}

			// drive
			// stinger
			if (!isAiming)
			{
				if (joyLeftMan.getButtonChanged(2) || joyRightMan.getButtonChanged(2))
				{
					stinger.setStinger(joyLeftMan.getButtonState(2) || joyRightMan.getButtonState(2));
				}

				double leftSpeed = -joyLeft.getY();
				double rightSpeed = -joyRight.getY();

				// fast mode
				drive.setFastMode((joyLeftMan.getButtonState(1) && joyLeftMan.getButtonChanged(1))
						|| (joyRightMan.getButtonState(1) && joyLeftMan.getButtonChanged(1)));

				leftSpeed = leftSpeed * Math.abs(leftSpeed);
				rightSpeed = rightSpeed * Math.abs(rightSpeed);

				drive.setLeft(leftSpeed);
				drive.setRight(rightSpeed);
			}

			Thread.yield();
		}
	}

	@Override
	public void test()
	{
	}
}
