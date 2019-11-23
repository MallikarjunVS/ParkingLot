/**
 * 
 */
package com.gojek.parkinglot.dao;

import com.gojek.parkinglot.model.Vehicle;

/**
 * @author Mallikarjun
 *
 */
public interface ParkingLevelDataDAO<T extends Vehicle>
{
	int checkAvailableSolts(T vehicle);
	
	public boolean leaveCar(int slotNumber);
	
	public void doCleanUp();
	
}
