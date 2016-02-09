package org.usfirst.frc.team3928.robot.excetions;

public class EncoderUnpluggedException extends Exception
{
	private static final long serialVersionUID = -1323475313714913168L;

	public EncoderUnpluggedException()
	{
		super();
	}
	
	public EncoderUnpluggedException(String msg)
	{
		super(msg);
	}
}
