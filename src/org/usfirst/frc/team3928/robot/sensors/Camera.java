package org.usfirst.frc.team3928.robot.sensors;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Camera 
{
	
	NetworkTable cameraTable;

	public Camera()
	{
		cameraTable = NetworkTable.getTable("GRIP/myContoursReport");
	}
	
	
	public double getHighestHeight()
	{
		double[] height = cameraTable.getNumberArray("height", new double[0]);
		double temp = 0;
		for(double h : height)
		{
			if(h > temp)
			{
				temp = h;
			}
		}
		return temp;
	}
	
	public double getWidestWidth()
	{
		double[] width = cameraTable.getNumberArray("width", new double[0]);
		double temp = 0;
		for(double w : width)
		{
			if(w > temp)
			{
				temp = w;
			}
		}
		return temp;
	}
	
	public double getMostCenterX()
	{
		double[] centerX = cameraTable.getNumberArray("centerX", new double[0]);
		double temp = 0;
		for(double x : centerX)
		{
			if(x > temp)
			{
				temp = x;
			}
		}
		return temp;
	}
	
	public double getMostCenterY()
	{
		double[] centerY = cameraTable.getNumberArray("centerY", new double[0]);
		double temp = 0;
		for(double y : centerY)
		{
			if(y > temp)
			{
				temp = y;
			}
		}
		return temp;
	}
	
	public double getLargestArea()
	{
		double[] area = cameraTable.getNumberArray("area", new double[0]);
		double temp = 0;
		for(double a : area)
		{
			if(a > temp)
			{
				temp = a;
			}
		}
		return temp;
	}
	
}