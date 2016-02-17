package org.teamneutrino.stronghold.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
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
	private DigitalInput limitSwitchFront;
	private DigitalInput limitSwitchBack;
	private Solenoid flippers;

	private PIDController actuationPID;

	private boolean running;
	private boolean isSet;
	private boolean reverse;
	private boolean flippersActive;

	private Thread shooterSpeedThread;

	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final int CORRECTION_RPM = 2000;

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
		leftMotor.setInverted(true);

		flippers = new Solenoid(Constants.SHOOTER_SOLENOID_CHANNEL);

		beambreakLeft = new Counter(Constants.SHOOTER_BEAMBREAK_RIGHT_CHANNEL);
		beambreakRight = new Counter(Constants.SHOOTER_BEAMBREAK_LEFT_CHANNEL);
		
		limitSwitchFront = new DigitalInput(Constants.SHOOTER_LIMITSWITCH_FRONT_CHANNEL);
		limitSwitchBack = new DigitalInput(Constants.SHOOTER_LIMITSWITCH_BACK_CHANNEL);

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
		// TODO
		leftMotor.set(1);
		rightMotor.set(1);
//		reverse = false;
//
//		if (!running)
//		{
//			shooterSpeedThread.start();
//			running = true;
//		}
	}

	public void reverse()
	{
		// TODO
		leftMotor.set(-1);
		rightMotor.set(-1);
//		reverse = true;
//
//		if (!running)
//		{
//			shooterSpeedThread.start();
//			running = true;
//		}
	}

	public void stop()
	{
		// TODO
		leftMotor.set(0);
		rightMotor.set(0);
//		running = false;
	}

	public boolean isRunning()
	{
		return running;
	}

	public boolean isSet()
	{
		return isSet;
	}
	
	public void setAcutatorOverride(double speed)
	{
		actuationPID.disable();
		actuatorMotor.set(speed);
	}

	public void setAngle(double angle)
	{
		actuationPID.setSetpoint(angle);
		actuationPID.enable();
		
		// TODO put limit switch in thread
	}

	public void setFlipper(boolean on)
	{
		flippers.set(on);
		flippersActive = on;
	}

	public boolean isFlipperActive()
	{
		return flippersActive;
	}

	@Override
	public void run()
	{
		String printout = "currTime, timeInterval, countLeft, countRight, RPMiliTarget, RPMilliLeft, RPMilliRight, "
				+ "RPMilliMin, integral, error, targetPower, leftCorrection, rightCorrection\n";

		long lastResetTime = System.currentTimeMillis();

		double RPMilliTarget = ((double) Constants.SHOOTER_RPM) / MILLISECONDS_PER_MINUTE;

		double integral = 0;

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
			int countRight = beambreakRight.get();
			beambreakLeft.reset();
			beambreakRight.reset();

			double RPMilliLeft = (((double) countLeft) / timeInterval);
			double RPMilliRight = (((double) countRight) / timeInterval);

			double RPMilliMin = Math.min(RPMilliLeft, RPMilliRight);

			// TODO Remove magic numbers
			if ((RPMilliMin < RPMilliTarget * 1.05) && (RPMilliMin > (RPMilliTarget - (RPMilliTarget * .05))))
			{
				isSet = true;
				// TODO Printouts should only be used for testing, remove
				System.out.println("Shooter is set!");
			}
			else
			{
				isSet = false;
			}

			double inegral = integral + RPMilliMin * timeInterval;
			double error = RPMilliTarget - RPMilliMin;

			double targetPower = RPMiliToPower(RPMilliTarget) + error * Constants.SHOOTER_K_P
					+ inegral * Constants.SHOOTER_K_I;

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

			printout += currTime + " ," + timeInterval + " ," + countLeft + " ," + countRight + " ," + RPMilliTarget
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
}
