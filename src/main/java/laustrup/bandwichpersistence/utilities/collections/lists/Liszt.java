package laustrup.bandwichpersistence.utilities.collections.lists;

import laustrup.bandwichpersistence.utilities.collections.ICollectionUtility;
import laustrup.bandwichpersistence.utilities.console.Printer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Implements a List of element E in an append way of adding elements.
 * It also implements ILiszt, which contains extra useful methods.
 * An extra detail is that this class also uses a map, which means that
 * the approach of getting also can be done through the map, this also
 * means, that they will be saved doing add, costing lower performance.
 * Index can both start at 0 or 1, every method starting with an uppercase
 * letter starts with 1 instead 0 in the parameters.
 * @param <E> The type of element that are wished to be used in this class.
 */
public class Liszt<E> extends ListUtility<E> implements List<E>, ICollectionUtility<E> {

    /** Creates the Liszt with empty data and a hash type of map. */
    public Liszt() { this(false); }

    /**
     * Creates the Liszt with empty data.
     * @param isLinked Decides if the map should be linked or hash type.
     */
    public Liszt(boolean isLinked) { super(isLinked, LocalDateTime.now().getYear(),1,1); }

    /**
     * Creates the Liszt with data and a hash type of map.
     * @param data The data that will be added.
     */
    public Liszt(E[] data) { this(data,false); }

    /**
     * Creates the Liszt with data.
     * @param data The data that will be added.
     * @param isLinked Decides if the map should be linked or hash type.
     */
    public Liszt(E[] data, boolean isLinked) {
        super(LocalDateTime.now().getYear(),1,1);
        _destinationKeys = new String[0];

        if (isLinked) _map = new LinkedHashMap<>(); else _map = new HashMap<>();
        if (isLinked) _destinations = new LinkedHashMap<>(); else _destinations = new HashMap<>();
        _data = convert(new Object[0]);

        add(data);
    }

    @Override public int size() { return _data.length; }
    @Override public boolean isEmpty() { return _data.length == 0 && _map.isEmpty(); }
    @Override
    public boolean contains(Object object) {
        if (object != null) {
            @SuppressWarnings("all") boolean exists = _map.containsValue(object);
            if (!exists) {
                for (E data : _data) {
                    if (object == data) {
                        exists = true;
                        break;
                    }
                }
            }

            return exists;
        }
        return false;
    }
    public boolean contains(String key) { return _map.containsKey(key); }
    @Override public Iterator<E> iterator() { return Arrays.stream(_data).iterator(); }

    @Override
    public void forEach(Consumer<? super E> action) { List.super.forEach(action); }

    @Override public Object[] toArray() { return Arrays.stream(_data).toArray(); }
    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) { return (T[]) Arrays.stream(_data).toArray(); }

    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(IntFunction<T[]> generator) { return (T[]) List.super.toArray(Object[]::new); }

    @Override public boolean add(E element) { return add(convert(new Object[]{element})); }

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

    @Override
    public E[] set(E[] elements, E[] replacements) {
        boolean elementsIsNullOrEmpty = elements == null || elements.length == 0,
                replacementsIsNullOrEmpty = replacements == null || replacements.length == 0;

        if (elementsIsNullOrEmpty && replacementsIsNullOrEmpty)
            return convert(new Object[]{});
        else if (replacementsIsNullOrEmpty)
            remove(elements);
        else if (elementsIsNullOrEmpty)
            add(replacements);
        else if (elements.length == size() && replacements.length == size()) {
            _data = replacements;
            _map.clear();
            for (E element : replacements)
                _map.put(element.toString(),element);
        }
        else {
            int replacement = 0, elementsRemoved = 0;
            for (int i = 1; i <= size(); i++) {
                for (E element : elements) {
                    if (Get(i).toString().equals(element.toString())) {
                        if (replacements.length - 1 >= replacement) {
                            set(i, replacements[replacement]);
                            elementsRemoved++;
                            replacement++;
                        }
                        break;
                    }
                }
                if (elementsRemoved > elements.length || replacements.length <= replacement)
                    break;
            }
            if (replacement < replacements.length) {
                E[] extras = convert(new Object[elements.length - elementsRemoved]);

                for (int i = 0; i + replacement < replacements.length; i++)
                    extras[i] = replacements[i + replacement];

                add(extras);
            }
            if (elementsRemoved < elements.length) {
                E[] extras = convert(new Object[elements.length - elementsRemoved]);

                for (int i = 0; i + elementsRemoved < elements.length; i++)
                    extras[i] = elements[i + elementsRemoved];

                remove(extras);
            }
        }

        return Get(Objects.requireNonNull(replacements));
    }

    @Override
    public E[] Get(E[] elements) {
        E[] gathered = convert(new Object[elements.length]);

        for (int i = 0; i < gathered.length; i++)
            gathered[i] = Get(elements[i].toString());

        return gathered;
    }

    @Override
    public E set(E element, E replacement) {
        for (int i = 0; i < size(); i++)
            if (get(i).toString().equals(element.toString()))
                return set(i,replacement);

        return contains(element.toString()) ? element : Get(replacement.toString());
    }

    @Override
    public E set(int index, E element) {
        try {
            _map.remove(_data[index-1].toString());
            _data[index-1] = element;
            _map.put(element.toString(),element);
        } catch (IndexOutOfBoundsException e) {
            Printer.get_instance().print("At setting " + element + " in Liszt, the index " + index +
                    " was out of bounce of size " + size() + "...",e);
        }

        return _map.containsKey(element.toString()) ? Get(element.toString()) : _data[index-1];
    }

    private void handleAdd(E[] elements) {
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

    private E[] filterElements(E[] elements) {
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

    private void insertDestinationsIntoMap() {
        for (String destinationKey : _destinationKeys)
            _map.put(destinationKey, _destinations.get(destinationKey));

        _destinationKeys = new String[0];
        _destinations.clear();
    }

    @Override
    public E[] remove(E[] elements) {
        elements = filterElements(elements);

        try {
            for (E element : elements)
                remove(element);
        }
        catch (Exception e) {
            Printer.get_instance().print("Couldn't remove object in remove multiple elements...", e);
        }

        return _data;
    }

    @Override
    public boolean contains(E[] elements) {
        for (E element : elements)
            if (!contains(element))
                return false;

        return true;
    }

    @Override
    public boolean remove(Object object) {
        if (object != null)
            try {
                int index = indexOf(object);

                while (index >= 0) {
                    if (remove(index) == null)
                        break;

                    index = indexOf(object);
                }
                return true;
            }
            catch (Exception e) {
                Printer.get_instance().print("Couldn't remove " + object + "...", e);
            }

        return false;
    }

    public E Remove(int index) { return remove(index-1); }
    @Override
    public E remove(int index) {
        if (index > size() || 0 > index)
            return null;

        E element = null;
        E[] storage = convert(new Object[size()-1]);
        int storageIndex = 0;

        for (int i = 0; i < size(); i++) {
            if (i != index) {
                storage[storageIndex] = get(i);
                storageIndex++;
            }
            else {
                element = get(i);
                _map.remove(get(i).toString());
            }
        }
        _data = storage;

        return element;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (E datum : _data)
            if (!collection.contains(datum)) return false;
        for (Object item : collection)
            if (!_map.containsKey(item.toString()))
                return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        try {
            handleAdd(convert(collection.toArray()));
            return true;
        } catch (Exception e) {
            Printer.get_instance().print("Couldn't add all items...",e);
            return false;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        for (Object item : collection) { if (!remove(item)) { return false; } }
        return true;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return List.super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Object[] storage = new Object[collection.size()];
        int index = 1;

        try {
            for (Object item : collection) {
                if (!_map.containsKey(item.toString())) {
                    remove(index);
                }
                index++;
            }

            _data = convert(storage);
        } catch (Exception e) {
            Printer.get_instance().print("Couldn't retain all of collection...",e);
            return false;
        }
        return true;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        List.super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        List.super.sort(c);
    }

    @Override
    public void clear() {
        _data = convert(new Object[0]);
        _map.clear();
    }

    @Override
    public E get(int index) {
        if (index < 0 || isEmpty())
            return null;
        return _data[index];
    }

    public E Get(int index) {
        if (index <= 0 || isEmpty())
            return null;
        return _data[index-1];
    }

    public E Get(String key) {
        return _map.get(key);
    }

    @Override
    public void add(int index, E element) {

    }

    public int IndexOf(E element) {
        return indexOf(element) + 1;
    }
    @Override
    public int indexOf(Object object) {
        for (int i = 0; i < _data.length; i++)
            if (_data[i] == object)
                return i;

        return -1;
    }

    public int LastIndexOf(E element) { return lastIndexOf(element) + 1; }
    @Override
    public int lastIndexOf(Object object) {
        for (int i = _data.length-1; i == 0; i--)
            if (get(i) == object) return i;

        return -1;
    }

    @Override
    public ListIterator<E> listIterator() { return Arrays.stream(_data).toList().listIterator(); }
    @Override
    public ListIterator<E> listIterator(int index) { return Arrays.stream(_data).toList().listIterator(index); }

    public List<E> SubList(int min, int max) { return subList(min - 1, max - 1); }
    @Override
    public List<E> subList(int min, int max) { return Arrays.stream(subArray(min, max)).toList(); }

    public Liszt<E> SubLiszt(int min, int max) { return subLiszt(min - 1, max - 1); }
    public Liszt<E> subLiszt(int min, int max) { return new Liszt<>(subArray(min, max)); }

    public E[] SubArray(int min, int max) { return subArray(min - 1, max - 1); }

    //TODO Test subArray in Liszt
    public E[] subArray(int min, int max) {
        E[] subList = convert(new Object[max-min]);

        for (int iteration = min, index = 0;
             iteration < max;
             iteration++, index++
        ) subList[index] = _data[iteration];

        return subList;
    }


    @Override
    public Spliterator<E> spliterator() {
        return List.super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return List.super.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return List.super.parallelStream();
    }

    public E getLast() { return _data.length > 0 ? _data[size()-1] : null; }
    public E getFirst() { return _data[0]; }

    @Override
    public String toString() {
        return "Liszt(" +
                    "size:"+size()+
                    ",isLinked:"+(_map.getClass() == LinkedHashMap.class ? "Linked" : "Unlinked") +
                    ",map:" + _map.keySet() +
                ")";
    }
}
