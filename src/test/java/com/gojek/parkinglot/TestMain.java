package com.gojek.parkinglot;

/**
 * @author Mallikarjun
 * 
 * 
 *         Test Class
 *
 */

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gojek.parkinglot.exception.ErrorCode;
import com.gojek.parkinglot.exception.ParkingLotException;
import com.gojek.parkinglot.model.Car;
import com.gojek.parkinglot.service.IParkingLotService;
import com.gojek.parkinglot.service.impl.ParkingLotServiceImpl;

/**
 * 
 * @author Mallikarjun Unit test for Parking Lot Application
 */
public class TestMain
{
	private int							parkingLevel;
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void init()
	{
		parkingLevel = 1;
		System.setOut(new PrintStream(outContent));
	}
	
	@After
	public void cleanUp()
	{
		System.setOut(null);
	}
	
	@Test
	public void createParkingLot() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		instance.createParkingLot(parkingLevel, 65);
		assertTrue("createdparkinglotwith65slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	
	@Test
	public void alreadyExistParkingLot() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		instance.createParkingLot(parkingLevel, 65);
		assertTrue("createdparkinglotwith65slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_ALREADY_EXIST.getMessage()));
		instance.createParkingLot(parkingLevel, 65);
		instance.doCleanup();
	}
	
	@Test
	public void testParkingLotCapacity() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 11);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertEquals(3, instance.getAvailableSlotsCount(parkingLevel));
		instance.doCleanup();
	}
	
	@Test
	public void testEmptyParkingLot() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertTrue("Sorry,CarParkingDoesnotExist".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.createParkingLot(parkingLevel, 6);
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nSlotNo.\tRegistrationNo.\tColor\nSorry,parkinglotisempty."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	
	@Test
	public void testParkingLotIsFull() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 2);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertTrue("createdparkinglotwith2slots\\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSorry,parkinglotisfull"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	
}
