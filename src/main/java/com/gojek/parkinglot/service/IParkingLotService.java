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
	
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingLotException;
	
	public Optional<Integer> parkVehicle(int level, Vehicle vehicle) throws ParkingLotException;
	
	public void unParkVehicle(int level, int slotNumber) throws ParkingLotException;
	
	public void getStatus(int level) throws ParkingLotException;
	
	public void getRegNumberForColor(int level, String color) throws ParkingLotException;
	
	public void getSlotNumbersFromColor(int level, String colour) throws ParkingLotException;
	
	public void doCleanup();
}
