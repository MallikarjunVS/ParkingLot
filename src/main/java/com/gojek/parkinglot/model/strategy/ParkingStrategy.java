/**
 * 
 */
package com.gojek.parkinglot.model.strategy;

/**
 * @author Mallikarjun
 *
 */
public interface ParkingStrategy
{
	public void add(int i);
	
	public int getSlot();
	
	public void removeSlot(int slot);
}
