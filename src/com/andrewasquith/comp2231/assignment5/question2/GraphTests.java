package com.andrewasquith.comp2231.assignment5.question2;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphTests {

	private Graph<String> graph;
	
	@Before
	public void setUp() throws Exception {
		graph = new Graph<String>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test2VertexSimpleGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		String[] expected = {"A", "B"};
		assertArrayEquals(expected, graph.getVertices());
	}
	
	@Test
	public final void test3VertexGraphWhereOneIsNull() {
		graph.addVertex("A");
		graph.addVertex();
		graph.addVertex("B");
		String[] expected = {"A", null, "B"};
		assertArrayEquals(expected, graph.getVertices());
	}
	
	@Test
	public final void testSizeFor2VertexGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		assertEquals(2, graph.size());
	}
	
	@Test
	public final void testSizeFor5VertexGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex();
		graph.addVertex("C");
		graph.addVertex("D");
		assertEquals(5, graph.size());
	}
	
	@Test
	public final void testIsConnectedForConnectedGraph() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addEdge("A", "B");
		graph.addEdge("B", "C");
		assertTrue(graph.isConnected());
	}
	
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
	
	@Test
	public final void testAdjacencyMatrixFor2VertexSimpleGraphWithEdge() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		boolean[][] expected = {{false, true}, {true, false}};
		assertArrayEquals(expected, graph.getAdjacencyMatrix());
	}
	
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

}
