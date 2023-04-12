package laustrup.bandwichpersistence.utilities.collections.lists;

import laustrup.bandwichpersistence.utilities.collections.CollectionUtility;

public abstract class ListUtility<E> extends CollectionUtility<E> {

    /**
     * Creates the Utility with empty data and a hash type of map.
     * @param year The year of the Utility.
     * @param version The middle index of version.
     * @param update The update of the version.
     */
    protected ListUtility(int year, int version, int update) {
        super(year,version,update);
    }

    /**
     * Creates the Utility with empty data.
     * @param isLinked Decides if the map should be linked or hash type.
     * @param year The year of the Utility.
     * @param version The middle index of version.
     * @param update The update of the version.
     */
    protected ListUtility(boolean isLinked, int year, int version, int update) {
        super(isLinked, year, version, update);
    }
}
