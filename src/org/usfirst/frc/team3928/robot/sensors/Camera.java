package org.usfirst.frc.team3928.robot.sensors;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera implements Runnable
{
	public Camera()
	{
		new Thread(this).start();
	}

	public double getHighestHeight()
	{
		// TODO
		return 0.0;
	}

	public double getWidestWidth()
	{
		// TODO
		return 0.0;
	}

	public double getMostCenterX()
	{
		// TODO
		return 0.0;
	}

	public double getMostCenterY()
	{
		// TODO
		return 0.0;
	}

	public double getLargestArea()
	{
		// TODO
		return 0.0;
	}

	public boolean isAimed()
	{
		// TODO
		return false;
	}

	@Override
	public void run()
	{
		int hueLow = 71;
		int hueHigh = 102;
		int saturationLow = 167;
		int saturationHigh = 255;
		int luminenceLow = 155;
		int luminenceHigh = 251;
		
		BinaryImage img;
		
		USBCamera cam = new USBCamera("cam1");
		cam.startCapture();

		HSLImage camImage;

		try
		{
			camImage = new HSLImage();
		}
		catch (NIVisionException e)
		{
			camImage = null;
			e.printStackTrace();
		}

		while (true)
		{
			cam.getImage(camImage.image);
			
			CameraServer.getInstance().setImage(camImage.image);
			
			try
			{
				img = camImage.thresholdHSL(hueLow, hueHigh, saturationLow, saturationHigh, luminenceLow, luminenceHigh);
			} catch (NIVisionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Thread.yield();
		}
	}
}