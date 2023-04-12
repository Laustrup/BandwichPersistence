package laustrup.bandwichpersistence.utilities.collections;

import laustrup.bandwichpersistence.utilities.Utility;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Utility that contains collections of data such as a Map and an array.
 * @param <E> The type of element that are wished to be used in this class.
 */
public abstract class CollectionUtility<E> extends Utility {

    /** Contains all the elements that are inside the Liszt. */
    @Getter
    protected E[] _data;

    /** Containing all data elements in a map for quick access. */
    @SuppressWarnings("all")
    protected Map<String, E> _map;

    /** The destinations for the map, before being inserted into the map */
    @SuppressWarnings("all")
    protected Map<String,E> _destinations;

    /** The keys that the map can use for inserting relevant data from the destinations into the map. */
    protected String[] _destinationKeys;

    /**
     * Creates the Utility with empty data and a hash type of map.
     * @param year The year of the Utility.
     * @param version The middle index of version.
     * @param update The update of the version.
     */
    protected CollectionUtility(int year, int version, int update) {
        this(false, year, version, update);
    }

    /**
     * Creates the Utility with empty data.
     * @param isLinked Decides if the map should be linked or hash type.
     * @param year The year of the Utility.
     * @param version The middle index of version.
     * @param update The update of the version.
     */
    protected CollectionUtility(boolean isLinked, int year, int version, int update) {
        super(year,version,update);
        _data = convert(new Object[0]);
        _destinationKeys = new String[0];

        if (isLinked) _map = new LinkedHashMap<>(); else _map = new HashMap<>();
        if (isLinked) _destinations = new LinkedHashMap<>(); else _destinations = new HashMap<>();
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
}
