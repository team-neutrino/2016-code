package org.teamneutrino.stronghold.robot.util;

import java.util.ArrayList;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CurrentMonitor
{
	private PowerDistributionPanel pdp;
	private Compressor pcm;

	private ArrayList<SubsystemPower> subsystems;

	public CurrentMonitor()
	{
		subsystems = new ArrayList<SubsystemPower>();

		pdp = new PowerDistributionPanel();
		pcm = new Compressor();

		SubsystemPower drive = new SubsystemPower("Drive");
		drive.addChannel(new PowerChannel("Left 1", Constants.DRIVE_LEFT_1_POWER_CHANNEL));
		drive.addChannel(new PowerChannel("Left 2", Constants.DRIVE_LEFT_2_POWER_CHANNEL));
		drive.addChannel(new PowerChannel("Left 3", Constants.DRIVE_LEFT_3_POWER_CHANNEL));
		drive.addChannel(new PowerChannel("Right 1", Constants.DRIVE_RIGHT_1_POWER_CHANNEL));
		drive.addChannel(new PowerChannel("Right 2", Constants.DRIVE_RIGHT_2_POWER_CHANNEL));
		drive.addChannel(new PowerChannel("Right 3", Constants.DRIVE_RIGHT_3_POWER_CHANNEL));
		subsystems.add(drive);

		SubsystemPower shooter = new SubsystemPower("Shooter");
		shooter.addChannel(new PowerChannel("Left", Constants.SHOOTER_LEFT_MOTOR_POWER_CHANNEL));
		shooter.addChannel(new PowerChannel("Right", Constants.SHOOTER_RIGHT_MOTOR_POWER_CHANNEL));
		shooter.addChannel(new PowerChannel("Actuator", Constants.SHOOTER_ACTUATOR_MOTOR_POWER_CHANNEL));
		subsystems.add(shooter);

		SubsystemPower intake = new SubsystemPower("Intake");
		intake.addChannel(new PowerChannel("Side to Side", Constants.INTAKE_SIDE_TO_SIDE_MOTOR_POWER_CHANNEL));
		intake.addChannel(new PowerChannel("Front to Back", Constants.INTAKE_FRONT_TO_BACK_MOTOR_POWER_CHANNEL));
		intake.addChannel(new PowerChannel("Actuator", Constants.INTAKE_ACUATOR_MOTOR_POWER_CHANNEL));
		subsystems.add(intake);
	}

	public void send()
	{
		try
		{
			Thread.sleep(Constants.DRIVER_STATION_REFRESH_RATE);
		}
		catch (InterruptedException e)
		{
		}

		// Subsystems
		for (SubsystemPower subsystem : subsystems)
		{
			outputSubsystem(subsystem);
		}

		// PCM
		SmartDashboard.putNumber("Compressor Current", pcm.getCompressorCurrent());

		// Overall Current
		double totalCurrent = pdp.getTotalCurrent() + pcm.getCompressorCurrent();
		SmartDashboard.putNumber("Total Current", totalCurrent);
		SmartDashboard.putBoolean("Current Over 120", totalCurrent > 120);
	}

	public boolean getCurrentOver120()
	{
		return pdp.getTotalCurrent() + pcm.getCompressorCurrent() > 120;
	}

	private void outputSubsystem(SubsystemPower subsystem)
	{
		ArrayList<PowerChannel> channels = subsystem.getChannels();
		if (channels.size() == 0)
		{
			DriverStation.reportError("Empty Subsystem [" + subsystem.getName() + "]", false);
		}
		else if (channels.size() == 1)
		{
			SmartDashboard.putNumber(subsystem.getName() + " Current", pdp.getCurrent(channels.get(0).getChannel()));
		}
		else
		{
			double totalCurrent = 0;

			for (PowerChannel channel : channels)
			{
				double current = pdp.getCurrent(channel.getChannel());
				totalCurrent += current;
				SmartDashboard.putNumber(subsystem.getName() + " - " + channel.getName() + " Current", current);
			}

			SmartDashboard.putNumber(subsystem.getName() + " Current", totalCurrent);
		}
	}
	
	private class SubsystemPower
	{
		private String name;
		private ArrayList<PowerChannel> channels;

		public SubsystemPower(String name)
		{
			this.name = name;

			channels = new ArrayList<PowerChannel>();
		}

		public void addChannel(PowerChannel channel)
		{
			channels.add(channel);
		}

		public String getName()
		{
			return name;
		}

		public ArrayList<PowerChannel> getChannels()
		{
			return channels;
		}
	}

	private class PowerChannel
	{
		private String name;
		private int channel;

		public PowerChannel(String name, int channel)
		{
			this.name = name;
			this.channel = channel;
		}

		public String getName()
		{
			return name;
		}

		public int getChannel()
		{
			return channel;
		}
	}
}
