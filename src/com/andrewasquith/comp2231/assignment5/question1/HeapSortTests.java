package com.andrewasquith.comp2231.assignment5.question1;

import static org.junit.Assert.*;

import org.junit.Test;

public class HeapSortTests {

	@Test
	public final void testHeapSortSixElements() {
		
		Integer[] starting = { 18, 21, 11, 5, 17, 9};
		Integer[] expected = { 5, 9, 11, 17, 18, 21};
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.HeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}
	
	@Test
	public final void testHeapSortNineElements() {
		Integer[] starting = { 13, 11, 15, 16, 7, 3, 21, 5, 18 };
		Integer[] expected = { 3, 5, 7, 11, 13, 15, 16, 18, 21 };
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.HeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}
	
	@Test
	public final void testHeapSortNineElementsWithOneDuplicate() {
		Integer[] starting = { 13, 11, 15, 16, 7, 3, 21, 5, 16 };
		Integer[] expected = { 3, 5, 7, 11, 13, 15, 16, 16, 21 };
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.HeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}

}
