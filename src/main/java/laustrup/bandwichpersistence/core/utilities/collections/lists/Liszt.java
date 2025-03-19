package laustrup.bandwichpersistence.core.utilities.collections.lists;

import jdk.jshell.spi.ExecutionControl;
import laustrup.bandwichpersistence.core.utilities.collections.ICollection;
import laustrup.bandwichpersistence.core.utilities.console.Printer;
import lombok.SneakyThrows;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Implements a List of element E in an append way of adding elements.
 * It also implements the interface ICollectionUtility, which contains extra useful methods.
 * An extra detail is that this class also uses a map, which means that
 * the approach of getting also can be done through the map, this also
 * means, that they will be saved doing add and removed at remove.
 * Index can both start at 0 or 1, every method starting with an uppercase
 * letter starts with 1 instead 0 in the parameters.
 * @param <E> The type of element that are wished to be used in this class.
 */
public class Liszt<E> extends laustrup.bandwichpersistence.core.utilities.collections.Collection<E> implements ILiszt<E>, List<E>, ICollection<E> {

    /** Creates the Liszt with empty data and a hash type of map. */
    public Liszt() {
        this(false);
    }

    public Liszt(Stream<E> stream) {
        this((E[]) stream.toArray());
    }

    /**
     * Creates the Liszt with empty data.
     * @param isLinked Decides if the map should be linked or hash type.
     */
    public Liszt(boolean isLinked) { super(isLinked); }

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
        super(isLinked);
        add(data);
    }

    @Override public int size() { return _data.length; }
    @Override public boolean isEmpty() { return _data.length == 0 && _map.isEmpty(); }
    @Override
    public boolean contains(Object object) {
        if (object != null) {
            boolean exists = _map.containsValue(object);
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
    public Liszt<E> Add(E element) {
        add(element);
        return this;
    }

    @Override
    public Liszt<E> Add(E[] elements) {
        add(elements);
        return this;
    }

    @Override
    public Liszt<E> Add(laustrup.bandwichpersistence.core.utilities.collections.Collection<E> collection) {
        add(collection.get_data());
        return this;
    }

    @Override
    public Liszt<E> set(laustrup.bandwichpersistence.core.utilities.collections.Collection<E> originals, laustrup.bandwichpersistence.core.utilities.collections.Collection<E> replacements) {
        return set(originals.get_data(),replacements.get_data());
    }

    @Override //TODO Consider if this could be implemented as clean as Seszt.
    public Liszt<E> set(E[] originals, E[] replacements) {
        E[] replaced = convert(new Object[replacements.length]);
        boolean elementsIsNullOrEmpty = originals == null || originals.length == 0,
                replacementsIsNullOrEmpty = replacements == null || replacements.length == 0;

        if (elementsIsNullOrEmpty && replacementsIsNullOrEmpty)
            return this;
        else if (replacementsIsNullOrEmpty)
            remove(originals);
        else if (elementsIsNullOrEmpty)
            add(replacements);
        else {
            int replacementIndex = 0;
            for (int i = 1; i <= size(); i++) {
                for (E element : originals) {
                    if (Get(i).toString().equals(element.toString())) {
                        Set(i, replacements[replacementIndex]);
                        replaced[replacementIndex] = replacements[replacementIndex];
                        if (replacementIndex < replacements.length - 1)
                            replacementIndex++;

                        break;
                    }
                }
                if (replacements.length == replacementIndex - 1)
                    break;
            }
            if (replaced.length < replacements.length) {
                E[] extras = convert(new Object[replacements.length - replaced.length]);
                System.arraycopy(replacements, replaced.length, extras, replaced.length, replacements.length - replaced.length);
                add(extras);
            }
            if (replaced.length < originals.length) {
                E[] extras = convert(new Object[originals.length - replaced.length]);
                System.arraycopy(originals, replaced.length, extras, replaced.length, originals.length - replaced.length);
                remove(extras);
            }
        }

        return this;
    }

    @Override
    public Liszt<E> set(E original, E replacement) {
        for (int i = 0; i < size(); i++)
            if (get(i).toString().equals(original.toString()))
                set(i,replacement);

        return this;
    }

    @Override
    public Liszt<E> Set(int index, E element) {
        set(index-1,element);
        return this;
    }

    @Override
    public E set(int index, E element) {
        handleSet(index,element);
        return _map.containsKey(element.toString()) ? Get(element.toString()) : _data[index];
    }

    @Override
    public Liszt<E> remove(E[] elements) {
        elements = filterElements(elements);

        try {
            for (E element : elements)
                remove(element);
        }
        catch (Exception e) {
            Printer.get_instance().print("Couldn't remove object in remove multiple elements...", e);
        }

        return this;
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

    @Override
    public Liszt<E> Remove(int index) {
        remove(index-1);
        return this;
    }

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

    @SneakyThrows
    @Override
    public void add(int index, E element) {
        throw new ExecutionControl.NotImplementedException("The add operation is not supported");
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

    public static <M> Liszt<M> of(Stream<M> elements) {
        return new Liszt<>(elements);
    }

    public static <M> Liszt<M> of(M... elements) {
        return new Liszt<>(elements);
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

    @Override public E getLast() { return _data.length > 0 ? get_data()[size()-1] : null; }
    @Override public E getFirst() { return get_data()[0]; }
}
