/**
 * 
 */
package com.gojek.parkinglot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.gojek.parkinglot.constants.Constants;
import com.gojek.parkinglot.dao.ParkingDataDAO;
import com.gojek.parkinglot.dao.impl.ParkingDataDAOImpl;
import com.gojek.parkinglot.exception.ErrorCode;
import com.gojek.parkinglot.exception.ParkingLotException;
import com.gojek.parkinglot.model.Vehicle;
import com.gojek.parkinglot.model.strategy.NearestParkingLotStrategy;
import com.gojek.parkinglot.model.strategy.ParkingStrategy;
import com.gojek.parkinglot.service.IParkingLotService;

/**
 * 
 * 
 * @author Mallikarjun
 *
 */
public class ParkingLotServiceImpl implements IParkingLotService
{
	private ParkingDataDAO<Vehicle> parkingDataDAO = null;
	
	// A read-write lock will improve performance over the use of a mutual exclusion
	// lock if the frequency of vehicle
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public void createParkingLot(int level, int capacity) throws ParkingLotException
	{
		if (parkingDataDAO != null)
			throw new ParkingLotException(ErrorCode.PARKING_ALREADY_EXIST.getMessage());
		List<Integer> parkingLevels = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		List<ParkingStrategy> parkingStrategies = new ArrayList<>();
		parkingLevels.add(level);
		capacityList.add(capacity);
		parkingStrategies.add(new NearestParkingLotStrategy());
		this.parkingDataDAO = ParkingDataDAOImpl.getInstance(parkingLevels, capacityList, parkingStrategies);
		System.out.println("Created parking lot with " + capacity + " slots");
	}
	
	@Override
	public Optional<Integer> parkVehicle(int level, Vehicle vehicle) throws ParkingLotException
	{
		Optional<Integer> value = Optional.empty();
		lock.writeLock().lock();
		validateParkingLot();
		try
		{
			value = Optional.of(parkingDataDAO.checkAvailableSolts(level, vehicle));
			if (value.get() == Constants.NOT_AVAILABLE)
				System.out.println("Sorry, parking lot is full");
			else if (value.get() == Constants.VEHICLE_ALREADY_EXIST)
				System.out.println("Sorry, vehicle is already parked.");
			else
				System.out.println("Allocated slot number: " + value.get());
		}
		catch (Exception e)
		{
			throw new ParkingLotException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
		return value;
	}
	
	/**
	 * @throws ParkingLotException
	 */
	private void validateParkingLot() throws ParkingLotException
	{
		if (parkingDataDAO == null)
		{
			throw new ParkingLotException(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage());
		}
	}
	
	@Override
	public void unParkVehicle(int level, int slotNumber) throws ParkingLotException
	{
		lock.writeLock().lock();
		validateParkingLot();
		try
		{
			
			if (parkingDataDAO.leaveCar(level, slotNumber))
				System.out.println("Slot number " + slotNumber + " is free");
			else
				System.out.println("Slot number is Empty Already.");
		}
		catch (Exception e)
		{
			throw new ParkingLotException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void getStatus(int level) throws ParkingLotException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			System.out.println("Slot No.\tRegistration No.\tColor");
			List<String> statusList = parkingDataDAO.getStatus(level);
			if (statusList.size() == 0)
				System.out.println("Sorry, parking lot is empty.");
			else
			{
				for (String statusSting : statusList)
				{
					System.out.println(statusSting);
				}
			}
		}
		catch (Exception e)
		{
			throw new ParkingLotException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public void doCleanup()
	{
		if (parkingDataDAO != null)
			parkingDataDAO.doCleanup();
	}
	
}
