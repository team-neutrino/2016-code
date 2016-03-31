package org.teamneutrino.stronghold.robot;

public class Constants
{
	public static final boolean REAL_BOT = false;

	// Autonomous Constants
	public static final double AUTO_MOVE_SPEED = .25;
	public static final int GYRO_CHANNEL = 0;
	public static final double GOAL_HEIGHT_MULTIPLIER = 6.6/1.2;  //TODO
	public static final int THUMBWHEEL_SWITCH_BIT_1_CHANNEL = 4;
	public static final int THUMBWHEEL_SWITCH_BIT_2_CHANNEL = 5;
	public static final int THUMBWHEEL_SWITCH_BIT_4_CHANNEL = 6;
	public static final int THUMBWHEEL_SWITCH_BIT_8_CHANNEL = 7;

	// Controllers
	public static final int JOY_LEFT = 0;
	public static final int JOY_RIGHT = 1;
	public static final int GAMEPAD = 2;

	// Button Mapping - Joy sticks

	// Button Mapping - Game pad

	// Camera Constants
	public static final String CAMERA_NAME = "cam0";
	public static final int CAMERA_DEFAULT_HUE_LOW = 100;
	public static final int CAMERA_DEFAULT_HUE_HIGH = 150;
	public static final int CAMERA_DEFAULT_SATURATION_LOW = 84;
	public static final int CAMERA_DEFAULT_SATURATION_HIGH = 255;
	public static final int CAMERA_DEFAULT_LUMINENCE_LOW = 34;
	public static final int CAMERA_DEFAULT_LUMINENCE_HIGH = 255;
	public static final int CAMERA_LIGHT_POWER_CHANNEL = 1;
	public static final double CAMERA_TARGET_X = 240;
	public static final double CAMERA_TARGET_X_OFFSET = -10;
	public static final double CAMERA_TARGET_Y = 320;
	public static final double CAMERA_TARGET_Y_OFFSET = -200;
	public static final double MIN_PARTICLE_SIZE = 10;
	public static final double CAMERA_DISTANCE_BASE = 1.17875473; //DISTANCE IN FEET AT WICH THE GOAL HEIGHT IS EQUAL TO THE IMAGE HEIGHT WHEN VIEWED AT 0 DEGREES
	public static final double CAMERA_FOV_MULTIPLIER = 106d/144d;

	// Drive Constants
	public static final int ENCODER_LEFT_A_CHANNEL = 0;
	public static final int ENCODER_LEFT_B_CHANNEL = 1;
	public static final int ENCODER_RIGHT_A_CHANNEL = 2;
	public static final int ENCODER_RIGHT_B_CHANNEL = 3;
	public static final double ENCODER_DISTANCE_PER_PULSE = (.5 * Math.PI) * 1.0 / 360 / 3;
	public static final int DRIVE_LEFT_1_CHANNEL = 0;
	public static final int DRIVE_LEFT_1_POWER_CHANNEL = 13;
	public static final int DRIVE_LEFT_2_CHANNEL = 1;
	public static final int DRIVE_LEFT_2_POWER_CHANNEL = 14;
	public static final int DRIVE_LEFT_3_CHANNEL = 2;
	public static final int DRIVE_LEFT_3_POWER_CHANNEL = 15;
	public static final int DRIVE_RIGHT_1_CHANNEL = 3;
	public static final int DRIVE_RIGHT_1_POWER_CHANNEL = 0;
	public static final int DRIVE_RIGHT_2_CHANNEL = 4;
	public static final int DRIVE_RIGHT_2_POWER_CHANNEL = 1;
	public static final int DRIVE_RIGHT_3_CHANNEL = 5;
	public static final int DRIVE_RIGHT_3_POWER_CHANNEL = 2;

	// Intake Constants
	public static final int INTAKE_ACUATOR_MOTOR_CHANNEL = 9;
	public static final int INTAKE_ACUATOR_MOTOR_POWER_CHANNEL = 9;
	public static final int INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL = (REAL_BOT ? 6 : 10);
	public static final int INTAKE_FRONT_TO_BACK_MOTOR_POWER_CHANNEL = 6;
	public static final int INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL = (REAL_BOT ? 7 : 11);
	public static final int INTAKE_SIDE_TO_SIDE_MOTOR_POWER_CHANNEL = 5;
	public static final int INTAKE_ENCODER_CHANNEL = 2;
	public static final double INTAKE_ENCODER_SCALE = 360;
	public static final double INTAKE_ENCODER_OFFSET = -157;
	public static final double INTAKE_ACTUATION_K_P = (REAL_BOT ? .02 : .005);
	public static final double INTAKE_ACTUATION_K_I = 0;
	public static final double INTAKE_ACTUATION_K_D = .001;

	// Shooter Constants
	public static final int SHOOTER_LEFT_MOTOR_CHANNEL = (REAL_BOT ? 10 : 6);
	public static final int SHOOTER_LEFT_MOTOR_POWER_CHANNEL = 4;
	public static final int SHOOTER_RIGHT_MOTOR_CHANNEL = (REAL_BOT ? 11 : 7);
	public static final int SHOOTER_RIGHT_MOTOR_POWER_CHANNEL = 11;
	public static final int SHOOTER_ACTUATOR_MOTOR_CHANNEL = 8;
	public static final int SHOOTER_ACTUATOR_MOTOR_POWER_CHANNEL = 8;
	public static final int SHOOTER_REFRESH_RATE = 5;
	public static final int SHOOTER_RPM = 1500;
	public static final double SHOOTER_TARGET_SPEED_TOLERANCE_RPM = 200;
	public static final int SHOOTER_BEAMBREAK_RIGHT_CHANNEL = 8;
	public static final int SHOOTER_BEAMBREAK_LEFT_CHANNEL = 9;
	public static final int SHOOTER_BEAMBREAK_1_POWER_CHANNEL = 6;
	public static final int SHOOTER_BEAMBREAK_2_POWER_CHANNEL = 7;
	public static final double SHOOTER_PERCENT_POWER_PER_RPMILLI = 1/1500;
	public static final double SHOOTER_K_P = .1; // TODO
	public static final double SHOOTER_K_I = 0; // TODO
	public static final int SHOOTER_ENCODER_CHANNEL = 1;
	public static final double SHOOTER_ENCODER_SCALE = 360;
	public static final double SHOOTER_ENCODER_MAX = 150;
	public static final double SHOOTER_ENCODER_OFFSET = (REAL_BOT ? -121 : -203);
	public static final double SHOOTER_ACTUATION_MAX_SPEED = .5;
	public static final double SHOOTER_ACTUATION_K_P = (REAL_BOT ? .01 : .005);
	public static final double SHOOTER_ACTUATION_K_I = 0;
	public static final double SHOOTER_ACTUATION_K_D = .005;
	public static final int SHOOTER_FLIPPER_OPEN_CYLINDER_CHANNEL = 5;
	public static final int SHOOTER_FLIPPER_CLOSE_CYLINDER_CHANNEL = 2;

	// Stinger Constants
	public static final int STINGER_CYLINDER_EXTEND_CHANNEL = 3;
	public static final int STINGER_CYLINDER_RETRACT_CHANNEL = 4;

	// Misc Constants
	public static final int PNEUMATIC_PRESSURE_SENSOR_CHANNEL = 3;
	public static final int DRIVER_STATION_REFRESH_RATE = 250;
	public static final boolean USE_TIME_FOR_SHOOTER_AIMING = true;
}
