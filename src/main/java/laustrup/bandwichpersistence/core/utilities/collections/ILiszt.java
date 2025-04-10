package laustrup.bandwichpersistence.core.utilities.collections;

import laustrup.bandwichpersistence.core.utilities.Coollection;

/** Contains the unique features of a Liszt. */
public interface ILiszt<E> {

    /**
     * Adds the element to the array data and the map data, making it available to be iterated through and used in map.
     * Filters out elements that are null.
     * @param element An element of generic E that will be added.
     * @return This Liszt<E> containing the added element.
     */
    Liszt<E> Add(E element);

    /**
     * Adds multiple elements to the array data and the map data, making them available to be iterated through and used in map.
     * Filters out elements that are null.
     * @param elements Some elements of generic E that will be added.
     * @return This Liszt<E> containing the added elements.
     */
    Liszt<E> Add(E[] elements);

    /**
     * Adds multiple elements to the array data and the map data, making them available to be iterated through and used in map.
     * Filters out elements that are null.
     * @param coollection A collection of elements of generic E that will be added, this collection can either be a Liszt or Seszt.
     * @return This Liszt<E> containing the added elements.
     */
    Liszt<E> Add(Coollection<E> coollection);

    /**
     * Replaces the elements with the override of the specified elements.
     * Uses the key from the toString(), to identify the element.
     * @param originals The element to replace.
     * @param replacements The element that will become the replacement.
     * @return This Liszt<E> containing the updated elements.
     */
    Liszt<E> set(Coollection<E> originals, Coollection<E> replacements);

    /**
     * Replaces the element with the override with the specified element.
     * Uses the key from the toString(), to identify the element.
     * @param original The element to replace.
     * @param replacement The element that will become the replacement.
     * @return This Liszt<E> containing the updated elements.
     */
    Liszt<E> set(E original, E replacement);

    /**
     * Replaces the element with the override with the specified element.
     * Uses the key from the toString(), to identify the element.
     * If elements are empty or null the replacements will be added,
     * otherwise if replacements are empty or null, the elements will be removed,
     * these if cases also takes place for any indexes that is left.
     * @param originals The elements to replace.
     * @param replacements The elements that will become the replacements.
     * @return This Liszt<E> containing the updated elements.
     */
    Liszt<E> set(E[] originals, E[] replacements);

    /**
     * Replaces the value at the index, both in the array data and map, with the input.
     * @param index The index position, starting from 1, of  the element that will be replaced.
     * @param element The new element of the index position.
     * @return This Liszt<E> containing the updated elements.
     */
    Liszt<E> Set(int index, E element);

    /**
     * Removes all selected elements, if they exist with same toString in the collection and aren't null.
     * @param elements The elements that will be removed.
     * @return This Liszt<E> containing the remaining elements.
     */
    Liszt<E> remove(E[] elements);

    /**
     * Removes the datum at the specified index both in data array and map.
     * @param index The index, starting from 1, of the datum that will be removed.
     * @return This Liszt<E> containing the remaining elements.
     */
    Liszt<E> Remove(int index);
}
