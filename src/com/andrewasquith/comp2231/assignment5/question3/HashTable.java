/**
 * @author Andrew Asquith
 * COMP 2231
 * Assignment 5
 * Question 3
 * Implementation of a Hash Table using double hashing and open addressing
 */
package com.andrewasquith.comp2231.assignment5.question3;

/**
 * Import the required exception class
 */
import jsjf.exceptions.ElementNotFoundException;

/**
 * 
 * Hash Table implementation storing a defined element consisting
 * of a social security number as key and name as value
 * The implementation uses double hashing via extraction and 
 * division on parts of the social security number and open addressing
 *
 */
public class HashTable {

	/**
	 * Default initial size of 31
	 */
	private static int DEFAULT_INITIAL_SIZE = 31;

	/**
	 * Default load factor of 0.8
	 */
	private static double DEFAULT_LOAD_FACTOR = 0.8;

	/**
	 * private calculated current resizing threshold value
	 */
	private int threshold;

	/**
	 * The array storing the hash table entries
	 */
	private HashTableEntry[] hashTable;

	/**
	 * current number of elements in the hashtable
	 */
	private int numberOfElements;

	

	/**
	 * Public constructor using default size and load factor
	 */
	public HashTable() {

		hashTable = new HashTableEntry[DEFAULT_INITIAL_SIZE];
		threshold = calculateThreshold(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Protected Constructor allowing unit tests to create a smaller table
	 * to exercise functionality like resize and threshold without going
	 * all the way to DEFAULT_SIZE
	 * @param initialSize
	 */
	protected HashTable(int initialSize) {
		hashTable = new HashTableEntry[initialSize];
		threshold = calculateThreshold(initialSize, DEFAULT_LOAD_FACTOR);
	}


	/**
	 * Helper method to determine the threshold value based on 
	 * @param size of the internal array
	 * @param loadFactor to be used to determine resize threshold
	 * @return the number of elements to hold before resize
	 */
	private int calculateThreshold(int size, double loadFactor) {
		return (int) (size * loadFactor);
	}

	/**
	 * Private helper method that checks the size compared
	 * to the threshold and calls resize if necessary
	 */
	private void resizeIfNecessary() {

		if (numberOfElements >= threshold) {
			resize();
		}
	}

	/**
	 * Private helper method to resize the internal array to
	 * double it's current size
	 * Any elements that have been removed are discarded and 
	 * all entries are re-hashed as they are converted
	 */
	private void resize() {

		// double size of array
		HashTableEntry[] originalTable = hashTable;
		int newSize = originalTable.length * 2;
		hashTable = new HashTableEntry[newSize];

		// rehash contents
		// since we're not implementing an iterator in
		// the hashtable (which would have to brute force)
		// we're just going to brute for the indexes
		// and re-hash anything that is not deleted
		// and copy it to it's new position
		for (int i = 0; i < originalTable.length; i++) {
			if (originalTable[i] != null && !originalTable[i].isDeleted()) {
				String key = originalTable[i].getKey();
				int newPosition = calculatePositionForKey(key);
				hashTable[newPosition] = originalTable[i];
			}
		}

		// recalculate threshold
		threshold = calculateThreshold(newSize, DEFAULT_LOAD_FACTOR);

	}
	
	/**
	 * Public accesor for the current number of elements 
	 * @return the number of elements in the hash table
	 */
	public int size() {
		return numberOfElements;
	}

	/**
	 * Adds a new entry to the hash table
	 * @param socialSecurityNumber the key to be used
	 * @param name the value to be stored
	 */
	public void add(String socialSecurityNumber, String name) {
		// all ssns should follow the format AAA-BB-CCCC
		String ssnPattern = "\\d{3}-\\d{2}-\\d{4}";
		if (!socialSecurityNumber.matches(ssnPattern)) {
			throw new IllegalArgumentException("SSN should be of the format ###-##-####");
		}

		// get a position for this entry
		int entryPosition = calculatePositionForKey(socialSecurityNumber);

		// store the entry in our entry class
		hashTable[entryPosition] = new HashTableEntry(socialSecurityNumber, name);

		// increase the number of elements
		numberOfElements++;

		// resize and rehash if necessary
		resizeIfNecessary();

	}

	/**
	 * Determines if a given key is present in the hash table
	 * @param key the key to search for
	 * @return a boolean representing if the key exists
	 */
	public boolean contains(String key) {

		// use our findPositionForKey method and return true if
		// a valid index was returned
		return (findPositionForKey(key) != -1);
	}

	/**
	 * Return the value assoicated with a key
	 * @param key the key to search for
	 * @return the value associated with that key
	 * @throws ElementNotFoundException if the key does not exist
	 */
	public String get(String key) {
		//search for the index for the given key
		int index = findPositionForKey(key);
		//if it was not found, throw
		if (index == -1) {
			throw new ElementNotFoundException("HashTable");
		}
		
		// return the value
		return hashTable[index].getValue();
	}

	/**
	 * Removes a key/value pair from the hash table
	 * @param key the key of the element to remove
	 * @return the value of the removed element
	 */
	public String remove(String key) {
		
		//search for the idnex for the given key
		int index = findPositionForKey(key);
		//if it was not found, throw
		if (index == -1) {
			throw new ElementNotFoundException("HashTable");
		}
		
		// mark the element deleted as to not break searching until rehash
		hashTable[index].markDeleted();
		
		// decrement the number of elements in the hash table
		numberOfElements--;
		
		//return the value of the element
		return hashTable[index].getValue();
		
	}

	/**
	 * Find the index in the array containing the key unless
	 * that element is marked as deleted
	 * 
	 * @param key the key to search for
	 * @return the index in the array where the element can be found
	 */
	private int findPositionForKey(String key) {

		// we return -1 if it's not found
		int NOT_FOUND = -1;

		// do the initial hash routine
		int initialPosition = primaryHash(key);

		// if the first entry is null, key does not exist
		if (hashTable[initialPosition] == null) {
			return NOT_FOUND;
		}

		// if the first entry is not null or deleted and the keys are equal
		if (hashTable[initialPosition].getKey().equals(key) && !hashTable[initialPosition].isDeleted()) {
			return initialPosition;
		}

		// double hash to get the second possible position
		int secondPosition = (initialPosition + secondaryHash(key)) % hashTable.length;

		// if the second position is empty, key does not exist
		if (hashTable[secondPosition] == null) {
			return NOT_FOUND;
		}

		// if the second position contains the entry we want, return it
		if (hashTable[secondPosition].getKey().equals(key) && !hashTable[secondPosition].isDeleted()) {
			return secondPosition;
		}

		// key did not exist at first or second possibilities, need to check the rest
		// using an open addressing approach of h(e) = p1 + (i * p2) where i >= 2

		int counter = 2;
		// still need to divide by total length so we don't run out of bounds
		int nextPosition = (initialPosition + (counter * secondPosition)) % hashTable.length;

		// while the next position is not null we have a candidate
		while (hashTable[nextPosition] != null) {

			// if that position holds what we want return it and is not deleted
			if (hashTable[nextPosition].getKey().equals(key) && !hashTable[nextPosition].isDeleted()) {
				return nextPosition;
			}

			// increment the counter
			counter++;
			// and update the open addressing attempt
			nextPosition = (initialPosition + (counter * secondPosition)) % hashTable.length;
		}

		// hit a null position doing open addressing, therefore we
		// could not find the requested key, return NOT_FOUND
		return NOT_FOUND;

	}

	/**
	 * Calculates the index to use for inserting a new entry into the hash table
	 * @param key the key to be hashed 
	 * @return the index to be used for this key
	 */
	private int calculatePositionForKey(String key) {

		int initialPosition = primaryHash(key);
		int secondPosition = initialPosition;
		int finalPosition = initialPosition;

		// collision on primary hash routine
		// entry is not null and has not been deleted
		if (hashTable[initialPosition] != null && !hashTable[initialPosition].isDeleted()) {
			secondPosition = (initialPosition + secondaryHash(key)) % hashTable.length;
			finalPosition = secondPosition;
		}

		// loop through possible positions until one is found; if original
		// double hashed value was valid the loop will be skipped
		// if a value has been deleted we can re-use it without breaking
		// searching for colliding values
		int counter = 2;
		while (hashTable[finalPosition] != null && !hashTable[finalPosition].isDeleted()) {
			// still need modulous so we don't run out of bounds
			finalPosition = (initialPosition + (counter * secondPosition)) % hashTable.length;
			counter++;
		}

		// because we are guaranteed to find an open position if we're here it's
		// correct
		return finalPosition;
	}

	/**
	 * The primary hash routine, uses the last 4 digits of the 
	 * social security number, which is used as the key in 
	 * this hash table implementation
	 * @param key the social security number to extract digits from
	 * @return the hash code for this key 
	 */
	private int primaryHash(String key) {
		// use the last 4 digits of the SSN
		int keyAsInteger = Integer.parseInt(key.substring(7));
		// use the current hashTable.length since it may have been resized
		return keyAsInteger % hashTable.length;
	}

	/**
	 * The secondary hash routine, which uses the first 3 digits
	 * of the social security number, which is used as the key
	 * in this hash table implementation
	 * @param key the social security number to extract digits from
	 * @return the hashcode for this key
	 */
	private int secondaryHash(String key) {

		// use the first 3 digits of the SSN
		int keyAsInteger = Integer.parseInt(key.substring(0, 3));
		// use the current hashTable.length since it may have been resized
		return keyAsInteger % hashTable.length;
	}

	/**
	 * 
	 * Private helper class to store hash table entries
	 * Consists of the key/value pair as well as an indicator 
	 * if the entry has been removed from the hash table
	 */
	private class HashTableEntry {

		/**
		 * The key of the entry (social security number)
		 */
		protected String key;
		
		/**
		 * The value of the entry (name)
		 */
		protected String value;
		
		/**
		 * Indicator if the entry was deleted/removed from the table
		 */
		protected boolean isDeleted;

		public HashTableEntry(String socialInsuranceNumber, String name) {
			key = socialInsuranceNumber;
			value = name;
			isDeleted = false;
		}

		/**
		 * Public accessor for deleted state
		 * @return true if deleted, false if not
		 */
		public boolean isDeleted() {

			return isDeleted;
		}

		/**
		 * Accessor for the key value
		 * @return the social security number used as key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * Accessor for the value of the entry
		 * @return the name used as the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Marks the entry as deleted/removed from the hash table
		 */
		public void markDeleted() {
			isDeleted = true;
		}
	}
}
