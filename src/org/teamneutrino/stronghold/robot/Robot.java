package org.teamneutrino.stronghold.robot;

import org.teamneutrino.stronghold.robot.autonomous.AutoController;
import org.teamneutrino.stronghold.robot.autonomous.AutoDriver;
import org.teamneutrino.stronghold.robot.autonomous.modes.DoNothing;
import org.teamneutrino.stronghold.robot.autonomous.modes.LowBar;
import org.teamneutrino.stronghold.robot.autonomous.modes.BD;
import org.teamneutrino.stronghold.robot.autonomous.modes.BDHighGoalLeft;
import org.teamneutrino.stronghold.robot.autonomous.modes.BDHighGoalRight;
import org.teamneutrino.stronghold.robot.autonomous.modes.LowBarHighGoal;
import org.teamneutrino.stronghold.robot.sensors.Camera;
import org.teamneutrino.stronghold.robot.subsystems.Drive;
import org.teamneutrino.stronghold.robot.subsystems.Intake;
import org.teamneutrino.stronghold.robot.subsystems.Shooter;
import org.teamneutrino.stronghold.robot.subsystems.Stinger;
import org.teamneutrino.stronghold.robot.util.CurrentMonitor;
import org.teamneutrino.stronghold.robot.util.JoystickButtonManager;
import org.teamneutrino.stronghold.robot.util.SmartDashboardOutputs;
import org.teamneutrino.stronghold.robot.util.Util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
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
	private Camera camera;
	private CurrentMonitor currMon;

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
		camera = new Camera();
		driver = new AutoDriver(drive, shooter, camera);
		currMon = new CurrentMonitor();

		new SmartDashboardOutputs(currMon, shooter, intake, driver);

		// set up auto modes
		autoController = new AutoController();
		autoController.assignMode(0, new DoNothing());
		autoController.assignMode(1, new LowBar(driver, shooter, intake));
		autoController.assignMode(2, new BD(driver, shooter, intake));
		autoController.assignMode(3, new LowBarHighGoal(driver, shooter, intake, drive, camera));
		autoController.assignMode(4, new BDHighGoalRight(driver, shooter, intake, drive));
		autoController.assignMode(5, new BDHighGoalLeft(driver, shooter, intake, drive));
	}

	@Override
	public void robotInit()
	{
		System.out.println("--- INIT ---");
	}

	@Override
	public void disabled()
	{
		System.out.println("--- DISABLED ---");
	}

	@Override
	public void autonomous()
	{
		System.out.println("--- AUTO ---");
		autoController.run();
	}

	@Override
	public void operatorControl()
	{
		System.out.println("--- TELEOP ---");
		joyLeftMan = new JoystickButtonManager(joyLeft);
		joyLeftMan.updateButtons();
		joyRightMan = new JoystickButtonManager(joyRight);
		joyRightMan.updateButtons();
		gamepadMan = new JoystickButtonManager(gamepad);
		gamepadMan.updateButtons();

		intake.setSetpoint(shooter.getPosition());
		shooter.setSetpoint(shooter.getPosition());

		boolean shooterSpinPrev = false;

		boolean shooterOverrideEnabled = false;
		boolean intakeOverrideEnabled = false;

		boolean outtakingPrev = false;
		boolean outtaking = false;

		boolean overCurrent = false;

		long lastCurrentUpdateTime = 0;

		driver.stopAim();

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

			if (joyRightMan.getButtonState(8))
			{
				autoAimShoot();
			}

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
			boolean aimingTriggered = (joyLeftMan.getButtonState(3) || joyRightMan.getButtonState(3))
					&& camera.targetInFrame();

			if (aimingTriggered && !isAiming)
			{
				driver.aim();
				isAiming = true;
			}
			else if (!aimingTriggered && isAiming)
			{
				driver.stopAim();
				isAiming = false;
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
			}

			double intakeAxis = gamepad.getRawAxis(1);

			// intake position
			if (intakeOverrideEnabled)
			{
				// override mode
				if (Math.abs(intakeAxis) > .1)
				{
					intake.setActuatorOverride(-.6 * intakeAxis);
				}
				else
				{
					intake.setActuatorOverride(0);
				}
			}
			else
			{
				double joyPosition = -intakeAxis;

				if (Math.abs(joyPosition) < 0.2)
				{
					intake.setTargetPosition(Intake.Position.INTAKE);
				}
				else if (joyPosition >= 0.2)
				{
					intake.setSetpoint(Util.scale(joyPosition, 0.2, 1, Intake.Position.INTAKE.location,
							Intake.Position.UP.location));
				}
				else
				{
					intake.setSetpoint(Util.scale(joyPosition, -0.2, -1, Intake.Position.INTAKE.location,
							Intake.Position.DOWN.location));
				}
			}

			double shooterOverrideAxis = gamepad.getRawAxis(5);

			// shooter position
			if (isAiming)
			{
				// don't move shooter position when aiming
			}
			else if (shooterOverrideEnabled)
			{
				if (Math.abs(shooterOverrideAxis) > .1)
				{
					shooter.setActuatorOverride(-shooterOverrideAxis);
				}
				else
				{
					shooter.setActuatorOverride(0);
				}
			}
			else if (intaking || outtaking || gamepadMan.getButtonState(4))
			{
				shooter.setTargetPosition(Shooter.Position.INTAKE);
			}
			else if (gamepadMan.getButtonState(2))
			{
				shooter.setTargetPosition(Shooter.Position.SHOOT);
			}
			else if (gamepadMan.getButtonState(3))
			{
				shooter.setTargetPosition(Shooter.Position.FRONT);
			}
			else if (gamepadMan.getButtonState(1))
			{
				shooter.setTargetPosition(Shooter.Position.BACK);
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
				shooter.stopEjectThread();
				shooter.setFlippers(
						shooting && gamepadMan.getButtonState(6) && (intakePosition < 10 || intakeOverrideEnabled));
			}

			// drive
			// stinger
			if (!isAiming)
			{
				double leftSpeed = -joyLeft.getY();
				double rightSpeed = -joyRight.getY();

				if (joyLeftMan.getButtonState(2) || joyRightMan.getButtonState(2))
				{
					leftSpeed = leftSpeed * .5;
					rightSpeed = rightSpeed * .5;
					// stinger.setStinger(joyLeftMan.getButtonState(2) ||
					// joyRightMan.getButtonState(2));
				}

				// fast mode
				if (joyLeftMan.getButtonChanged(1) || joyRightMan.getButtonChanged(1))
					drive.setFastMode(joyLeftMan.getButtonState(1) || joyRightMan.getButtonState(1));

				leftSpeed = leftSpeed * Math.abs(leftSpeed);
				rightSpeed = rightSpeed * Math.abs(rightSpeed);

				drive.setLeft(leftSpeed);
				drive.setRight(rightSpeed);
			}

			long currTime = System.currentTimeMillis();

			if (currTime - lastCurrentUpdateTime > 50)
			{
				overCurrent = currMon.getCurrentOver120();
				lastCurrentUpdateTime = currTime;
			}

			gamepad.setRumble(RumbleType.kLeftRumble, (overCurrent ? 1f : 0f));
			gamepad.setRumble(RumbleType.kRightRumble, (overCurrent ? 1f : 0f));

			Util.sleep(5);
		}
	}

	@Override
	public void test()
	{
		System.out.println("--- TEST ---");
	}

	private void autoAimShoot()
	{
		// aim
		driver.aim();

		while (camera.targetInFrame() && !driver.isAimed())
		{
			Util.sleep(5);

			if (!camera.targetInFrame() || joyRight.getRawButton(9))
			{
				driver.stopAim();
				return;
			}
		}

		driver.stopAim();

		// shoot
		shooter.start();

		Util.sleep(1000);

		while (!shooter.isAtTargetSpeed())
		{
			Util.sleep(5);
			if (joyRight.getRawButton(9))
			{
				shooter.stop();
				return;
			}
		}

		shooter.setFlippers(true);

		Util.sleep(500);

		shooter.setFlippers(false);
		shooter.stop();
	}
}
