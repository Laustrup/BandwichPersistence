package laustrup.bandwichpersistence.core.utilities.collections;

import laustrup.bandwichpersistence.core.utilities.Coollection;

import java.util.Set;
import java.util.function.Function;

/** Contains the unique features of a Seszt. */
public interface ISeszt<E> {

    /**
     * Adds the element to the array data and the map data, if it doesn't already share a toString with another datum,
     * making it available to be iterated through and used in map.
     * Filters out elements that are null.
     * @param element An element of generic E that will be added.
     * @return This Seszt<E> containing the added element.
     */
    Seszt<E> Add(E element);

    /**
     * Adds elements to the array data and the map data, if they don't already share toStrings with other data,
     * making them available to be iterated through and used in map.
     * Filters out elements that are null.
     * @param elements Some elements of generic E that will be added.
     * @return This Seszt<E> containing the added elements.
     */
    Seszt<E> Add(E[] elements);

    /**
     * Adds elements to the array data and the map data, if they don't already share toStrings with other data,
     * making them available to be iterated through and used in map.
     * Filters out elements that are null.
     * @param coollection A collection of either Liszt or Seszt with some element data of generic E that will be added.
     * @return This Seszt<E> containing the added elements.
     */
    Seszt<E> Add(Coollection<E> coollection);

    /**
     * Replaces the elements with the override of the specified elements.
     * Uses the key from the toString(), to identify the element.
     * @param originals The element to replace.
     * @param replacements The element that will become the replacement.
     * @return This Seszt<E> containing the updated elements.
     */
    Seszt<E> set(Coollection<E> originals, Coollection<E> replacements);

    /**
     * Replaces the element with the override with the specified element.
     * Uses the key from the toString(), to identify the element.
     * @param element The element to replace.
     * @param replacement The element that will become the replacement.
     * @return This Seszt<E> containing the updated elements.
     */
    Seszt<E> set(E element, E replacement);

    /**
     * Replaces the element with the override with the specified element.
     * Uses the key from the toString(), to identify the element.
     * If elements are empty or null the replacements will be added,
     * otherwise if replacements are empty or null, the elements will be removed,
     * these if cases also takes place for any indexes that is left.
     * @param elements The elements to replace.
     * @param replacements The elements that will become the replacements.
     * @return This Seszt<E> containing the updated elements.
     */
    Seszt<E> set(E[] elements, E[] replacements);

    /**
     * Removes all selected elements, if they exist with same toString in the collection and aren't null.
     * @param elements The elements that will be removed.
     * @return This Seszt<E> containing the remaining elements.
     */
    Seszt<E> remove(E[] elements);

    /**
     * Removes the datum at the specified index both in data array and map.
     * @param index The index, starting from 0, of the datum that will be removed.
     * @return This Seszt<E> containing the remaining elements.
     */
    Seszt<E> remove(int index);

    /**
     * Removes the datum at the specified index both in data array and map.
     * @param index The index, starting from 1, of the datum that will be removed.
     * @return This Seszt<E> containing the remaining elements.
     */
    Seszt<E> Remove(int index);

    /**
     * Finds the index value in the data array of a datum element.
     * @param element The element which index is to be found.
     * @return The found index value of the datum element.
     * @throws ClassNotFoundException Will be thrown if it didn't find the datum element.
     */
    int indexOf(E element) throws ClassNotFoundException;

    /**
     * Finds a datum of an element at a given index.
     * @param index The index of a datum, that starts at 0, that is to be found.
     * @return The found datum, null if it doesn't exist.
     */
    E get(int index);

    /**
     * Finds a datum of an element at a given index.
     * @param index The index of a datum, that starts at 1, that is to be found.
     * @return The found datum, null if it doesn't exist.
     */
    E Get(int index);

    /**
     * Finds a datum of an element from a given string of the map, that is of the datum's toString.
     * Skips iteration through the data array.
     * @param key The key to the value that is to be found, is made of the datum's toString.
     * @return The found datum, null if it doesn't exist.
     */
    E get(String key);

    /**
     * Replaces the value at the index, both in the array data and map, with the input.
     * @param index The index position, starting from 0, of  the element that will be replaced.
     * @param element The new element of the index position.
     * @return This Seszt<E> containing the updated elements.
     */
    Seszt<E> set(int index, E element);

    /**
     * Replaces the value at the index, both in the array data and map, with the input.
     * @param index The index position, starting from 1, of  the element that will be replaced.
     * @param element The new element of the index position.
     * @return This Seszt<E> containing the updated elements.
     */
    Seszt<E> Set(int index, E element);

    /**
     * Removes all other elements except the ones that shares the toString of any data in the parameter.
     * @param elements The data elements that are identical to data that will be kept in this collection.
     * @return This Seszt<E> containing the retained elements.
     */
    Seszt<E> retain(E[] elements);

    /**
     * Creates a new Set with the data of this object.
     * @return The Set of the data of this object.
     */
    Set<E> asSet();

    /**
     * <div>Creates a new Set with the data of this object.</div>
     * <div>Also able to convert the data with mapping.</div>
     * @param conversion Defines how the new data should be converted
     * @return A Set of the data from this object converted by conversion.
     * @param <M> The previous E type.
     */
    <M> Set<M> asSet(Function<E, M> conversion);
}
