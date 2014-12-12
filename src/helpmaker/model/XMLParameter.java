package helpmaker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class XMLParameter {

    public final static String PARAMETER_ID = "id";
    public final static String PARAMETER_CAPTION = "caption";

    private StringProperty key;
    private StringProperty value;

    public XMLParameter() {
        this(null, null);
    }

    public XMLParameter(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public StringProperty getKeyProperty() {
        return key;
    }

    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.setValue(key);
    }

    public StringProperty getValueProperty() {
        return value;
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.setValue(value);
    }

    public boolean isValid() {
        return ((key.get() != null) && (value.get() != null) &&
                (!key.get().equals("")) && (!value.get().equals("")) );
    }
}
