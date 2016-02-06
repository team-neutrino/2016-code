package org.usfirst.frc.team3928.robot;

import org.usfirst.frc.team3928.robot.autonomous.AutoController;
import org.usfirst.frc.team3928.robot.autonomous.AutoDriver;
import org.usfirst.frc.team3928.robot.autonomous.modes.DoNothing;
import org.usfirst.frc.team3928.robot.autonomous.modes.MoveForward;
import org.usfirst.frc.team3928.robot.autonomous.modes.TestMode;
import org.usfirst.frc.team3928.robot.sensors.Camera;
import org.usfirst.frc.team3928.robot.subsystems.Drive;
import org.usfirst.frc.team3928.robot.subsystems.Intake;
import org.usfirst.frc.team3928.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends SampleRobot
{
	private Joystick joyLeft;
	private Joystick joyRight;
	private Joystick gamepad;
	private Drive drive;
	private AutoController autoController;
	private AutoDriver driver;
	private Camera cam;
	private Intake intake;
	private Shooter shooter;
	private Encoder encLeft;
	private Encoder encRight;
	private AnalogGyro gyro;

	public Robot()
	{
		joyLeft = new Joystick(Constants.JOY_LEFT);
		joyRight = new Joystick(Constants.JOY_RIGHT);
		gamepad = new Joystick(Constants.GAMEPAD);
		encLeft = new Encoder(Constants.ENCODER_LEFT_A_CHANNEL, Constants.ENCODER_LEFT_B_CHANNEL, true);
		encRight = new Encoder(Constants.ENCODER_RIGHT_A_CHANNEL, Constants.ENCODER_RIGHT_B_CHANNEL, false);
		drive = new Drive();
		cam = new Camera();
		intake = new Intake();
		gyro = new AnalogGyro(Constants.GYRO_CHANNEL);
		driver = new AutoDriver(drive, cam, encLeft, encRight, gyro);

		// set up auto modes
		autoController = new AutoController();
		autoController.assignMode(0, new DoNothing());
		autoController.assignMode(1, new MoveForward(driver, encLeft, encRight, drive));
		autoController.assignMode(2, new TestMode(driver, shooter));
	}

	@Override
	public void robotInit()
	{
		gyro.calibrate();
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
			drive.setRightSpeed(rightSpeed * Math.abs(rightSpeed));
			Timer.delay(0.005); // wait for a motor update time
		}
	}

	@Override
	public void test()
	{
		double ctr = 0;
		while (isTest())
		{
			ctr++;

			if (ctr == 200)
			{
				ctr = 0;

				DriverStation.reportError("Left Distance:" + encLeft.getDistance(), false);
				DriverStation.reportError("Right Distance:" + encRight.getDistance(), false);
			}

			double leftSpeed = -joyLeft.getY();
			double rightSpeed = -joyRight.getY();
			drive.setLeftSpeed(leftSpeed * Math.abs(leftSpeed));
			drive.setRightSpeed(rightSpeed * Math.abs(rightSpeed));

			Timer.delay(0.005); // wait for a motor update time
		}
	}
}
