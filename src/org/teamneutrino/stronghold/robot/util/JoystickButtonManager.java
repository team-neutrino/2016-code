package org.teamneutrino.stronghold.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class JoystickButtonManager
{
	Joystick joy;
	int numButtons;

	boolean[] states;
	boolean[] changed;

	public JoystickButtonManager(Joystick joy)
	{
		this.joy = joy;

		int buttonCount = joy.getButtonCount();
		states = new boolean[buttonCount];
		changed = new boolean[buttonCount];
	}

	public void updateButtons()
	{
		for (int i = 0; i < states.length; i++)
		{
			boolean oldState = states[i];
			states[i] = joy.getRawButton(i + 1);
			changed[i] = oldState != states[i];
		}
	}

	public boolean getButtonChanged(int buttonNum)
	{
		if (buttonNum > changed.length)
		{
			DriverStation.reportError("Button [" + buttonNum + "] on Joystick not existant (unplugged?)", false);
			return false;
		}
		return changed[buttonNum - 1];
	}

	public boolean getButtonState(int buttonNum)
	{
		if (buttonNum > changed.length)
		{
			DriverStation.reportError("Button [" + buttonNum + "] on Joystick not existant (unplugged?)", false);
			return false;
		}
		return states[buttonNum - 1];
	}
}
