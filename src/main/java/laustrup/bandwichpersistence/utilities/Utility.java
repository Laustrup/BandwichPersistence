package laustrup.bandwichpersistence.utilities;

import lombok.Getter;

/**
 * The super class of any utilities that shares common methods or fields.
 */
public class Utility {

    /** Used for the version number as year|version|update. */
    @Getter
    protected int _year, _update;

    /** The version of the Utility */
    @Getter
    protected String _version;

    /** Is used to generate the version of the Utility.
     * @param year The year of the Utility.
     * @param version The middle index of version.
     * @param update The update of the version.
     */
    public Utility(int year, int version, int update) {
        _year = year;
        _update = update;

        define(version);
    }

    /**
     * Determines the version from year|version|update.
     * @param version The middle index of the full version.
     */
    private void define(int version) {
        _version = _year + "|" + version + "|" + _update;
    }
}
