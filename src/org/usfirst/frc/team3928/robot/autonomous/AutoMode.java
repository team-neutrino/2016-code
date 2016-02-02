package org.usfirst.frc.team3928.robot.autonomous;

/**
 * All autonomous modes implement this interface. Each mode has a name and a method to be run.
 */
public interface AutoMode
{
	public String getName();
	public void run();
}
