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
public interface ParkingDataDAO<T extends Vehicle>
{
	public int checkAvailableSolts(int level, T vehicle);
	
	public boolean leaveCar(int level, int slotNumber);
	
	public List<String> getStatus(int level);
	
	public void doCleanup();
}
