package org.teamneutrino.stronghold.robot;

public class Constants
{
	public static final boolean REAL_BOT = true;

	// Autonomous Constants
	public static final double AUTO_MOVE_SPEED = .25;
	public static final int GYRO_CHANNEL = 0;
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
	public static final int DEFAULT_HUE_LOW = 101;
	public static final int DEFAULT_HUE_HIGH = 136;
	public static final int DEFAULT_SATURATION_LOW = 84;
	public static final int DEFAULT_SATURATION_HIGH = 255;
	public static final int DEFAULT_LUMINENCE_LOW = 34;
	public static final int DEFAULT_LUMINENCE_HIGH = 255;

	// Drive Constants
	public static final int ENCODER_LEFT_A_CHANNEL = 0;
	public static final int ENCODER_LEFT_B_CHANNEL = 1;
	public static final int ENCODER_RIGHT_A_CHANNEL = 2;
	public static final int ENCODER_RIGHT_B_CHANNEL = 3;
	public static final double ENCODER_DISTANCE_PER_PULSE = (.5 * Math.PI) * 1.0 / 360 / 3;
	public static final int DRIVE_LEFT_1_CHANNEL = 0;
	public static final int DRIVE_LEFT_1_POWER_CHANNEL = 1; // TODO
	public static final int DRIVE_LEFT_2_CHANNEL = 1;
	public static final int DRIVE_LEFT_2_POWER_CHANNEL = 2; // TODO
	public static final int DRIVE_LEFT_3_CHANNEL = 2;
	public static final int DRIVE_LEFT_3_POWER_CHANNEL = 3; // TODO
	public static final int DRIVE_RIGHT_1_CHANNEL = 3;
	public static final int DRIVE_RIGHT_1_POWER_CHANNEL = 4; // TODO
	public static final int DRIVE_RIGHT_2_CHANNEL = 4;
	public static final int DRIVE_RIGHT_2_POWER_CHANNEL = 5; // TODO
	public static final int DRIVE_RIGHT_3_CHANNEL = 5;
	public static final int DRIVE_RIGHT_3_POWER_CHANNEL = 6; // TODO

	// Intake Constants
	public static final int INTAKE_ACUATOR_MOTOR_CHANNEL = 9;
	public static final int INTAKE_FRONT_TO_BACK_MOTOR_CHANNEL = 8;
	public static final int INTAKE_SIDE_TO_SIDE_MOTOR_CHANNEL = 7;
	public static final int INTAKE_ENCODER_CHANNEL = 2;
	public static final double INTAKE_ENCODER_SCALE = 5; // TODO
	public static final double INTAKE_ENCODER_OFFSET = 0; // TODO
	public static final double INTAKE_ACTUATION_K_P = 1;
	public static final double INTAKE_ACTUATION_K_I = 0;
	public static final double INTAKE_ACTUATION_K_D = 0;

	// Shooter Constants
	public static final int SHOOTER_LEFT_MOTOR_CHANNEL = 10;
	public static final int SHOOTER_LEFT_MOTOR_POWER_CHANNEL = 7; // TODO
	public static final int SHOOTER_RIGHT_MOTOR_CHANNEL = 11;
	public static final int SHOOTER_RIGHT_MOTOR_POWER_CHANNEL = 8; // TODO
	public static final int SHOOTER_ACTUATOR_MOTOR_CHANNEL = 6;
	public static final int SHOOTER_ACTUATOR_MOTOR_POWER_CHANNEL = 9; // TODO
	public static final int SHOOTER_REFRESH_RATE = 50;
	public static final int SHOOTER_RPM = 1500;
	public static final double SHOOTER_TARGET_SPEED_TOLERANCE_RPM = 200;
	public static final int SHOOTER_BEAMBREAK_RIGHT_CHANNEL = 8;
	public static final int SHOOTER_BEAMBREAK_LEFT_CHANNEL = 9;
	public static final double SHOOTER_PERCENT_POWER_PER_RPMILLI = 1/1500;
	public static final double SHOOTER_K_P = .1; // TODO
	public static final double SHOOTER_K_I = 0; // TODO
	public static final double SHOOTER_REST_POSITION = 0; // TODO
	public static final int SHOOTER_ENCODER_CHANNEL = 1;
	public static final double SHOOTER_ENCODER_SCALE = 360;
	public static final double SHOOTER_ENCODER_MAX = 150;
	public static final double SHOOTER_ENCODER_OFFSET = 0; // TODO
	public static final double SHOOTER_ACTUATION_K_P = 1;
	public static final double SHOOTER_ACTUATION_K_I = 0;
	public static final double SHOOTER_ACTUATION_K_D = 0;
	public static final int SHOOTER_FLIPPER_CYLINDER_CHANNEL = 0;

	// Stinger Constants
	public static final int STINGER_CYLINDER_CHANNEL = 1;

	// Misc Constants
	public static final int PNEUMATIC_PRESSURE_SENSOR_CHANNEL = 0;
	public static final int DRIVER_STATION_REFRESH_RATE = 1000;
}
