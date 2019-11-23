/**
 * 
 */
package com.gojek.parkinglot.dao;

import com.gojek.parkinglot.model.Vehicle;

/**
 * @author Mallikarjun
 *
 */
public interface ParkingDataDAO<T extends Vehicle>
{
	
	public void doCleanup();
}
