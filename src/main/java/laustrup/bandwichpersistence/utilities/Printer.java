package laustrup.bandwichpersistence.utilities;

import lombok.Data;

import java.util.Collection;

/**
 * This class will handle printing of statements.
 */
public class Printer implements IPrinter {

    /**
     * The border content, used to determine the border.
     */
    private final String _border = "-----------------------------------------------------------------------------------" +
            "-------------------------------------------------------";
    /**
     * Is used to be reused as a border for beginning and ending of a print.
     */
    private final String _startBorder = "\n++ " + _border + "\n\n",
        _endBorder = "\n\n++ " + _border + "\n";

    /**
     * Singleton instance of the Printer.
     */
    public static Printer _instance = null;

    public static Printer get_instance() {
        if (_instance == null) {
            _instance = new Printer();
        }
        return _instance;
    }

    @Override
    public void print(String content) { System.out.println(_startBorder + content + _endBorder); }
    @Override
    public void print(String content, Exception ex) { System.err.println(_startBorder + content + "\n\n" + ex + _endBorder); }

    @Override
    public void print(Object[] array) {
        String content = "{ ";

        for (int i = 0; i < array.length; i++) {
            content += array[i].toString();

            if (i > array.length - 1)
                content += " | ";
        }

        content += " }";
        print(content);
    }


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
