package laustrup.bandwichpersistence.utilities;

import lombok.Data;

import java.util.Collection;

/**
 * This class will handle printing of statements.
 */
public class Printer implements IPrinter {

    /**
     * Is used to be reused as a border for beginning and ending of a print.
     */
    private final String _border = "\n\n-----------------------------------------------------------------------------------------\n\n";

    /**
     * Singleton instance of the Printer.
     */
    public static Printer _instance = null;

    public static Printer get_instance() {
        if (_instance ==null) {
            _instance = new Printer();
        }
        return _instance;
    }

    @Override public void print(String content) { System.out.println(_border + content + _border); }
    @Override public void print(String content,Exception ex) { System.err.println(_border + content + "\n\n" + ex + _border); }

    @Override
    public void compare(Collection<Object> objects, Collection<Double[]> values) {
        if (objects.size()==values.size()) {
            Object[] convertedObjects = objects.toArray();
            Object[] convertedValues = values.toArray();

            Liszt<ComparingObject> comparingObjects = new Liszt<>();
            for (int i = 0; i < objects.size(); i++) { comparingObjects.add(new ComparingObject(convertedObjects[i], (Double) convertedValues[i])); }

            String content = "Comparing of following objects are: \n\n";
            int place = 1;
            for (ComparingObject object : comparingObjects) {
                content += "\tPlace " + place + " is " + object.get_data().toString() +
                    " with the value of " + convertedValues[place-1].toString() + ".\n";
                place++;
            }

            print(content);
        }
        print("The two array inputs are not the same in Printer...", new ArrayIndexOutOfBoundsException());
    }

    @Data
    private static class ComparingObject {
        private Object _data;
        private double _value;

        public ComparingObject(Object data, double value) {
            _data = data;
            _value = value;
        }
    }
}
