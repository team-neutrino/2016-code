package org.usfirst.frc.team3928.robot.sensors;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera implements Runnable
{
	USBCamera cam;
	Image i;
	
	public Camera()
	{
		cam = new USBCamera("cam1");
		cam.startCapture();
		i = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		
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
		while (true)
		{
			cam.getImage(i);

			CameraServer.getInstance().setImage(i);
			
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}