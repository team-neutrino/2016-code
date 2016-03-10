package org.teamneutrino.stronghold.robot.util;

public class Util
{
	public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin,
			final double limitMax)
	{
		return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
	}
}
