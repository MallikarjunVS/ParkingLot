/**
 * 
 */
package com.gojek.parkinglot.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gojek.parkinglot.dao.ParkingDataDAO;
import com.gojek.parkinglot.dao.ParkingLevelDataDAO;
import com.gojek.parkinglot.model.Vehicle;
import com.gojek.parkinglot.model.strategy.NearestParkingLotStrategy;
import com.gojek.parkinglot.model.strategy.ParkingStrategy;

/**
 * This class is a singleton class to manage the data of parking system
 * 
 * @author Mallikarjun
 * @param <T>
 */
public class ParkingDataDAOImpl<T extends Vehicle> implements ParkingDataDAO<T>
{
	private Map<Integer, ParkingLevelDataDAO<T>> levelParkingMap;
	
	@SuppressWarnings("rawtypes")
	private static ParkingDataDAOImpl instance = null;
	
	@SuppressWarnings("unchecked")
	public static <T extends Vehicle> ParkingDataDAOImpl<T> getInstance(List<Integer> parkingLevels,
			List<Integer> capacityList, List<ParkingStrategy> parkingStrategies)
	{
		// Make sure the each of the lists are of equal size
		if (instance == null)
		{
			synchronized (ParkingDataDAOImpl.class)
			{
				if (instance == null)
				{
					instance = new ParkingDataDAOImpl<T>(parkingLevels, capacityList, parkingStrategies);
				}
			}
		}
		return instance;
	}
	
	private ParkingDataDAOImpl(List<Integer> parkingLevels, List<Integer> capacityList,
			List<ParkingStrategy> parkingStrategies)
	{
		if (levelParkingMap == null)
			levelParkingMap = new HashMap<>();
		for (int i = 0; i < parkingLevels.size(); i++)
		{
			levelParkingMap.put(parkingLevels.get(i), ParkingLevelDataDAOImpl.getInstance(parkingLevels.get(i),
					capacityList.get(i), new NearestParkingLotStrategy()));
			
		}
	}
	
	@Override
	public int checkAvailableSolts(int level, T vehicle)
	{
		return levelParkingMap.get(level).checkAvailableSolts(vehicle);
	}
	
	@Override
	public boolean leaveCar(int level, int slotNumber)
	{
		return levelParkingMap.get(level).leaveCar(slotNumber);
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	public void doCleanup()
	{
		for (ParkingLevelDataDAO<T> levelDataManager : levelParkingMap.values())
		{
			levelDataManager.doCleanUp();
		}
		levelParkingMap = null;
		instance = null;
	}
}
