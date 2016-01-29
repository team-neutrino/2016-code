package org.usfirst.frc.team3928.robot;

import java.io.IOException;

import com.ni.vision.NIVision;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class Robot extends SampleRobot
{
	NetworkTable cameraTable;

	public Robot()
	{
		cameraTable = NetworkTable.getTable("GRIP/myContoursReport");
	}

	public void robotInit()
	{
		double[] def = new double[0];
		while(true)
		{
			double[] areas = cameraTable.getNumberArray("area", def);
			System.out.println("areas");
			for(double area: areas)
			{
				System.out.println(area);
			}
			Timer.delay(1);
		}
	}

	public void disabled()
	{
		
	}

	public void autonomous()
	{

	}

	public void operatorControl()
	{
		
			
			while (isOperatorControl() && isEnabled())
			{

			}
		
	}

	/**
	 * Runs during test mode
	 */
	public void test()
	{

	}
}
