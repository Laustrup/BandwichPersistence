package laustrup.bandwichpersistence.quality_assurance;

/**
 * An interface for editing Test.
 */
public interface TestEditor {

    /**
     * A method that will be used after the @beforeEach.
     * Will only be used, if the editor is initialised.
     */
    void beforeEach();
}
