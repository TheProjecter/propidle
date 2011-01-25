package acceptance;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Values implements Iterable<Object>{
    private final List<Object> values = new ArrayList<Object>();

    private Values() {

    }
    public Values and(Object value) {
        values.add(value);
        return this;
    }

    public static Values noValues() {
        return new Values();
    }
    public static Values with(Object value) {
        Values values = new Values();
        values.and(value);
        return values;
    }

    public Iterator<Object> iterator() {
        return values.iterator();
    }
}
