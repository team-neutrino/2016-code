package org.teamneutrino.stronghold.robot.sensors;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Created for the A7D Series Thumbwheel Switch from Omron Electronics Inc.
 * Reads a binary coded decimal from four digital inputs.
 */
public class ThumbwheelSwitch
{
	private DigitalInput bit1;
	private DigitalInput bit2;
	private DigitalInput bit4;
	private DigitalInput bit8;

	/**
	 * Create a new thumbwheel switch with Digital Inputs channels given in constants
	 * constants
	 */
	public ThumbwheelSwitch()
	{
		bit1 = new DigitalInput(Constants.THUMBWHEEL_SWITCH_BIT_1_CHANNEL);
		bit2 = new DigitalInput(Constants.THUMBWHEEL_SWITCH_BIT_2_CHANNEL);
		bit4 = new DigitalInput(Constants.THUMBWHEEL_SWITCH_BIT_4_CHANNEL);
		bit8 = new DigitalInput(Constants.THUMBWHEEL_SWITCH_BIT_8_CHANNEL);
	}

	/**
	 * Read the value of the the thumbwheel switch
	 * 
	 * @return the value of the thubwheel switch
	 */
	public int get()
	{
		return (bit1.get() ? 0 : 1) + (bit2.get() ? 0 : 2) + (bit4.get() ? 0 : 4) + (bit8.get() ? 0 : 8);
	}
}
