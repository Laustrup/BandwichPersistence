package laustrup.bandwichpersistence.utilities.collections.sets;

import laustrup.bandwichpersistence.utilities.collections.CollectionUtility;
import laustrup.bandwichpersistence.utilities.collections.ICollectionUtility;
import laustrup.bandwichpersistence.utilities.console.Printer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Implements a Set of element E in an append way of adding elements.
 * It also implements the interface ICollectionUtility, which contains extra useful methods.
 * An extra detail is that this class also uses a map, which means that
 * the approach of getting also can be done through the map, this also
 * means, that they will be saved doing add and removed at remove.
 * Index can both start at 0 or 1, every method starting with an uppercase
 * letter starts with 1 instead 0 in the parameters.
 * @param <E> The type of element that are wished to be used in this class.
 */
public class Seszt<E> extends SetUtility<E> implements Set<E>, ICollectionUtility<E> {

    /** Default constructor that will build the Seszt without containing any data and the map as linked. */
    public Seszt() {
        this(false);
    }

    /**
     * A Seszt without containing any data.
     * @param isLinked Will initialize the map as linked if true, else as hash.
     */
    public Seszt(boolean isLinked) {
        super(isLinked, LocalDateTime.now().getYear(), 1, 1);
    }

    /**
     * The Seszt will contain a single datum and the map will be initialized as linked.
     * @param datum The datum that will be contained at initialization.
     */
    @SuppressWarnings("unchecked")
    public Seszt(E datum) {
        this((E[]) new Object[]{datum});
    }

    /**
     * The Seszt will contain data and the map will be initialized as linked.
     * @param data The data that will be contained at initialization.
     */
    public Seszt(E[] data) {
        this(data,false);
    }

    /**
     * The Seszt will contain data at initialization.
     * @param data The data that will be contained at initialization.
     * @param isLinked Will initialize the map as linked if true, else as hash.
     */
    public Seszt(E[] data, boolean isLinked) {
        super(LocalDateTime.now().getYear(), 1, 1);

        _map = isLinked ? new LinkedHashMap<>() : new HashMap<>();
        _destinations = isLinked ? new LinkedHashMap<>() : new HashMap<>();

        add(data);
    }

    @Override public int size() { return _data.length; }
    @Override public boolean isEmpty() { return _data.length == 0; }
    @Override public Iterator<E> iterator() { return Arrays.stream(_data).iterator(); }
    @Override public void forEach(Consumer<? super E> action) { Set.super.forEach(action); }
    @Override public Object[] toArray() { return Arrays.stream(_data).toArray(); }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object item : collection)
            if (!contains(item))
                return false;

        return true;
    }

    @Override
    public boolean contains(E[] elements) {
        for (E element : elements)
            if (!_map.containsValue(element))
                return false;

        return true;
    }
    @Override
    public boolean contains(Object object) {
        if (size() > 0 && object.getClass() == get(0).getClass())
            return contains(convert(new Object[]{ object }));

        return false;
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) { return (T[]) Arrays.stream(_data).toArray(); }

    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(IntFunction<T[]> generator) { return (T[]) Set.super.toArray(Object[]::new); }

    @Override
    public boolean add(E element) {
        int size = size();
        Add(convert(new Object[]{element}));
        return size < size();
    }

    @Override
    public boolean add(E[] elements) {
        int size = size();
        Add(elements);
        return size < size();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        int previousSize = size();
        Add(convert(collection.toArray()));
        return previousSize == size() - collection.size();
    }

    @Override
    public boolean addAll(CollectionUtility<? extends E> collection) {
        int previousSize = size();
        Add(collection.get_data());
        return previousSize == size() - collection.get_data().length;
    }

    @Override
    public E[] Add(E element) {
        return Add(convert(new Object[]{element}));
    }

    @Override
    public E[] Add(E[] elements) {
        handleAdd(filterUniques(elements));
        return get_data();
    }

    /**
     * Since the Seszt is a Set kind, it will only allow elements that are unique.
     * Therefor this filters away any that aren't unique,
     * This also applies to toStrings.
     * @param elements The elements to be filtered.
     * @return The filtered elements.
     */
    private E[] filterUniques(E[] elements) {
        E[] filtered = convert(new Object[elements.length]);

        for (int i = 0; i < elements.length; i++) {
            boolean notIncludedInFilter = true;
            for (E element : filtered)
                if (element != null && element.toString().equals(elements[i].toString()))
                    notIncludedInFilter = false;

            if (elements[i] != null && !contains(elements[i]) && notIncludedInFilter)
                filtered[i] = elements[i];
        }

        return filtered;
    }

    @Override
    public E set(E element, E replacement) {
        return null;
    }

    @Override
    public E[] set(E[] elements, E[] replacements) {
        return null;
    }
    @Override public E get(int index) { return handleGet(index); }

    @Override
    public E get(E element) {
        if (size() > 0 && !element.getClass().isPrimitive())
            return contains(element) ? _map.get(element.toString()) : null;
        else
            for (E datum : get_data())
                if (datum == element)
                    return datum;

        return null;
    }

    @Override
    public E Get(int index) {
        if (index > 0)
            return handleGet(index-1);

        return null;
    }

    @Override
    public E get(String key) {
        return _map.get(key);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        int previousSize = size();
        remove(convert(collection.toArray()));
        return previousSize == size() + collection.size();
    }

    @Override
    public E[] remove(E[] elements) {
        for (E element : elements)
            remove(element);

        return _data;
    }

    @Override
    public boolean remove(Object object) {
        int index = -1;

        for (int i = 0; i < size(); i++) {
            if (object.toString().equals(_data[i].toString())) {
                index = i;
                break;
            }
        }

        if (index < 0)
            return false;

        return remove(index);
    }

    @Override
    public boolean Remove(int index) {
        if (index == 0)
            return false;

        return remove(index-1);
    }

    @Override
    public boolean remove(int index) {
        if (index < 0)
            return false;

        int previousSize = size();
        handleRemove(index);
        return previousSize == size() + 1;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        try {
            retain(convert(collection.toArray()));
        } catch (Exception e) {
            Printer.get_instance().print(Printer.get_instance().arrayContent(collection.toArray()) +
                " couldn't be contained, since it is of different type that E...",e);
        }

        return Arrays.equals(_data, convert(collection.toArray()));
    }

    @Override
    public E[] retain(E[] elements) {
        try {
            E[] removes = convert(new Object[elements.length]);
            int index = 0;
            for (E element : elements)
                if (!contains(element))
                    removes[index] = element;

            _data = elements;

            for (E element : removes)
                remove(element);
        } catch (Exception e) {
            Printer.get_instance().print(Printer.get_instance().arrayContent(elements) +
                    " couldn't be contained, since it is of different type that E...",e);
        }

        return get_data();
    }

    @Override
    public boolean containsAll(CollectionUtility<?> collection) {
        for (E datum : convert(collection.get_data()))
            if (!contains(datum))
                return false;

        return true;
    }

    @Override
    public boolean removeAll(CollectionUtility<?> collection) {
        int previousSize = size();
        remove(collection.get_data());
        return previousSize == size() + collection.get_data().length;
    }

    @Override public boolean removeIf(Predicate<? super E> filter) { return Set.super.removeIf(filter); }

    @Override
    public void clear() {
        _data = convert(new Object[0]);
        _map = _map.getClass() == LinkedHashMap.class ? new LinkedHashMap<>() : new HashMap<>();
    }

    @Override public Spliterator<E> spliterator() { return Set.super.spliterator(); }
    @Override public Stream<E> stream() { return Set.super.stream(); }
    @Override public Stream<E> parallelStream() { return Set.super.parallelStream(); }

}
