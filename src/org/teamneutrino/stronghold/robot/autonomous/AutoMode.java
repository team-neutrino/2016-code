package org.teamneutrino.stronghold.robot.autonomous;

/**
 * All autonomous modes implement this interface. Each mode has a name and a method to be run.
 */
public interface AutoMode
{
	public String getName();
	public void run();
}
