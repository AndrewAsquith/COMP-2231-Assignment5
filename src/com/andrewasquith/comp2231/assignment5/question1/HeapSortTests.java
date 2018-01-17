package com.andrewasquith.comp2231.assignment5.question1;

import static org.junit.Assert.*;

import org.junit.Test;

public class HeapSortTests {

	@Test
	public final void testAscHeapSortSixElements() {
		
		Integer[] starting = { 18, 21, 11, 5, 17, 9};
		Integer[] expected = { 5, 9, 11, 17, 18, 21};
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.AscendingHeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}
	
	@Test
	public final void testDescHeapSortSixElements() {
		
		Integer[] starting = { 18, 21, 11, 5, 17, 9};
		Integer[] expected = { 21, 18, 17, 11, 9, 5};
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.DescendingHeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}

}
