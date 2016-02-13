package org.teamneutrino.stronghold.robot;

public class Constants
{
	public static final boolean REAL_BOT = false;

	// Autonomous Constants
	public static final int AUTO_MODE = 2;
	public static final double AUTO_MOVE_SPEED = .25;
	public static final int GYRO_CHANNEL = 0;
	public static final double AUTO_SLOW_DISTANCE = 1; // IN FEET
	public static final int THUMBWHEEL_SWITCH_BIT_1_CHANNEL = 4;
	public static final int THUMBWHEEL_SWITCH_BIT_2_CHANNEL = 5;
	public static final int THUMBWHEEL_SWITCH_BIT_4_CHANNEL = 6;
	public static final int THUMBWHEEL_SWITCH_BIT_8_CHANNEL = 7;

	// Controllers
	public static final int JOY_LEFT = 1;
	public static final int JOY_RIGHT = 0;
	public static final int GAMEPAD = 2;

	// Button Mapping - Joy sticks

	// Button Mapping - Game pad
	
	// Camera Constants
	public static final String CAMERA_NAME = "cam1";
	public static final int DEFAULT_HUE_LOW = 71;
	public static final int DEFAULT_HUE_HIGH = 102;
	public static final int DEFAULT_SATURATION_LOW = 167;
	public static final int DEFAULT_SATURATION_HIGH = 255;
	public static final int DEFAULT_LUMINENCE_LOW = 155;
	public static final int DEFAULT_LUMINENCE_HIGH = 255;
	public static final int DEFAULT_EXPOSURE = 50;
	public static final boolean DEFAULT_AUTO_EXPOSURE = true;

	// Drive Constants
	public static final int ENCODER_LEFT_A_CHANNEL = 0;
	public static final int ENCODER_LEFT_B_CHANNEL = 1;
	public static final int ENCODER_RIGHT_A_CHANNEL = 2;
	public static final int ENCODER_RIGHT_B_CHANNEL = 3;
	public static final double ENCODER_DISTANCE_PER_PULSE = (.5 * Math.PI)*1.0/360/3;
	public static final int DRIVE_LEFT_1_CHANNEL = 0;
	public static final int DRIVE_LEFT_1_POWER_CHANNEL = 10;
	public static final int DRIVE_LEFT_2_CHANNEL = 1;
	public static final int DRIVE_LEFT_2_POWER_CHANNEL = 10;
	public static final int DRIVE_LEFT_3_CHANNEL = 2;
	public static final int DRIVE_LEFT_3_POWER_CHANNEL = 10;
	public static final int DRIVE_RIGHT_1_CHANNEL = 3;
	public static final int DRIVE_RIGHT_1_POWER_CHANNEL = 10;
	public static final int DRIVE_RIGHT_2_CHANNEL = 4;
	public static final int DRIVE_RIGHT_2_POWER_CHANNEL = 10;
	public static final int DRIVE_RIGHT_3_CHANNEL = 5;
	public static final int DRIVE_RIGHT_3_POWER_CHANNEL = 10;
	
	// Intake Constants
	public static final int INTAKE_POSITION_MOTOR_CHANNEL = 10;
	public static final int INTAKE_CENTER_MOTOR_CHANNEL = 11;
	public static final int INTAKE_SIDE_MOTOR_CHANNEL = 12;
	
	// Shooter Constants
	public static final int SHOOTER_MOTOR_LEFT = 20;
	public static final int SHOOTER_MOTOR_RIGHT = 21;
	public static final int SHOOTER_REFRESH_RATE = 50;
	public static final int SHOOTER_RPM = 1500;
	public static final int SHOOTER_BEAMBREAKE_RIGHT_CHANNEL = 8;
	public static final int SHOOTER_BEAMBREAKE_LEFT_CHANNEL = 9;
	public static final double SHOOTER_PERCENT_POWER_PER_RPMilli = 2; // TODO
	public static final double SHOOTER_K_P = .1; // TODO
	public static final double SHOOTER_K_I = 0; // TODO

	// Misc Constants
	public static final int DRIVER_STATION_REFRESH_RATE = 1000;
}
