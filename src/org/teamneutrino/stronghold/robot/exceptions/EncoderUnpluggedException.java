package org.teamneutrino.stronghold.robot.exceptions;

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
