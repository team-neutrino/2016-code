package org.teamneutrino.stronghold.robot.sensors;

import java.util.Vector;

import org.teamneutrino.stronghold.robot.Constants;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ParticleReport;
import com.ni.vision.NIVision.Range;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
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
	private double maxArea;
	private double maxHeight;
	private double centerX;
	private double centerY;
	private double ang;
	private double distanceFromGoal;
	private Rect rectangle;

	private Solenoid lightPower;

	private int currentFrame;

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
		rectangle = new Rect();

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

		new Thread(this).start();
		new Thread(new SmartDashboardThread()).start();
	}

	public int getCurrentFrame()
	{
		return currentFrame;
	}

	public double getTargetX()
	{
		return centerX;
	}

	public double getTargetY()
	{
		return centerY;
	}

	public double getTargetArea()
	{
		return maxArea;
	}

	public double getHighestHeight()
	{
		return maxHeight;
	}

	@Override
	public void run()
	{
		int session;

		boolean areParticlesPresent = false;

		session = NIVision.IMAQdxOpenCamera(Constants.CAMERA_NAME,
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		long val = NIVision.IMAQdxGetAttributeMinimumI64(session, "CameraAttributes::Exposure::Value") + 5;
		NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::WhiteBalance::Mode", "Auto");
		NIVision.IMAQdxSetAttributeString(session, "CameraAttributes::Exposure::Mode", "Manual");
		NIVision.IMAQdxSetAttributeI64(session, "CameraAttributes::Exposure::Value", val);
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxStartAcquisition(session);
		while (true)
		{
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
			if (numParticles > 0)
			{
				int nParticles = 0;
				// Measure particles
				Vector<ParticleReport> particles = new Vector<ParticleReport>();
				for (int particleIndex = 0; particleIndex < numParticles; particleIndex++)
				{
					ParticleReport par = new ParticleReport();
					par.area = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
							NIVision.MeasurementType.MT_CONVEX_HULL_AREA);
					par.projectionX = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
							NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
					par.projectionY = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
							NIVision.MeasurementType.MT_CENTER_OF_MASS_Y);
					int top = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
					int left = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
					int height = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);
					int width = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
							NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
					Rect rect = new Rect(top, left, height, width);
					par.boundingBox = rect;
					if (par.area > Constants.MIN_PARTICLE_SIZE)
					{
						particles.add(par);
						nParticles++;
					}
				}
				ParticleReport[] myArr = new ParticleReport[particles.size()];
				ParticleReport min = null;

				// Get largest area
				if (nParticles > 0)
				{
					areParticlesPresent = true;
					for (int i = 0; i < particles.size();)
					{
						min = particles.get(i);
						int k = i;
						for (int j = 0; j < particles.size(); ++j)
						{
							if (particles.get(j).area < min.area)
							{
								k = j;
								min = particles.get(j);
							}
						}
						myArr[i] = min;
						particles.remove(k);
					}
					maxArea = myArr[0].area;
					// System.out.println("Area: " + maxArea);
					centerY = myArr[0].projectionX;
					centerX = myArr[0].projectionY;
					rectangle = myArr[0].boundingBox;
					maxHeight = rectangle.width;
				}
				else
				{
					areParticlesPresent = false;
				}
				currentFrame++;
			}
			if (outMode == OutputMode.THRESHOLD_IMAGE)
			{
				CameraServer.getInstance().setImage(image);
			}
			if (outMode == OutputMode.RECTANGLE_OVERLAY && areParticlesPresent)
			{

				NIVision.imaqDrawShapeOnImage(raw, raw, rectangle, DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0.0f);

				CameraServer.getInstance().setImage(raw);
			}
			image.free();
			raw.free();
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

				outMode = (OutputMode) outModeChooser.getSelected();
			}
		}
	}

}