/**
 * 
 */
package com.gojek.parkinglot.dao.impl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.gojek.parkinglot.constants.Constants;
import com.gojek.parkinglot.dao.ParkingLevelDataDAO;
import com.gojek.parkinglot.model.Vehicle;
import com.gojek.parkinglot.model.strategy.NearestParkingLotStrategy;
import com.gojek.parkinglot.model.strategy.ParkingStrategy;

/**
 * 
 * Created singleton class which manages the data of parking lot system
 * 
 * @author Mallikarjun
 * 
 */

public class ParkingLevelDataDAOImpl<T extends Vehicle> implements ParkingLevelDataDAO<T>
{
	// For Multilevel Parking lot - 0 -> Ground floor 1 -> First Floor etc
	private AtomicInteger	level			= new AtomicInteger(0);
	private AtomicInteger	capacity		= new AtomicInteger();
	private AtomicInteger	availability	= new AtomicInteger();
	// Allocation Strategy for parking
	private ParkingStrategy parkingStrategy;
	// this is per level - slot - vehicle
	private Map<Integer, Optional<T>> slotVehicleMap;
	
	@SuppressWarnings("rawtypes")
	private static ParkingLevelDataDAOImpl instance = null;
	
	@SuppressWarnings("unchecked")
	public static <T extends Vehicle> ParkingLevelDataDAOImpl<T> getInstance(int level, int capacity,
			ParkingStrategy parkingStrategy)
	{
		if (instance == null)
		{
			synchronized (ParkingLevelDataDAOImpl.class)
			{
				if (instance == null)
				{
					instance = new ParkingLevelDataDAOImpl<T>(level, capacity, parkingStrategy);
				}
			}
		}
		return instance;
	}
	
	private ParkingLevelDataDAOImpl(int level, int capacity, ParkingStrategy parkingStrategy)
	{
		this.level.set(level);
		this.capacity.set(capacity);
		this.availability.set(capacity);
		this.parkingStrategy = parkingStrategy;
		if (parkingStrategy == null)
			parkingStrategy = new NearestParkingLotStrategy();
		slotVehicleMap = new ConcurrentHashMap<>();
		for (int i = 1; i <= capacity; i++)
		{
			slotVehicleMap.put(i, Optional.empty());
			parkingStrategy.add(i);
		}
	}
	
	// This method return available slots
	
	@Override
	public int checkAvailableSolts(T vehicle)
	{
		int availableSlot;
		if (availability.get() == 0)
		{
			return Constants.NOT_AVAILABLE;
		}
		else
		{
			availableSlot = parkingStrategy.getSlot();
			if (slotVehicleMap.containsValue(Optional.of(vehicle)))
				return Constants.VEHICLE_ALREADY_EXIST;
			
			slotVehicleMap.put(availableSlot, Optional.of(vehicle));
			availability.decrementAndGet();
			parkingStrategy.removeSlot(availableSlot);
		}
		return availableSlot;
	}
	
	@Override
	public boolean leaveCar(int slotNumber)
	{
		if (!slotVehicleMap.get(slotNumber).isPresent()) // Slot empty
			return false;
		availability.incrementAndGet();
		parkingStrategy.add(slotNumber);
		slotVehicleMap.put(slotNumber, Optional.empty());
		return true;
	}
	
	public int getAvailableSlotsCount()
	{
		return availability.get();
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	@Override
	public void doCleanUp()
	{
		this.level = new AtomicInteger();
		this.capacity = new AtomicInteger();
		this.availability = new AtomicInteger();
		this.parkingStrategy = null;
		slotVehicleMap = null;
		instance = null;
	}
}
