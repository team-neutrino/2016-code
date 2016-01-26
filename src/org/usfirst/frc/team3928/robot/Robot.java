package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;
import com.ni.vision.VisionException;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.vision.AxisCamera;


public class Robot extends SampleRobot
{
	int session;
	Image frame;
	CameraServer server;
	AxisCamera cam;

	public Robot()
	{
		cam = new AxisCamera("10.39.28.29");
	}

	public void robotInit()
	{
		try
		{
			
			server = CameraServer.getInstance();
			server.setQuality(100);
			cam.getImage(frame);
			
		} catch (Exception E)
		{
			System.out.println("Camera?");
		}

	}

	public void disabled()
	{
		while(is)
	}

	public void autonomous()
	{

	}

	public void operatorControl()
	{
		try
		{
			
			while (isOperatorControl() && isEnabled())
			{
				
				cam.getImage(frame);
				server.setImage(frame);
				
			}
			NIVision.IMAQdxStopAcquisition(session);
		} catch (Exception E)
		{
			System.out.println("Camera?");
		}
	}

	/**
	 * Runs during test mode
	 */
	public void test()
	{

	}
}
