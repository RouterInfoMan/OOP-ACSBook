package TemaTest;

import java.util.ArrayList;

public class JSONList {
    private final ArrayList<JSONObj> objects = new ArrayList<>();

    public JSONList() {

    }
    public void append(JSONObj obj) {
        objects.add(obj);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean empty = true;

        for (JSONObj obj : objects) {
            empty = false;
            builder.append(obj.toString());
            builder.append(",");
        }
        if (!empty) {
            int last = builder.length() - 1;
            builder.replace(last, last + 1, "");
        }
        builder.append("]");
        return builder.toString();
    }
    public String toStringWithSpaceAfter() {
        return this + " ";
    }
}
