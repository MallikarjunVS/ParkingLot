/**
 * 
 */
package com.gojek.parkinglot.processor;

import com.gojek.parkinglot.constants.Constants;
import com.gojek.parkinglot.exception.ErrorCode;
import com.gojek.parkinglot.exception.ParkingLotException;
import com.gojek.parkinglot.service.IAbstractService;
import com.gojek.parkinglot.service.IParkingLotService;

/**
 * @author Mallikarjun
 * 
 */
public class RequestProcessor implements IAbstractProcessor
{
	private IParkingLotService parkingService;
	
	public void setParkingService(IParkingLotService parkingService) throws ParkingLotException
	{
		this.parkingService = parkingService;
	}
	
	@Override
	public void execute(String input) throws ParkingLotException
	{
		int level = 1;
		String[] inputs = input.split(" ");
		String key = inputs[0];
		switch (key)
		{
			case Constants.CREATE_PARKING_LOT:
				try
				{
					int capacity = Integer.parseInt(inputs[1]);
					parkingService.createParkingLot(level, capacity);
				}
				catch (NumberFormatException e)
				{
					throw new ParkingLotException(
							ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "capacity"));
				}
				break;
			case Constants.PARK:
				// ToDo
				break;
			case Constants.LEAVE:
				// ToDo
				break;
			case Constants.STATUS:
				// ToDo
				break;
			case Constants.REG_NUMBER_FOR_CARS_WITH_COLOR:
				// ToDo
				break;
			case Constants.SLOTS_NUMBER_FOR_CARS_WITH_COLOR:
				// ToDo
				break;
			case Constants.SLOTS_NUMBER_FOR_REG_NUMBER:
				// ToDo
				break;
			default:
				break;
		}
	}
	
	@Override
	public void setService(IAbstractService service)
	{
		this.parkingService = (IParkingLotService) service;
	}
}
