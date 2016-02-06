package org.usfirst.frc.team3928.robot;

public class Constants
{
	public static final boolean REAL_BOT = false;

	// Autonomous Constants
	public static final int AUTO_MODE = 1;
	public static final double AUTO_MOVE_SPEED = .25;
	public static final boolean AUTO_USE_GYRO = false;
	public static final int GYRO_CHANNEL = 0;
	public static final double AUTON_SLOW_DISTANCE = 1; // IN FEET

	// Controllers
	public static final int JOY_LEFT = 0;
	public static final int JOY_RIGHT = 1;
	public static final int GAMEPAD = 2;

	// Button Mapping - Joy sticks

	// Button Mapping - Game pad
	
	// Camera Constants
	public static final double IMAGE_CENTER_X = 100;
	public static final double IMAGE_CENTER_Y = 100;

	// Sensor Constants
	public static final int ENCODER_LEFT_A_CHANNEL = 0;
	public static final int ENCODER_LEFT_B_CHANNEL = 1;
	public static final int ENCODER_RIGHT_A_CHANNEL = 2;
	public static final int ENCODER_RIGHT_B_CHANNEL = 3;
	public static final double ENCODER_DISTANCE_PER_PULSE = (.5 * Math.PI)*1.0/360/3;
	public static final int THUMBWHEEL_SWITCH_BIT_1_CHANNEL = 4;
	public static final int THUMBWHEEL_SWITCH_BIT_2_CHANNEL = 5;
	public static final int THUMBWHEEL_SWITCH_BIT_4_CHANNEL = 6;
	public static final int THUMBWHEEL_SWITCH_BIT_8_CHANNEL = 7;
	public static final int BEAMBREAKER_0_CHANNEL = 8;
	public static final int BEAMBREAKER_1_CHANNEL = 9;

	// Drive Constants
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
	
	//Intake Constants
	public static final int INTAKE_UP_DOWN = 10;
	public static final int INTAKE0 = 11;
	public static final int INTAKE1 = 12;
	
	//Shooter Constants
	public static final int SHOOTER_0 = 20;
	public static final int SHOOTER_1 = 21;
}
