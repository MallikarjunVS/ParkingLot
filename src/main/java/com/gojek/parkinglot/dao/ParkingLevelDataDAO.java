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
	
	public void doCleanUp();
	
}
