/**
 * 
 */
package com.gojek.parkinglot.service;

import java.util.Optional;

import com.gojek.parkinglot.exception.ParkingLotException;
import com.gojek.parkinglot.model.Vehicle;

/**
 * @author Mallikarjun
 *
 */
public interface IParkingLotService extends IAbstractService
{
	
	public void createParkingLot(int level, int capacity) throws ParkingLotException;
	
	public Optional<Integer> parkVehicle(int level, Vehicle vehicle) throws ParkingLotException;
	
	public void unParkVehicle(int level, int slotNumber) throws ParkingLotException;
	
	public void doCleanup();
}
