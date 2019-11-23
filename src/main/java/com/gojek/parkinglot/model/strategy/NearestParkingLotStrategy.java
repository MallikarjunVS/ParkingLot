/**
 * 
 */
package com.gojek.parkinglot.model.strategy;

import java.util.TreeSet;

/**
 * @author Mallikarjun
 *
 */
public class NearestParkingLotStrategy implements ParkingStrategy
{
	private TreeSet<Integer> freeSlots;
	
	public NearestParkingLotStrategy()
	{
		freeSlots = new TreeSet<Integer>();
	}
	
	@Override
	public void add(int i)
	{
		freeSlots.add(i);
	}
	
	@Override
	public int getSlot()
	{
		return freeSlots.first();
	}
	
	@Override
	public void removeSlot(int availableSlot)
	{
		freeSlots.remove(availableSlot);
	}
}
