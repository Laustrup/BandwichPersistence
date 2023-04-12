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
 * It also implements ISeszt, which contains extra useful methods.
 * An extra detail is that this class also uses a map, which means that
 * the approach of getting also can be done through the map, this also
 * means, that they will be saved doing add, costing lower performance.
 * Index can both start at 0 or 1, every method starting with an uppercase
 * letter starts with 1 instead 0 in the parameters.
 * @param <E> The type of element that are wished to be used in this class.
 */
public class Seszt<E> extends SetUtility<E> implements Set<E>, ICollectionUtility<E> {

    public Seszt() {
        this(false);
    }

    public Seszt(boolean isLinked) {
        super(isLinked, LocalDateTime.now().getYear(), 1, 1);
    }

    public Seszt(E[] data) {
        this(data,false);
    }

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

    @Override @SuppressWarnings("all")
    public boolean contains(Object object) { return _map.containsValue(object); }
    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) { return (T[]) Arrays.stream(_data).toArray(); }
    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(IntFunction<T[]> generator) { return (T[]) Set.super.toArray(Object[]::new); }

    @Override
    public boolean add(E element) {
        int size = size();
        add(convert(new Object[]{element}));
        return size < size();
    }

    @Override
    public boolean add(E[] elements) {
        return false;
    }

    @Override
    public E set(E element, E replacement) {
        return null;
    }

    @Override
    public E[] set(E[] elements, E[] replacements) {
        return null;
    }

    @Override
    public E[] Get(E[] elements) {
        return null;
    }

    @Override
    public E[] remove(E[] elements) {
        return null;
    }

    @Override
    public boolean contains(E[] elements) {
        for (E element : elements)
            if (!contains(element))
                return false;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        int previousSize = size();
        add(convert(collection.toArray()));
        return previousSize == size() - collection.size();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        int previousSize = size();

        try {
            _data = convert(collection.toArray());
        } catch (Exception e) {
            Printer.get_instance().print(Printer.get_instance().arrayContent(collection.toArray()) +
                " couldn't be contained, since it is of different type that E...",e);
        }

        return Arrays.equals(_data, convert(collection.toArray()));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        int previousSize = size();
        remove(collection.toArray());
        return previousSize == size() + collection.size();
    }

    @Override
    public boolean containsAll(CollectionUtility<?> collection) {
        for (E datum : convert(collection.get_data()))
            if (!contains(datum))
                return false;

        return true;
    }

    @Override
    public boolean addAll(CollectionUtility<? extends E> collection) {
        int previousSize = size();
        add(collection.get_data());
        return previousSize == size() - collection.get_data().length;
    }

    @Override
    public boolean removeAll(CollectionUtility<?> collection) {
        int previousSize = size();
        remove(collection.get_data());
        return previousSize == size() + collection.get_data().length;
    }

    @Override
    public boolean retainAll(CollectionUtility<?> c) {
        return false;
    }

    @Override public boolean removeIf(Predicate<? super E> filter) {
        return Set.super.removeIf(filter);
    }

    @Override
    public void clear() {
        _data = convert(new Object[0]);
        _map = _map.getClass() == LinkedHashMap.class ? new LinkedHashMap<>() : new HashMap<>();
    }

    @Override public Spliterator<E> spliterator() {
        return Set.super.spliterator();
    }
    @Override public Stream<E> stream() {
        return Set.super.stream();
    }
    @Override public Stream<E> parallelStream() {
        return Set.super.parallelStream();
    }
}
