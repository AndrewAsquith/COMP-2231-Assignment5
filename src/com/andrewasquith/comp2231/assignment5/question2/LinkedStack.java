/**
 * @author Andrew Asquith
 * COMP 2231
 * Assignment 5
 * Question 2 - Implementation of graph with adjacency matrix
 */
package com.andrewasquith.comp2231.assignment5.question2;

/**
 * Import the LinearNode class
 */
import jsjf.LinearNode;
/**
 * Import the StackADT interface 
 */
import jsjf.StackADT;
/**
 * Import the exception we need to throw
 */
import jsjf.exceptions.EmptyCollectionException;

/**
 * Simple linked list implementation of a stack to support the adjacency matrix graph
 *
 * @param <T>
 *            The type of element the stack will hold
 */
public class LinkedStack<T> implements StackADT<T> {

	/**
	 * Counter for number of elements in stack
	 */
	private int counter;

	/**
	 * The top node pointer for the stack
	 */
	private LinearNode<T> top;

	/**
	 * Public constructor, linked list internally so no size restrictions
	 */
	public LinkedStack() {
		counter = 0;
		top = null;
	}

	/**
	 * Pushes a new element onto the stack
	 */
	public void push(T element) {

		// create a new node for the element
		LinearNode<T> node = new LinearNode<T>(element);

		// set the next node to the existing top
		node.setNext(top);

		// point top to the new node
		top = node;

		// increment the counter
		counter++;
	}

	/**
	 * Returns the element at the top of the stack
	 * 
	 * @throws EmptyCollectionException
	 *             if the stack is empty
	 */
	public T pop() throws EmptyCollectionException {

		// throw if the stack is empty
		if (isEmpty()) {
			throw new EmptyCollectionException("stack");
		}

		// get the current top node
		LinearNode<T> node = top;

		// set top to the next node
		top = top.getNext();

		// decrement the counter
		counter--;

		// return the element from the original top node
		return node.getElement();
	}

	/**
	 * Returns a reference to the element at the top of the stack
	 * 
	 * @throws EmptyCollectionException
	 *             if the stack is empty
	 */
	public T peek() throws EmptyCollectionException {

		// if the stack is empty, throw
		if (isEmpty()) {
			throw new EmptyCollectionException("stack");
		}

		// return a reference to the top node element
		return top.getElement();
	}

	/**
	 * Determine if the stack is empty
	 * 
	 * @return true if the stack is currently empty, false if not
	 */
	public boolean isEmpty() {

		// if size is 0 the stack is empty
		return (size() == 0);
	}

	/**
	 * Determine the number of elements in the stack
	 * 
	 * @return the number of elements currently in the stack
	 */
	public int size() {

		// return the counter variable
		return counter;
	}
}
