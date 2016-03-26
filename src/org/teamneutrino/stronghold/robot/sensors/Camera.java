package org.teamneutrino.stronghold.robot.sensors;

import java.util.ArrayList;

import org.teamneutrino.stronghold.robot.Constants;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera implements Runnable
{
	private int hueLow;
	private int hueHigh;
	private int saturationLow;
	private int saturationHigh;
	private int luminenceLow;
	private int luminenceHigh;
	private SendableChooser outModeChooser;
	private OutputMode outMode;
	private Particle target;
	private int session;

	private Solenoid lightPower;

	private int currentFrame;

	private int frameCount;

	enum OutputMode
	{
		RAW_IMAGE, THRESHOLD_IMAGE, RECTANGLE_OVERLAY
	}

	public Camera()
	{
		hueLow = Constants.CAMERA_DEFAULT_HUE_LOW;
		hueHigh = Constants.CAMERA_DEFAULT_HUE_HIGH;
		saturationLow = Constants.CAMERA_DEFAULT_SATURATION_LOW;
		saturationHigh = Constants.CAMERA_DEFAULT_SATURATION_HIGH;
		luminenceLow = Constants.CAMERA_DEFAULT_LUMINENCE_LOW;
		luminenceHigh = Constants.CAMERA_DEFAULT_LUMINENCE_HIGH;
		try
		{
			session = NIVision.IMAQdxOpenCamera(Constants.CAMERA_NAME,
					NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);

			currentFrame = 0;

			SmartDashboard.putNumber("Hue Low", hueLow);
			SmartDashboard.putNumber("Hue High", hueHigh);
			SmartDashboard.putNumber("Saturation Low", saturationLow);
			SmartDashboard.putNumber("Saturation High", saturationHigh);
			SmartDashboard.putNumber("Luminence Low", luminenceLow);
			SmartDashboard.putNumber("Luminence High", luminenceHigh);

			outMode = OutputMode.RAW_IMAGE;

			outModeChooser = new SendableChooser();
			outModeChooser.addObject("Rectangle", OutputMode.RECTANGLE_OVERLAY);
			outModeChooser.addDefault("Raw", OutputMode.RAW_IMAGE);
			outModeChooser.addObject("Threshold", OutputMode.THRESHOLD_IMAGE);
			SmartDashboard.putData("Image Stage", outModeChooser);

			lightPower = new Solenoid(Constants.CAMERA_LIGHT_POWER_CHANNEL);
			lightPower.set(true);

//			new Thread(this).start();
//			new Thread(new SmartDashboardThread()).start();
		}
		catch (VisionException e)
		{
			DriverStation.reportError("Camera Not Found", false);
		}
	}

	public int getCurrentFrame()
	{
		return currentFrame;
	}

	public boolean targetInFrame()
	{
		return target != null;
	}

	public double getTargetX()
	{
		if (target == null)
		{
			return 0;
		}
		return target.x;
	}

	public double getTargetY()
	{
		if (target == null)
		{
			return 0;
		}
		return target.y;
	}

	public double getTargetArea()
	{
		if (target == null)
		{
			return 0;
		}
		return target.area;
	}

	public double getTargetHeight()
	{
		if (target == null)
		{
			return 0;
		}
		return target.height;
	}

	public double getTargetWidth()
	{
		if (target == null)
		{
			return 0;
		}
		return target.width;
	}

	/**
	 * @return Distance in Inches
	 */
	public double getDistance()
	{
		double goalHeightPixels = getTargetHeight();
		double distanceInches = ((12 * 1280) / (2 * goalHeightPixels * Math.tan(Math.toRadians(32.25))));
		double scaledDistance = distanceInches * (109d / 144d);
		return scaledDistance;
	}

	public double getPixelsPerDegree()
	{
		double pixelsPerDegree = (-8.46883d / 12d) * (getDistance() - 124d) - 36.009d;
		return pixelsPerDegree;
	}

	public double getOffsetDegrees()
	{
		double currOffsetPixels = getTargetY() - Constants.CAMERA_TARGET_Y;
		double currOffsetDegrees = currOffsetPixels / getPixelsPerDegree();
		return currOffsetDegrees;
	}

	@Override
	public void run()
	{
		try
		{
			long val = NIVision.IMAQdxGetAttributeMinimumI64(session, "CameraAttributes::Exposure::Value") + 5;
			NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::WhiteBalance::Mode", "Auto");
			NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::Exposure::Mode", "Manual");
			NIVision.IMAQdxSetAttributeI64(session, "CameraAttributes::Exposure::Value", val);
			NIVision.IMAQdxStartAcquisition(session);
		}
		catch (VisionException e)
		{
			DriverStation.reportError("Error setting up camera:" + e.getMessage(), false);
			e.printStackTrace();
		}

		while (true)
		{
			try
			{
				frameCount++;
				Image image = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
				Image raw = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

				NIVision.IMAQdxGrab(session, image, 1);
				NIVision.imaqDuplicate(raw, image);

				if (outMode == OutputMode.RAW_IMAGE)
				{
					CameraServer.getInstance().setImage(image);
				}
				NIVision.imaqColorThreshold(image, image, 255, NIVision.ColorMode.HSL, new Range(hueLow, hueHigh),
						new Range(saturationLow, saturationHigh), new Range(luminenceLow, luminenceHigh));
				int numParticles = NIVision.imaqCountParticles(image, 0);

				ArrayList<Particle> particles = new ArrayList<Particle>();

				if (numParticles > 0)
				{
					// Get particles and put in particle report
					for (int particleIndex = 0; particleIndex < numParticles; particleIndex++)
					{
						Particle par = new Particle();
						par.area = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
								NIVision.MeasurementType.MT_CONVEX_HULL_AREA);
						par.x = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
								NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
						par.y = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
								NIVision.MeasurementType.MT_CENTER_OF_MASS_Y);
						par.top = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
								NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
						par.left = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
								NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
						par.height = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
								NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);
						par.width = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
								NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
						particles.add(par);
					}
					currentFrame++;
				}

				Particle largestParticle = null;
				
				for (Particle particle : particles)
				{
					if (particle.area >= Constants.MIN_PARTICLE_SIZE
							&& (largestParticle == null || particle.area > largestParticle.area))
					{
						largestParticle = particle;
					}
				}
				
				target = largestParticle;

				if (outMode == OutputMode.THRESHOLD_IMAGE)
				{
					CameraServer.getInstance().setImage(image);
					System.out.println("Threashold Image");
				}

				if (outMode == OutputMode.RECTANGLE_OVERLAY)
				{
					if (largestParticle != null)
					{
						Rect rect = new Rect(largestParticle.top, largestParticle.left, largestParticle.height,
								largestParticle.width);
						NIVision.imaqDrawShapeOnImage(raw, raw, rect, DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0.0f);
						rect.free();
					}

					CameraServer.getInstance().setImage(raw);
				}

				raw.free();
				image.free();
			}
			catch (VisionException e)
			{
				DriverStation.reportError("Error getting image: " + e.getMessage(), false);
				e.printStackTrace();
			}

			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	private class SmartDashboardThread implements Runnable
	{
		@Override
		public void run()
		{
			long lastFramerateUpdate = System.currentTimeMillis();

			while (true)
			{
				long currTime = System.currentTimeMillis();

				if (currTime - lastFramerateUpdate > 5000)
				{
					double frameRate = frameCount * 1000 / ((double) (currTime - lastFramerateUpdate));
					SmartDashboard.putNumber("Framerate", frameRate);
					lastFramerateUpdate = currTime;
					frameCount = 0;
				}

				try
				{
					Thread.sleep(Constants.DRIVER_STATION_REFRESH_RATE);
				}
				catch (InterruptedException e)
				{
				}

				hueLow = (int) SmartDashboard.getNumber("Hue Low", hueLow);
				hueHigh = (int) SmartDashboard.getNumber("Hue High", hueHigh);
				saturationLow = (int) SmartDashboard.getNumber("Saturation Low", saturationLow);
				saturationHigh = (int) SmartDashboard.getNumber("Saturation High", saturationHigh);
				luminenceLow = (int) SmartDashboard.getNumber("Luminence Low", luminenceLow);
				luminenceHigh = (int) SmartDashboard.getNumber("Luminence High", luminenceHigh);
				SmartDashboard.putNumber("Hue Low", hueLow);
				SmartDashboard.putNumber("Hue High", hueHigh);
				SmartDashboard.putNumber("Saturation Low", saturationLow);
				SmartDashboard.putNumber("Saturation High", saturationHigh);
				SmartDashboard.putNumber("Luminence Low", luminenceLow);
				SmartDashboard.putNumber("Luminence High", luminenceHigh);
				SmartDashboard.putNumber("Distance From Goal", getDistance());
				SmartDashboard.putNumber("Pixels Per Degree", getPixelsPerDegree());
				SmartDashboard.putNumber("Target X", (target == null ? 0 : target.x));
				SmartDashboard.putNumber("Target Y", (target == null ? 0 : target.y));

				outMode = (OutputMode) outModeChooser.getSelected();
			}
		}
	}

	private class Particle
	{
		int y;
		int x;
		int top;
		int left;
		int width;
		int height;
		int area;
	}
}
