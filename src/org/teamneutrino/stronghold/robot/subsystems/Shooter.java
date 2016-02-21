package org.teamneutrino.stronghold.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Shooter implements Runnable
{
	private SpeedController leftMotor;
	private SpeedController rightMotor;
	private SpeedController actuatorMotor;

	private Counter beambreakLeft;
	private Counter beambreakRight;
	private AnalogPotentiometer encoder;
	private Solenoid flippersOpenCylinder;
	private Solenoid flippersCloseCylinder;

	// TODO change to left & right
	private Solenoid beambreak1Power;
	private Solenoid beambreak2Power;

	private PIDController actuationPID;

	private boolean running;
	private boolean ejectThreadRunning;
	private boolean atTargetSpeed;

	private boolean leftBeamBreakNoSignal;
	private boolean rightBeamBreakNoSignal;

	private Thread speedThread;
	private Thread ejectThread;

	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final int CORRECTION_RPM = 2000;

	private static final int NO_SIGNAL_DETECTION_COUNT = 5;

	public Shooter()
	{
		if (Constants.REAL_BOT)
		{
			leftMotor = new CANTalon(Constants.SHOOTER_LEFT_MOTOR_CHANNEL);
			rightMotor = new CANTalon(Constants.SHOOTER_RIGHT_MOTOR_CHANNEL);
			actuatorMotor = new Talon(Constants.SHOOTER_ACTUATOR_MOTOR_CHANNEL);
		}
		else
		{
			leftMotor = new Victor(Constants.SHOOTER_LEFT_MOTOR_CHANNEL);
			rightMotor = new Victor(Constants.SHOOTER_RIGHT_MOTOR_CHANNEL);
			actuatorMotor = new Victor(Constants.SHOOTER_ACTUATOR_MOTOR_CHANNEL);
		}
		rightMotor.setInverted(true);

		flippersOpenCylinder = new Solenoid(Constants.SHOOTER_FLIPPER_OPEN_CYLINDER_CHANNEL);
		flippersCloseCylinder = new Solenoid(Constants.SHOOTER_FLIPPER_CLOSE_CYLINDER_CHANNEL);

		beambreakLeft = new Counter(Constants.SHOOTER_BEAMBREAK_RIGHT_CHANNEL);
		beambreakRight = new Counter(Constants.SHOOTER_BEAMBREAK_LEFT_CHANNEL);

		beambreak1Power = new Solenoid(Constants.SHOOTER_BEAMBREAK_1_POWER_CHANNEL);
		beambreak2Power = new Solenoid(Constants.SHOOTER_BEAMBREAK_2_POWER_CHANNEL);
		beambreak1Power.set(true);
		beambreak2Power.set(true);

		encoder = new AnalogPotentiometer(Constants.SHOOTER_ENCODER_CHANNEL, Constants.SHOOTER_ENCODER_SCALE,
				Constants.SHOOTER_ENCODER_OFFSET);

		actuationPID = new PIDController(Constants.SHOOTER_ACTUATION_K_P, Constants.SHOOTER_ACTUATION_K_I,
				Constants.SHOOTER_ACTUATION_K_D, encoder, actuatorMotor);
		actuationPID.setContinuous(true);

		// TODO remove
		actuationPID.setOutputRange(-.25, .25);

		leftBeamBreakNoSignal = false;
		rightBeamBreakNoSignal = false;

		running = false;

		setFlippers(false);
		
		ejectThread = new Thread(new ejectThread());
		speedThread = new Thread(this);
	}
	
	public void startEjectThread()
	{
		ejectThread.start();
		if (!running && !ejectThreadRunning)
		{
			ejectThreadRunning = true;
		}
	}

	public void start()
	{
		leftMotor.set(1);
		rightMotor.set(1);
		//
		// if (!running)
		// {
		// shooterSpeedThread.start();
		// running = true;
		// }
		running = true;
	}

	public void setOverrideSpeed(double speed)
	{
		if (!running)
		{
			leftMotor.set(speed);
			rightMotor.set(speed);
		}
	}

	public void stop()
	{
		leftMotor.set(0);
		rightMotor.set(0);
		running = false;
	}

	public boolean isRunning()
	{
		return running;
	}

	public boolean isAtTargetSpeed()
	{
		return atTargetSpeed;
	}

	public void setActuatorOverride(double speed)
	{
		actuationPID.disable();
		actuatorMotor.set(speed);
	}

	public void setSetpoint(double angle)
	{
		// bound angle between max and 0
		if (angle < 0)
		{
			angle = 0;
		}
		else if (angle > Constants.SHOOTER_ENCODER_MAX)
		{
			angle = Constants.SHOOTER_ENCODER_MAX;
		}
		actuationPID.setSetpoint(angle);
		actuationPID.enable();
	}

	public double getSetpoint()
	{
		return actuationPID.getSetpoint();
	}

	public double getPosition()
	{
		return encoder.get();
	}

	public void setFlippers(boolean triggered)
	{
		flippersOpenCylinder.set(triggered);
		flippersCloseCylinder.set(!triggered);
	}

	@Override
	public void run()
	{
		String printout = "currTime, timeInterval, countLeft, countRight, RPMiliTarget, RPMilliLeft, RPMilliRight, "
				+ "RPMilliMin, integral, error, targetPower, leftCorrection, rightCorrection\n";

		long lastResetTime = System.currentTimeMillis();

		double RPMilliTarget = ((double) Constants.SHOOTER_RPM) / MILLISECONDS_PER_MINUTE;
		double RPMilliTolerence = ((double) Constants.SHOOTER_TARGET_SPEED_TOLERANCE_RPM) / MILLISECONDS_PER_MINUTE;

		double integral = 0;

		int leftNoSignalCount = 0;
		int rightNoSignalCount = 0;

		beambreakLeft.reset();
		beambreakRight.reset();

		leftMotor.set(RPMiliToPower(RPMilliTarget));
		rightMotor.set(RPMiliToPower(RPMilliTarget));

		while (running && DriverStation.getInstance().isEnabled())
		{
			try
			{
				Thread.sleep(Constants.SHOOTER_REFRESH_RATE);
			}
			catch (InterruptedException e)
			{
			}

			long currTime = System.currentTimeMillis();
			int timeInterval = (int) (lastResetTime - currTime);
			lastResetTime = currTime;
			int countLeft = beambreakLeft.get();
			beambreakLeft.reset();
			int countRight = beambreakRight.get();
			beambreakRight.reset();

			if (countLeft == 0)
			{
				leftNoSignalCount++;
			}
			else
			{
				leftNoSignalCount = 0;
			}
			if (countRight == 0)
			{
				rightNoSignalCount++;
			}
			else
			{
				rightNoSignalCount = 0;
			}

			if (!leftBeamBreakNoSignal && leftNoSignalCount > NO_SIGNAL_DETECTION_COUNT)
			{
				leftBeamBreakNoSignal = true;
				DriverStation.reportError("No signal from left breambreak (maybe unplugged)", false);
			}
			if (!rightBeamBreakNoSignal && rightNoSignalCount > NO_SIGNAL_DETECTION_COUNT)
			{
				rightBeamBreakNoSignal = true;
				DriverStation.reportError("No signal from right breambreak (maybe unplugged)", false);
			}

			double RPMilliLeft = (((double) countLeft) / timeInterval);
			double RPMilliRight = (((double) countRight) / timeInterval);

			double RPMilliMin;
			if (leftBeamBreakNoSignal)
			{
				RPMilliMin = RPMilliRight;
			}
			else if (rightBeamBreakNoSignal)
			{
				RPMilliMin = RPMilliLeft;
			}
			else
			{
				RPMilliMin = Math.min(RPMilliLeft, RPMilliRight);
			}

			integral = integral + RPMilliMin * timeInterval;
			double error = RPMilliTarget - RPMilliMin;

			if (Math.abs(error) <= RPMilliTolerence)
			{
				atTargetSpeed = true;
			}
			else
			{
				atTargetSpeed = false;
			}

			double targetPower = RPMiliToPower(RPMilliTarget) + (leftBeamBreakNoSignal && rightBeamBreakNoSignal ? 0
					: error * Constants.SHOOTER_K_P + integral * Constants.SHOOTER_K_I);

			// Keep target power between 0 and 1
			targetPower = Math.min(1, Math.max(0, targetPower));

			// correct if one side is too slow compared to the other
			double diff = RPMilliRight - RPMilliLeft;
			double leftCorrection;
			double rightCorrection;
			if (diff > 0 && !leftBeamBreakNoSignal)
			{
				// right is too fast
				leftCorrection = 1;
				rightCorrection = Math.max(1 - (diff / CORRECTION_RPM), 0);
			}
			else if (diff < 0 && !rightBeamBreakNoSignal)
			{
				// left is too fast
				leftCorrection = Math.max(1 - (-diff / CORRECTION_RPM), 0);
				rightCorrection = 1;
			}
			else
			{
				// everything good
				leftCorrection = 1;
				rightCorrection = 1;
			}

			leftMotor.set(targetPower * leftCorrection);
			rightMotor.set(targetPower * rightCorrection);

			printout += currTime + " ," + timeInterval + " ," + countLeft + " ," + countRight + " ," + RPMilliTarget
					+ " ," + RPMilliLeft + " ," + RPMilliRight + " ," + RPMilliMin + " ," + integral + " ," + error
					+ " ," + targetPower + " ," + leftCorrection + " ," + rightCorrection + "\n";
		}

		leftMotor.set(0);
		rightMotor.set(0);

		running = false;

		writeFile("/home/lvuser/shooterRun.csv", printout);
	}

	// TODO Remove
	public void writeShooterCurves()
	{
		String printout = "currTime, timeInterval, countLeft, countRight, RPMilliLeft, RPMilliRight, " + "\n";

		beambreakLeft.reset();
		beambreakRight.reset();

		long lastResetTime = System.currentTimeMillis();

		for (double i = 0; i <= 1; i += .005)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
			long currTime = System.currentTimeMillis();
			int timeInterval = (int) (lastResetTime - currTime);
			lastResetTime = currTime;
			int countLeft = beambreakLeft.get();
			int countRight = beambreakRight.get();
			beambreakLeft.reset();
			beambreakRight.reset();

			double RPMilliLeft = (((double) countLeft) / timeInterval);
			double RPMilliRight = (((double) countRight) / timeInterval);

			printout += currTime + " ," + timeInterval + " ," + countLeft + " ," + countRight + " ," + RPMilliLeft
					+ " ," + RPMilliRight + "\n";
		}
		leftMotor.set(0);
		rightMotor.set(0);

		writeFile("/home/lvuser/shooterRun.csv", printout);
	}

	private double RPMiliToPower(double RPMili)
	{
		return RPMili * Constants.SHOOTER_PERCENT_POWER_PER_RPMILLI;
	}

	// TODO Remove
	private void writeFile(String filename, String contents)
	{
		try
		{
			PrintWriter writer = new PrintWriter(filename);
			writer.println(contents);
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			DriverStation.reportError("Can't write file!", false);
		}
	}
	
	private class ejectThread implements Runnable
	{

		@Override
		public void run()
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
			}
			
			setFlippers(true);
			ejectThreadRunning = false;
		}
		
	}
}
