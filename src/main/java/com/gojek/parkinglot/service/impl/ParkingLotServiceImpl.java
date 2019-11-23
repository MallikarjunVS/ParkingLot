/**
 * 
 */
package com.gojek.parkinglot.service.impl;

import java.util.ArrayList;
import java.util.List;

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
	public void doCleanup()
	{
		if (parkingDataDAO != null)
			parkingDataDAO.doCleanup();
	}
	
}
