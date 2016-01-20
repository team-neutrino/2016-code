package org.usfirst.frc.team3928.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.wpi.first.wpilibj.DriverStation;

public class Constants
{
	private static final ArrayList<Constant> constList = new ArrayList<Constant>();
	
	// Autonomous Constants
	
	// Controllers
	public static final Constant JOY_LEFT = new Constant("Left Joystick", 1);
	public static final Constant JOY_RIGHT = new Constant("Right Joystick", 2);
	public static final Constant GAMEPAD = new Constant("Gamepad", 3);
	// Button Mapping - Joy sticks

	// Button Mapping - Game pad
	
	// Drive Constants
	public static final Constant DRIVE_LEFT_1_CHANNEL = new Constant("DriveLeft1Channel", 1);
	public static final Constant DRIVE_LEFT_1_POWER_CHANNEL = new Constant("DriveLeft1PowerChannel", 2);
	public static final Constant DRIVE_LEFT_2_CHANNEL = new Constant("DriveLeft2Channel", 3);
	public static final Constant DRIVE_LEFT_2_POWER_CHANNEL = new Constant("DriveLeft2Powex≥≥hannel", 4);
	public static final Constant DRIVE_RIGHT_1_CHANNEL = new Constant("DriveRight1Channel", 5);
	public static final Constant DRIVE_RIGHT_1_POWER_CHANNEL = new Constant("DriveRight1PowerChannel", 6);
	public static final Constant DRIVE_RIGHT_2_CHANNEL = new Constant("DriveRight2Channel", 7);
	public static final Constant DRIVE_RIGHT_2_POWER_CHANNEL = new Constant("DriveRight2PowerChannel", 8);

	
	// Intake Constants
	

	// Shooter Constants
	
	private static final String CONSTANTS_FILE_PATH = "/home/lvuser/constants";

	static
	{
		// read constant overrides
		try
		{
			Scanner in = new Scanner(new File(CONSTANTS_FILE_PATH));
			while (in.hasNext())
			{
				Scanner line = new Scanner(in.nextLine());
				line.useDelimiter(":");

				if (line.hasNext())
				{
					String name = line.next();
					if (line.hasNextDouble())
					{
						double value = line.nextDouble();
						overrideConst(name, value);
					} else if (line.hasNextBoolean())
					{
						boolean value = line.nextBoolean();
						overrideConst(name, value);
					}
				}

				line.close();
			}

			in.close();
		} catch (FileNotFoundException e)
		{
			DriverStation.reportError("No Constants File Found", true);
		}
	}

	private static void overrideConst(String name, double value)
	{
		Constant constant = getConstByName(name);

		if (constant == null)
		{
			DriverStation.reportError("Can not override not existent constant [" + name
					+ "]", true);
			return;
		}

		constant.updateValue(value);
		DriverStation.reportError("Overriding constant [" + name + "] with [" + value
				+ "]", true);
	}

	private static void overrideConst(String name, boolean value)
	{
		Constant constant = getConstByName(name);

		if (constant == null)
		{
			DriverStation.reportError("Can not override not existent constant [" + name
					+ "]", true);
			return;
		}

		constant.updateValue(value);
		DriverStation.reportError(("Overriding constant [" + name + "] with [" + value
				+ "]"), true);
	}

	private static Constant getConstByName(String name)
	{
		for (Constant constant : constList)
		{
			if (constant.getName().equals(name))
			{
				return constant;
			}
		}

		return null;
	}

	public static class Constant
	{

		private String name;

		private double value;

		public Constant(String name, double value)
		{
			this.name = name;
			this.value = value;
			constList.add(this);
		}

		public Constant(String name, boolean value)
		{
			this.name = name;
			this.value = value ? 1 : 0;
		}

		public void updateValue(double value)
		{
			this.value = value;
		}

		public void updateValue(boolean value)
		{
			this.value = value ? 1 : 0;
		}

		public String getName()
		{
			return name;
		}

		public int getInt()
		{
			return (int) value;
		}

		public double getDouble()
		{
			return value;
		}

		public float getFloat()
		{
			return (float) value;
		}

		public boolean getBoolean()
		{
			if(value == 1)
				return true;
			else if(value == 0)
				return false;
			else
			{
				DriverStation.reportError("Not a boolean.", true);
				return false;
			}
		}
	}
}
