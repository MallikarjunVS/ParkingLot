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
		instance.parkVehicle(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertEquals(2, instance.getAvailableSlotsCount(parkingLevel));
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
		instance.parkVehicle(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertTrue("createdparkinglotwith2slots\\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSorry,parkinglotisfull"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	
	@Test
	public void leave() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.unParkVehicle(parkingLevel, 2);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 6);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-7777", "Red"));
		instance.unParkVehicle(parkingLevel, 4);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nAllocatedslotnumber:3\nSlotnumber4isfree"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	
	@Test
	public void testWhenVehicleAlreadyPresent() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 3);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-7777", "Red"));
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith3slots\nAllocatedslotnumber:1\nSorry,vehicleisalreadyparked."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	
	@Test
	public void testIfVehicleAlreadyPickedUp() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 99);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-7777", "White"));
		instance.unParkVehicle(parkingLevel, 1);
		instance.unParkVehicle(parkingLevel, 1);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith99slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotnumberisEmptyAlready."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	
	@Test
	public void testVehicleSlotStatus() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 8);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-7777", "White"));
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith8slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\tKA-01-HH-1234\tWhite\n2\tKA-01-HH-7777\tWhite"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
		
	}
	
	@Test
	public void testGetVehicleSlotsByRegistrationNumber() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 10);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-3141");
		assertEquals("Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith10slots\n" + "\n"
				+ "Allocatedslotnumber:1\n" + "\n" + "Allocatedslotnumber:2\n1\nNotFound",
				outContent.toString().trim().replace(" ", ""));
		// instance.doCleanup();
	}
	
	@Test
	public void testGetVehicleSlotsByColor() throws Exception
	{
		IParkingLotService instance = new ParkingLotServiceImpl();
		thrown.expect(ParkingLotException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getRegNumberForColor(parkingLevel, "white");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 7);
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.parkVehicle(parkingLevel, new Car("KA-01-HH-7777", "Red"));
		instance.getStatus(parkingLevel);
		instance.getRegNumberForColor(parkingLevel, "Cyan");
		assertEquals(
				"Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith7slots\n" + "\n" + "Allocatedslotnumber:1\n"
						+ "\n" + "Allocatedslotnumber:2\nKA-01-HH-1234,KA-01-HH-7777",
				outContent.toString().trim().replace(" ", ""));
		instance.getRegNumberForColor(parkingLevel, "Red");
		instance.doCleanup();
		
	}
}
