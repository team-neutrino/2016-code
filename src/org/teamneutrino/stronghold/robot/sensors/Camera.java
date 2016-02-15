package org.teamneutrino.stronghold.robot.sensors;

import java.util.Vector;

import org.teamneutrino.stronghold.robot.Constants;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageInfo;
import com.ni.vision.NIVision.ParticleReport;
import com.ni.vision.NIVision.Range;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
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
	private double centerX;
	private double centerY;
	private Rect rectangle;
	private ImageInfo imgInfo;

	enum OutputMode
	{
		RAW_IMAGE, THRESHOLD_IMAGE, CONVEX_HULL, RECTANGLE_OVERLAY
	}

	public Camera()
	{
		hueLow = Constants.DEFAULT_HUE_LOW;
		hueHigh = Constants.DEFAULT_HUE_HIGH;
		saturationLow = Constants.DEFAULT_SATURATION_LOW;
		saturationHigh = Constants.DEFAULT_SATURATION_HIGH;
		luminenceLow = Constants.DEFAULT_LUMINENCE_LOW;
		luminenceHigh = Constants.DEFAULT_LUMINENCE_HIGH;
		rectangle = new Rect();

		SmartDashboard.putNumber("Hue Low", hueLow);
		SmartDashboard.putNumber("Hue High", hueHigh);
		SmartDashboard.putNumber("Saturation Low", saturationLow);
		SmartDashboard.putNumber("Saturation High", saturationHigh);
		SmartDashboard.putNumber("Luminence Low", luminenceLow);
		SmartDashboard.putNumber("Luminence High", luminenceHigh);

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
		return centerX;
	}

	public double getMostCenterY()
	{
		return centerY;
	}

	public double getLargestArea()
	{
		return maxArea;
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

		imgInfo = new ImageInfo();

		raw = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		outpt = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
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
			NIVision.IMAQdxGrab(session, raw, 1);
			NIVision.imaqDuplicate(outpt, raw);
			
			if (outMode == OutputMode.RAW_IMAGE)
			{
				CameraServer.getInstance().setImage(raw);
			}
			NIVision.imaqColorThreshold(raw, raw, 255, NIVision.ColorMode.HSL, new Range(hueLow, hueHigh),
					new Range(saturationLow, saturationHigh),
					new Range(luminenceLow, luminenceHigh));
			int numParticles = NIVision.imaqCountParticles(raw, 0);
			if (numParticles > 0)
			{
				// Measure particles and sort by particle size
				 Vector<ParticleReport> particles = new Vector<ParticleReport>();

				for (int particleIndex = 0; particleIndex < numParticles; particleIndex++)
				{
					ParticleReport par = new ParticleReport();
					par.area = (int)NIVision.imaqMeasureParticle(raw, particleIndex, 0, NIVision.MeasurementType.MT_CONVEX_HULL_AREA);
					par.projectionX = (int)NIVision.imaqMeasureParticle(raw, particleIndex, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
					par.projectionY = (int)NIVision.imaqMeasureParticle(raw, particleIndex, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_Y);
					int top = (int)NIVision.imaqMeasureParticle(raw, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
					int left = (int)NIVision.imaqMeasureParticle(raw, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
					int height = (int)NIVision.imaqMeasureParticle(raw, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);
					int width = (int)NIVision.imaqMeasureParticle(raw, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
					Rect rect = new Rect(top, left, height, width);
					par.boundingBox = rect;
					particles.add(par);
					SmartDashboard.putString("Added particle: ", par.toString() + "");
				}
				ParticleReport[] myArr = new ParticleReport[particles.size()];
				ParticleReport min = null;
				for(int i = 0; i < particles.size();)
				{
					min = particles.get(i);
					int k = i;
					for(int j = 0; j < particles.size(); ++j)
					{
						if(particles.get(j).area < min.area)
						{
							k = j;
							min = particles.get(j);
						}
					}
					myArr[i] = min;
					particles.remove(k);
				}
				maxArea = myArr[0].area;
				centerX = myArr[0].projectionX;
				centerY = myArr[0].projectionY;
				rectangle = myArr[0].boundingBox;
			}
			if (outMode == OutputMode.THRESHOLD_IMAGE)
			{
				CameraServer.getInstance().setImage(raw);
			}
			if (outMode == OutputMode.CONVEX_HULL)
			{
				CameraServer.getInstance().setImage(raw);
			}
			if (outMode == OutputMode.RECTANGLE_OVERLAY)
			{
				NIVision.imaqDrawShapeOnImage(outpt, outpt, rectangle,
	                    DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.0f);
				
				CameraServer.getInstance().setImage(outpt);
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

				hueLow = (int) SmartDashboard.getNumber("Hue Low", hueLow);
				hueHigh = (int) SmartDashboard.getNumber("Hue High", hueHigh);
				saturationLow = (int) SmartDashboard.getNumber("Saturation Low", saturationLow);
				saturationHigh = (int) SmartDashboard.getNumber("Saturation High", saturationHigh);
				luminenceLow = (int) SmartDashboard.getNumber("Luminence Low", luminenceLow);
				luminenceHigh = (int) SmartDashboard.getNumber("Luninence High", luminenceHigh);

				outMode = (OutputMode) outModeChooser.getSelected();
			}
		}
	}
}