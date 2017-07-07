package application;
/**
 * This interface is used for heap items
 * @author group_0549
 *
 */

public interface HeapItem extends Comparable<HeapItem> {
	/**
	 * This method sets the heap index to the one specified
	 * @param index -> the number to be set to the heap index
	 */
	public void setHeapIndex(int index);
	
	/**
	 * This method returns the heap index
	 * @return -> the current heap index 
	 */
	public int getHeapIndex();
	
	/**
	 * This method compares two Heap items 
	 */
	int compareTo(HeapItem other);

}
