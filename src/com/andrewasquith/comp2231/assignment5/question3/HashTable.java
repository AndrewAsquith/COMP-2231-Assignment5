package com.andrewasquith.comp2231.assignment5.question3;

import jsjf.exceptions.ElementNotFoundException;

public class HashTable {

	private static int DEFAULT_INITIAL_SIZE = 31;

	private static double DEFAULT_LOAD_FACTOR = 0.8;

	/**
	 * Protected threshold value accessible to unit tests
	 */
	protected int threshold;

	private HashTableEntry[] hashTable;

	/**
	 * current number of elements in the hashtable
	 */
	private int numberOfElements;

	

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


	private int calculateThreshold(int size, double loadFactor) {
		return (int) (size * loadFactor);
	}

	private void resizeIfNecessary() {

		if (numberOfElements >= threshold) {
			resize();
		}
	}

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
	
	public int size() {
		return numberOfElements;
	}

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

	public boolean contains(String key) {

		// use our findPositionForKey method and return true if
		// a valid index was returned
		return (findPositionForKey(key) != -1);
	}

	public String get(String key) {
		int index = findPositionForKey(key);
		if (index == -1) {
			throw new ElementNotFoundException("HashTable");
		}
		
		return hashTable[index].getValue();
	}

	public String remove(String key) {
		
		int index = findPositionForKey(key);
		if (index == -1) {
			throw new ElementNotFoundException("HashTable");
		}
		
		hashTable[index].markDeleted();
		numberOfElements--;
		return hashTable[index].getValue();
		
	}

	/**
	 * No assumptions are made that SSNs are unique, so finding a match that's
	 * been deleted is not actually considered as a match
	 * 
	 * @param key
	 * @return
	 */
	private int findPositionForKey(String key) {

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

	private int primaryHash(String key) {
		// use the last 4 digits of the SSN
		int keyAsInteger = Integer.parseInt(key.substring(7));
		// use the current hashTable.length since it may have been resized
		return keyAsInteger % hashTable.length;
	}

	private int secondaryHash(String key) {

		// use the first 3 digits of the SSN
		int keyAsInteger = Integer.parseInt(key.substring(0, 3));
		// use the current hashTable.length since it may have been resized
		return keyAsInteger % hashTable.length;
	}

	private class HashTableEntry {

		protected String key;
		protected String value;
		protected boolean isDeleted;

		public HashTableEntry(String socialInsuranceNumber, String name) {
			key = socialInsuranceNumber;
			value = name;
			isDeleted = false;
		}

		public boolean isDeleted() {

			return isDeleted;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public void markDeleted() {
			isDeleted = true;
		}
	}
}
