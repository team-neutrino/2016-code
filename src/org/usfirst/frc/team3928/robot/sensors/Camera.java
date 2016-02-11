package org.usfirst.frc.team3928.robot.sensors;

import java.util.concurrent.atomic.AtomicInteger;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera implements Runnable
{
	private AtomicInteger hueLow;
	private AtomicInteger hueHigh;
	private AtomicInteger saturationLow;
	private AtomicInteger saturationHigh;
	private AtomicInteger luminenceLow;
	private AtomicInteger luminenceHigh;
	private boolean autoExposure;
	private AtomicInteger exposure;

	private USBCamera cam;

	public Camera()
	{
		hueLow = new AtomicInteger(Constants.DEFAULT_HUE_LOW);
		hueHigh = new AtomicInteger(Constants.DEFAULT_HUE_HIGH);
		saturationLow = new AtomicInteger(Constants.DEFAULT_SATURATION_LOW);
		saturationHigh = new AtomicInteger(Constants.DEFAULT_SATURATION_HIGH);
		luminenceLow = new AtomicInteger(Constants.DEFAULT_LUMINENCE_LOW);
		luminenceHigh = new AtomicInteger(Constants.DEFAULT_LUMINENCE_HIGH);
		autoExposure = Constants.DEFAULT_AUTO_EXPOSURE;
		exposure = new AtomicInteger(autoExposure ? 0 : Constants.DEFAULT_EXPOSURE);

		cam = new USBCamera(Constants.CAMERA_NAME);
		updateCameraExposure();

		SmartDashboard.putNumber("Hue Low", hueLow.get());
		SmartDashboard.putNumber("Hue High", hueHigh.get());
		SmartDashboard.putNumber("Saturation Low", saturationLow.get());
		SmartDashboard.putNumber("Saturation High", saturationHigh.get());
		SmartDashboard.putNumber("Luminence Low", luminenceLow.get());
		SmartDashboard.putNumber("Luminence High", luminenceHigh.get());
		SmartDashboard.putBoolean("Auto Exposure", autoExposure);
		SmartDashboard.putNumber("Exposure", exposure.get());

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
		BinaryImage img;

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
				img = camImage.thresholdHSL(hueLow.get(), hueHigh.get(), saturationLow.get(), saturationHigh.get(),
						luminenceLow.get(), luminenceHigh.get());
			}
			catch (NIVisionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Thread.yield();
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

				boolean smartdashboardAutoExposure = SmartDashboard.getBoolean("Auto Exposure", autoExposure);
				
				if (smartdashboardAutoExposure)
				{
					SmartDashboard.putNumber("Exposure", 0);
					if (!autoExposure)
					{
						autoExposure = true;
						updateCameraExposure();
					}
				} else
				{
					int smartdashboardExposure = (int) SmartDashboard.getNumber("Exposure", exposure.get());
					if (smartdashboardExposure != exposure.get())
					{
						autoExposure = false;
						exposure.set(smartdashboardExposure);
						updateCameraExposure();
					}
				}
			}
		}
	}

	private void updateCameraExposure()
	{
		if (autoExposure)
		{
			cam.setExposureAuto();
		}
		else
		{
			cam.setExposureManual(0);
		}
	}
}