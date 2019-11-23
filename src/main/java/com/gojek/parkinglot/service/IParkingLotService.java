/**
 * 
 */
package com.gojek.parkinglot.service;

import com.gojek.parkinglot.exception.ParkingLotException;

/**
 * @author Mallikarjun
 *
 */
public interface IParkingLotService extends IAbstractService
{
	
	public void createParkingLot(int level, int capacity) throws ParkingLotException;
	
	void doCleanup();
	
}
