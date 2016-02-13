package org.usfirst.frc.team3928.robot.sensors;

import java.util.concurrent.atomic.AtomicInteger;

import org.usfirst.frc.team3928.robot.Constants;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera implements Runnable
{
	private AtomicInteger hueLow;
	private AtomicInteger hueHigh;
	private AtomicInteger saturationLow;
	private AtomicInteger saturationHigh;
	private AtomicInteger luminenceLow;
	private AtomicInteger luminenceHigh;

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

		raw = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		session = NIVision.IMAQdxOpenCamera(Constants.CAMERA_NAME,
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::WhiteBalance::Mode", "Auto");
		NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::Exposure::Mode", "AutoAperaturePriority");
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxStartAcquisition(session);
		while (true)
		{
			NIVision.IMAQdxGrab(session, raw, 1);
//			NIVision.imaqColorThreshold(raw, raw, 255, NIVision.ColorMode.HSL, new Range(hueLow.get(), hueHigh.get()),
//					new Range(saturationLow.get(), saturationHigh.get()),
//					new Range(luminenceLow.get(), luminenceHigh.get()));
			CameraServer.getInstance().setImage(raw);
			System.out.println("lel" + session);
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
				}
				catch (InterruptedException e)
				{
				}

				hueLow.set((int) SmartDashboard.getNumber("Hue Low", hueLow.get()));
				hueHigh.set((int) SmartDashboard.getNumber("Hue High", hueHigh.get()));
				saturationLow.set((int) SmartDashboard.getNumber("Saturation Low", saturationLow.get()));
				saturationHigh.set((int) SmartDashboard.getNumber("Saturation High", saturationHigh.get()));
				luminenceLow.set((int) SmartDashboard.getNumber("Luminence Low", luminenceLow.get()));
				luminenceHigh.set((int) SmartDashboard.getNumber("Luninence High", luminenceHigh.get()));
			}
		}
	}
}