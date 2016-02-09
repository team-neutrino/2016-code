package org.usfirst.frc.team3928.robot.sensors;

import java.io.IOException;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Camera 
{
	
	NetworkTable cameraTable;

	public Camera()
	{
		cameraTable = NetworkTable.getTable("GRIP/myContoursReport");
		try {
            new ProcessBuilder("/home/lvuser/grip").inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
	public boolean isAimed()
	{
		boolean aimed = false;
		double height = getHighestHeight();
		double width = getWidestWidth();
		double area = getLargestArea();
		double centerX = getMostCenterX();
		double centerY = getMostCenterY();
		double imageCenterX = Constants.IMAGE_CENTER_X;
		double imageCenterY = Constants.IMAGE_CENTER_Y;
		
		double heightWidthRatio = height/width;
		
		
		
		return aimed;
	}
}