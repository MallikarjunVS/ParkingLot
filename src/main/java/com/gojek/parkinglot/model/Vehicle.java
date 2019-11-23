/**
 * 
 */
package com.gojek.parkinglot.model;

/**
 * @author Mallikarjun
 *
 */
public abstract class Vehicle
{
	private String	vehicleRegNo	= null;
	private String	color			= null;
	
	public Vehicle(String registrationNo, String color)
	{
		this.vehicleRegNo = registrationNo;
		this.color = color;
	}
	
	@Override
	public String toString()
	{
		return "[vehicleRegNo=" + vehicleRegNo + ", color=" + color + "]";
	}
	
	/**
	 * @return the vehicleRegNo
	 */
	public String getVehicleRegNo()
	{
		return vehicleRegNo;
	}
	
	/**
	 * @param vehicleRegNo
	 *            the vehicleRegNo to set
	 */
	public void setRegistrationNo(String vehicleRegNo)
	{
		this.vehicleRegNo = vehicleRegNo;
	}
	
	/**
	 * @return the color
	 */
	public String getColor()
	{
		return color;
	}
	
	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color)
	{
		this.color = color;
	}
	
}
