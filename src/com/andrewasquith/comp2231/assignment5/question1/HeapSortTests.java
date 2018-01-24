/**
 * @author Andrew Asquith
 * COMP 2231
 * Assignment 5
 * Question 1 
 * In place Heap Sort Implementation
 */
package com.andrewasquith.comp2231.assignment5.question1;

/**
 * Import the JUnit Assertions
 */
import static org.junit.Assert.*;

/**
 * Import the JUnit attributes
 */
import org.junit.Test;

/**
 * 
 * Class to exercise the in place heap sort
 *
 */
public class HeapSortTests {

	
	/**
	 * Test a heap sort with six elements
	 */
	@Test
	public final void testHeapSortSixElements() {
		
		Integer[] starting = { 18, 21, 11, 5, 17, 9};
		Integer[] expected = { 5, 9, 11, 17, 18, 21};
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.HeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}
	
	
	/**
	 * Test a heap sort with nine elements
	 */
	@Test
	public final void testHeapSortNineElements() {
		Integer[] starting = { 13, 11, 15, 16, 7, 3, 21, 5, 18 };
		Integer[] expected = { 3, 5, 7, 11, 13, 15, 16, 18, 21 };
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.HeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}
	
	
	/**
	 * Test a heap sort with nine elements where one is duplicated
	 */
	@Test
	public final void testHeapSortNineElementsWithOneDuplicate() {
		Integer[] starting = { 13, 11, 15, 16, 7, 3, 21, 5, 16 };
		Integer[] expected = { 3, 5, 7, 11, 13, 15, 16, 16, 21 };
		
		HeapSort<Integer> heaper = new HeapSort<Integer>();
		heaper.HeapSort(starting);
		
		assertArrayEquals(expected, starting);
	}

}
