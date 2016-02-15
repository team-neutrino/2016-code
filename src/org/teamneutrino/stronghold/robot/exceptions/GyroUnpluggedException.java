package org.teamneutrino.stronghold.robot.exceptions;

public class GyroUnpluggedException extends Exception
{
	private static final long serialVersionUID = -5468751483933066599L;

	public GyroUnpluggedException()
	{
		super();
	}
	
	public GyroUnpluggedException(String msg)
	{
		super(msg);
	}
}
