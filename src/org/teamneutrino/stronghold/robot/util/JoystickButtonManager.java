package org.teamneutrino.stronghold.robot.util;

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
			states[i] = joy.getRawButton(i);
			changed[i] = oldState != states[i];
		}
	}

	public boolean getButtonChanged(int buttonNum)
	{
		return changed[buttonNum];
	}

	public boolean getButtonState(int buttonNum)
	{
		return states[buttonNum];
	}
}
