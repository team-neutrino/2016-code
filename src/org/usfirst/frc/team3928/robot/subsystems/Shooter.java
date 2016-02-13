package org.usfirst.frc.team3928.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Shooter implements Runnable
{
	private SpeedController motorLeft;
	private SpeedController motorRight;

	private Counter beambreakLeft;
	private Counter beambreakRight;

	private boolean on;

	private Thread shooterSpeedThread;

	private static final int MILISECONDS_PER_MINUTE = 60000;
	private static final int CORRECTION_RPM = 2000;

	public Shooter()
	{
		motorLeft = new Talon(Constants.SHOOTER_MOTOR_LEFT);
		motorRight = new Talon(Constants.SHOOTER_MOTOR_RIGHT);
		motorLeft.setInverted(true);
		beambreakLeft = new Counter(Constants.SHOOTER_BEAMBREAKE_RIGHT_CHANNEL);
		beambreakRight = new Counter(Constants.SHOOTER_BEAMBREAKE_LEFT_CHANNEL);
		on = false;

		shooterSpeedThread = new Thread(this);
	}

	public void start()
	{
		if (!on)
		{
			shooterSpeedThread.start();
			on = true;
		}
	}

	public void stop()
	{
		on = false;
	}

	public boolean isOn()
	{
		return on;
	}

	public void shooterTest()
	{

	}

	public void run()
	{
		String printout = "currTime, timeInterval, countLeft, countRight, RPMiliTarget, RPMilliLeft, RPMilliRight, "
				+ "RPMilliMin, integral, error, targetPower, leftCorrection, rightCorrection\n";

		long lastResetTime = System.currentTimeMillis();

		double RPMiliTarget = ((double) Constants.SHOOTER_RPM) / MILISECONDS_PER_MINUTE;

		double integral = 0;

		beambreakLeft.reset();
		beambreakRight.reset();

		motorLeft.set(RPMiliToPower(RPMiliTarget));
		motorRight.set(RPMiliToPower(RPMiliTarget));

		while (on)
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

			motorLeft.set(targetPower * leftCorrection);
			motorRight.set(targetPower * rightCorrection);

			printout += currTime + " ," + timeInterval + " ," + countLeft + " ," + countRight + " ," + RPMiliTarget
					+ " ," + RPMilliLeft + " ," + RPMilliRight + " ," + RPMilliMin + " ," + integral + " ," + error
					+ " ," + targetPower + " ," + leftCorrection + " ," + rightCorrection + "\n";
		}

		writeFile("/home/lvuser/shooterRun.csv", printout);

		motorLeft.set(0);
		motorRight.set(0);
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
		motorLeft.set(0);
		motorRight.set(0);

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
