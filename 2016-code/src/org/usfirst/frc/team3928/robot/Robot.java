
package org.usfirst.frc.team3928.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;
import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
	int session;
    Image frame;
    CameraServer server;

    public Robot() {
        
    }
    
    public void robotInit() {
    	server = CameraServer.getInstance();
        server.setQuality(75);
        //the camera name (ex "cam0") can be found through the roborio web interface
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam0",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
    }
    public void disabled(){
    	
    	
    }
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the if-else structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomous() {
    	
    	
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
    	NIVision.IMAQdxStartAcquisition(session);

        /**
         * grab an image, draw the circle, and provide it for the camera server
         * which will in turn send it to the dashboard.
         */
        NIVision.IMAQdxGrab(session, frame, 1);
        int Width = NIVision.imaqGetImageSize(frame).width;
        int Height = NIVision.imaqGetImageSize(frame).height;
        NIVision.Rect rect = new NIVision.Rect(Height/3, Width/3, Height/3, Width/3);
        NIVision.Rect rect2 = new NIVision.Rect(Height*3/8, Width*3/8, Height/4, Width/4);
        
        while (isOperatorControl() && isEnabled()) {

            NIVision.IMAQdxGrab(session, frame, 1);
            NIVision.imaqDrawShapeOnImage(frame, frame, rect,
                    DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 10f);
            NIVision.imaqDrawShapeOnImage(frame, frame, rect2,
                    DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.0f);
//            NIVision.BestLine(new NIVision.PointFloat(0,0), new NIVision.PointFloat(0,0),
//            		new NIVision.LineEquation(1, 1, 1), 1, 12, 100);
            NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_INVERT, new NIVision.Point(Width/2, Height/3), new NIVision.Point(Width/2, Height*2/3), 0.1f);
            NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_INVERT, new NIVision.Point(Width/3, Height/2), new NIVision.Point(Width*2/3, Height/2), 0.1f);
            
            

            /** robot code here! **/
            Timer.delay(0.005);		// wait for a motor update time
            server.setImage(frame);
        }
        NIVision.IMAQdxStopAcquisition(session);
    }

    /**
     * Runs during test mode
     */
    public void test() {
    	
    }
}
