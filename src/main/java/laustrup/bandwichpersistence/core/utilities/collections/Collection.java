package laustrup.bandwichpersistence.core.utilities.collections;

import laustrup.bandwichpersistence.core.utilities.Utility;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.core.utilities.console.Printer;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;

/**
 * A Utility that contains collections of data such as a Map and an array.
 * @param <E> The type of element that are wished to be used in this class.
 */
public abstract class Collection<E> extends Utility<E> implements java.util.Collection<E> {

    /** Contains all the elements that are inside the Liszt. */
    @Getter
    protected E[] _data;

    /** Containing all data elements in a map for quick access. */
    protected Map<String, E> _map;

    /** The destinations for the map, before being inserted into the map */
    protected Map<String,E> _destinations;

    /** The keys that the map can use for inserting relevant data from the destinations into the map. */
    protected String[] _destinationKeys;

    /**
     * Creates the Utility with empty data.
     * @param isLinked Decides if the map should be linked or hash type.
     */
    protected Collection(boolean isLinked) {
        _data = convert(new Object[0]);
        _destinationKeys = new String[0];

        if (isLinked) _map = new LinkedHashMap<>(); else _map = new HashMap<>();
        if (isLinked) _destinations = new LinkedHashMap<>(); else _destinations = new HashMap<>();
    }

    /**
     * Will perform all Java utility functions with a try catch of converting collections.
     * @param function The function that should be executed for each item in the collection.
     * @param collection The inputs as a collection of ? objects.
     * @param action The title of the action that will be printed, if an Exception is caught, presented as past tense.
     * @return True if there is a change in the size after the actions.
     */
    protected boolean All(Function<E,Object> function, Object[] collection, String action) {
        int previousSize = get_data().length;

        for (Object item : collection) {
            try {
                function.apply(convert(item));
            } catch (Exception e) {
                Printer.get_instance().print(item.toString() + " couldn't " + action +
                        " of " + getClass() + ", probably because the type is different than its generic...",e);
            }
        }

        return get_data().length != previousSize;
    }

    /**
     * Will remove the index of the data and its key in the map.
     * @param index The index of the data that will be removed.
     */
    protected void handleRemove(int index) {
        E[] storage = convert(new Object[_data.length-1]);
        int storageIndex = 0;

        for (int i = 0; i < _data.length; i++) {
            if (i != index) {
                storage[storageIndex] = _data[i];
                storageIndex++;
            }
            else
                _map.remove(_data[i].toString());
        }
        _data = storage;
    }

    /**
     * Will find the element at the index position, with index starting at 0.
     * @param index The index position of the element to be found.
     * @return The found element.
     */
    protected E handleGet(int index) {
        if (index < _data.length)
            return _data[index];

        return null;
    }

    /**
     * Will add the elements to the E[] data and the map.
     * Null elements will be filtered away.
     * @param elements The elements to add.
     */
    protected void handleAdd(E[] elements) {
        elements = filterElements(elements);
        E[] storage = convert(new Object[_data.length + elements.length]);

        System.arraycopy(_data, 0, storage, 0, _data.length);

        int index = _data.length;
        for (E element : elements) {
            storage[index] = addElementToDestination(element);
            index++;
        }

        _data = storage;
        insertDestinationsIntoMap();
    }

    /**
     * Elements that are null will be removed.
     * @param elements The elements that will be filtered.
     * @return The filtered elements.
     */
    protected E[] filterElements(E[] elements) {
        int length = 0;
        for (E element : elements)
            if (element != null)
                length++;

        int index = 0;
        E[] filtered = convert(new Object[length]);
        for (E element : elements) {
            if (element != null) {
                filtered[index] = element;
                index++;
            }
        }

        return filtered;
    }

    /**
     * Adds the element to destination, before it's either added to data or map.
     * This is for the reason, to prevent two of the same keyes in maps,
     * if element's toString() already is a key, it will at its hashcode.
     * @param element An element that is wished to be added.
     * @return The same element of the input.
     */
    private E addElementToDestination(E element) {
        String key = _map.containsKey(element.toString()) ? String.valueOf(element.hashCode()) : element.toString();

        _destinations.put(key,element);
        addDestinationKey(key);

        return element;
    }

    /**
     * Adds the potential key to the destinationKeys.
     * @param key The potential key of an element.
     * @return The destinationKeys
     */
    private String[] addDestinationKey(String key) {
        String[] storage = new String[_destinationKeys.length+1];

        for (int i = 0; i < storage.length; i++) {
            if (i < _destinationKeys.length) storage[i] = _destinationKeys[i];
            else storage[i] = key;
        }
        _destinationKeys = storage;

        return storage;
    }

    /**
     * Inserts the destination keys into the map.
     * Is intended to be removed for better performance.
     */
    private void insertDestinationsIntoMap() {
        for (String destinationKey : _destinationKeys)
            _map.put(destinationKey, _destinations.get(destinationKey));

        _destinationKeys = new String[0];
        _destinations.clear();
    }

    protected E handleSet(int index, E element) {
        if (element != null)
            try {
                _map.remove(_data[index].toString());
                _data[index] = element;
                _map.put(element.toString(),element);

                return _map.containsKey(element.toString()) ? _map.get(element.toString()) : _data[index];
            } catch (IndexOutOfBoundsException e) {
                Printer.get_instance().print("At setting " + element + " in Liszt, the index " + index +
                        " was out of bounce of size " + _data.length + "...",e);
            }

        return null;
    }

    public E Get(String key) {
        return _map.get(key);
    }

    /**
     * Converting Objects into the element type and suppresses warning of cast.
     * @param objects The Objects that will become element type.
     * @return The element type version of the Objects.
     */
    @SuppressWarnings("unchecked")
    protected E[] convert(Object[] objects) { return (E[]) objects; }

    /**
     * Converting an Object into the element type and suppresses warning of cast.
     * @param object The Object that will become element type.
     * @return The element type version of the Objects.
     */
    @SuppressWarnings("unchecked")
    protected E convert(Object object) { return (E) object; }

    public Set<E> toSet() {
        return new HashSet<>(_map.values());
    }

    public Liszt<E> toLiszt() {
        return Liszt.of(get_data());
    }

    @Override
    public String toString() {
        String[] keySet = _map.keySet().toString().split(",");
        StringBuilder keys = new StringBuilder();

        for (int i = 0; i < keySet.length; i++) {
            if (i < keySet.length - 1 &&
                (keySet[i+1].contains("description")
                    || keySet[i+1].contains("timestamp")
                    || keySet[i+1].contains("runner")))
                keys.append("\n\t\t");
            String key = keySet[i];
            keys.append(key);
        }

        keys.insert(1,"\n\t\t");
        keys.insert(keys.length()-1, "\n\t");

        return getClass().getSimpleName() + "(" +
            "\n\tsize:" + _data.length +
            ",\n\tisLinked:" + (_map.getClass() == LinkedHashMap.class ? "Linked" : "Unlinked") +
            ",\n\tmap:" + keys +
        "\n)";
    }
}
