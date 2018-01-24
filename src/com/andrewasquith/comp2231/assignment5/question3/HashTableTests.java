/**
 * @author Andrew Asquith
 * COMP 2231
 * Assignment 5
 * Question 3
 * Implementation of a Hash Table using double hashing and open addressing
 */
package com.andrewasquith.comp2231.assignment5.question3;

/**
 * Import the JUnit Assertions
 */
import static org.junit.Assert.*;

/**
 * Import the JUnit attributes
 */
import org.junit.Before;
import org.junit.Test;

/**
 * Import the required exception
 */
import jsjf.exceptions.ElementNotFoundException;

/**
 * Set of tests for the hash table implementation
 *
 */
public class HashTableTests {

	/**
	 * The hashtable under test
	 */
	private HashTable hashTable;
	
	@Before
	public void setUp() throws Exception {
		hashTable = new HashTable();
	}

	/**
	 * Test of a single entry, size and retrieval
	 */
	@Test
	public final void testSingleEntry() {
		hashTable.add("555-00-1234", "Jimbo Jones");
		assertEquals(1, hashTable.size());
		assertEquals("Jimbo Jones", hashTable.get("555-00-1234"));
	}
	
	/**
	 * Test size is reported correctly after a resize
	 */
	@Test
	public final void testSizeReportsCorrectlyAfterResize() {
		hashTable = new HashTable(7); //make a smaller hashtable for this test
		hashTable.add("555-00-1234", "Jimbo Jones");
		hashTable.add("555-00-5678", "Montgomery Burns");
		hashTable.add("123-00-4567", "Apu Nahasapeemapetilon");
		hashTable.add("123-00-1234", "Moe Szyslak"); // this should cause a collision
		assertEquals(4, hashTable.size());
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); //should have resized by now
		hashTable.add("999-00-3333", "Bart Simpson");
		hashTable.add("999-00-4444", "Lisa Simspon"); // exceed original capacity
		assertEquals(8, hashTable.size());
	}
	
	/**
	 * Test size is reported correctly after a removal
	 */
	@Test
	public final void testSizeReportsCorrectlyAfterRemoval() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("999-00-3333", "Bart Simpson");
		hashTable.add("999-00-4444", "Lisa Simspon");
		hashTable.remove("999-00-1111");
		assertEquals(3, hashTable.size());
	}
	
	/**
	 * Test contains returns true if element is present
	 */
	@Test
	public final void testContainsReturnsTrueWhenElementIsPresent() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("999-00-3333", "Bart Simpson");
		hashTable.add("999-00-4444", "Lisa Simspon");
		assertTrue(hashTable.contains("999-00-2222"));
	}
	
	/**
	 * Test contains returns false if element is not present
	 */
	@Test
	public final void testContainsReturnsFalseWhenElementNotPresent() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("999-00-3333", "Bart Simpson");
		hashTable.add("999-00-4444", "Lisa Simspon");
		assertFalse(hashTable.contains("123-45-6789"));
	}

	/**
	 * Test entries that create a single collision can
	 * be retrieved
	 */
	@Test
	public final void testSingleCollision() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("888-00-2222", "Bart Simpson"); //single collision
		hashTable.add("999-00-4444", "Lisa Simspon");
		hashTable.add("999-00-5555", "Maggie Simpson");
		assertEquals("Marg Simpson", hashTable.get("999-00-2222"));
		assertEquals("Bart Simpson", hashTable.get("888-00-2222"));
	}
	
	/**
	 * Test entries that are involved in a double collision
	 * can be retrieved demonstrating double hashing works
	 */
	@Test
	public final void testDoubleCollision() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("888-00-2222", "Bart Simpson"); //single collision
		hashTable.add("999-00-4444", "Lisa Simspon");
		hashTable.add("999-11-2222", "Maggie Simpson"); //double collision
		// should be able to retrieve all 5 values
		assertEquals("Marg Simpson", hashTable.get("999-00-2222"));
		assertEquals("Bart Simpson", hashTable.get("888-00-2222"));
		assertEquals("Maggie Simpson", hashTable.get("999-11-2222"));
	}
	
	/**
	 * Test entries involved in a quadruple collision 
	 * can be retrieved demonstrating open addressing works
	 */
	@Test
	public final void testQuadrupleCollision() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("888-00-2222", "Bart Simpson"); //single collision
		hashTable.add("999-00-4444", "Lisa Simspon");
		hashTable.add("999-11-2222", "Maggie Simpson"); //double collision
		hashTable.add("999-22-2222", "Grandpa Simpson"); // triple collision
		hashTable.add("999-33-2222", "Jacqueline Bouvier");
		assertEquals("Marg Simpson", hashTable.get("999-00-2222"));
		assertEquals("Bart Simpson", hashTable.get("888-00-2222"));
		assertEquals("Maggie Simpson", hashTable.get("999-11-2222"));
		assertEquals("Grandpa Simpson", hashTable.get("999-22-2222"));
		assertEquals("Jacqueline Bouvier", hashTable.get("999-33-2222"));
	}
	
	/**
	 * Test open addressing and removal still allows retrieval of the
	 * entry that is placed via open addressing
	 */
	@Test 
	public final void testCanStillGetEntryAfterQuadrupleCollisionAndDeletions() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("888-00-2222", "Bart Simpson"); //single collision
		hashTable.add("999-00-4444", "Lisa Simspon");
		hashTable.add("999-11-2222", "Maggie Simpson"); //double collision
		hashTable.add("999-22-2222", "Grandpa Simpson"); // triple collision
		hashTable.add("999-33-2222", "Jacqueline Bouvier");
		hashTable.remove("999-00-2222");
		hashTable.remove("888-00-2222");
		hashTable.remove("999-11-2222");
		hashTable.remove("999-22-2222");
		assertEquals("Jacqueline Bouvier", hashTable.get("999-33-2222"));
	}
	
	/**
	 * Test that finding an element works after removing a collided with entry
	 */
	@Test
	public final void testFindingElementAfterRemovingCollision() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("888-00-2222", "Bart Simpson"); //single collision
		hashTable.add("999-00-4444", "Lisa Simspon");
		hashTable.remove("999-00-2222"); // remove the collided with value
		assertEquals("Bart Simpson", hashTable.get("888-00-2222"));
	}
	
	/**
	 * Test that removing an element and then adding one that would collide
	 * works
	 */
	@Test
	public final void testRemovingElementAndThenAddingOneThatWouldCollide() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson");
		hashTable.remove("999-00-2222");
		hashTable.add("888-00-2222", "Bart Simpson");
		assertFalse(hashTable.contains("999-00-2222"));
		hashTable.add("999-00-4444", "Lisa Simspon");
		assertEquals("Bart Simpson", hashTable.get("888-00-2222"));
	}
	
	/**
	 * Test that attempting to get a non-existent entry
	 * throws ElementNotFoundException
	 */
	@Test(expected = ElementNotFoundException.class) 
	public final void testDoesNotFindElementThatDoesNotExist() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.get("555-00-1234"); // this should throw
	}
	
	/**
	 * Test that not finding an element because it was removed
	 * throws ElementNotFoundException
	 */
	@Test(expected = ElementNotFoundException.class) 
	public final void testDoesNotFindElementThatDoesNotExistBecauseItWasRemoved() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.remove("999-00-1111");
		hashTable.add("888-00-2345", "Bart Simpson");
		hashTable.get("999-00-1111"); // this should throw
	}
	
	/**
	 * Test the hash table enforces the format of the social
	 * security number by throwing IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAddThrowsWhenNotASocialSecurityNumber() {
		hashTable.add("Bogus SSN", "Mr Fail");
	}
	
	/**
	 * Test that contains returns false on a non-existant entry
	 * when said entry would be a triple collision and placed
	 * by open addressing
	 */
	@Test
	public final void testContainsReturnsFalseAfterDoubleCollision() {
		hashTable.add("999-00-1111", "Homer Simpson");
		hashTable.add("999-00-2222", "Marg Simpson"); 
		hashTable.add("888-00-2222", "Bart Simpson"); //single collision
		hashTable.add("999-00-4444", "Lisa Simspon");
		hashTable.add("999-11-2222", "Maggie Simpson"); //double collision
		assertFalse(hashTable.contains("999-22-2222"));
	}
}
