package laustrup.bandwichpersistence.core.utilities.collections;

/** Contains features that are used in all collection classes. */
public interface ICollection<E> {

    /**
     * Adds some elements to the array data and the map data, making them available to be iterated through and used in map.
     * Filters out elements that are null.
     * @param elements Some elements of generic E that will be added.
     * @return True if the data is added, otherwise false.
     */
    boolean add(E[] elements);

    /**
     * Checks if the given data exist in this collection by getting from the map.
     * @param elements The data that should exist in this collection.
     * @return True if all elements exist in the collection, otherwise false.
     */
    boolean contains(E[] elements);

    /**
     * Finds the first data in the datum array.
     * @return The found data element.
     */
    E getFirst();

    /**
     * Finds the last data in the datum array.
     * @return The found data element.
     */
    E getLast();
}
