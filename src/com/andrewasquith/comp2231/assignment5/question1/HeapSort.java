package com.andrewasquith.comp2231.assignment5.question1;

public class HeapSort<T> {

	/**
	 * Performs a Heap Sort returning the array in ascending order
	 * 
	 * @param data
	 */
	public void AscendingHeapSort(T[] data) {

		// start at first non leaf node
		// would be length - 1 / 2
		int start = (data.length - 1) / 2;

		// build the heap from the existing elements
		// need to build a max heap since we want a ascending sort order as
		// result and we're going to eventually treat the array as two parts
		// unsorted heap - | sorted
		// [new root][][][][] | [][largest]
		// otherwise we'd have to shift the sorted on every removal
		// or alternatively we could have reversed the resulting array
		// but both methods would be less efficient
		for (int i = start; i >= 0; i--) {
			maxHeapify(data, i, data.length - 1);
		}

		// loop over array again
		// remove max by moving it to the sorted portion
		// then re-heapify what remains in the heap
		// equivalent to the traditional "remove" operation

		for (int i = 0; i < data.length; i++) {

			// shrink the heap portion of the array
			int last = data.length - (i + 1);

			// remove max and store temporarily
			T temp = data[0];

			// store the new root
			data[0] = data[last];

			// store the next max at the end
			data[last] = temp;

			// reheapify what remains
			maxHeapify(data, 0, last - 1);

		}

	}

	/**
	 * Adjust the subtree starting at root and ending to end to be a valid heap
	 * 
	 * @param data
	 * @param root
	 * @param end
	 */
	private void maxHeapify(T[] data, int root, int end) {

		int i = root;
		int left = 2 * i + 1;
		int right = 2 * (i + 1);

		// assume parent largest to start
		int largest = i;

		// if left child is within bounds and larger than root make it largest
		if (left <= end && (((Comparable) data[largest]).compareTo(data[left]) < 0)) {
			largest = left;
		}

		// if right child is within bounds and larger than left and root make it
		// largest
		if (right <= end && (((Comparable) data[largest]).compareTo(data[right]) < 0)) {
			largest = right;
		}

		// if the parent is not the largest, swap
		if (largest != i) {
			T temp = data[largest];
			data[largest] = data[i];
			data[i] = temp;

			// then need to heapify the subtree after the swap
			maxHeapify(data, largest, end);
		}
		

	}

	private void minHeapify(T[] data, int root, int end) {

		int i = root;
		int left = 2 * i + 1;
		int right = 2 * (i + 1);

		// assume parent smallest to start
		int smallest = i;

		// if left child is within bounds and smaller than root make it smaller
		if (left <= end && (((Comparable) data[smallest]).compareTo(data[left]) > 0)) {
			smallest = left;
		}

		// if right child is within bounds and smaller than left and root make
		// it smallest
		if (right <= end && (((Comparable) data[smallest]).compareTo(data[right]) > 0)) {
			smallest = right;
		}

		// if the parent is not the largest, swap
		if (smallest != i) {
			T temp = data[smallest];
			data[smallest] = data[i];
			data[i] = temp;

			// then need to heapify the subtree after the swap
			minHeapify(data, smallest, end);
		}

	}

	public void DescendingHeapSort(T[] data) {
		// start at first non leaf node
				// would be length - 1 / 2
				int start = (data.length - 1) / 2;

				// build the heap from the existing elements
				// need to build a max heap since we want a ascending sort order as
				// result and we're going to eventually treat the array as two parts
				// unsorted heap - | sorted
				// [new root][][][][] | [][largest]
				// otherwise we'd have to shift the sorted on every removal
				// or alternatively we could have reversed the resulting array
				// but both methods would be less efficient
				for (int i = start; i >= 0; i--) {
					minHeapify(data, i, data.length - 1);
				}

				// loop over array again
				// remove max by moving it to the sorted portion
				// then re-heapify what remains in the heap
				// equivalent to the traditional "remove" operation

				for (int i = 0; i < data.length; i++) {

					// shrink the heap portion of the array
					int last = data.length - (i + 1);

					// remove max and store temporarily
					T temp = data[0];

					// store the new root
					data[0] = data[last];

					// store the next max at the end
					data[last] = temp;

					// re-heapify what remains
					minHeapify(data, 0, last - 1);

				}
	}



}
