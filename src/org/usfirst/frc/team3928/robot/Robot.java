package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends SampleRobot
{
	NetworkTable cameraTable;
	double[] centerX;
	double[]centerY;
	double[] width;
	double[] height;
	double distance;
	boolean left;
	Victor v1;
	Victor v2;
	Victor v3;
	Victor v4;
	Victor v5;
	Victor v6;
	
	
	public Robot()
	{
		cameraTable = NetworkTable.getTable("GRIP/myContoursReport");
	}

	public void robotInit()
	{
//		v1 = new Victor(0);
//		v2 = new Victor(1);
//		v3 = new Victor(2);
//		v4 = new Victor(3);
//		v5 = new Victor(4);
//		v6 = new Victor(5);
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
				double temp = 0;
				double[] areas = cameraTable.getNumberArray("area", def);
				System.out.println("areas");
				for(int i = 1; i < areas.length - 1; ++i)
				{
					if(areas[i] > areas[i - 1])
						temp = areas[i];
				}
					
//				v1.set(.1);
//				v2.set(.1);
//				v3.set(.1);
				System.out.println("moveRight");
				Timer.delay(2);
				double temp2 = 0;
				for(int i = 1; i < areas.length - 1; ++i)
				{
					if(areas[i] > areas[i - 1])
						temp2 = areas[i];
				}
				
				if(temp > temp2)
				{
//					v1.set(0);
//					v2.set(0);
//					v3.set(0);
//					v4.set(0.1);
//					v5.set(0.1);
//					v6.set(0.1);
					System.out.println("moveLeft");
				}
				else{
					System.out.println("moveRight");
//					v4.set(0);
//					v5.set(0);
//					v6.set(0);
//					v1.set(0.1);
//					v2.set(0.1);
//					v3.set(0.1);
				}
				
				
				for(int i = 1; i < areas.length - 1; ++i)
				{
					if(areas[i] > areas[i - 1])
						temp = areas[i];
				}
					
//				v1.set(.1);
//				v2.set(.1);
//				v3.set(.1);
				System.out.println("moveBackward");
				Timer.delay(2);
				System.out.println("moveBackward");
				for(int i = 1; i < areas.length - 1; ++i)
				{
					if(areas[i] > areas[i - 1])
						temp2 = areas[i];
				}
				
				if(temp > temp2)
				{
//					v1.set(0);
//					v2.set(0);
//					v3.set(0);
//					v4.set(0.1);
//					v5.set(0.1);
//					v6.set(0.1);
					System.out.println("moveForward");
				}
				else{
					System.out.println("moveBackward");
//					v4.set(0);
//					v5.set(0);
//					v6.set(0);
//					v1.set(0.1);
//					v2.set(0.1);
//					v3.set(0.1);
				}
					
				
//				def = new double[0];
//				double[] centerX = cameraTable.getNumberArray("centerX", def);
//				System.out.println("centerX");
//				for(double centx: centerX)
//				{
//					System.out.println(centx);
//				}
//				
//				def = new double[0];
//				double[] centerY = cameraTable.getNumberArray("centerY", def);
//				System.out.println("centerY");
//				for(double centy: centerY)
//				{
//					System.out.println(centy);
//				}
//				
//				def = new double[0];
//				double[] width = cameraTable.getNumberArray("width", def);
//				System.out.println("width");
//				for(double wid: width)
//				{
//					System.out.println(wid);
//					distance = wid;
//				}
//				
//				def = new double[0];
//				double[] height = cameraTable.getNumberArray("height", def);
//				System.out.println("height");
//				for(double high: height)
//				{
//					System.out.println(high);
//					distance = high;
//				}
//				Timer.delay(1);
			}
	}

	/**
	 * Runs during test mode
	 */
	public void test()
	{

	}
}
