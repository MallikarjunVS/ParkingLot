/**
 * 
 */
package com.gojek.parkinglot.processor;

import com.gojek.parkinglot.constants.CommandInputMap;
import com.gojek.parkinglot.exception.ParkingLotException;
import com.gojek.parkinglot.service.IAbstractService;

/**
 * @author Mallikarjun
 *
 */
public interface IAbstractProcessor
{
	public void setService(IAbstractService service);
	
	public void execute(String action) throws ParkingLotException;
	
	public default boolean validate(String inputString)
	{
		// Split the input string to validate command and input value
		boolean valid = true;
		try
		{
			String[] inputs = inputString.split(" ");
			int params = CommandInputMap.getCommandsParameterMap().get(inputs[0]);
			switch (inputs.length)
			{
				case 1:
					if (params != 0) // e.g status -> inputs = 1
						valid = false;
					break;
				case 2:
					if (params != 1) // create_parking_lot 6 -> inputs = 2
						valid = false;
					break;
				case 3:
					if (params != 2) // park KA-01-P-333 White -> inputs = 3
						valid = false;
					break;
				default:
					valid = false;
			}
		}
		catch (Exception e)
		{
			valid = false;
		}
		return valid;
	}
}
