package laustrup.bandwichpersistence.utilities;

import java.util.Collection;
import java.util.InputMismatchException;

public interface ILiszt<E> {

    /**
     * Puts an element replacement at the former element, which is replaced.
     * @param replacement The new element, that is wished to be replaced.
     * @param index The index the replacement will happen. Is counted as 1 being the first value
     * @return All the data of the Liszt.
     */
    E[] replace(E replacement, int index) throws InputMismatchException, ClassNotFoundException;

    /**
     * Puts an element replacement at the former element, which is replaced.
     * @param replacement The new element, that is wished to be replaced.
     * @param original The original element, that will be replaced.
     * @return All the data of the Liszt.
     */
    E[] replace(E replacement, E original) throws InputMismatchException, ClassNotFoundException;

    /**
     * Will add the input into the data array and map as well.
     * @param element An object of type E.
     * @return The same object, that has been added to the Liszt.
     * Will return null, if it is not a successful add.
     */
    E addDda(E element);

    /**
     * Will add the input into the data array and map as well.
     * @param elements An Array of objects of type E.
     * @return The whole Liszt object and its elements.
     * Will return null, if it is not a successful add.
     */
    Liszt<E> addDdaForAll(E[] elements);

    /**
     * Will add the input into the data array and map as well.
     * @param element An object of type E.
     * @return The whole Liszt object and its elements.
     * Will return null, if it is not a successful add.
     */
    Liszt<E> addDdas(E element);

    /**
     * Will add the input into the data array and map as well.
     * @param elements An array of objects of type E.
     * @return The data of Liszt as an array.
     * Will return null, if it is not a successful add.
     */
    E[] addDdasForAll(Collection<E> elements);

    /**
     * Will add the input into the data array and map as well.
     * @param elements An array of objects of type E.
     * @return The data of Liszt as an array.
     * Will return null, if it is not a successful add.
     */
    E[] addDdasForAll(E[] elements);

    /**
     * Ensures that this collection contains the specified element (optional operation).
     * Returns true if this collection changed as a result of the call.
     * (Returns false if this collection does not permit duplicates and already contains the specified element).
     * Collections that support this operation may place limitations on what elements may be added to this collection.
     * In particular, some collections will refuse to add null elements,
     * and others will impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any restrictions on what elements may be added.
     * If a collection refuses to add a particular element for any reason other than that it already contains the element,
     * it must throw an exception (rather than returning false).
     * This preserves the invariant that a collection always contains the specified element after this call returns.
     * @param elements Elements whose presence in this collection is to be ensured.
     * @return true if this collection changed as a result of the call
     */
    boolean add(E[] elements);

    /**
     * Will only add the element, if it doesn't contain the key in the map.
     * @param element The element that will be added depending on the situation.
     * @return All the element data of the Liszt.
     */
    E[] addUnique(E element);

    /**
     * Will only add the elements, if it doesn't contain the key in the map.
     * @param elements The elements that will be added depending on the situation.
     * @return All the element data of the Liszt.
     */
    E[] addUnique(E[] elements);
}
