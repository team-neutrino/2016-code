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
	double[] centerX;
	double[]centerY;
	double[] width;
	double[] height;

	public Robot()
	{
		cameraTable = NetworkTable.getTable("GRIP/myContoursReport");
	}

	public void robotInit()
	{
		
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
				double[] def = new double[0];
				
				double[] areas = cameraTable.getNumberArray("area", def);
				System.out.println("areas");
				for(double area: areas)
				{
					System.out.println(area);
				}
				
				def = new double[0];
				double[] centerX = cameraTable.getNumberArray("centerX", def);
				System.out.println("centerX");
				for(double centx: centerX)
				{
					System.out.println(centx);
				}
				
				def = new double[0];
				double[] centerY = cameraTable.getNumberArray("centerY", def);
				System.out.println("centerY");
				for(double centy: centerY)
				{
					System.out.println(centy);
				}
				Timer.delay(1);
				
			}
		
	}

	/**
	 * Runs during test mode
	 */
	public void test()
	{

	}
}
