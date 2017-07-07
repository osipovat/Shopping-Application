package application;

import java.util.Arrays;

/**
 * This method represents a Heap
 * @author group_0549
 *
 * @param <T> -> an object that extends the HeapItem interface
 */
public class Heap<T extends HeapItem> {
	
	protected T[] items; // Array that is used to store heap items. items[0] is the highest priority element.
	protected int maxHeapSize; // The capacity of the heap
	protected int currentItemCount; // How many elements we have currently on the heap
	
	/**
	 * This method constructs a new Heap 
	 * @param maxHeapSize -> the maximum size of the Heap
	 */
	public Heap(int maxHeapSize) {
		this.maxHeapSize = maxHeapSize;
		items = (T[]) new HeapItem[maxHeapSize];
		currentItemCount = 0; // heap is empty!
	}

	/**
	 * This method checks if the heap is empty
	 * @return -> true if the heap is empty, else returns true
	 */
	public boolean isEmpty() {
		return currentItemCount == 0;
	}

	/**
	 * This method checks if the heap is full
	 * @return -> true if the heap is full, otherwise returns false
	 */
	public boolean isFull() {
		return currentItemCount == maxHeapSize;
	}

	/**
	 * This method adds the item specified to the heap
	 * @param item -> the name of the object to be added
	 * @throws HeapFullException -> thrown when the heap is full 
	 */
	public void add(T item) throws HeapFullException
	// Adds item T to its correct position on the heap
	{
		if (isFull())
			throw new HeapFullException();
		else {
			item.setHeapIndex(currentItemCount);
			items[currentItemCount] = item;  // the element is added to the bottom
			sortUp(item); // Move the element up to its legitimate place. Check the diagram on the handout!
			currentItemCount++;
		}
	}

	/**
	 * This method checks if the heap contains the item specified
	 * @param item -> the name of the object
	 * @return -> true if the heap contains the object, else returns false
	 */
	public boolean contains(T item)
	{
		return items[item.getHeapIndex()].equals(item);
	}

	/**
	 * This method returns the size of the heap
	 * @return -> the amount of objects in the heap
	 */
	public int count() {
		return currentItemCount;
	}

	/**
	 * This method updates the item specified
	 * @param item -> name of the Object to be updated
	 */
	public void updateItem(T item) {
		sortUp(item);
	}

	/**
	 * This method removes the first item in the Heap
	 * @return -> the object in the first position of the Heap
	 * @throws HeapEmptyException -> thrown when the Heap is empty
	 */
	public T removeFirst() throws HeapEmptyException
	// Removes and returns the element sitting on top of the heap
	{
		if (isEmpty())
			throw new HeapEmptyException();
		else {
			T firstItem = items[0]; // element of top of the heap is stored in firstItem variable
			currentItemCount--;
			items[0] = items[currentItemCount]; //last element moves on top
			items[0].setHeapIndex(0);
			sortDown(items[0]); // move the element to its legitimate position. Please check the diagram on the handout.
			items[currentItemCount]=null;
			return firstItem;
		}
	}
	
	/**
	 * This method sorts the Heap up using the specified Object
	 * @param item -> name of the object to be sorted up
	 */
	private void sortUp(T item) {
		  int nodeIndex = item.getHeapIndex();
		  int parentIndex;
		  
          while (nodeIndex!=0) {
                parentIndex = (int) Math.floor(nodeIndex-1)/2;
                if (items[parentIndex].compareTo(items[nodeIndex])>0) {
                		this.swap(nodeIndex, parentIndex);          		
                }
                nodeIndex--;
          }		
	}
	
	/**
	 * This method sorts the Heap down, using the specified item 
	 * @param item -> name of the object to be sorted down
	 */
	private void sortDown(T item) {
		int root = 0;
		while((root*2 +1) <= currentItemCount-1){ // if left child exist loop iterate
			int child = root * 2 + 1; // set temporary child as left child
			if(child + 1 <= currentItemCount-1 && items[child].compareTo(items[child + 1])>0) // if right one exist and if it is smaller than left one
				child = child + 1;           //... then point to the right child instead
			if(items[root].compareTo(items[child])>0){     //out of max-heap order
				swap(root,child); // swap child and roots
			}else return;
	     }	
	}
	
	/**
	 * This method swaps the objects in the heap at the specified indexes
	 * @param i -> the position of one item
	 * @param j -> the position of another item
	 */
	private void swap(int i, int j) {
		  
		T temp = items[i];
		items[i] = items[j];
		items[i].setHeapIndex(j);
		items[j] = temp;
		items[j].setHeapIndex(i);
	}

}