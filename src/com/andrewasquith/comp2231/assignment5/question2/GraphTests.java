/**
 * @author Andrew Asquith
 * COMP 2231
 * Assignment 5
 * Question 2
 * Implementation of a Graph using an adjacency matrix
 */
package com.andrewasquith.comp2231.assignment5.question2;

/**
 * Import the JUnit Assertions
 */
import static org.junit.Assert.*;

/**
 * Import the JUnit Attributes
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Import the Iterator from the API
 */
import java.util.Iterator;

/**
 * Import required exceptions
 */
import jsjf.exceptions.ElementNotFoundException;
import jsjf.exceptions.EmptyCollectionException;

/**
 * Set of tests exercising the Graph functionality
 * Only methods which were not previously implemented 
 * are tested 
 *
 */
public class GraphTests {

	/**
	 * The graph under test
	 */
	private Graph<String> graph;
	
	@Before
	public void setUp() throws Exception {
		graph = new Graph<String>();
	}

	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * Test of a 2 vertex graph with one edge between them
	 */
	@Test
	public final void test2VertexSimpleGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		String[] expected = {"A", "B"};
		assertArrayEquals(expected, graph.getVertices());
	}
	
	
	/**
	 * Test of a 3 vertex graph where one is null
	 */
	@Test
	public final void test3VertexGraphWhereOneIsNull() {
		graph.addVertex("A");
		graph.addVertex();
		graph.addVertex("B");
		String[] expected = {"A", null, "B"};
		assertArrayEquals(expected, graph.getVertices());
	}
	
	
	/**
	 * Test of the size method for a 2 vertex graph
	 */
	@Test
	public final void testSizeFor2VertexGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		assertEquals(2, graph.size());
	}
	
	
	/**
	 * Test of the size method for a 5 vertex graph
	 */
	@Test
	public final void testSizeFor5VertexGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex();
		graph.addVertex("C");
		graph.addVertex("D");
		assertEquals(5, graph.size());
	}
	
	
	/**
	 * Test of isConnected for a connected graph
	 */
	@Test
	public final void testIsConnectedForConnectedGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addEdge("A", "B");
		graph.addEdge("B", "C");
		assertTrue(graph.isConnected());
	}
	
	
	/**
	 * Test of isConnected for a more complex connected graph
	 */
	@Test
	public final void testIsConnectedForAlternativeConnectedGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A",  "B");
		graph.addEdge("B",  "C");
		graph.addEdge("B", "D");
		assertTrue(graph.isConnected());
	}
	
	
	/**
	 * Test of isConnected for a disconnected graph
	 */
	@Test
	public final void testIsConnectedForDisconnectedGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A",  "B");
		graph.addEdge("B",  "C");
		assertFalse(graph.isConnected());
	}
	
	/**
	 * Test of isConnected for graph which changes from 
	 * connected to disconnected
	 */
	@Test
	public final void testIsConnectedChangesFromConnectedToNot() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A",  "B");
		graph.addEdge("B",  "C");
		graph.addEdge("B", "D");
		assertTrue(graph.isConnected());
		graph.removeEdge("A", "B");
		assertFalse(graph.isConnected());
	}
	
	/**
	 * Test of the adjacency matrix after removing an edge
	 */
	@Test
	public final void testAdjacencyMatrixAfterRemovingEdge() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A",  "B");
		graph.addEdge("B",  "C");
		graph.addEdge("B", "D");
		boolean[][] expectedInitial = {{false, true, false, false}, 
					{true, false, true, true}, {false, true, false, false},
					{false, true, false, false}};
		assertArrayEquals(expectedInitial, graph.getAdjacencyMatrix());
		
		boolean[][] expectedFinal = {{false, true, false, false},
				{true, false, false, true}, {false, false, false, false},
				{false, true, false, false}};
		graph.removeEdge("B", "C");
		assertArrayEquals(expectedFinal, graph.getAdjacencyMatrix());
		
	}
	
	/**
	 * Test of the iterator after removing an edge
	 */
	@Test
	public final void testIteratorAfterRemovingEdge() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A",  "B");
		graph.addEdge("B",  "C");
		graph.addEdge("B", "D");
		
		Iterator<String> initialIterator = graph.iteratorBFS("A");
		assertEquals("A", initialIterator.next());
		assertEquals("B", initialIterator.next());
		assertEquals("C", initialIterator.next());
		assertEquals("D", initialIterator.next());
		
		graph.removeEdge("B", "C");
		Iterator<String> postRemovalIterator = graph.iteratorBFS("A");
		assertEquals("A", postRemovalIterator.next());
		assertEquals("B", postRemovalIterator.next());
		assertEquals("D", postRemovalIterator.next());
		assertFalse(postRemovalIterator.hasNext());
	}
	
	/**
	 * Test of the Adjacency Matrix for a simple 2 vertex
	 * graph with an edge between them
	 */
	@Test
	public final void testAdjacencyMatrixFor2VertexSimpleGraphWithEdge() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		boolean[][] expected = {{false, true}, {true, false}};
		assertArrayEquals(expected, graph.getAdjacencyMatrix());
	}
	
	/**
	 * Test of the iterator for a simple 2 vertex graph
	 * with an edge between them
	 */
	@Test
	public final void testIteratorFor2VertexSimpleGraphWithEdge() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		
		Iterator<String> iterator = graph.iteratorBFS("A");
		assertTrue(iterator.hasNext());
		assertEquals("A", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("B", iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	/**
	 * Test of the iterator after removing a vertex
	 */
	@Test
	public final void testIteratorFor4VertexGraphAfterRemovingOne() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A",  "B");
		graph.addEdge("B",  "C");
		graph.addEdge("B", "D");
		graph.removeVertex("C");
		Iterator<String> initialIterator = graph.iteratorBFS("A");
		assertEquals("A", initialIterator.next());
		assertEquals("B", initialIterator.next());
		assertEquals("D", initialIterator.next());
	}
	
	/**
	 * Test ensuring EmptyCollectionException is thrown
	 * when trying to remove an edge on an empty graph
	 */
	@Test(expected = EmptyCollectionException.class)
	public final void testRemoveEdgeThrowsOnEmptyCollection() {
		graph.removeEdge(1, 2);
	}
	
	/**
	 * Test ensuring ElementNotFoundException is thrown
	 * when removing an edge that contains a non
	 * existant vertex
	 */
	@Test(expected = ElementNotFoundException.class) 
	public final void testRemoveEdgeThrowsWhenAVertexIsInvalid() 	{
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addEdge("A", "B");
		graph.removeEdge("A", "D");
	}
	
	/**
	 * Test ensuring EmptyCollectionException is thrown
	 * when removing a vertex from an empty graph
	 */
	@Test(expected = EmptyCollectionException.class) 
	public final void testRemoveVertexThrowsOnEmptyCollection() {
		graph.removeVertex("B");
	}

}
