package org.usfirst.frc.team3928.robot.subsystems;

import org.usfirst.frc.team3928.robot.Constants;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Shooter implements Runnable
{
	Talon t0;
	Talon t1;
	int sum1;
	int sum2;
	int total;

	double time;

	double RPS1;
	double RPS2;

	double RPM1;
	double RPM2;

	double maxRPM1;
	double maxRPM2;

	double tgtSpd1;
	double tgtSpd2;

	Counter count2;
	Counter count1;

	int counter1;
	int counter2;

	int waitTime;

	boolean isSetToRPM;

	DigitalInput break0;
	DigitalInput break1;

	public boolean on;

	public Shooter()
	{
		new Thread(this).start();
		t0 = new Talon(Constants.SHOOTER_MOTOR_0);
		t1 = new Talon(Constants.SHOOTER_MOTOR_1);
		break0 = new DigitalInput(Constants.SHOOTER_BEAMBREAKE_0_CHANNEL);
		break1 = new DigitalInput(Constants.SHOOTER_BEAMBREAKE_1_CHANNEL);
		isSetToRPM = false;
	}

	public void start()
	{
		on = true;
	}

	public void stop()
	{
		on = false;
	}

	public void run()
	{
		while (true)
		{
			if (on)
			{
				setRPM(Constants.RPM_OF_SHOOTER);
			}
		}
	}

	public boolean isSetToRPM()
	{
		if (isSetToRPM == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void setRPM(double RPM)
	{
		count1 = new Counter(break0);
		count2 = new Counter(break1);
		time = System.currentTimeMillis();

		RPS1 = count1.getRate();
		RPS2 = count2.getRate();

		System.out.println("RPM1: " + RPS1 * 60);
		// System.out.println("(RPS2, RPM2): " + RPS2 + ", " + RPS2*60);
		System.out.println("Target RPM: " + RPM);
		count1.reset();
		count2.reset();
		double RPS = RPM * 60;

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

		isSetToRPM = true;

	}

}
