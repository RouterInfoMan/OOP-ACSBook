package TemaTest;
import java.util.LinkedHashMap;
import java.util.Set;

public class JSONObj {
    private final LinkedHashMap<String, String> contents= new LinkedHashMap<>();

    public JSONObj(String key, String value) {
        this();
        this.addKeyValuePair(key, value);
    }

    public JSONObj() {
    }
    public void addKeyValuePair(String key, String value) {
        contents.put(key, value);
    }
    public void setKeyValue(String key, String value) {
        contents.replace(key, value);
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Set<String> keys = contents.keySet();
        builder.append("{");
        for (String key : keys) {
            builder.append(key);
            builder.append(" : ");
            builder.append(contents.get(key));
            builder.append(", ");
        }
        /* Deleting ", " */
        int last = builder.length() - 1;
        builder.replace(last - 1, last + 1, "");
        builder.append("}");
        return builder.toString();
    }
}
