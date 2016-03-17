package org.teamneutrino.stronghold.robot.sensors;

import org.teamneutrino.stronghold.robot.Constants;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.ControllerPower;

public class PressureSensor
{
	AnalogInput pSensor;
	
	public PressureSensor()
	{
		pSensor = new AnalogInput(Constants.PNEUMATIC_PRESSURE_SENSOR_CHANNEL);
	}
	
	public double getPressure()
	{
		double pressure = 250*(pSensor.getVoltage()/ControllerPower.getVoltage5V())-25;
		return pressure;
	}
}
