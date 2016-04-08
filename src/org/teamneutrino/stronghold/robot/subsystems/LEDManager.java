package org.teamneutrino.stronghold.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class LEDManager
{
	private NetworkTable ledTable;

	private boolean disabled;
	private boolean autoMode;
	private boolean overCurrent;
	private boolean shooterAimed;
	private boolean shooterSpunUp;

	private static final String TABLE_NAME = "LEDs";

	private static final Color ORANGE = new Color(255, 40, 0);
	private static final Color WHITE = new Color(255, 255, 255);
	private static final Color RED = new Color(255, 0, 0);
	private static final Color GREEN = new Color(0, 255, 0);

	public LEDManager()
	{
		ledTable = NetworkTable.getTable(TABLE_NAME);
		disabled = false;
		autoMode = false;
		overCurrent = false;
		shooterAimed = false;
		shooterSpunUp = false;
	}

	public void setDisabled(boolean disabled)
	{
		boolean needsUpdate = this.disabled != disabled;
		this.disabled = disabled;

		if (needsUpdate)
		{
			updateOutput();
		}
	}

	public void setAutoMode(boolean autoMode)
	{
		boolean needsUpdate = this.autoMode != autoMode;
		this.autoMode = autoMode;
		
		if (needsUpdate)
		{
			updateOutput();
		}
	}

	public void setOverCurrent(boolean overCurrent)
	{
		boolean needsUpdate = this.overCurrent != overCurrent;
		this.overCurrent = overCurrent;
		
		if (needsUpdate)
		{
			updateOutput();
		}
	}

	public void setShooterAimed(boolean shooterAimed)
	{
		boolean needsUpdate = this.shooterAimed != shooterAimed;
		this.shooterAimed = shooterAimed;
		
		if (needsUpdate)
		{
			updateOutput();
		}
	}

	public void setShooterSpunUp(boolean shooterSpunUp)
	{
		boolean needsUpdate = this.shooterSpunUp != shooterSpunUp;
		this.shooterSpunUp = shooterSpunUp;
		
		if (needsUpdate)
		{
			updateOutput();
		}
	}

	public void setCommand(String command)
	{
		ledTable.putString("command", command);
	}

	public void setRGB1(Color color)
	{
		ledTable.putNumber("r1", color.red);
		ledTable.putNumber("g1", color.green);
		ledTable.putNumber("b1", color.blue);
	}

	public void setRGB2(Color color)
	{
		ledTable.putNumber("r1", color.red);
		ledTable.putNumber("g1", color.green);
		ledTable.putNumber("b1", color.blue);
	}

	public void sendCommand()
	{
		ledTable.putNumber("commandNum", ledTable.getNumber("commandNum", 0) + 1);
	}

	private void updateOutput()
	{
		if (disabled)
		{
			if (DriverStation.getInstance().getAlliance() == Alliance.Blue)
			{
				setCommand("blueSolid");
			}
			else
			{
				setCommand("redSolid");
			}
		}
		else
		{
			Color baseColor = ORANGE;

			if (autoMode)
			{
				baseColor = GREEN;
			}

			if (overCurrent)
			{
				baseColor = RED;
			}

			if (shooterAimed && shooterSpunUp)
			{
				setCommand("whiteSolid");
			}
			else if (shooterSpunUp)
			{
				setRGB1(WHITE);
				setRGB2(baseColor);
				setCommand("2c");
			}
			else if (shooterAimed)
			{
				setRGB1(baseColor);
				setRGB2(WHITE);
				setCommand("2c");
			}
			else
			{
				setRGB1(baseColor);
				setCommand("c");
			}
		}

		sendCommand();
	}

	public static class Color
	{
		int red;
		int green;
		int blue;

		public Color(int red, int green, int blue)
		{
			this.red = red;
			this.green = green;
			this.blue = blue;
		}
	}
}
