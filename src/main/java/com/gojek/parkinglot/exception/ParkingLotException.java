/**
 * 
 */
package com.gojek.parkinglot.exception;

/**
 * @author Mallikarjun
 *
 */
public class ParkingLotException extends Exception
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1820832031995032621L;
	
	private String		errorCode		= null;	// this will hold system defined error code
	private Object[]	errorParameters	= null;	// this will hold parameters for error code/message
	
	/**
	 * @param message
	 * @param throwable
	 */
	public ParkingLotException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
	
	/**
	 * @param message
	 */
	public ParkingLotException(String message)
	{
		super(message);
	}
	
	/**
	 * @param throwable
	 */
	public ParkingLotException(Throwable throwable)
	{
		super(throwable);
	}
	
	/**
	 * @param errorCode
	 * @param message
	 * @param errorParameters
	 */
	public ParkingLotException(String errorCode, String message, Object[] errorParameters)
	{
		super(message);
		this.setErrorCode(errorCode);
		this.setErrorParameters(errorParameters);
	}
	
	/**
	 * @param errorCode
	 * @param message
	 * @param throwable
	 */
	public ParkingLotException(String errorCode, String message, Throwable throwable)
	{
		super(message, throwable);
		this.setErrorCode(errorCode);
	}
	
	/**
	 * @param errorCode
	 * @param message
	 * @param errorParameters
	 * @param throwable
	 */
	public ParkingLotException(String errorCode, String message, Object[] errorParameters, Throwable throwable)
	{
		super(message, throwable);
		this.setErrorCode(errorCode);
		this.setErrorParameters(errorParameters);
	}
	
	public String getErrorCode()
	{
		return errorCode;
	}
	
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}
	
	public Object[] getErrorParameters()
	{
		return errorParameters;
	}
	
	public void setErrorParameters(Object[] errorParameters)
	{
		this.errorParameters = errorParameters;
	}
}
