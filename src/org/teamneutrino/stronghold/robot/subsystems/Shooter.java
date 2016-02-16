package org.teamneutrino.stronghold.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Shooter implements Runnable
{
	// TODO limit switches
	private SpeedController leftMotor;
	private SpeedController rightMotor;
	private SpeedController actuatorMotor;

	private Counter beambreakLeft;
	private Counter beambreakRight;
	private AnalogPotentiometer encoder;

	private PIDController actuationPID;

	private boolean running;
	private boolean reverse;

	private Thread shooterSpeedThread;

	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final int CORRECTION_RPM = 2000;

	public Shooter()
	{
		if (Constants.REAL_BOT)
		{
			leftMotor = new CANTalon(Constants.SHOOTER_LEFT_MOTOR);
			rightMotor = new CANTalon(Constants.SHOOTER_RIGHT_MOTOR);
			actuatorMotor = new Talon(Constants.SHOOTER_ACTUATOR_MOTOR);
		}
		else
		{
			leftMotor = new Victor(Constants.SHOOTER_LEFT_MOTOR);
			rightMotor = new Victor(Constants.SHOOTER_RIGHT_MOTOR);
			actuatorMotor = new Victor(Constants.SHOOTER_ACTUATOR_MOTOR);
		}
		leftMotor.setInverted(true);

		beambreakLeft = new Counter(Constants.SHOOTER_BEAMBREAKE_RIGHT_CHANNEL);
		beambreakRight = new Counter(Constants.SHOOTER_BEAMBREAKE_LEFT_CHANNEL);

		encoder = new AnalogPotentiometer(Constants.SHOOTER_ENCODER_CHANNEL, Constants.SHOOTER_ENCODER_SCALE,
				Constants.SHOOTER_ENCODER_OFFSET);

		actuationPID = new PIDController(Constants.SHOOTER_ACTUATION_K_P, Constants.SHOOTER_ACTUATION_K_I,
				Constants.SHOOTER_ACTUATION_K_D, encoder, actuatorMotor);

		running = false;
		reverse = false;

		shooterSpeedThread = new Thread(this);
	}
	
	public void start()
	{
		reverse = false;
		
		if (!running)
		{
			shooterSpeedThread.start();
			running = true;
		}
	}
	
	public void reverse()
	{
		reverse = true;
		
		if (!running)
		{
			shooterSpeedThread.start();
			running = true;
		}
	}

	public void stop()
	{
		running = false;
	}

	public boolean isRunning()
	{
		return running;
	}
	
	public void setAngle(double angle)
	{
		actuationPID.setSetpoint(angle);
		actuationPID.enable();
	}

	@Override
	public void run()
	{
		String printout = "currTime, timeInterval, countLeft, countRight, RPMiliTarget, RPMilliLeft, RPMilliRight, "
				+ "RPMilliMin, integral, error, targetPower, leftCorrection, rightCorrection\n";

		long lastResetTime = System.currentTimeMillis();

		double RPMiliTarget = ((double) Constants.SHOOTER_RPM) / MILLISECONDS_PER_MINUTE;

		double integral = 0;

		beambreakLeft.reset();
		beambreakRight.reset();

		leftMotor.set(RPMiliToPower(RPMiliTarget));
		rightMotor.set(RPMiliToPower(RPMiliTarget));

		while (running)
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
			int countRight = beambreakRight.get();
			beambreakLeft.reset();
			beambreakRight.reset();

			double RPMilliLeft = (((double) countLeft) / timeInterval);
			double RPMilliRight = (((double) countRight) / timeInterval);

			double RPMilliMin = Math.min(RPMilliLeft, RPMilliRight);

			double inegral = integral + RPMilliMin * timeInterval;
			double error = RPMiliTarget - RPMilliMin;

			double targetPower = RPMiliToPower(RPMiliTarget) + error * Constants.SHOOTER_K_P
					+ inegral * Constants.SHOOTER_K_P;

			// Keep target power between 0 and 1
			targetPower = Math.min(1, Math.max(0, targetPower));

			// correct if one side is too slow compared to the other
			double diff = RPMilliRight - RPMilliLeft;
			double leftCorrection;
			double rightCorrection;
			if (diff > 0)
			{
				// right is too fast
				leftCorrection = 1;
				rightCorrection = Math.max(1 - (diff / CORRECTION_RPM), 0);
			}
			else if (diff < 0)
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

			leftMotor.set((reverse ? -1 : 1) * targetPower * leftCorrection);
			rightMotor.set((reverse ? -1 : 1) * targetPower * rightCorrection);

			printout += currTime + " ," + timeInterval + " ," + countLeft + " ," + countRight + " ," + RPMiliTarget
					+ " ," + RPMilliLeft + " ," + RPMilliRight + " ," + RPMilliMin + " ," + integral + " ," + error
					+ " ," + targetPower + " ," + leftCorrection + " ," + rightCorrection + "\n";
		}

		writeFile("/home/lvuser/shooterRun.csv", printout);

		leftMotor.set(0);
		rightMotor.set(0);
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
		return RPMili * Constants.SHOOTER_PERCENT_POWER_PER_RPMilli;
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
}
