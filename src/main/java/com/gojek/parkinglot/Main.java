package com.gojek.parkinglot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.owasp.esapi.reference.Log4JLogger;

import com.gojek.parkinglot.exception.ErrorCode;
import com.gojek.parkinglot.exception.ParkingLotException;
import com.gojek.parkinglot.processor.AbstractProcessor;
import com.gojek.parkinglot.processor.RequestProcessor;
import com.gojek.parkinglot.service.impl.ParkingServiceImpl;

/**
 * @author Mallikarjun
 * 
 * 
 *         Main class for the 1) Interactive Mode 2) File I/O
 *
 */
public class Main
{
	public static void main(String[] args)
	{
		Logger logger = Log4JLogger.getLogger(Main.class);
		AbstractProcessor processor = new RequestProcessor();
		processor.setService(new ParkingServiceImpl());
		BufferedReader bufferReader = null;
		String input = null;
		try
		{
			System.out.println("\n\n\n\n\n");
			System.out.println("===================================================================");
			System.out.println("===================      GOJEK PARKING LOT APP     ====================");
			System.out.println("===================================================================");
			printUsage();
			switch (args.length)
			{
				case 0: // Interactive Mode : please provide command-line input/output
				{
					System.out.println("Please Enter 'exit' to end Execution");
					System.out.println("Input:");
					while (true)
					{
						try
						{
							bufferReader = new BufferedReader(new InputStreamReader(System.in));
							input = bufferReader.readLine().trim();
							if (input.equalsIgnoreCase("exit"))
							{
								break;
							}
							else
							{
								if (processor.validate(input))
								{
									try
									{
										processor.execute(input.trim());
									}
									catch (Exception e)
									{
										System.out.println(e.getMessage());
									}
								}
								else
								{
									printUsage();
								}
							}
						}
						catch (Exception e)
						{
							throw new ParkingLotException(ErrorCode.INVALID_REQUEST.getMessage(), e);
						}
					}
					break;
				}
				case 1:// File I/O
				{
					File inputFile = new File(args[0]);
					try
					{
						bufferReader = new BufferedReader(new FileReader(inputFile));
						int lineNo = 1;
						while ((input = bufferReader.readLine()) != null)
						{
							input = input.trim();
							if (processor.validate(input))
							{
								try
								{
									processor.execute(input);
								}
								catch (Exception e)
								{
									logger.error(e.getMessage(), e);
								}
							}
							else
								logger.info("Incorrect Command Found at line: " + lineNo + " ,Input: " + input);
							lineNo++;
						}
					}
					catch (Exception e)
					{
						throw new ParkingLotException(ErrorCode.INVALID_FILE.getMessage(), e);
					}
					break;
				}
				default:
					System.out.println("Invalid input. Usage Style: java -jar <jar_file_path> <input_file_path>");
			}
		}
		catch (ParkingLotException e)
		{
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (bufferReader != null)
					bufferReader.close();
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	private static void printUsage()
	{
		StringBuffer buffer = new StringBuffer();
		buffer = buffer.append(
				"--------------Please Enter one of the below commands. {variable} to be replaced -----------------------")
				.append("\n");
		buffer = buffer.append("A) For creating parking lot of size n               ---> create_parking_lot {capacity}")
				.append("\n");
		buffer = buffer
				.append("B) To park a car                                    ---> park <<car_number>> {car_clour}")
				.append("\n");
		buffer = buffer.append("C) Remove(Unpark) car from parking                  ---> leave {slot_number}")
				.append("\n");
		buffer = buffer.append("D) Print status of parking slot                     ---> status").append("\n");
		buffer = buffer.append(
				"E) Get cars registration no for the given car color ---> registration_numbers_for_cars_with_color {car_color}")
				.append("\n");
		buffer = buffer.append(
				"F) Get slot numbers for the given car color         ---> slot_numbers_for_cars_with_color {car_color}")
				.append("\n");
		buffer = buffer.append(
				"G) Get slot number for the given car number         ---> slot_number_for_registration_number {car_number}")
				.append("\n");
		System.out.println(buffer.toString());
	}
}
