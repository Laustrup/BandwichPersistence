package laustrup.bandwichpersistence.utilities.collections;

//TODO update documentation
public interface ICollectionUtility<E> {

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

    E[] Add(E element);

    E[] Add(E[] elements);

    /**
     * Replaces the element with the override with the specified element.
     * Uses the key from the toString(), to identify the element.
     * @param element The element to replace.
     * @param replacement The element that will become the replacement.
     * @return The final element.
     */
    E set(E element, E replacement);

    /**
     * Replaces the element with the override with the specified element.
     * Uses the key from the toString(), to identify the element.
     * If elements are empty or null the replacements will be added,
     * otherwise if replacements are empty or null, the elements will be removed,
     * these if cases also takes place for any indexes that is left.
     * @param elements The elements to replace.
     * @param replacements The elements that will become the replacements.
     * @return An empty array if both parameters are null or empty otherwise the data elements.
     */
    E[] set(E[] elements, E[] replacements);

    E[] Get(E[] elements);

    E[] remove(E[] elements);
    boolean contains(E[] elements);
}
