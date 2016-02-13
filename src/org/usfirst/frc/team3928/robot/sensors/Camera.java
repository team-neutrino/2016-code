package org.usfirst.frc.team3928.robot.sensors;

import java.util.concurrent.atomic.AtomicInteger;

import org.usfirst.frc.team3928.robot.Constants;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera implements Runnable
{
	private AtomicInteger hueLow;
	private AtomicInteger hueHigh;
	private AtomicInteger saturationLow;
	private AtomicInteger saturationHigh;
	private AtomicInteger luminenceLow;
	private AtomicInteger luminenceHigh;
	private SendableChooser outModeChooser;
	private OutputMode outMode;

	enum OutputMode
	{
		RAW_IMAGE, THRESHOLD_IMAGE, CONVEX_HULL, RECTANGLE_OVERLAY
	}

	public Camera()
	{
		hueLow = new AtomicInteger(Constants.DEFAULT_HUE_LOW);
		hueHigh = new AtomicInteger(Constants.DEFAULT_HUE_HIGH);
		saturationLow = new AtomicInteger(Constants.DEFAULT_SATURATION_LOW);
		saturationHigh = new AtomicInteger(Constants.DEFAULT_SATURATION_HIGH);
		luminenceLow = new AtomicInteger(Constants.DEFAULT_LUMINENCE_LOW);
		luminenceHigh = new AtomicInteger(Constants.DEFAULT_LUMINENCE_HIGH);

		SmartDashboard.putNumber("Hue Low", hueLow.get());
		SmartDashboard.putNumber("Hue High", hueHigh.get());
		SmartDashboard.putNumber("Saturation Low", saturationLow.get());
		SmartDashboard.putNumber("Saturation High", saturationHigh.get());
		SmartDashboard.putNumber("Luminence Low", luminenceLow.get());
		SmartDashboard.putNumber("Luminence High", luminenceHigh.get());

		outMode = OutputMode.RAW_IMAGE;

		outModeChooser = new SendableChooser();
		outModeChooser.addDefault("Raw", OutputMode.RAW_IMAGE);
		outModeChooser.addObject("Threshold", OutputMode.THRESHOLD_IMAGE);
		outModeChooser.addObject("Convex Hull", OutputMode.CONVEX_HULL);
		outModeChooser.addObject("Rectangle", OutputMode.RECTANGLE_OVERLAY);
		SmartDashboard.putData("Image Stage", outModeChooser);

		new Thread(this).start();
		new Thread(new SmartDashboardThread()).start();
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
		int session;
		Image raw;
		Image outpt;

		raw = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		outpt = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		session = NIVision.IMAQdxOpenCamera(Constants.CAMERA_NAME,
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		long val = NIVision.IMAQdxGetAttributeMinimumI64(session, "CameraAttributes::Exposure::Value");
		NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::WhiteBalance::Mode", "Auto");
		NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::Exposure::Mode", "Manual");
		NIVision.IMAQdxSetAttributeI64(session, "CameraAttributes::Exposure::Value", val);
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxStartAcquisition(session);
		while (true)
		{
			NIVision.IMAQdxGrab(session, raw, 1);

			if (outMode == OutputMode.RAW_IMAGE)
			{
				CameraServer.getInstance().setImage(raw);
			}
			NIVision.imaqColorThreshold(raw, raw, 255, NIVision.ColorMode.HSL, new Range(hueLow.get(), hueHigh.get()),
					new Range(saturationLow.get(), saturationHigh.get()),
					new Range(luminenceLow.get(), luminenceHigh.get()));
			if (outMode == OutputMode.THRESHOLD_IMAGE)
			{
				CameraServer.getInstance().setImage(raw);
			}
//			NIVision.imaqSizeFilter(raw, raw, 1, 3, NIVision.SizeType.KEEP_LARGE,
//					new NIVision.StructuringElement(3, 3, 0));
			if (outMode == OutputMode.CONVEX_HULL)
			{
				CameraServer.getInstance().setImage(raw);
			}
			if (outMode == OutputMode.RECTANGLE_OVERLAY)
			{
				CameraServer.getInstance().setImage(raw);
			}
		}
	}

	private class SmartDashboardThread implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(Constants.DRIVER_STATION_REFRESH_RATE);
				} catch (InterruptedException e)
				{
				}

				hueLow.set((int) SmartDashboard.getNumber("Hue Low", hueLow.get()));
				hueHigh.set((int) SmartDashboard.getNumber("Hue High", hueHigh.get()));
				saturationLow.set((int) SmartDashboard.getNumber("Saturation Low", saturationLow.get()));
				saturationHigh.set((int) SmartDashboard.getNumber("Saturation High", saturationHigh.get()));
				luminenceLow.set((int) SmartDashboard.getNumber("Luminence Low", luminenceLow.get()));
				luminenceHigh.set((int) SmartDashboard.getNumber("Luninence High", luminenceHigh.get()));

				outMode = (OutputMode) outModeChooser.getSelected();
			}
		}
	}
}