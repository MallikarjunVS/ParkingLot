/**
 * 
 */
package com.gojek.parkinglot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;

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
	@Autowired
	private ParkingDataDAO<Vehicle> parkingDataDAO = null;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private ParkingDataDAOImpl parkingDataDAOImpl;
	
	// A read-write lock will improve performance over the use of a mutual exclusion
	// lock if the frequency of vehicle
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	@SuppressWarnings("static-access")
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
		this.parkingDataDAO = parkingDataDAOImpl.getInstance(parkingLevels, capacityList, parkingStrategies);
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
	
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingLotException
	{
		lock.readLock().lock();
		Optional<Integer> value = Optional.empty();
		validateParkingLot();
		try
		{
			value = Optional.of(parkingDataDAO.getAvailableSlotsCount(level));
		}
		catch (Exception e)
		{
			throw new ParkingLotException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
		return value;
	}
	
	@Override
	public void getRegNumberForColor(int level, String color) throws ParkingLotException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			List<String> registrationList = parkingDataDAO.getRegistrationNumberForColor(level, color);
			if (registrationList.size() == 0)
				System.out.println("Not Found");
			else
				System.out.println(String.join(",", registrationList));
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
	public void getSlotNumbersFromColor(int level, String color) throws ParkingLotException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			List<Integer> slotList = parkingDataDAO.getSlotNumbersFromColor(level, color);
			if (slotList.size() == 0)
				System.out.println("Not Found");
			StringJoiner joiner = new StringJoiner(",");
			for (Integer slot : slotList)
			{
				joiner.add(slot + "");
			}
			System.out.println(joiner.toString());
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
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingLotException
	{
		int value = -1;
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			value = parkingDataDAO.getSlotNoFromRegistrationNo(level, registrationNo);
			System.out.println(value != -1 ? value : "Not Found");
		}
		catch (Exception e)
		{
			throw new ParkingLotException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
		return value;
	}
	
	@Override
	public void doCleanup()
	{
		if (parkingDataDAO != null)
			parkingDataDAO.doCleanup();
	}
}
