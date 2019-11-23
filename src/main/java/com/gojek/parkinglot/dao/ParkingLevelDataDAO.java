/**
 * 
 */
package com.gojek.parkinglot.dao;

import java.util.List;

import com.gojek.parkinglot.model.Vehicle;

/**
 * @author Mallikarjun
 *
 */
public interface ParkingLevelDataDAO<T extends Vehicle>
{
	int checkAvailableSolts(T vehicle);
	
	public boolean leaveCar(int slotNumber);
	
	public List<String> getStatus();
	
	public List<String> getRegNumberForColor(String color);
	
	public List<Integer> getSlotNumbersFromColor(String colour);
	
	public int getAvailableSlotsCount();
	
	public void doCleanUp();
	
}
