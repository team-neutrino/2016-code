package org.usfirst.frc.team3928.robot.sensors;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera implements Runnable
{
	int session;
	Image raw;

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
		raw = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	    session = NIVision.IMAQdxOpenCamera("cam1", 
	    		NIVision.IMAQdxCameraControlMode.CameraControlModeController);
	    NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::WhiteBalance::Mode", "Auto");
	    NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::Exposure::Mode", "AutoAperaturePriority");
	    NIVision.IMAQdxConfigureGrab(session);
	    NIVision.IMAQdxStartAcquisition(session);
	    while(true)
	    {
	    	NIVision.IMAQdxGrab(session, raw, 1);
	        //NIVision.imaqColorThreshold(raw, raw, 255, NIVision.ColorMode.HSL,
	          //      new Range(71, 102), new Range(167, 255), new Range(115, 255));
	        CameraServer.getInstance().setImage(raw);
	        System.out.println("lel" + session);
	    }
	}

}