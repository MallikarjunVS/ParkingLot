package com.gojek.parkinglot;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
	
}
