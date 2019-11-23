package com.gojek.parkinglot;

import org.apache.log4j.Logger;
import org.owasp.esapi.reference.Log4JLogger;

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
		logger.info("Welcome to Parking Lot");
		
	}
}
