package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Shooter implements Runnable
{
	private Talon t0;
	private Talon t1;

	private double time;
	
	private DigitalInput break0;
	private DigitalInput break1;

	private Counter count2;
	private Counter count1;

	private int waitTime;

	private boolean on;

	public Shooter()
	{
		new Thread(this).start();
		t0 = new Talon(Constants.SHOOTER_MOTOR_0);
		t1 = new Talon(Constants.SHOOTER_MOTOR_1);
		break0 = new DigitalInput(Constants.SHOOTER_BEAMBREAKE_0_CHANNEL);
		break1 = new DigitalInput(Constants.SHOOTER_BEAMBREAKE_1_CHANNEL);
		count1 = new Counter(break0);
		count2 = new Counter(break1);
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
		time = System.currentTimeMillis();

		double RPS1 = count1.getRate();
		double RPS2 = count2.getRate();
		
		double tgtSpd1;
		double tgtSpd2;

		System.out.println("RPM1: " + RPS1 * 60);
		// System.out.println("(RPS2, RPM2): " + RPS2 + ", " + RPS2*60);
		System.out.println("Target RPM: " + Constants.SHOOTER_RPM);
		count1.reset();
		count2.reset();
		double RPS = Constants.SHOOTER_RPM * 60;

		while ((System.currentTimeMillis() - time < waitTime))
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
			t0.set(-tgtSpd1);
			t1.set(tgtSpd2);
		}

		Timer.delay(0.005); // wait for a motor update time
	}
}
