package laustrup.bandwichpersistence.utilities;

import lombok.Getter;

import java.util.*;

/**
 * Implements a List of element E in an append way of adding elements.
 * It also implements ILiszt, which contains extra useful methods.
 * An extra detail is that this class also uses a map, which means that
 * the approach of getting also can be done through the map, this also
 * means, that they will be saved doing add, costing lower performance.
 * @param <E> The type of element that are wished to be used in this class
 */
public class Liszt<E> implements List<E>, ILiszt<E> {

    /**
     * Contains all the elements that are inside the Liszt.
     * The data is in an array
     */
    @Getter
    private E[] _data;
    private Map<String,E> _map, _destinations;
    private String[] _destinationKeys;

    public Liszt() { this(false); }
    public Liszt(boolean isLinked) {
        _data = (E[]) new Object[0];
        _destinationKeys = new String[0];

        if (isLinked) _map = new LinkedHashMap<>(); else _map = new HashMap<>();
        if (isLinked) _destinations = new LinkedHashMap<>(); else _destinations = new HashMap<>();
    }
    public Liszt(E[] data) { this(data,false); }
    public Liszt(E[] data, boolean isLinked) {
        _destinationKeys = new String[0];

        if (isLinked) _map = new LinkedHashMap<>(); else _map = new HashMap<>();
        if (isLinked) _destinations = new LinkedHashMap<>(); else _destinations = new HashMap<>();
        _data = (E[]) new Object[0];

        add(data);
    }

    @Override public int size() { return _data.length; }
    @Override public boolean isEmpty() { return _data.length == 0 && _map.isEmpty(); }
    @Override
    public boolean contains(Object object) {
        if (object != null) {
            boolean exists = _map.containsValue(object);
            if (!exists)
                for (E data : _data)
                    if (object == data)
                        exists = true;

            return exists;
        }
        return false;
    }
    public boolean contains(String key) { return _map.containsKey(key); }
    @Override public Iterator<E> iterator() { return Arrays.stream(_data).iterator(); }
    @Override public Object[] toArray() { return Arrays.stream(_data).toArray(); }
    @Override public <T> T[] toArray(T[] a) { return (T[]) Arrays.stream(_data).toArray(); }

    @Override
    public E[] replace(E replacement, int index) throws InputMismatchException, ClassNotFoundException {
        if (index > 0) {
            E element = (E) new Object();
            boolean elementIsFound = false;

            for (int i = 0; i < _data.length; i++)
                if (i + 1 == index) {
                    element = _data[i];
                    _data[i] = replacement;
                    elementIsFound = true;
                    break;
                }
            if (!elementIsFound) throw new ClassNotFoundException();

            _map.remove(_map.containsKey(element.toString()) ? element.toString() : String.valueOf(element.hashCode()));
            _map.put(_map.containsKey(String.valueOf(replacement.hashCode())) ? String.valueOf(element.hashCode())
                    : element.toString(), replacement);

            return _data;
        }
        throw new InputMismatchException();
    }

    @Override
    public E[] replace(E replacement, E original) throws InputMismatchException, ClassNotFoundException {
        if (_map.containsKey(original.toString()) || _map.containsKey(String.valueOf(original.hashCode()))) {
            boolean keyIsToString = _map.containsKey(original.toString());

            E element = keyIsToString ? _map.get(original.toString())
                    : _map.get(String.valueOf(original.hashCode()));
            boolean elementIsFound = false;

            if (element != null)
                for (int i = 0; i < _data.length; i++)
                    if (element.toString().equals(element.toString())) {
                        _data[i] = replacement;
                        elementIsFound = true;
                        break;
                    }
            if (!elementIsFound) throw new ClassNotFoundException();

            _map.remove(_map.containsKey(element.toString()) ? element.toString() : String.valueOf(element.hashCode()));
            _map.put(_map.containsKey(String.valueOf(replacement.hashCode())) ? String.valueOf(element.hashCode())
                    : element.toString(), replacement);

            return _data;
        }
        throw new InputMismatchException();
    }

    @Override
    public boolean add(E element) { return add((E[]) new Object[]{element}); }

    @Override
    public E addDda(E element) {
        if (add((E[]) new Object[]{element})) return element;
        return null;
    }

    @Override
    public Liszt<E> addDdaForAll(E[] elements) {
        if (add((E[]) elements)) return new Liszt<E>(elements, _map.getClass() == LinkedHashMap.class);
        return null;
    }

    @Override
    public Liszt<E> addDdas(E element) {
        if (add((E[]) new Object[]{element})) new Liszt<E>((E[]) new Object[]{element}, _map.getClass() == LinkedHashMap.class);
        return null;
    }

    @Override
    public E[] addDdasForAll(Collection<E> elements) {
        if (add((E[]) elements.toArray())) return _data;
        return null;
    }

    @Override
    public E[] addDdasForAll(E[] elements) {
        if (add(elements)) return _data;
        return null;
    }

    @Override
    public boolean add(E[] elements) {
        try { handleAdd(elements); }
        catch (Exception e) {
            if (elements.length>1)
                Printer.get_instance().print("Couldn't add elements of " + Arrays.toString(elements) + " to Liszt...", e);
            else Printer.get_instance().print("Couldn't add element of " + Arrays.toString(elements) + " to Liszt...", e);
            return false;
        }

        return true;
    }
    private void handleAdd(E[] elements) {
        elements = filterElements(elements);
        E[] storage = (E[]) new Object[_data.length + elements.length];;

        for (int i = 0; i < _data.length; i++) storage[i] = _data[i];

        int index = _data.length;
        for (E element : elements) {
            storage[index] = addElementToDestination(element);
            index++;
        }

        _data = storage;
        insertDestinationsIntoMap();
    }

    private E[] filterElements(E[] elements) {
        int length = 0;
        for (int i = 0; i < elements.length; i++)
            if (elements[i]!=null)
                length++;

        int index = 0;
        E[] filtered = (E[]) new Object[length];
        for (int i = 0; i < elements.length; i++) {
            if (elements[i]!=null) {
                filtered[index] = elements[i];
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
    private void insertDestinationsIntoMap() {
        for (int i = 0; i < _destinationKeys.length; i++) { _map.put(_destinationKeys[i], _destinations.get(_destinationKeys[i])); }

        _destinationKeys = new String[0];
        _destinations.clear();
    }

    @Override
    public boolean remove(Object object) {
        Object[] storage = new Object[_data.length-1];

        try {
            for (int i = 0; i < storage.length; i++) {
                if (_data[i] != object) {
                    storage[i] = _data[i];
                }
            }
            _data = (E[]) storage;
            if (!_map.remove(object.toString(),object)) { _map.remove(object.hashCode()); }
        }
        catch (Exception e) {
            Printer.get_instance().print("Couldn't remove object " + object + "...", e);
            return false;
        }

        return true;
    }

    public boolean remove(E[] elements) {
        elements = filterElements(elements);
        Object[] storage = new Object[_data.length - elements.length];

        try {
            for (int i = 0; i < storage.length; i++)
                if (contains(elements[i]))
                    storage[i] = _data[i];

            _data = (E[]) storage;
            for (E element : elements)
                if (!_map.remove(element.toString(),element)) { _map.remove(element.hashCode()); }
        }
        catch (Exception e) {
            Printer.get_instance().print("Couldn't remove object an object in remove multiple elements...", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (E e : _data) { if (!collection.contains(e)) { return false; } }
        for (Object item : collection) {
            if (!_map.containsKey(item.toString()) || !_map.containsKey(item.hashCode())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        try {
            handleAdd((E[]) collection.toArray());
            return true;
        } catch (Exception e) {
            Printer.get_instance().print("Couldn't add all items...",e);
            return false;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        for (Object item : collection) { if (!remove(item)) { return false; } }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Object[] storage = new Object[collection.size()];
        int index = 1;

        try {
            for (Object item : collection) {
                if (!_map.containsKey(item.toString()) || !_map.containsKey(item.hashCode())) {
                    remove(index);
                }
                index++;
            }

            _data = (E[]) storage;
        } catch (Exception e) {
            Printer.get_instance().print("Couldn't retain all of collection...",e);
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        _data = (E[]) new Object[0];
        _map.clear();
    }

    @Override
    public E get(int index) {
        return _data[index-1];
    }
    public E get(String key) {
        return _map.get(key);
    }

    @Override
    public E set(int index, E element) {
        _data[index-1] = element;
        addElementToDestination(element);
        insertDestinationsIntoMap();
        return _data[index-1];
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object object) {
        for (int i = 0; i < _data.length; i++) { if (_data[i] == object) return i; }
        return -1;
    }

    @Override
    public int lastIndexOf(Object object) {
        for (int i = _data.length; i >= 0; i--) { if (_data[i] == object) return i; }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String toString() {
        return "Liszt(size:"+size()+
                ",map:"+(_map.getClass() == LinkedHashMap.class ? "Linked)" : "Unlinked)");
    }
}
