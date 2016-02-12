package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Shooter implements Runnable
{
	private Talon motor1;
	private Talon motor2;

	private Counter beambreak1;
	private Counter beambreak2;

	private int waitTime;

	private boolean on;

	public Shooter()
	{
		new Thread(this).start();
		motor1 = new Talon(Constants.SHOOTER_MOTOR_0);
		motor2 = new Talon(Constants.SHOOTER_MOTOR_1);
		beambreak1 = new Counter(Constants.SHOOTER_BEAMBREAKE_0_CHANNEL);
		beambreak2 = new Counter(Constants.SHOOTER_BEAMBREAKE_1_CHANNEL);
		on = false;
	}

	public void start()
	{
		on = true;
	}

	public void stop()
	{
		on = false;
	}

	public boolean isOn()
	{
		return on;
	}

	public void run()
	{
		double startTime = System.currentTimeMillis();

		double RPS1 = beambreak1.getRate();
		double RPS2 = beambreak2.getRate();
		
		double tgtSpd1;
		double tgtSpd2;

		System.out.println("RPM1: " + RPS1 * 60);
		// System.out.println("(RPS2, RPM2): " + RPS2 + ", " + RPS2*60);
		System.out.println("Target RPM: " + Constants.SHOOTER_RPM);
		beambreak1.reset();
		beambreak2.reset();
		double RPS = Constants.SHOOTER_RPM * 60;

		while ((System.currentTimeMillis() - startTime < waitTime))
		{
			if (RPS != RPS1)
			{
				if (RPS > RPS1)
				{
					tgtSpd1 = RPS1 / RPS;
				}
				else if (RPS < RPS1)
				{
					tgtSpd1 = (2 - (RPS1 / RPS));
				}
			}
			if (RPS != RPS2)
			{
				if (RPS > RPS2)
				{
					tgtSpd2 = RPS2 / RPS;
				}
				else if (RPS < RPS2)
				{
					tgtSpd2 = (2 - (RPS2 / RPS));
				}
			}
			if (RPS1 == 0)
			{
				tgtSpd1 = 1;
			}
			if (RPS2 == 0)
			{
				tgtSpd2 = 1;
			}
			if (tgtSpd1 < 0)
			{
				tgtSpd1 = 0;
			}
			if (tgtSpd2 < 0)
			{
				tgtSpd2 = 0;
			}
			if (tgtSpd1 > 1)
			{
				tgtSpd1 = 1;
			}
			if (tgtSpd2 > 1)
			{
				tgtSpd2 = 1;
			}
			motor1.set(-tgtSpd1);
			motor2.set(tgtSpd2);
		}
		Timer.delay(0.005); // wait for a motor update time
	}
}
